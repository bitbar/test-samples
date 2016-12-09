# Server Side Appium Java Sample

This out of the box Java Project sample shows most importantly the usage mobile test automation on real devices in
[Bitbar Testing](http://bitbar.com/testing) cloud service. This project is particularly suited for automated tests on native and hybrid apps for Android or iOS using Appium as the test automation framework.

The project is pre-configured to launch tests against a local Appium server at the default port 4723, which means it can be used for local test automation as well as for the Server side executed Appium testing in the [Bitbar Testing](http://bitbar.com/testing) device lab cloud service.

You can also configure the project for the more traditional remote Client side Appium testing by changing the desired capabilities to fit your use case.

The project uses:

- [Bitbar Testing](http://bitbar.com/testing) - Web service for running automated tests on real mobile devices. You'll need to have an activated account for launching these tests in the cloud.

- [Maven](https://maven.apache.org/) - Compiles and launches the test code.

## Run tests locally from Command Line with Maven

This sample project uses the Bitbar Sample apps available in this repository at apps/builds. The project expects to find these applications at the root of the project with names `application.apk` and `application.ipa`. You'll also need to have an Appium server launched. For iOS, the UDID of the device should be pre-set in the server settings.

Run the tests with maven using:

    mvn -Dtest=<TestClass> test

For example, to run only the test `mainPageTest` from class AndroidSample.java you would use:

    mvn -Dtest=AndroidSample#mainPageTest test

To first clean all the previous test result files, add keyword `clean` to the command:

    mvn -Dtest=AndroidSample#mainPageTest clean test
    
You can also use an IDE to launch the tests. Make sure the project is correctly imported as a Maven project and that the `pom.xml` file has been correctly discovered for dependency management.

**Reports**

The reports, screenshots and everything else will be found under: *./target/reports/*

## Run tests as Server Side Appium in [Bitbar Testing](http://bitbar.com/testing)

*NOTE!* Server side Appium tests on Bitbar Testing are available only with a [paid plan](http://bitbar.com/testing/pricing/public-cloud/). Please contact `sales@bitbar.com` for more information.

You will need a Testdroid project of type `Appium Android server side` or `Appium iOS server side` according to the target platform.

Once you have your Testdroid project created, use the provided scripts to create the test zip from your project:

    ./createAndroidZip.sh
or

    ./createiOSZip.sh

Now that you have your test zip and application file (this sample uses the Bitbar Sample Apps available in this repository at [apps/builds](https://github.com/bitbar/testdroid-samples/tree/master/apps/builds)), you're ready to begin creating a testrun in your project at the [test cloud](https://cloud.testdroid.com). Upload the app and test zip in the appropriate pages, choose the device group and finally make sure you have set high enough timeout for your test in the Advanced options (default is 10 minutes).

If you change the name of your Android or iOS test class, you will need to update it to the run-tests_android.sh and run-tests_ios.sh TEST and JUNIT variables as appropriate:

    # Name of the desired test suite and optionally specific test case, eg: AndroidSample#mainPageTest
    TEST=${TEST:="AndroidSample#mainPageTest"}
    # JUnit file wont have the #caseName ending
    JUNIT="AndroidSample"

## Run tests in Bitbar Testing from Command Line with launch-tests.sh 

The launch-tests.sh is a shell script with built-in functions to communicate with the Bitbar Testing Rest API. The available parameters are displayed by launching the script with the `-h` option:
```
./launch-tests.sh - create and upload test project to Bitbar Testing and run it
 
Usage: ./launch-tests.sh -a/i -g DEVICE_GROUP_NAME -k API_KEY [options]
 
Options:
 -a for Android test
 -i for iOS test
 -g DEVICE_GROUP_NAME to tell which group of devices to launch against
 -k API_KEY for authentication, find your API key at https://cloud.testdroid.com/#service/my-account
Optional: -p PROJECT_NAME to choose a project by its name. If not given, a new project will be created
Optional: -t for creating and uploading a new test zip file
Optional: -f APP_FILE_PATH for uploading a new apk/ipa file
Optional: -e API_ENDPOINT for private cloud instances (e.g. "https://trial.testdroid.com/cloud")
 
Documentation can be found at https://docs.testdroid.com
```