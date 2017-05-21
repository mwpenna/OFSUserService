@service
Feature: User is returned when get user by token endpoint is called

  Scenario: A user exists with a token. When the token is received by the getUserByToken endpoint the user info should be returned
    Given A company and user exists with token
    When A request to get the user by token is received
    Then the response should have a status of 200
    And I should see the user was returned