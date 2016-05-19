@UItest
Feature: Test UI components
   
  Scenario: Test orientation
    Given I start the test
    Then I take a screenshot
    Then I press view with id "mm_iv_title"
    Then I set screen to landscape
    Then I wait
    Then I take a screenshot
    Then I scroll down
    Then I wait
    Then I take a screenshot
    Then I set screen to portrait
    Then I go back to start

  Scenario: Run native activity, arrange tiles correctly
    Given I start the test
    Then I press the "Native Activity" button
    Then I wait
    Then I take a screenshot
    Then I press image button number 3
    Then I press image button number 2
    Then I press image button number 6
    Then I press image button number 5
    Then I press image button number 6
    Then I press image button number 3
    Then I press image button number 6
    Then I press image button number 5
    Then I should see text containing "Correct"
    Then I take a screenshot
    Then I go back to start

  Scenario: Run native activity, arrange tiles incorrectly
    Given I start the test
    Then I press the "Native Activity" button
    Then I wait
    Then I take a screenshot
    Then I press tiles nbr eight & five ten times
    Then I should see text containing "Ask mommy"
    Then I take a screenshot
    Then I go back to start

  Scenario: Run native activity, tap the score button
    Given I start the test
    Then I press the "Native Activity" button
    Then I wait
    Then I take a screenshot
    Then I press view with id "na_tv_score"
    Then I should see text containing "Ask mommy"
    Then I take a screenshot
    Then I press view with id "na_tv_score"
    Then I wait
    Then I take a screenshot
    Then I go back to start

  Scenario: Run native activity, tap the score button and select incorrect options
    Given I start the test
    Then I press the "Native Activity" button
    Then I wait
    Then I take a screenshot
    Then I press view with id "na_tv_score"
    Then I should see text containing "Ask mommy"
    Then I take a screenshot
    Then I press view with id "na_rb_option1"
    Then I enter text "Hello Calabash" into field with id "na_et_name"
    Then I wait
    Then I take a screenshot
    Then I hide keyboard
    Then I press the "Answer" button
    Then I should see text containing "Oops"
    Then I take a screenshot
    Then I go back
    Then I press view with id "na_rb_option3"
    Then I wait
    Then I take a screenshot
    Then I press the "Answer" button
    Then I should see text containing "Oops"
    Then I take a screenshot
    Then I go back
    Then I go back to start

  Scenario: Run native activity, tap the score button and select correct option
    Given I start the test
    Then I press the "Native Activity" button
    Then I wait
    Then I take a screenshot
    Then I press view with id "na_tv_score"
    Then I should see text containing "Ask mommy"
    Then I take a screenshot
    Then I press view with id "na_rb_option2"
    Then I enter text "Hello Calabash" into field with id "na_et_name"
    Then I wait
    Then I take a screenshot
    Then I hide keyboard
    Then I press the "Answer" button
    Then I should see text containing "Correct"
    Then I should see text containing "Hello Calabash"
    Then I take a screenshot
    Then I go back to start

  @fail
  Scenario: Failing on purpose 
    Given I start the test
    Then I press the "Native Activity" button
    Then I wait
    Then I take a screenshot
    Then I press view with id "na_tv_score"
    Then I should see text containing "Ask mummy"
    Then I take a screenshot
    Then I press view with id "na_rb_option2"
    Then I enter text "Hello Calabash" into field with id "na_et_name"
    Then I wait
    Then I take a screenshot
    Then I hide keyboard
    Then I press the "Answer" button
    Then I should see text containing "Correct"
    Then I should see text containing "Hello Calabash"
    Then I take a screenshot
    Then I go back to start
