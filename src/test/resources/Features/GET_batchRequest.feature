#Author : Hima

@BatchGet
Feature: Batch module API testing

  @BatchGet_01
  Scenario: Verify GetAll Batches with valid status code and schema
    Given User verify  batch module url is set with valid url
    When User GET All batches 
    Then User gets success "200" status code

 