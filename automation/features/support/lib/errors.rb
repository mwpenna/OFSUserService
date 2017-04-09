class Errors
  def self.required_field_missing(field)
    {"code" => "user.#{field.underscore}.required_field_missing",
     "property" => field,
     "message" => "Validation error. Cannot create User without #{field}.",
     "developerMessage" => "Missing required field #{field.split('.').last}",
     "properties" => {"field" => field}}
  end

  def self.field_not_acceptable(field)
    {"code" => "user.#{field.underscore}.not_acceptable",
     "property" => field,
     "message" => "Validation error. Cannot create user with #{field} in request. The value of #{field} is a system-generated read-only value.",
     "developerMessage" => "instance matched a schema which it should not have",
     "properties" => {"field" => field}}
  end

  def self.company_id_invalid
    {"code"=>"user.company.id.invalid",
     "property"=>"company.id",
     "message"=>"Invalid company id. Company does not exits.",
     "developerMessage"=>"Invalid company id. Company does not exits.",
     "properties"=>{"field"=>"company.id"}}
  end

  def self.username_exists
    {"code"=>"user.username.exists",
     "property"=>"user.username",
     "message"=>"Invalid username. Username already exists.",
     "developerMessage"=>"Invalid username. Username already exists.",
     "properties"=>{"field"=>"user.username"}}
  end

  def self.emailaddress_exists
    {"code"=>"user.emailaddress.exists",
     "property"=>"user.emailaddress",
     "message"=>"Invalid email address. Email address already exists.",
     "developerMessage"=>"Invalid email address. Email address already exists.",
     "properties"=>{"field"=>"user.emailaddress"}}
  end

  def self.company_required_field_missing(field)
    {"code" => "company.#{field.underscore}.required_field_missing",
     "property" => field,
     "message" => "Validation error. Cannot create Company without #{field}.",
     "developerMessage" => "Missing required field #{field.split('.').last}",
     "properties" => {"field" => field}}
  end

  def self.company_field_not_acceptable(field)
    {"code" => "company.#{field.underscore}.not_acceptable",
     "property" => field,
     "message" => "Validation error. Cannot create Company with #{field} in request. The value of #{field} is a system-generated read-only value.",
     "developerMessage" => "instance matched a schema which it should not have",
     "properties" => {"field" => field}}
  end

  def self.user_authentication_failed
    {"code"=>"user.authentication.failed",
     "message"=>"Username/Password is not valid. Please retry with correct credentials.",
     "developerMessage"=>"Username/Password is not valid. Please retry with correct credentials."}
  end

  def self.user_role_invalid
    {"code"=>"user.role.invalid_enum_value",
     "property"=>"role",
     "message"=>"Validation error. Cannot create user with status - must be ADMIN, ACCOUNT_MANAGER, WAREHOUSE, CUSTOMER. Passed in value TEST.",
     "developerMessage"=>"instance value (\"TEST\") not found in enum (possible values: [\"ADMIN\",\"ACCOUNT_MANAGER\",\"WAREHOUSE\",\"CUSTOMER\"])",
     "properties"=>{"field"=>"role", "valid"=>"[\"ADMIN\",\"ACCOUNT_MANAGER\",\"WAREHOUSE\",\"CUSTOMER\"]", "found"=>"TEST"}
    }
  end
end