require 'pry'
require 'pry-nav'
require 'factory_girl'
require 'test-helpers/all'
require 'chronic'
require 'active_record'
require 'faker'

$PROJECT_ROOT = File.expand_path(File.join(File.dirname(__FILE__), '../..'))
Dir["#{$PROJECT_ROOT}/features/support/lib/*.rb"].each { |file| require file }
Dir["#{$PROJECT_ROOT}/features/support/models/*.rb"].each { |file| require file }

environment = ENV['ENVIRONMENT'] ||='local'

def yaml(file)
  YAML.load(File.read("#{$PROJECT_ROOT}/config/#{file}.yml"))[ENV['ENVIRONMENT']]
end

include TestHelpers::Wait