@service
Feature: User is returned when get user endpoint is called

  Scenario: Try to get a valid user
    Given A company and user exists
    When A request to get the user is received
    Then the response should have a status of 200
    And I should see the user was returned

  Scenario: Try to get a user that does not exists
    When A request to get a user that does not exists
    Then the response should have a status of 404