require 'faker'

Given(/^A users company all ready exists$/) do
  company = FactoryGirl.build(:company, name: Faker::Company.name + (SecureRandom.random_number(999) + 1000).to_s)
  result = @service_client.post_to_url("/company", company.create_to_json)
  href = result.headers['location']
  @company = FactoryGirl.build(:company, name: company.name, href: href, id: href.split("/id/").last)
end

Given(/^A company and user exists$/) do
  company = FactoryGirl.build(:company, name: Faker::Company.name + (SecureRandom.random_number(999) + 1000).to_s)
  result = @service_client.post_to_url("/company", company.create_to_json)
  href = result.headers['location']
  @company = FactoryGirl.build(:company, name: company.name, href: href, id: href.split("/id/").last)
  name = Faker::Pokemon.name + (SecureRandom.random_number(999) + 1000).to_s
  email = name + "@pokemon.com"
  @user = FactoryGirl.build(:user, userName: name, emailAddress: email, company_href: @company.href, company_name: @company.name)
  @result = @service_client.post_to_url("/users", @user.create_to_json)
end

When(/^A request to create a user is received with missing (.*?)$/) do |field|
  @user = FactoryGirl.build(:user, company_href: @company.href, company_name: @company.name)
  body = @user.create_to_hash
  body.delete(field.to_sym)
  @result = @service_client.post_to_url("/users", body.to_json)
end

Then(/^the response should have a status of (\d+)$/) do |response_code|
  expect(@result.response.code.to_i).to eql response_code.to_i
end

And(/^I should see an error message with (.*?) missing$/) do |property|
  expect(@result["errors"][0]).to eql Errors.required_field_missing(property)
end

When(/^A create user request is received with (.*?)$/) do |field|
  @user = FactoryGirl.build(:user, company_href: @company.href, company_name: @company.name)
  body = @user.create_to_hash
  body[field]="test"
  @result = @service_client.post_to_url("/users", body.to_json)
end

And(/^I should see an error message with (.*?) not allowed/) do |property|
  expect(@result["errors"][0]).to eql Errors.field_not_acceptable(property)
end

When(/^A request to create a user is received with an invalid company$/) do
  @user = FactoryGirl.build(:user, company_href: @service_client.get_base_uri + "/company/id/123")
  @result = @service_client.post_to_url("/users", @user.create_to_json)
end

And(/^I should see an error message with company does not exists$/) do
  expect(@result["errors"][0]).to eql Errors.company_id_invalid
end

When(/^A request to create a user is received$/) do
  name = Faker::Pokemon.name + (SecureRandom.random_number(999) + 1000).to_s
  email = name + "@pokemon.com"
  @user = FactoryGirl.build(:user, userName: name, emailAddress: email, company_href: @company.href, company_name: @company.name)
  @result = @service_client.post_to_url("/users", @user.create_to_json)
end

And(/^I should see the user was created$/) do
  location = @result.headers['location']
  expect(location).to_not be_nil
  response = @service_client.get_by_url(location);

  expect(response["id"]).to eql location.split("/id/").last
  expect(response["firstName"]).to eql @user.to_hash[:firstName]
  expect(response["lastName"]).to eql @user.to_hash[:lastName]
  expect(response["role"]).to eql @user.to_hash[:role]
  expect(response["userName"]).to eql @user.to_hash[:userName]
  expect(response["password"]).to_not eql @user.to_hash[:password]
  expect(response["emailAddress"]).to eql @user.to_hash[:emailAddress]
  expect(response["activeFlag"]).to eql true
  expect(response["company"]["href"]).to eql @user.to_hash[:company][:href]
  expect(response["company"]["name"]).to eql @user.to_hash[:company][:name]
end

And(/^I should see the location header populated$/) do
  expect(@result.headers['location']).to_not be_nil
end

When(/^A request to create a user is received with a duplicate username$/) do
  email = @user.to_hash[:userName] + (SecureRandom.random_number(999) + 1000).to_s + "@pokemon.com"
  duplicateUserName = FactoryGirl.build(:user, userName: @user.to_hash[:userName], emailAddress: email, company_href: @company.href, company_name: @company.name)
  @result = @service_client.post_to_url("/users", duplicateUserName.create_to_json)
end

And(/^I should see an error message with duplicate username$/) do
  expect(@result["errors"][0]).to eql Errors.username_exists
end

When(/^A request to create a user is received with a duplicate emailAddress$/) do
  userName = @user.to_hash[:userName] + (SecureRandom.random_number(999) + 1000).to_s
  duplicateEmailAddress = FactoryGirl.build(:user, userName: userName, emailAddress: @user.to_hash[:emailAddress], company_href: @company.href, company_name: @company.name)
  @result = @service_client.post_to_url("/users", duplicateEmailAddress.create_to_json)
end

And(/^I should see an error message with duplicate emailAddress$/) do
  expect(@result["errors"][0]).to eql Errors.emailaddress_exists
end