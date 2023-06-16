# Based on the [Serenity JUnit Starter](https://github.com/serenity-bdd/serenity-junit-starter) project

## Running the tests under Maven

To run the tests open resources/serenity.conf and modify it.
The only really needed change is to set up the API_KEY
```
webdriver {
  driver = "remote"
  capabilities {
    platformName = "Windows"
    browserName = "chrome"
    browserVersion = "latest"
    "bitbar:options" {
      osVersion = "10"
      screenResolution = "1920x1080"
    }
  }
}
bitbar {
  active = true
  apiKey = "API_KEY"
  hub = "eu-desktop-hub"
  cloudUrl = "https://cloud.bitbar.com"
}
```

To run the tests with Maven, open a command window and run:

    mvn clean verify

Or for gradle:

    gradle test

## Viewing the reports

The command provided above will produce a Serenity test report in the `target/site/serenity` directory. Go take a look!
