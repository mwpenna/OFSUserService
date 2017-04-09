require 'date'

When(/^A request to update a user that does not exists$/) do
  name = Faker::Pokemon.name + (SecureRandom.random_number(999) + 1000).to_s
  email = name + "@pokemon.com"
  user = FactoryGirl.build(:user, userName: name, emailAddress: email)
  @result = @service_client.post_to_url("/users/id/123", user.update_to_json)
end

When(/^A request to update the user with invalid (.*?)$/) do |field|
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  body[field]="test"
  @result = @service_client.post_to_url("/users/id/"+ @location.split("/id/").last, body.to_json)
end

When(/^A request to update the user tokenExpDate$/) do
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  @updatedValue = DateTime.now.strftime "%Y-%m-%dT%H:%M:%SZ"
  body["tokenExpDate"]=@updatedValue
  @result = @service_client.post_to_url("/users/id/"+ @location.split("/id/").last, body.to_json)
end

And(/^I should see the (.*?) was updated$/) do |field|
  @result = @service_client.get_by_url(@location)
  expect(@result[field]).to eql @updatedValue
end

When(/^A request to update the user activeFlag$/) do
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  @updatedValue = false
  body["activeFlag"]=@updatedValue
  @result = @service_client.post_to_url("/users/id/"+ @location.split("/id/").last, body.to_json)
end

When(/^A request to update the user firstName$/) do
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  @updatedValue = "test"
  body["firstName"]=@updatedValue
  @result = @service_client.post_to_url("/users/id/"+ @location.split("/id/").last, body.to_json)
end

When(/^A request to update the user lastName$/) do
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  @updatedValue = "test"
  body["lastName"]=@updatedValue
  @result = @service_client.post_to_url("/users/id/"+ @location.split("/id/").last, body.to_json)
end

When(/^A request to update the user role$/) do
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  @updatedValue = "ADMIN"
  body["role"]=@updatedValue
  @result = @service_client.post_to_url("/users/id/"+ @location.split("/id/").last, body.to_json)
end

When(/^A request to update the user role with invalid role$/) do
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  body["role"]="TEST"
  @result = @service_client.post_to_url("/users/id/"+ @location.split("/id/").last, body.to_json)
end

And(/^I should see an error message indicating role invaild enum$/) do
  expect(@result["errors"][0]).to eql Errors.user_role_invalid
end

When(/^A request to update the user password$/) do
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  @updatedValue = "test"
  body["password$"]=@updatedValue
  @result = @service_client.post_to_url("/users/id/"+ @location.split("/id/").last, body.to_json)
end

When(/^A request to update the user emailAddress$/) do
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  @updatedValue = "test@test.com"
  body["emailAddress"]=@updatedValue
  @result = @service_client.post_to_url("/users/id/"+ @location.split("/id/").last, body.to_json)
end

Then(/^I should see the password has changed$/) do
  @result = @service_client.get_by_url(@location)
  expect(@result["password"]).to_not eql "test"
end