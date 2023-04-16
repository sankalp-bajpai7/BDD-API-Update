Feature: Validate post login tests

  @AuthorizedUser
Scenario Outline: An authorized user is able to add books
    Given User has a valid auth token with  "<requestbody>" "<username>" "<password>" "<statuscode>"
    When A list of books is available
    And User adds a book to the reading list "<requestbody1>""<bookId>"
    Then Validate that the book is added successfully
    Examples:
      |requestbody| username | password | statuscode |requestbody1|bookId|
      |validrequest| TOOLSQA-Test | Test@@123 | 200 | addabookrequest|      |