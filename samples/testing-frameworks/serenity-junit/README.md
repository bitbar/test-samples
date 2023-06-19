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


## Environments

In the serenity.conf there can be multiple environments specified, for example
```
environments {
  windows_10_chrome {
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
  }
  linux_firefox {
    webdriver {
      driver = "remote"
      capabilities {
        platformName = "Linux"
        browserName = "firefox"
        browserVersion = "latest"
        "bitbar:options" {
          screenResolution = "1920x1080"
        }
      }
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

Then we can run a specific one with a single command:

    mvn clean verify -Dbitbar.apiKey=API_KEY -Denvironment=linux_firefox

Or for gradle:

    gradle test -Dbitbar.apiKey=API_KEY -Denvironment=windows_10_chrome
