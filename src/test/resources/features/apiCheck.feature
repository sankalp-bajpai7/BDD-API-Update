Feature: Validate API test

  @ValidCredentials @Login
  Scenario Outline: Login with validated credentials
    Given the request api is formed with valid requestbody
    When User sends the request api with appropriate requestbody "<username>" "<password>"
    Then User should be able to validate success status code "<statuscode>"
    Examples:
      | username | password | statuscode |
      | TOOLSQA-Test | Test@@123 | 200 |