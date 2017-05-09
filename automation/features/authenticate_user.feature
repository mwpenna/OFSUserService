@service
Feature: User is authenticated when endpoint is called

  Scenario: Try to authenticate a user without authorization header
    Given A company and user exists with token
    When A request to authenticate the user without authorization header
    Then the response should have a status of 403

  Scenario: Try to authenticate a user without Bearer authorization
    Given A company and user exists with token
    When A request to authenticate the user without Bearer authorization
    Then the response should have a status of 403

  Scenario: Try to authenticate a user with invalid JWT
    Given A company and user exists with token
    When A request to authenticate the user with invalid JWT token
    Then the response should have a status of 403

  Scenario: Try to authenticate a user that is not found
    When A request to authenticate a user that does not exists
    Then the response should have a status of 403

  Scenario: Try to authenticate a user with a token that has expired
    Given A company and user exists with token
    When A request to authenticate the user with expired JWT token
    Then the response should have a status of 403

  Scenario: Try to authenticate a user that token does not match
    Given A company and user exists with token
    When A request to authenticate the user with wrong JWT token
    Then the response should have a status of 403

  Scenario: Authenticate a user succeeds and userHref is returned
    Given A company and user exists with token
    When A request to authenticate the user
    Then the response should have a status of 200
    And I should see the href is returned

  Scenario: Authenticate a user succeeds and companyHref is returned
    Given A company and user exists with token
    When A request to authenticate the user
    Then the response should have a status of 200
    And I should see the companyHref is returned

  Scenario: Authenticate a user succeeds and firstName is returned
    Given A company and user exists with token
    When A request to authenticate the user
    Then the response should have a status of 200
    And I should see the firstName is returned

  Scenario: Authenticate a user succeeds and lastName is returned
    Given A company and user exists with token
    When A request to authenticate the user
    Then the response should have a status of 200
    And I should see the lastName is returned

  Scenario: Authenticate a user succeeds and role is returned
    Given A company and user exists with token
    When A request to authenticate the user
    Then the response should have a status of 200
    And I should see the role is returned

  Scenario: Authenticate a user succeeds and userName is returned
    Given A company and user exists with token
    When A request to authenticate the user
    Then the response should have a status of 200
    And I should see the userName is returned

  Scenario: Authenticate a user succeeds and userEmailAddress is returned
    Given A company and user exists with token
    When A request to authenticate the user
    Then the response should have a status of 200
    And I should see the emailAddress is returned

  Scenario: Authenticate a user succeeds and tokenExpDate is updated
    Given A company and user exists with token
    When A request to authenticate the user
    Then the response should have a status of 200
    And I should see the tokenExp was updated
