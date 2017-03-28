When(/^A request is made to get a token with authentication header missing$/) do
  @result = @service_client.get_by_url(@service_client.get_base_uri.to_s+"/users/getToken")
end

When(/^A request is made to get a token with authentication header null$/) do
  @result = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/getToken", "Basic ")
end
