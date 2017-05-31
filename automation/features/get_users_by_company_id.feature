@service
Feature: User list is returned when get user by company id endpoint is called

  Scenario: User List is returned when a request to get users by company id is received
    Given A company and multiple users exists
    When A request to get the users by company id is received
    Then the response should have a status of 200
    And I should see the user list was returned

  Scenario: Empty User List is returned when a request to get users by company id is received
    Given A company exists without users
    When A request to get the users by company id is received
    Then the response should have a status of 200
    And I should see an empty list was returned

  Scenario: Empty User list is returned when a company does not exists
    When A request to get the users for a company id that does not exists is received
    Then the response should have a status of 200
    And I should see an empty list was returned