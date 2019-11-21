# Appium Server Side Ruby Samples

This folder includes sample Appium tests using Ruby for Android and iOS, which can be run in [Bitbar Cloud](https://cloud.bitbar.com/).

## Server Side Test Execution

Create a zip file containing the project, which will be uploaded to [Bitbar Cloud](https://cloud.bitbar.com/).

* On OSX/Linux machines you can just run the following command at the project's root directory:

    `./createAndroidZip.sh` This creates a zip package called `android-test.zip`
    `./createiOSZip.sh` This creates a zip package called `ios-test.zip`

* You can also manually zip the project's sources. You have to include at least the following files in the zip package.
Note that these files have to be at the root of the zip file, i.e. not inside any additional directory.

  * run-tests.sh
  * Gemfile
  * setup_appium.rb
  * android_sample_spec.rb / ios_sample_spec.rb

### Quick Start

* Install [Bundler](http://bundler.io/) to install all necessary gems
and dependencies.

* Install necessary gems and dependencies. Run in current directory:

bundle install

* Update your test script (setup_appium.rb) with necessary information
(see below).

* Start test case run with eg.:

    ```sh
    rspec android_sample.rb
    ```

or run following command at the project's root directory:

```sh
./run-tests_android.sh
./run-tests_ios.sh
```

### Common Necessary Settings

There are some common settings that you need to set in all scripts
regardless of the app type that you are testing.

Here are all the values that you need to edit:

* *app_file_android* - your android app.
* *app_file_ios* - your android app.

### Native iOS Specific Settings

To configure this script for testing your own app edit the setup_appium.rb script,
in addition to the common settings done above, you need to change the following values.

In addition to the above mentioned Appium capabilities for iOS testing
you need set

* *bundleId* - this is your application's unique name

### Native Android Specific Settings

To configure this script for testing your own app edit the setup_appium.rb script,
in addition to the common settings done above, you need to change the following values.

* *appPackage* - Java package of the Android app you want to run

* *appActivity* - activity name for the Android activity you want to
launch from your package. Typically this is the main activity.

## Helpful Resources

* [Complete list of available devices](https://cloud.bitbar.com/#public/devices)
* [Bitbar Documentation](http://docs.bitbar.com/)
