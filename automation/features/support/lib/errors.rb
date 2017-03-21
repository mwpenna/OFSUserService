class Errors
  def self.required_field_missing(field)
    {"code" => "user.#{field.underscore}.required_field_missing",
     "property" => field,
     "message" => "Validation error. Cannot create User without #{field}.",
     "developerMessage" => "Missing required field #{field.split('.').last}",
     "properties" => {"field" => field}}
  end
end