Feature: Example

  @smoke
  Scenario: Smoke
    # Make sure main activity is running
    Then I wait upto 5 seconds for the "MM_MainMenu" screen to appear
    # Customer screenshot step
    Then I take a screenshot "smoke"

  Scenario: Open device info
    Given I press the "Device Info" button
    Then I wait upto 5 seconds for the "DI_DeviceInfo" screen to appear
    Then I take a screenshot

  Scenario: Open device info FAIL
    Given I press the "Device Info" button
    Then I wait upto 5 seconds for the "DI_DeviceInfo" screen to appear
    # Fail on purpose to see how Calabash acts on fail
    Then I wait up to 2 seconds to see "DOES NOT EXIST"
    Then I take a screenshot

  @skip
  Scenario: Skip me
    Then I take a screenshot

