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