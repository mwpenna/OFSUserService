@service
Feature: User is deleted when delete user is called

  Scenario: Try to delete a user that exists
    Given A company and user exists with token
    When A request is made to delete the user
    Then the response should have a status of 204
    And I should see the user does not exists

  Scenario: Try to delete a user that does not exists
    When A request is made to delete a user the does not exists
    Then the response should have a status of 404

  Scenario: Try to delete a user with a role that is not SYSTEM_ADMIN
    Given A company and user exists with token
    When A request is made to delete the user with a role that is not SYSTEM_ADMIN
    Then the response should have a status of 400