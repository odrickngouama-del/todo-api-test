Feature: Task Service

  Scenario: Create and fetch task
    When I create a task with label "BDD Task" and description "bdd"
    Then status should be 201
    And I store task id
    When I fetch task by id
    Then status should be 200
    And label should be "BDD Task"

  Scenario: Create a task
    When I create a task ...
    Then status should be 201


  Scenario: Invalid label
    When I create a task with label "abc"
    Then status should be 400

  Scenario Outline: Invalid label
    When I create a task with label "<label>"
    Then status should be 400

    Examples:
      | label |
      | abc   |
      | a     |
      | ""    |