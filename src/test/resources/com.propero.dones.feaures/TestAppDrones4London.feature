@Drones4London
Feature: Running a app which has one dispatcher server and two drones

  Scenario: Return an station found given some orders
    Given I am using a dispatcher server
    When I add 1 drones defined with pid:
        | 123 |
    Then I set the orders to be send toward the drones
      | 123 | "51.476105" | "-0.100224" | "2011-03-22 07:55:26" |
      | 123 | "51.475967" | "-0.100368" | "2011-03-22 07:55:40" |
    Then I defined per each dron a new thread to run in parallel orders

    And the drones should be able to find a Tube Stations which the following names
        | "Vauxhall" |



  Scenario: Return stations by different drones
    Given I am using a dispatcher server
    When I add 2 drones defined with pid
      | 123 | 321 |
    Then I set the orders to be send toward the drones
      | 123 | "51.476105" | "-0.100224" | "2011-03-22 07:55:26" |
      | 321 | "51.475967" | "-0.100368" | "2011-03-22 07:55:40" |
    And I defined per each dron a new thread to run in parallel the orders
    And the drones should be able to find a Tube Stations which the following names
      | "Vauxhall         |
      | "Elephan& Castle  |


