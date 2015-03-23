@crash
Feature: Crash

  Scenario: Crash the app
    Given I press the "Functions" button
    Then I wait upto 5 seconds for the "FC_Functions" screen to appear
    Then I take a screenshot
    Then I press the "Explode" button 

