@service
Feature: User is updated when update user endpoint is called

  Scenario: Try to update a user that does not exits
    When A request to update a user that does not exists
    Then the response should have a status of 404

  Scenario: Try to update a users id
    Given A company and user exists
    When A request to update the user id
    Then the response should have a status of 400
    And I should see an error message indicating id not allowed

  Scenario: Try to update a users href
    Given A company and user exists
    When A request to update the user href
    Then the response should have a status of 400
    And I should see an error message indicating href not allowed

  Scenario: Try to update a users createdOn
    Given A company and user exists
    When A request to update the user createdOn
    Then the response should have a status of 400
    And I should see an error message indicating createdOn not allowed

  Scenario: Try to update a users token
    Given A company and user exists
    When A request to update the user token
    Then the response should have a status of 400
    And I should see an error message indicating token not allowed

  Scenario: Try to update a users company
    Given A company and user exists
    When A request to update the user company
    Then the response should have a status of 400
    And I should see an error message indicating company not allowed

  Scenario: Try to update a users userName
    Given A company and user exists
    When A request to update the user userName
    Then the response should have a status of 400
    And I should see an error message indicating userName not allowed

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
    And I should see the password was updated

  Scenario: Try to update a users emailAddress
    Given A company and user exists
    When A request to update the user emailAddress
    Then the response should have a status of 204
    And I should see the emailAddress was updated