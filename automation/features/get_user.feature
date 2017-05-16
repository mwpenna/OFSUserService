@service
Feature: User is returned when get user endpoint is called

  Scenario: Try to get a valid user
    Given A company and user exists with token
    When A request to get the user is received
    Then the response should have a status of 200
    And I should see the user was returned

  Scenario: Try to get a user that does not exists
    When A request to get a user that does not exists
    Then the response should have a status of 404

  Scenario: User with role of SYSTEM_ADMIN can get any user
    Given A company and user exists with token
    When A request to get the user is received by SYSTEM_ADMIN
    Then the response should have a status of 200
    And I should see the user was returned for a SYSTEM_ADMIN

  Scenario: User with role of ADMIN cannot get user from different company
    Given A ADMIN user exists for a company
    And A company and user exists
    When A request to get the user is received by the ADMIN user for a different company
    Then the response should have a status of 400

  Scenario: User with role of ADMIN can view users within their company
    Given A ADMIN user exists for a company
    And A user exists for the ADMIN company
    When A request to get the user is received
    Then the response should have a status of 200
    And I should see the user was returned

  Scenario: User with role of ADMIN can get their user info
    Given A ADMIN user exists for a company
    When A request to get the user is received
    Then the response should have a status of 200
    And I should see the user was returned

  Scenario: User with role of ACCOUNT_MANAGER can only get there user info
    Given A ACCOUNT_MANAGER user exists for a company
    And A company and user exists
    When A request to get a different user is received
    Then the response should have a status of 400

  Scenario: User with role of ACCOUNT_MANAGER can get their user info
    Given A ACCOUNT_MANAGER user exists for a company
    When A request to get the user is received
    Then the response should have a status of 200
    And I should see the user was returned

  Scenario: User with role of CUSTOMER can only get there user info
    Given A CUSTOMER user exists for a company
    And A company and user exists
    When A request to get a different user is received
    Then the response should have a status of 400

  Scenario: User with role of CUSTOMER can get their user info
    Given A CUSTOMER user exists for a company
    When A request to get the user is received
    Then the response should have a status of 200
    And I should see the user was returned

  Scenario: User with role of WAREHOUSE can only get there user info
    Given A WAREHOUSE user exists for a company
    And A company and user exists
    When A request to get a different user is received
    Then the response should have a status of 400

  Scenario: User with role of WAREHOUSE can get their user info
    Given A WAREHOUSE user exists for a company
    When A request to get the user is received
    Then the response should have a status of 200
    And I should see the user was returned