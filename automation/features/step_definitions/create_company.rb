When(/^A request to create a company is received with missing (.*?)$/) do |field|
  @company = FactoryGirl.build(:company, name: nil)

  body = @company.create_to_hash
  body.delete(field.to_sym)

  @result = @service_client.post_to_url("/company", body.to_json)
end

And(/^I should see an error message indicating (.*?) missing$/) do |property|
  expect(@result["errors"][0]).to eql Errors.company_required_field_missing(property)
end

When(/^A create company request is received with (.*?)$/) do |field|
  @company = FactoryGirl.build(:company, name: Faker::Company.name + (SecureRandom.random_number(999) + 1000).to_s)
  body = @company.create_to_hash
  body[field]="test"
  @result = @service_client.post_to_url("/company", body.to_json)
end

And(/^I should see an error message indicating (.*?) not allowed$/) do |property|
  expect(@result["errors"][0]).to eql Errors.company_field_not_acceptable(property)
end

When(/^A request to create a company is received$/) do
  @company = FactoryGirl.build(:company, name: Faker::Company.name + (SecureRandom.random_number(999) + 1000).to_s)
  @result = @service_client.post_to_url("/company", @company.create_to_json)
end

