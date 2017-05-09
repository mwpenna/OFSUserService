When(/^A request to get the user is received$/) do
  @result = @service_client.get_by_url_with_auth(@location, "Bearer " + @user.token)
end

And(/^I should see the user was returned$/) do
  expect(@result["id"]).to_not be_nil
  expect(@result["lastName"]).to eql @user.lastName
  expect(@result["firstName"]).to eql @user.firstName
  expect(@result["role"]).to eql @user.role
  expect(@result["userName"]).to eql @user.userName
  expect(@result["password"]).to_not eql @user.password
  expect(@result["emailAddress"]).to eql @user.emailAddress
  expect(@result["activeFlag"]).to eql true
  expect(@result["company"]["href"]).to eql @user.company_href
  expect(@result["company"]["name"]).to eql @user.company_name
end

When(/^A request to get a user that does not exists$/) do
  basic_auth = Base64.encode64( "ofssystemadmin:p@$$Wordofs")
  authToken = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/getToken", "Basic "+ basic_auth)['token']
  @result = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/user/id/123", "Bearer " + authToken)
end