require_relative '../env'

def base_uri
  ServiceClient.get.base_uri
end

FactoryGirl.define do

  factory :user, class: User do
    firstName "fname"
    lastName "lname"
    role "ADMIN"
    company_href "http://localhost:8082/company/id/2fee4545-1997-4e87-9ae0-a6fb40101934"
    company_name "demoCompany"
    userName "fname.lname"
    password "password"
    emailAddress "something@something.com"
  end

end