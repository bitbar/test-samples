# Appium Server Side Java Sample

The project uses the Bitbar Sample App, will install it on the device, then go through the application

The project uses:

- [BitBar Cloud](https://cloud.bitbar.com) - Web service for automated testing on
  real mobile devices. You'll need to have an active account for launching the tests in cloud.

- [Maven](https://maven.apache.org/) - Compiles and launches the test code.

### Run tests locally from Command Line with Maven

The project uses Bitbar Sample apps, which are available in this repository
at [apps/](https://github.com/bitbar/bitbar-samples/tree/master/apps/). By default, the tested application is assumed to
be found at the root of the project with name `application.apk` or `application.ipa` depending on the tested platform.
The bitbar-ios-sample.ipa application file will also need to be first re-signed for your own test devices, if you'd like
to use try it out locally.

You'll also need to have an Appium server launched. For iOS, the UDID of the device should be pre-set in the server
settings.

Run the tests with maven using:

    mvn -Dtest=<TestClass> test

For example, to run only the test `mainPageTest` from test class AndroidSample.java you would use:

    mvn -Dtest=AndroidSample#mainPageTest test

To first clean all the previous test result files, add keyword `clean` to the command:

    mvn -Dtest=AndroidSample#mainPageTest clean test

You can also use an IDE to launch the tests. Make sure the project is correctly imported as a Maven project and that
the `pom.xml` file has been discovered for Maven's dependency management.

**Reports**

The reports, screenshots and everything else will be found under:
*./target/reports/*

### Run tests as Server Side Appium in [BitBar Cloud](https://cloud.bitbar.com)

Use the following scripts to create the test zip for your project:

    ./createAndroidZip.sh

or

    ./createiOSZip.sh

Now that you have your test zip and the application file (this sample uses the Bitbar Sample Apps available at
[apps/builds](https://github.com/bitbar/testdroid-samples/tree/master/apps/builds), you're ready to create a testrun
in your project at the [BitBar Cloud](https://cloud.bitbar.com). Upload the app and test zip in the Files Library
and go to the [Test Run Creator](https://cloud.bitbar.com/#testing/test-run-creator), choose proper OS type and pick
Server Side test framework. Use uploaded files, choose devices and run the test execution.

If you change the name of your Android or iOS test class, you will need to update it to the run-tests_android.sh and
run-tests_ios.sh TEST and TEST_CASE variables as appropriate:

    # Name of the desired test class and optionally specific test case, eg: AndroidSample#mainPageTest
    TEST=${TEST:="AndroidSample"}
    # OPTIONAL: add the name of TestCases to be used with the `mvn test` command
    # Leave blank to test the whole class!
    TEST_CASE="#mainPageTest"
