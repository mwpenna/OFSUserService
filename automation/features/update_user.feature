@service
Feature: User is updated when update user endpoint is called

  Scenario: Try to update a user that does not exits
    When A request to update a user that does not exists
    Then the response should have a status of 404

  Scenario: Try to update a users id
    Given A company and user exists with token
    When A request to update the user with invalid id
    Then the response should have a status of 400
    And I should see an error message with id not allowed

  Scenario: Try to update a users href
    Given A company and user exists with token
    When A request to update the user with invalid href
    Then the response should have a status of 400
    And I should see an error message with href not allowed

  Scenario: Try to update a users createdOn
    Given A company and user exists with token
    When A request to update the user with invalid createdOn
    Then the response should have a status of 400
    And I should see an error message with createdOn not allowed

  Scenario: Try to update a users token
    Given A company and user exists with token
    When A request to update the user with invalid token
    Then the response should have a status of 400
    And I should see an error message with token not allowed

  Scenario: Try to update a users company
    Given A company and user exists with token
    When A request to update the user with invalid company
    Then the response should have a status of 400
    And I should see an error message with company not allowed

  Scenario: Try to update a users userName
    Given A company and user exists with token
    When A request to update the user with invalid userName
    Then the response should have a status of 400
    And I should see an error message with userName not allowed

  Scenario: Try to update a users tokenExpDate
    Given A company and user exists with token
    When A request to update the user tokenExpDate
    Then the response should have a status of 204
    And I should see the tokenExpDate was updated

  Scenario: Try to update a users activeFlag
    Given A company and user exists with token
    When A request to update the user activeFlag
    Then the response should have a status of 204
    And I should see the activeFlag was updated

  Scenario: Try to update a users firstName
    Given A company and user exists with token
    When A request to update the user firstName
    Then the response should have a status of 204
    And I should see the firstName was updated

  Scenario: Try to update a users lastName
    Given A company and user exists with token
    When A request to update the user lastName
    Then the response should have a status of 204
    And I should see the lastName was updated

  Scenario: Try to update a users role
    Given A company and user exists with token
    When A request to update the user role
    Then the response should have a status of 204
    And I should see the role was updated

  Scenario: Try to update a users role
    Given A company and user exists with token
    When A request to update the user role with invalid role
    Then the response should have a status of 400
    And I should see an error message indicating role invaild enum

  Scenario: Try to update a users password
    Given A company and user exists with token
    When A request to update the user password
    Then the response should have a status of 204
    And I should see the password has changed

  Scenario: Try to update a users emailAddress
    Given A company and user exists with token
    When A request to update the user emailAddress
    Then the response should have a status of 204
    And I should see the emailAddress was updated

  Scenario: User with role of SYSTEM_ADMIN can update any user
    Given A company and user exists with token
    When A request to update the user emailAddress is received by SYSTEM_ADMIN
    Then the response should have a status of 204
    And I should see the emailAddress was updated

  Scenario: User with role of ADMIN cannot get user from different company
    Given A ADMIN user exists for a company
    And A company and user exists
    When A request to update the user is received by the ADMIN user for a different company
    Then the response should have a status of 400

  Scenario: User with role of ADMIN can update users within their company
    Given A ADMIN user exists for a company
    And A user exists for the ADMIN company
    When A request to update the users emailAddress is received
    Then the response should have a status of 204
    And I should see the emailAddress was updated

  Scenario: User with role of ADMIN can update their user info
    Given A ADMIN user exists for a company
    When A request to update the users emailAddress is received
    Then the response should have a status of 204
    And I should see the emailAddress was updated

  Scenario: User with role of ACCOUNT_MANAGER can only update there user info
    Given A ACCOUNT_MANAGER user exists for a company
    And A company and user exists
    When A request to update a different user is received
    Then the response should have a status of 400

  Scenario: User with role of ACCOUNT_MANAGER can update their user info
    Given A ACCOUNT_MANAGER user exists for a company
    When A request to update the users emailAddress is received
    Then the response should have a status of 204
    And I should see the emailAddress was updated

  Scenario: User with role of CUSTOMER can only update there user info
    Given A CUSTOMER user exists for a company
    And A company and user exists
    When A request to update a different user is received
    Then the response should have a status of 400

  Scenario: User with role of CUSTOMER can update their user info
    Given A CUSTOMER user exists for a company
    When A request to update the users emailAddress is received
    Then the response should have a status of 204
    And I should see the emailAddress was updated

  Scenario: User with role of WAREHOUSE can only update there user info
    Given A WAREHOUSE user exists for a company
    And A company and user exists
    When A request to update a different user is received
    Then the response should have a status of 400

  Scenario: User with role of WAREHOUSE can update their user info
    Given A WAREHOUSE user exists for a company
    When A request to update the users emailAddress is received
    Then the response should have a status of 204
    And I should see the emailAddress was updated