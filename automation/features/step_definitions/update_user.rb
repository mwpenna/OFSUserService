When(/^A request to update a user that does not exists$/) do
  name = Faker::Pokemon.name + (SecureRandom.random_number(999) + 1000).to_s
  email = name + "@pokemon.com"
  user = FactoryGirl.build(:user, userName: name, emailAddress: email)
  @result = @service_client.post_to_url("/users/id/123", user.update_to_json)
end

When(/^A request to update the user (.*?)$/) do |field|
  @user = FactoryGirl.build(:user,  company_href: @company.href, company_name: @company.name)
  body = @user.update_to_hash
  body[field]="test"
  @result = @service_client.post_to_url("/users/id/"+ @location.split("/id/").last, body.to_json)
end