# Appium Client Side + Nightwatch

## Client Side Test Execution

### Setup

* Install Gulp

    ```sh
    npm install -g gulp
    ```

* Install NPM Dependencies

    ```sh
    npm install
    ```

* Add your apiKey to `./.credentials.json`

   Create a file called ".credentials.json" in the project's root and add your testdroid apiKey to it as described below:

    ```json
    {
        "apiKey": "YOUR_TESTDROID_CLOUD_APIKEY"
    }
    ```

* Modify the `nightwatch.json` file according to your project

### Run

* To run the test on iOS

    ```sh
    gulp ios
    ```

* To run the test on Android

    ```sh
    gulp android
    ```

## Server Side Test Execution

### Setup

* Create a zip file to be uploaded to Testdroid
  * If you have Gulp installed, run the following command at the project's root folder:

      ```sh
      gulp zip
      ```

    This creates a zip-file of the project to the "dist"-folder.

  * Zip the project manually

### Run the test

On Bitbar Cloud:

* Create an Appium server side project
* Create a new testrun in new server side project
* Upload tested application (apk/ipa) through the “Application” step

  * When doing web-testing as in this example, you can upload any application as it will not be used.
  You can, for example, download an application from here: https://github.com/bitbar/testdroid-samples/tree/master/apps/builds

* Upload the zip with scripts through the “Upload test file” step
* Choose device group to use or create a new group for this run
* Start testrun
