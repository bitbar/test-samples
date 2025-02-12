# Appium Server Side + Nightwatch

### Setup

* Modify the `nightwatch.json` file according to your project


* Create a zip file to be uploaded to BitBar by running the following command at the project's root folder:

    ```sh
    ./create-zip.sh "sample-test"
    ```

### Run the test

On Bitbar Cloud:

* Create an Appium server side project
* Create a new testrun in new server side project
* Upload tested application (apk/ipa) through the “Application” step

  * When doing web-testing as in this example, you can upload any application as it will not be used.
  You can, for example, download an application from here: https://github.com/bitbar/sample-apps

* Upload the zip with scripts through the “Upload test file” step
* Choose device group to use or create a new group for this run
* Start testrun
