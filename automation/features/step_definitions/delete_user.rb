When(/^A request is made to delete the user$/) do
  @result = @service_client.delete_by_url(@service_client.get_base_uri + "/users/id/" + @location.split("/id/")[1])
end

And(/^I should see the user does not exists$/) do
  @result = @service_client.get_by_url(@location)
  expect(@result.response.code.to_i).to eql 404.to_i
end

When(/^A request is made to delete a user the does not exists$/) do
  @result = @service_client.delete_by_url(@service_client.get_base_uri + "/users/id/123")
end