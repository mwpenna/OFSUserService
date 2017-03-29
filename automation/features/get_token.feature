@service
Feature: Token is generated and returned when get token is called

  Scenario: Try to get a token when basic auth is missing
    When A request is made to get a token with authentication header missing
    Then the response should have a status of 403

  Scenario: Try to get a token when basic auth value missing
    When A request is made to get a token with authentication header null
    Then the response should have a status of 403

  Scenario: Try to get a token with invalid password
    Given A company and user exists
    When A request is made to get a token with invalid password
    Then the response should have a status of 400
    And I should see an error message indicating user authentication failed

  Scenario: Try to get a token with invalid user
    When A request is made to get a token for a user that does not exists
    Then the response should have a status of 400
    And I should see an error message indicating user authentication failed

  @pending
  Scenario: Try to get a token when user is inactive
    Given A user exists in an inactive state
    When A request is made to get a token with valid username and password
    Then the response should have a status of 400
    And I should see an error message indicating user is not active

  Scenario: Try to get a token with valid username/password
    Given A company and user exists
    When A request is made to get a token with valid username and password
    Then the response should have a status of 200
    And I should see a token response was returned

  Scenario: User updated with token after get token is called
    Given A company and user exists
    When A request is made to get a token with valid username and password
    Then the response should have a status of 200
    And I should see the user is populated with a token

  Scenario: User updated with token exp date after get token is called
    Given A company and user exists
    When A request is made to get a token with valid username and password
    Then the response should have a status of 200
    And I should see the user is populated with a tokenExpDate

  Scenario: Get token is called a second time for a user token response is correct
    Given A company and user exists with token
    When A request is made to get a token with valid username and password
    Then the response should have a status of 200
    And I should see a token response was returned

  Scenario: Get token is called a second time for a user token update for user in db
    Given A company and user exists with token
    When A request is made to get a token with valid username and password
    Then the response should have a status of 200
    And I should see the user is updated with the new token values

