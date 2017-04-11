@service
Feature: User is authenticated when endpoint is called

  Scenario: Try to authenticate a user without authorization header
  Scenario: Try to authenticate a user without beaerer authorization
  Scenario: Try to authenticate a user with invalid JWT
  Scenario: Try to authenticate a user that is not found
  Scenario: Try to authenticate a user with a token that has expired
  Scenario: Try to authenticate a user that token does not match
  Scenario: Try to authenticate a user that does not have a token stored in db
  Scenario: Authenticate a user succeeds and tokenExpDate updated
