class User

  attr_accessor :id, :firstName, :lastName, :company, :role, :userName, :password, :emailAddress, :token, :tokenExpDate, :activeFlag

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
      company: self.company,
      role: self.role,
      userName: self.userName,
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
        company: self.company,
        role: self.role,
        userName: self.userName,
        password: self.password,
        emailAddress: self.emailAddress,
        token: self.token,
        tokenExpDate: self.tokenExpDate,
        activeFlag: self.activeFlag
    }
  end
end