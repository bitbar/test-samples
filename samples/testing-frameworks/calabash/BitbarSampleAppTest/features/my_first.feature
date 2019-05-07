Feature: Click items

  Scenario: Run whole app
    Then I set screen to portrait
    Then I take a screenshot
    Then I set screen to landscape
    # Wait a bit to see landscape in screenshot
    Then I wait
    Then I take a screenshot
    Then I set screen to portrait
    Then I press view with id "radio0"
    Then I press view with id "radio1"
    Then I press view with id "radio2"
    Then I take a screenshot
    Then I enter text "Hello Calabash" into field with id "editText1"
    Then I take a screenshot
    Then I hide keyboard
    Then I press view with id "button1"
    Then I take a screenshot
    Then I go back
    Then I press view with id "radio1"
    Then I press view with id "button1"
    Then I take a screenshot
    
