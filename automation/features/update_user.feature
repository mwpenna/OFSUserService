@service
Feature: User is updated when update user endpoint is called

  Scenario: Try to update a user that does not exits
    When A request to update a user that does not exists
    Then the response should have a status of 404

  Scenario: Try to update a users id
    Given A company and user exists
    When A request to update the user with invalid id
    Then the response should have a status of 400
    And I should see an error message with id not allowed

  Scenario: Try to update a users href
    Given A company and user exists
    When A request to update the user with invalid href
    Then the response should have a status of 400
    And I should see an error message with href not allowed

  Scenario: Try to update a users createdOn
    Given A company and user exists
    When A request to update the user with invalid createdOn
    Then the response should have a status of 400
    And I should see an error message with createdOn not allowed

  Scenario: Try to update a users token
    Given A company and user exists
    When A request to update the user with invalid token
    Then the response should have a status of 400
    And I should see an error message with token not allowed

  Scenario: Try to update a users company
    Given A company and user exists
    When A request to update the user with invalid company
    Then the response should have a status of 400
    And I should see an error message with company not allowed

  Scenario: Try to update a users userName
    Given A company and user exists
    When A request to update the user with invalid userName
    Then the response should have a status of 400
    And I should see an error message with userName not allowed

  Scenario: Try to update a users tokenExpDate
    Given A company and user exists
    When A request to update the user tokenExpDate
    Then the response should have a status of 204
    And I should see the tokenExpDate was updated

  Scenario: Try to update a users activeFlag
    Given A company and user exists
    When A request to update the user activeFlag
    Then the response should have a status of 204
    And I should see the activeFlag was updated

  Scenario: Try to update a users firstName
    Given A company and user exists
    When A request to update the user firstName
    Then the response should have a status of 204
    And I should see the firstName was updated

  Scenario: Try to update a users lastName
    Given A company and user exists
    When A request to update the user lastName
    Then the response should have a status of 204
    And I should see the lastName was updated

  Scenario: Try to update a users role
    Given A company and user exists
    When A request to update the user role
    Then the response should have a status of 204
    And I should see the role was updated

  Scenario: Try to update a users role
    Given A company and user exists
    When A request to update the user role with invalid role
    Then the response should have a status of 400
    And I should see an error message indicating role invaild enum

  Scenario: Try to update a users password
    Given A company and user exists
    When A request to update the user password
    Then the response should have a status of 204
    And I should see the password has changed

  Scenario: Try to update a users emailAddress
    Given A company and user exists
    When A request to update the user emailAddress
    Then the response should have a status of 204
    And I should see the emailAddress was updated