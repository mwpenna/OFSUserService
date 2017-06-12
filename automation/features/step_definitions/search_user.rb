When(/^A request to search for users is received$/) do
  @result = @service_client.post_to_url_with_auth("/users/search", @user.search_to_json, "Bearer "+ @authToken)
end

When(/^A request to search for users by (.*?) is received$/) do |field|
  request = { field.to_sym => @baseSimilarValue}
  @result = @service_client.post_to_url_with_auth("/users/search", request.to_json, "Bearer "+ @authToken)
end

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
    user = FactoryGirl.build(:user, userName: name, emailAddress: email, company_href: @company.href, company_name: @company.name, role: "ADMIN")
    result = @service_client.post_to_url_with_auth("/users", user.create_to_json, "Bearer "+ authToken)
    user.id = result.headers['location'].split("/id/").last
    @users << user
  end

  name = Faker::Pokemon.name + (SecureRandom.random_number(999) + 1000).to_s
  email = name + "@pokemon.com"
  @user = FactoryGirl.build(:user, userName: name, emailAddress: email, company_href: @company.href, company_name: @company.name, role: "ADMIN")
  @result = @service_client.post_to_url_with_auth("/users", @user.create_to_json, "Bearer "+ authToken)
  @location = @result.headers['location']
  basic_auth = Base64.encode64( @user.userName+":"+ @user.password)
  sleep(1)
  @authToken = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/getToken", "Basic "+ basic_auth)['token']
  @user.id = @location.split("/id/").last
  @users << @user
end

When(/^An invalid request to search for users by id is received$/) do
  @result = @service_client.post_to_url_with_auth("/users/search","{\"id\":\"123\"}", "Bearer "+ @authToken)
end

When(/^An invalid request to search for users by company is received$/) do
  request = "{\"company\": {\"id\": \"cf4c62cf-76fb-4c94-aa93-56bdb526fe62\"}}"
  @result = @service_client.post_to_url_with_auth("/users/search",request, "Bearer "+ @authToken)
end

When(/^An invalid request to search for users by createdOn is received$/) do
  @result = @service_client.post_to_url_with_auth("/users/search","{\"createdOn\":\"2017-09-09\"}", "Bearer "+ @authToken)
end

Given(/^A company and multiple users exists with similar (.*?)$/) do |field|
  basic_auth = Base64.encode64( "ofssystemadmin:p@$$Wordofs")
  authToken = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/getToken", "Basic "+ basic_auth)['token']
  company = FactoryGirl.build(:company, name: Faker::Company.name + (SecureRandom.random_number(999) + 1000).to_s)
  result = @service_client.post_to_url_with_auth("/company", company.create_to_json, "Bearer "+ authToken)
  href = result.headers['location']
  @company = FactoryGirl.build(:company, name: company.name, href: href, id: href.split("/id/").last)

  @users = []
  i = 0;

  @baseSimilarValue = Faker::Pokemon.name

  rand(2..5).times do
    i = i + 1
    name = @baseSimilarValue + (SecureRandom.random_number(999) + 1000).to_s
    email = name + "@pokemon.com"
    user = FactoryGirl.build(:user, userName: name, emailAddress: email, company_href: @company.href, company_name: @company.name, role: "ADMIN")
    body = user.create_to_hash
    body[field.to_sym]=@baseSimilarValue + i.to_s
    result = @service_client.post_to_url_with_auth("/users", body.to_json, "Bearer "+ authToken)
    user.id = result.headers['location'].split("/id/").last
    body["id".to_sym] = user.id
    @users << body
  end

  name = Faker::Pokemon.name + (SecureRandom.random_number(999) + 1000).to_s
  email = name + "@pokemon.com"
  @user = FactoryGirl.build(:user, userName: name, emailAddress: email, company_href: @company.href, company_name: @company.name, role: "ADMIN")
  @result = @service_client.post_to_url_with_auth("/users", @user.create_to_json, "Bearer "+ authToken)
  @location = @result.headers['location']
  basic_auth = Base64.encode64( @user.userName+":"+ @user.password)
  sleep(1)
  @authToken = @service_client.get_by_url_with_auth(@service_client.get_base_uri.to_s+"/users/getToken", "Basic "+ basic_auth)['token']
  @user.id = @location.split("/id/").last
end

Then(/^I should see the user search list was returned$/) do
  expect(@result["count"]).to eql @users.size
  expect(@result["items"].size).to eql @users.size

  @result["items"].each do |response|
    user = @users.detect{|u| u["id".to_sym] == response['id']}
    expect(response["firstName"]).to eql user[:firstName]
    expect(response["lastName"]).to eql user[:lastName]
    expect(response["role"]).to eql user[:role]
    expect(response["userName"]).to eql user[:userName]
    expect(response["emailAddress"]).to eql user[:emailAddress]
  end
end

When(/^An invalid request to search for users by tokenExpDate is received$/) do
  @result = @service_client.post_to_url_with_auth("/users/search","{\"tokenExpDate\":\"2017-09-09\"}", "Bearer "+ @authToken)
end

When(/^An invalid request to search for users by token is received$/) do
  @result = @service_client.post_to_url_with_auth("/users/search","{\"tokenExpDate\":\"123456\"}", "Bearer "+ @authToken)
end

When(/^An invalid request to search for users by password is received$/) do
  @result = @service_client.post_to_url_with_auth("/users/search","{\"tokenExpDate\":\"123456\"}", "Bearer "+ @authToken)
end

When(/^An invalid request to search for users by href is received$/) do
  @result = @service_client.post_to_url_with_auth("/users/search","{\"tokenExpDate\":\"http://localhost:8080/users/123\"}", "Bearer "+ @authToken)
end