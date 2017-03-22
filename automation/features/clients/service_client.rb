require 'httparty'

class ServiceClient

    def get_base_uri
      return YAML.load(File.read("#{$PROJECT_ROOT}/features/clients/config/user.yml"))[ENV['ENVIRONMENT']]["base_uri"]
    end

    def post_to_url(path, body)
      return HTTParty.post((get_base_uri + path).to_str, :body=> body, :headers => { 'Content-Type' => 'application/json' })
    end

    def get_by_url(url)
      return HTTParty.get(url);
    end

  end
