Given(/^A company and multiple users exists$/) do
  basic_auth = Base64.encode64( "ofssystemadmin:p@$$Wordofs")
  authToken = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/getToken", "Basic "+ basic_auth)['token']
  company = FactoryGirl.build(:company, name: Faker::Company.name + (SecureRandom.random_number(999) + 1000).to_s)
  result = @service_client.post_to_url_with_auth("/company", company.create_to_json, "Bearer "+ authToken)
  href = result.headers['location']
  @company = FactoryGirl.build(:company, name: company.name, href: href, id: href.split("/id/").last)

  @users = []
  rand(2..5).times do
    name = Faker::Pokemon.name + (SecureRandom.random_number(999) + 1000).to_s
    email = name + "@pokemon.com"
    user = FactoryGirl.build(:user, userName: name, emailAddress: email, company_href: @company.href, company_name: @company.name, role: "WAREHOUSE")
    result = @service_client.post_to_url_with_auth("/users", user.create_to_json, "Bearer "+ authToken)
    user.id = result.headers['location'].split("/id/").last
    @users << user
  end

  name = Faker::Pokemon.name + (SecureRandom.random_number(999) + 1000).to_s
  email = name + "@pokemon.com"
  @user = FactoryGirl.build(:user, userName: name, emailAddress: email, company_href: @company.href, company_name: @company.name)
  @result = @service_client.post_to_url_with_auth("/users", @user.create_to_json, "Bearer "+ authToken)
  @location = @result.headers['location']
  basic_auth = Base64.encode64( @user.userName+":"+ @user.password)
  sleep(1)
  @authToken = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/getToken", "Basic "+ basic_auth)['token']
  @user.id = @location.split("/id/").last
  @users << @user
end

When(/^A request to get the users by company id is received$/) do
  @result = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/company/id/" + @company.id, "Bearer " + @authToken)
end

Then(/^I should see the user list was returned$/) do
  expect(@result["count"]).to eql @users.size
  expect(@result["items"].size).to eql @users.size

  @result["items"].each do |response|
    user = @users.detect{|u| u.id == response['id']}
    expect(response["id"]).to eql user.id
    expect(response["lastName"]).to eql user.lastName
    expect(response["firstName"]).to eql user.firstName
    expect(response["role"]).to eql user.role
    expect(response["userName"]).to eql user.userName
    expect(response["password"]).to be nil
    expect(response["emailAddress"]).to eql user.emailAddress
    expect(response["activeFlag"]).to eql true
    expect(response["company"]["href"]).to eql user.company_href
    expect(response["company"]["name"]).to eql user.company_name
    expect(response["token"]).to be nil
    expect(response["tokenExpDate"]).to be nil
  end
end

Given(/^A company exists without users$/) do
  basic_auth = Base64.encode64( "ofssystemadmin:p@$$Wordofs")
  @authToken = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/getToken", "Basic "+ basic_auth)['token']
  company = FactoryGirl.build(:company, name: Faker::Company.name + (SecureRandom.random_number(999) + 1000).to_s)
  result = @service_client.post_to_url_with_auth("/company", company.create_to_json, "Bearer "+ @authToken)
  href = result.headers['location']
  @company = FactoryGirl.build(:company, name: company.name, href: href, id: href.split("/id/").last)
end

Then(/^I should see an empty list was returned$/) do
  expect(@result["count"]).to eql 0
  expect(@result["items"].size).to eql 0
end

When(/^A request to get the users for a company id that does not exists is received$/) do
  basic_auth = Base64.encode64( "ofssystemadmin:p@$$Wordofs")
  @authToken = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/getToken", "Basic "+ basic_auth)['token']
  @result = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/company/id/123", "Bearer " + @authToken)
end
