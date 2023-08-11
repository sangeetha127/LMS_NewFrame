Feature: Program module
  
    @1
    Scenario Outline: create a program id
    Given The POST endpoint and the request payload from "<Sheetname>" and <RowNumber> for program
    When I send a POST reqeust for creating an program
    Then The program is successfully created with status 201
    
    Examples: 
      | Sheetname | RowNumber |
      | Sheet1    |         0 |
      | Sheet1    |         1 |
   
   @2  
   Scenario: Check if user able to delete a program with valid program ID
   Given User creates DELETE Request valid program ID
   When user will send the request with valid program iD
   Then Response body should be 200 successfully deleted