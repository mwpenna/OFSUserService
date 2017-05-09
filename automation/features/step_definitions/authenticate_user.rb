When(/^A request to authenticate the user without authorization header$/) do
  @result = @service_client.get_by_url(@service_client.get_base_uri.to_s+"/users/authenticate")
end

When(/^A request to authenticate the user without Bearer authorization$/) do
  @result = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/authenticate", "Basic 123")
end

When(/^A request to authenticate the user with invalid JWT token$/) do
  @result = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/authenticate", "Bearer 123")
end

When(/^A request to authenticate a user that does not exists$/) do
  @result = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/authenticate", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJKV1RTdWJqZWN0IiwiaWF0IjoxNDkxODM5NjEyLCJzdWIiOiJ7XCJ1c2VySHJlZlwiOlwiaHR0cDovL2xvY2FsaG9zdDo4MDgyL3VzZXJzL2lkLzFjMTllMjIwLTgzNTMtNGQxMS04OGE1LTA5NWRiNDM4ODA4M1wiLFwiY29tcGFueUhyZWZcIjpcImh0dHA6Ly9sb2NhbGhvc3Q6ODA4Mi9jb21wYW55L2lkL2RhYWJhOTJkLTA0MmYtNDkzZi05MjFkLTJlOGQ5ZTViM2JlZlwiLFwiZmlyc3ROYW1lXCI6XCJmbmFtZVwiLFwibGFzdE5hbWVcIjpcImxuYW1lXCIsXCJyb2xlXCI6XCJBRE1JTlwiLFwidXNlck5hbWVcIjpcIkdhc3RseTE3MTVcIixcInVzZXJFbWFpbEFkZHJlc3NcIjpcIkdhc3RseTE3MTVAcG9rZW1vbi5jb21cIn0iLCJpc3MiOiJPRlNVc2VyU2VydmljZSJ9.vVzzr0NGFa9bh3PXrENcNRpS3gXoNHLnmpl5K9ZGT6Q")
end

When(/^A request to authenticate the user with expired JWT token$/) do
  basic_auth = Base64.encode64( @user.userName + ":" + @user.password)
  token = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/getToken", "Basic "+ basic_auth)['token']

  updateUser = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = updateUser.update_to_hash
  body["tokenExpDate"]=(DateTime.now - (25/1440.0)).strftime "%Y-%m-%dT%H:%M:%SZ"
  @service_client.post_to_url_with_auth("/users/id/"+ @location.split("/id/").last, body.to_json, "Bearer "+token)
  @result = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/authenticate", "Bearer "+token)
end

When(/^A request to authenticate the user with wrong JWT token$/) do
  basic_auth = Base64.encode64( @user.userName + ":" + @user.password)
  @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/getToken", "Basic "+ basic_auth)
  @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/getToken", "Basic "+ basic_auth)
  @result = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/authenticate", "Bearer "+@user.token)
end

When(/^A request to authenticate the user$/) do
  @result = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/authenticate", "Bearer "+@user.token)
end

Then(/^I should see the emailAddress is returned$/) do
  expect(@result["emailAddress"]).to eql @user.to_hash[:emailAddress]
end

Then(/^I should see the href is returned$/) do
  expect(@result["href"]).to eql @user.to_hash[:href]
end

Then(/^I should see the companyHref is returned$/) do
  expect(@result["companyHref"]).to eql @user.to_hash[:company][:href]
end

Then(/^I should see the firstName is returned$/) do
  expect(@result["firstName"]).to eql @user.to_hash[:firstName]
end

Then(/^I should see the lastName is returned$/) do
  expect(@result["lastName"]).to eql @user.to_hash[:lastName]
end

Then(/^I should see the role is returned$/) do
  expect(@result["role"]).to eql @user.to_hash[:role]
end

Then(/^I should see the userName is returned$/) do
  expect(@result["userName"]).to eql @user.to_hash[:userName]
end