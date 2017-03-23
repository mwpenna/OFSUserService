class Company
  attr_accessor :name, :id, :href

  def base_uri
    ServiceClient.base_uri
  end

  def to_json
    {
        id: self.id,
        href: self.href,
        name: self.name
    }.to_json
  end

  def to_hash
    {
        id: self.id,
        href: self.href,
        name: self.name
    }
  end

  def create_to_hash
    {
        name: self.name
    }
  end

  def create_to_json
    {
        name: self.name
    }.to_json
  end
end