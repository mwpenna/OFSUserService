@service
Feature: Company is created when company endpoint is called

  Scenario: Try to create a company without name
    When A request to create a company is received with missing name
    Then the response should have a status of 400
    And I should see an error message indicating name missing

  Scenario: Try to create a company with id
    When A create company request is received with id
    Then the response should have a status of 400
    And I should see an error message indicating id not allowed

  Scenario: Try to create a company with href
    When A create company request is received with href
    Then the response should have a status of 400
    And I should see an error message indicating href not allowed

  Scenario: Validate when company is created location is returned in the header
    When A request to create a company is received
    Then the response should have a status of 201
    And I should see the location header populated

  Scenario: Validate user with role that is not SYSTEM_ADMIN cannot create company
    Given A ADMIN user exists for a company
    When A request to create a company is received from user with invalid role
    Then the response should have a status of 400