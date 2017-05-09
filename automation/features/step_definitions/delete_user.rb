When(/^A request is made to delete the user$/) do
  basic_auth = Base64.encode64( "ofssystemadmin:p@$$Wordofs")
  authToken = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/getToken", "Basic "+ basic_auth)['token']
  @result = @service_client.delete_by_url_with_auth(@service_client.get_base_uri + "/users/id/" + @location.split("/id/")[1], "Bearer " + authToken)
end

And(/^I should see the user does not exists$/) do
  basic_auth = Base64.encode64( "ofssystemadmin:p@$$Wordofs")
  authToken = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/getToken", "Basic "+ basic_auth)['token']
  @result = @service_client.get_by_url_with_auth(@location, "Bearer " + authToken)
  expect(@result.response.code.to_i).to eql 404.to_i
end

When(/^A request is made to delete a user the does not exists$/) do
  basic_auth = Base64.encode64( "ofssystemadmin:p@$$Wordofs")
  authToken = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/getToken", "Basic "+ basic_auth)['token']
  @result = @service_client.delete_by_url_with_auth(@service_client.get_base_uri + "/users/id/123", "Bearer " + authToken)
end