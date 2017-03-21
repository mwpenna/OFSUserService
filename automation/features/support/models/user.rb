class User

  attr_accessor :id, :firstName, :lastName, :role, :userName, :password,
                :emailAddress, :token, :tokenExpDate, :activeFlag, :company_href, :company_name

  def href
    "#{self.base_uri}/user/id/#{id}"
  end

  def href=(url)
    @href = url
  end

  def base_uri
    ServiceClient.base_uri
  end

  def to_json
    {
      id: self.id,
      firstName: self.firstName,
      lastName: self.lastName,
      company: {
          href: self.company_href,
          name: self.company_name
      },
      role: self.role,
      username: self.userName,
      password: self.password,
      emailAddress: self.emailAddress,
      token: self.token,
      tokenExpDate: self.tokenExpDate,
      activeFlag: self.activeFlag
    }.to_json
  end

  def to_hash
    {
        id: self.id,
        firstName: self.firstName,
        lastName: self.lastName,
        company: {
            href: self.company_href,
            name: self.company_name
        },
        role: self.role,
        username: self.userName,
        password: self.password,
        emailAddress: self.emailAddress,
        token: self.token,
        tokenExpDate: self.tokenExpDate,
        activeFlag: self.activeFlag
    }
  end

  def create_to_hash
    {
        firstName: self.firstName,
        lastName: self.lastName,
        company: {
            href: self.company_href,
            name: self.company_name
        },
        role: self.role,
        username: self.userName,
        password: self.password,
        emailAddress: self.emailAddress
    }
  end

  def create_to_json
    {
        firstName: self.firstName,
        lastName: self.lastName,
        company: {
            href: self.company_href,
            name: self.company_name
        },
        role: self.role,
        username: self.userName,
        password: self.password,
        emailAddress: self.emailAddress
    }.to_json
  end
end