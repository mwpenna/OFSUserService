require 'base64'

When(/^A request is made to get a token with authentication header missing$/) do
  @result = @service_client.get_by_url(@service_client.get_base_uri.to_s+"/users/getToken")
end

When(/^A request is made to get a token with authentication header null$/) do
  @result = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/getToken", "Basic ")
end

When(/^A request is made to get a token with invalid password$/) do
  basic_auth = Base64.encode64( @user.userName + ":" + @user.password + "1")
  @result = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/getToken", "Basic "+ basic_auth)
end

And(/^I should see an error message indicating user authentication failed$/) do
  expect(@result["errors"][0]).to eql Errors.user_authentication_failed
end

When(/^A request is made to get a token for a user that does not exists$/) do
  basic_auth = Base64.encode64( "someuser:somepassword")
  @result = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/getToken", "Basic "+ basic_auth)
end

When(/^A request is made to get a token with valid username and password$/) do
  basic_auth = Base64.encode64( @user.userName + ":" + @user.password)
  @result = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/getToken", "Basic "+ basic_auth)
end

And(/^I should see a token response was returned$/) do
  expect(@result['tokenType']).to eql "bearer"
  expect(@result['token']).to_not be_nil
end

Given(/^A company and user exists with token$/) do
  company = FactoryGirl.build(:company, name: Faker::Company.name + (SecureRandom.random_number(999) + 1000).to_s)
  result = @service_client.post_to_url("/company", company.create_to_json)
  href = result.headers['location']
  @company = FactoryGirl.build(:company, name: company.name, href: href, id: href.split("/id/").last)
  name = Faker::Pokemon.name + (SecureRandom.random_number(999) + 1000).to_s
  email = name + "@pokemon.com"
  @user = FactoryGirl.build(:user, userName: name, emailAddress: email, company_href: @company.href, company_name: @company.name)
  @result = @service_client.post_to_url("/users", @user.create_to_json)
  @location = @result.headers['location']
  result = @service_client.get_by_url(@location)
  @user.userHref = @location
  @user.token=result['token']
  @user.tokenExpDate=result['tokenExpDate']
end

And(/^I should see the user is populated with a (.*?)$/) do |field|
  result = @service_client.get_by_url(@location)
  expect(result[field.to_s]).to_not be_nil
end

And(/^I should see the user is updated with the new token values$/) do
  result = @service_client.get_by_url(@location)
  expect(@user.token).to_not be result['token']
  expect(@user.tokenExpDate).to_not be result['tokenExpDate']
end