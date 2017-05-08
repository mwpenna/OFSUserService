@service
Feature: User is created when user endpoint is called

  Scenario: Try to create a user without lastName
    Given A ADMIN user exists for a company
    When A request to create a user is received with missing lastName
    Then the response should have a status of 400
    And I should see an error message with lastName missing

  Scenario: Try to create a user without Company
    Given A ADMIN user exists for a company
    When A request to create a user is received with missing company
    Then the response should have a status of 400
    And I should see an error message with company missing

  Scenario: Try to create a user without role
    Given A ADMIN user exists for a company
    When A request to create a user is received with missing role
    Then the response should have a status of 400
    And I should see an error message with role missing

  Scenario: Try to create a user without username
    Given A ADMIN user exists for a company
    When A request to create a user is received with missing userName
    Then the response should have a status of 400
    And I should see an error message with userName missing

  Scenario: Try to create a user without emailAddress
    Given A ADMIN user exists for a company
    When A request to create a user is received with missing emailAddress
    Then the response should have a status of 400
    And I should see an error message with emailAddress missing

  Scenario: Try to create a user with id
    Given A ADMIN user exists for a company
    When A create user request is received with id
    Then the response should have a status of 400
    And I should see an error message with id not allowed

  Scenario: Try to create a user with href
    Given A ADMIN user exists for a company
    When A create user request is received with href
    Then the response should have a status of 400
    And I should see an error message with href not allowed

  Scenario: Try to create a user with token
    Given A ADMIN user exists for a company
    When A create user request is received with token
    Then the response should have a status of 400
    And I should see an error message with token not allowed

  Scenario: Try to create a user with tokenExpDate
    Given A ADMIN user exists for a company
    When A create user request is received with tokenExpDate
    Then the response should have a status of 400
    And I should see an error message with tokenExpDate not allowed

  Scenario: Try to create a user with activeFlag
    Given A ADMIN user exists for a company
    When A create user request is received with activeFlag
    Then the response should have a status of 400
    And I should see an error message with activeFlag not allowed

  Scenario: Validate user company exists for user
    When A request to create a user is received with an invalid company
    Then the response should have a status of 400
    And I should see an error message with company does not exists

  Scenario: Validate user username does not exists
    Given A ADMIN user exists for a company
    When A request to create a user is received with a duplicate username
    Then the response should have a status of 400
    And I should see an error message with duplicate username

  Scenario: Validate user emailAddress does not exists
    Given A ADMIN user exists for a company
    When A request to create a user is received with a duplicate emailAddress
    Then the response should have a status of 400
    And I should see an error message with duplicate emailAddress

  Scenario: Validate user is created in DB
    Given A ADMIN user exists for a company
    When A request to create a user is received
    Then the response should have a status of 201
    And I should see the user was created

  Scenario: Validate when user is created location is returned in the header
    Given A ADMIN user exists for a company
    When A request to create a user is received
    Then the response should have a status of 201
    And I should see the location header populated

  Scenario: A request to create a user is received by a SYSTEM_ADMIN user
    Given A SYSTEM_ADMIN user exists for a company
    When A request to create a user is received
    Then the response should have a status of 201
    And I should see the location header populated

  Scenario: A request to create a user is received by an ADMIN user
    Given A ADMIN user exists for a company
    When A request to create a user is received
    Then the response should have a status of 201
    And I should see the location header populated

  Scenario: A request to create a user is received by an ACCOUNT_MANAGER user
    Given A ACCOUNT_MANAGER user exists for a company
    When A request to create a user is received
    Then the response should have a status of 400

  Scenario: A request to create a user is received by an WAREHOUSE user
    Given A WAREHOUSE user exists for a company
    When A request to create a user is received
    Then the response should have a status of 400

  Scenario: A request to create a user is received by an CUSTOMER user
    Given A CUSTOMER user exists for a company
    When A request to create a user is received
    Then the response should have a status of 400

  Scenario: A request to create a user is received by an ADMIN user for a diffrent company
    Given A ADMIN user exists for a company
    When A request to create a user is received for a diffrent company
    Then the response should have a status of 400

