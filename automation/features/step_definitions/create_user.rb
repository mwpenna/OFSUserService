Given(/^A users company all ready exists$/) do

end

When(/^A request to create a user is received with missing (.*?)$/) do |field|
  @user = FactoryGirl.build(:user)
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