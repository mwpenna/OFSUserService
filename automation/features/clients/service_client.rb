  class ServiceClient

    def get_base_uri
      return YAML.load(File.read("#{$PROJECT_ROOT}/features/clients/config/user.yml"))[ENV['ENVIRONMENT']]["base_uri"]
    end

  end
