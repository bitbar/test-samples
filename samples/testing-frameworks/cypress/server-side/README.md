# Intro

This folder contains a sample Cypress server side test case for desktop browsers.
This test case was taken from https://github.com/cypress-io/cypress-example-kitchensink.
To read more about server Cypress runs in BitBar Cloud, check our online documentation from https://support.smartbear.com/bitbar/docs/en/web-app-tests/automated-testing/cypress.html

## Folder content

This folder contains the following files:

* Cypress official sample files:

    * `app/` is a folder that contains the application under test.

    * `cypress/` is a folder that contains the Cypress test cases.

    * `scripts/` is a folder that contains the scripts for Cypress to run properly via npm.

    * `cypress.config.js` is a configuration file for Cypress.

    * `package.json` and `package-lock.json` are files that contain the dependencies for Cypress.

    * `serve.json` is a configuration file for the Cypress server.

* Scripts to be executed by BitBar Cloud:

    * `run-test.sh` and `run-test.ps1` are shell scripts that will be executed by BitBar Cloud, for macOS
      and Windows respectively. They install the dependencies and run the tests on a web browser selected by user.

    * `create-test-zip.sh` is a script that packages all the test files into a zip file.
      The output zip file is then ready to be uploaded and used to your server side test run.

