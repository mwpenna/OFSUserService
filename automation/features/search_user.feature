@service
Feature: User list is returned when search user by endpoint is called

  Scenario: Bad Request is returned when a request to search for users is received by a user with ACCOUNT_MANAGER role
    Given A ACCOUNT_MANAGER user exists for a company
    When A request to search for users is received
    Then the response should have a status of 400

  Scenario: Bad Request is returned when a request to search for users is received by a user with WAREHOUSE role
    Given A WAREHOUSE user exists for a company
    When A request to search for users is received
    Then the response should have a status of 400

  Scenario: Bad Request is returned when a request to search for users is received by a user with CUSTOMER role
    Given A CUSTOMER user exists for a company
    When A request to search for users is received
    Then the response should have a status of 400

  Scenario: Bad Request is returned when a request to search for users is received with a search parameter of id
    Given A company and multiple users exists
    When A request to search for users by id is received
    Then the response should have a status of 400

  Scenario: Bad Request is returned when a request to search for users is received with a search parameter of company
    Given A company and multiple users exists
    When A request to search for users by company is received
    Then the response should have a status of 400

  Scenario: Bad Request is returned when a request to search for users is received with a search parameter of createdOn
    Given A company and multiple users exists
    When A request to search for users by createdOn is received
    Then the response should have a status of 400

  Scenario: Bad Request is returned when a request to search for users is received with a search parameter of tokenExpDate
    Given A company and multiple users exists
    When A request to search for users by tokenExpDate is received
    Then the response should have a status of 400

  Scenario: Bad Request is returned when a request to search for users is received with a search parameter of token
    Given A company and multiple users exists
    When A request to search for users by token is received
    Then the response should have a status of 400

  Scenario: Bad Request is returned when a request to search for users is received with a search parameter of password
    Given A company and multiple users exists
    When A request to search for users by password is received
    Then the response should have a status of 400

  Scenario: Bad Request is returned when a request to search for users is received with a search parameter of href
    Given A company and multiple users exists
    When A request to search for users by href is received
    Then the response should have a status of 400

  Scenario: User List is returned when a request to search for users by firstname is received
    Given A company and multiple users exists with similar firstname
    When A request to search for users by firstname is received
    Then the response should have a status of 200
    And I should see the user search list was returned

  Scenario: User List is returned when a request to search for users by lastname is received
    Given A company and multiple users exists with similar lastname
    When A request to search for users by lastname is received
    Then the response should have a status of 200
    And I should see the user search list was returned

  Scenario: User List is returned when a request to search for users by role is received
    Given A company and multiple users exists with similar role
    When A request to search for users by role is received
    Then the response should have a status of 200
    And I should see the user search list was returned

  Scenario: User List is returned when a request to search for users by username is received
    Given A company and multiple users exists with similar username
    When A request to search for users by username is received
    Then the response should have a status of 200
    And I should see the user search list was returned

  Scenario: User List is returned when a request to search for users by emailaddress is received
    Given A company and multiple users exists with similar emailaddress
    When A request to search for users by emailaddress is received
    Then the response should have a status of 200
    And I should see the user search list was returned

  Scenario: User List is returned when a request to search for users by activeflag is received
    Given A company and multiple users exists with similar activeflag
    When A request to search for users by activeflag is received
    Then the response should have a status of 200
    And I should see the user search list was returned