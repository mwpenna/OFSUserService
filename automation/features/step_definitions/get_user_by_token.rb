When(/^A request to get the user by token is received$/) do
  @result = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/token", "Bearer "+ @user.token)
end