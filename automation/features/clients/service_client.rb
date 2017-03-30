require 'httparty'

class ServiceClient

    def get_base_uri
      return YAML.load(File.read("#{$PROJECT_ROOT}/features/clients/config/user.yml"))[ENV['ENVIRONMENT']]["base_uri"]
    end

    def post_to_url(path, body)
      return HTTParty.post((get_base_uri + path).to_str, :body=> body, :headers => { 'Content-Type' => 'application/json' })
    end

    def post_to_url_with_auth(path, body, authHeader)
      return HTTParty.post((get_base_uri + path).to_str, :body=> body, :headers => { 'Content-Type' => 'application/json', 'Authorization' => authHeader })
    end

    def get_by_url(url)
      return HTTParty.get(url);
    end

    def get_by_url_with_auth(url, authHeader)
      return HTTParty.get(url, :headers => { 'Authorization' => authHeader });
    end

    def delete_by_url(url)
      return HTTParty.delete(url);
    end
  end
