Intro
=====

This folder contains a Java client-side Selenium sample for running a desktop browser test in BitBar Cloud.

Files
=====

* `pom.xml` defines the Maven project and Selenium dependency.
* `src/test/java/com/bitbar/selenium/BitbarSelenium.java` contains the sample main class that opens the BitBar sample page and validates the result text.

Prerequisites
=============

* Java 8+ 
* Maven
* A valid BitBar API key

Setup
=====

This sample currently uses Selenium RemoteWebDriver against the BitBar desktop hub and sends these capabilities:

* `platformName = linux`
* `browserName = firefox`
* `browserVersion = latest`
* `bitbar:options.project = Selenium sample project`
* `bitbar:options.testrun = Java sample test3`
* `bitbar:options.resolution = 1920x1080`

Update the API key in `src/test/java/com/bitbar/selenium/BitbarSelenium.java` before running the sample.

Run
===

If you are already in this folder, you can run:

```bash
mvn clean test-compile org.codehaus.mojo:exec-maven-plugin:3.5.0:java -Dexec.mainClass=com.bitbar.selenium.BitbarSelenium -Dexec.classpathScope=test
```

Successful output includes:

```text
Bitbar - Test Page for Samples
Bitbar
```

Troubleshooting
===============

* `SessionNotCreatedException: Full authentication is required to access this resource` usually means the Bitbar API key is missing, invalid, expired, or lacks the needed desktop access.
* `Failed to submit test run! No desktop browser matching Desired Capabilities` means the selected browser, platform, browser version, or Bitbar options do not match an available desktop environment.
* If Maven appears to run stale code after editing the sample, use the `clean test-compile` command shown above so the test class is rebuilt before execution.
