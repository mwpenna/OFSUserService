require 'date'

When(/^A request to update a user that does not exists$/) do
  basic_auth = Base64.encode64( "ofssystemadmin:p@$$Wordofs")
  authToken = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/getToken", "Basic "+ basic_auth)['token']
  name = Faker::Pokemon.name + (SecureRandom.random_number(999) + 1000).to_s
  email = name + "@pokemon.com"
  user = FactoryGirl.build(:user, userName: name, emailAddress: email)
  @result = @service_client.post_to_url_with_auth("/users/id/123", user.update_to_json, "Bearer "+ authToken)
end

When(/^A request to update the user with invalid (.*?)$/) do |field|
  authToken = @user.token
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  body[field]="test"
  @result = @service_client.post_to_url_with_auth("/users/id/"+ @location.split("/id/").last, body.to_json, "Bearer " + authToken)
end

When(/^A request to update the user tokenExpDate$/) do
  authToken = @user.token
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  @updatedValue = DateTime.now.strftime "%Y-%m-%dT%H:%M:%SZ"
  body["tokenExpDate"]=@updatedValue
  @result = @service_client.post_to_url_with_auth("/users/id/"+ @location.split("/id/").last, body.to_json, "Bearer " + authToken)
end

And(/^I should see the (.*?) was updated$/) do |field|
  basic_auth = Base64.encode64( "ofssystemadmin:p@$$Wordofs")
  authToken = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/getToken", "Basic "+ basic_auth)['token']
  @result = @service_client.get_by_url_with_auth(@location, "Bearer " + authToken)
  expect(@result[field]).to eql @updatedValue
end

When(/^A request to update the user activeFlag$/) do
  authToken = @user.token
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  @updatedValue = false
  body["activeFlag"]=@updatedValue
  @result = @service_client.post_to_url_with_auth("/users/id/"+ @location.split("/id/").last, body.to_json, "Bearer " + authToken)
end

When(/^A request to update the user firstName$/) do
  authToken = @user.token
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  @updatedValue = "test"
  body["firstName"]=@updatedValue
  @result = @service_client.post_to_url_with_auth("/users/id/"+ @location.split("/id/").last, body.to_json, "Bearer " + authToken)
end

When(/^A request to update the user lastName$/) do
  authToken = @user.token
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  @updatedValue = "test"
  body["lastName"]=@updatedValue
  @result = @service_client.post_to_url_with_auth("/users/id/"+ @location.split("/id/").last, body.to_json, "Bearer " + authToken)
end

When(/^A request to update the user role$/) do
  authToken = @user.token
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  @updatedValue = "ADMIN"
  body["role"]=@updatedValue
  @result = @service_client.post_to_url_with_auth("/users/id/"+ @location.split("/id/").last, body.to_json, "Bearer " + authToken)
end

When(/^A request to update the user role with invalid role$/) do
  authToken = @user.token
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  body["role"]="TEST"
  @result = @service_client.post_to_url_with_auth("/users/id/"+ @location.split("/id/").last, body.to_json, "Bearer " + authToken)
end

And(/^I should see an error message indicating role invaild enum$/) do
  expect(@result["errors"][0]).to eql Errors.user_role_invalid
end

When(/^A request to update the user password$/) do
  authToken = @user.token
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  @updatedValue = "test"
  body["password$"]=@updatedValue
  @result = @service_client.post_to_url_with_auth("/users/id/"+ @location.split("/id/").last, body.to_json, "Bearer " + authToken)
end

When(/^A request to update the user emailAddress$/) do
  authToken = @user.token
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  @updatedValue = "test@test.com"
  body["emailAddress"]=@updatedValue
  @result = @service_client.post_to_url_with_auth("/users/id/"+ @location.split("/id/").last, body.to_json, "Bearer " + authToken)
end

Then(/^I should see the password has changed$/) do
  @result = @service_client.get_by_url(@location)
  expect(@result["password"]).to_not eql "test"
end

When(/^A request to update the user emailAddress is received by SYSTEM_ADMIN$/) do
  basic_auth = Base64.encode64( "ofssystemadmin:p@$$Wordofs")
  authToken = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/getToken", "Basic "+ basic_auth)['token']
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  @updatedValue = "test@test.com"
  body["emailAddress"]=@updatedValue
  @result = @service_client.post_to_url_with_auth("/users/id/"+ @location.split("/id/").last, body.to_json, "Bearer " + authToken)
end

When(/^A request to update the user is received by the ADMIN user for a different company$/) do
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  @updatedValue = "test@test.com"
  body["emailAddress"]=@updatedValue
  @result = @service_client.post_to_url_with_auth("/users/id/"+ @location.split("/id/").last, body.to_json, "Bearer " + @authToken)
end

When(/^A request to update the users emailAddress is received$/) do
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  @updatedValue = "test@test.com"
  body["emailAddress"]=@updatedValue
  @result = @service_client.post_to_url_with_auth("/users/id/"+ @location.split("/id/").last, body.to_json, "Bearer " + @authToken)
end

When(/^A request to update a different user is received$/) do
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  @updatedValue = "test@test.com"
  body["emailAddress"]=@updatedValue
  @result = @service_client.post_to_url_with_auth("/users/id/"+ @location.split("/id/").last, body.to_json, "Bearer " + @authToken)
end