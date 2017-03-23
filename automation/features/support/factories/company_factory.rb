require_relative '../env'
require 'faker'

def base_uri
  ServiceClient.get.base_uri
end

FactoryGirl.define do

  factory :company, class: Company do
    name "name"
    href ""
    id ""
  end

end