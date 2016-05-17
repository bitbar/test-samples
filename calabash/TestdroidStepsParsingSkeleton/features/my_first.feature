Feature: Running a test

    @mytag
    Scenario: Select incorrect item
      Given my app is running
      Then I take picture

    @skip
    Scenario: Skip me
      Given my app is running
      Then I take picture

