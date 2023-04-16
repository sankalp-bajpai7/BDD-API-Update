Feature: Validate API test

  @ValidCredentials @Login
  Scenario Outline: Login with valid credentials
    Given the request api is formed with "<requestbody>"
    When User sends the request api with valid requestbody "<username>" "<password>"
    Then User should be able to validate valid status code "<statuscode>"
    And I want to append the "<username>" to the db
    Examples:
      |requestbody| username | password | statuscode |
      |validrequest| TOOLSQA-Test | Test@@123 | 200 |




  @InValidCredentials @Login
  Scenario Outline: Login with invalid credentials
    Given the request api is formed with "<requestbody>"
    When User sends the request api with invalid requestbody "<password>"
    Then User should be able to validate invalid status code "<invalidstatuscode>"
    Examples:
      |requestbody| password | invalidstatuscode|
      |invalidrequest| check123 | 400 |

  @ValidCredentials
  Scenario: I want to connect to mongo db and access a record
    Given I establish a connection
    When I access the record needed
    Then I am displayed the record



