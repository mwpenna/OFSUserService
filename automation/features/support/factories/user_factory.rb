require_relative '../env'

def base_uri
  ServiceClient.get.base_uri
end

FactoryGirl.define do

  factory :user, class: User do
    firstName "fname"
    lastName "lname"
    role "ADMIN"
    userName "fname.lname"
    password "password"
    emailAddress "something@something.com"
  end

end