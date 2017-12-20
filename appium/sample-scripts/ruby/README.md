# Appium Ruby Samples

Bitbar can be used to run Appium tests against real devices to test
native Android or iOS applications, hybrid (Android & iOS) or for web
testing (Safari on iOS and Chrome on Android).

You'll find here all steps you need to start running your mobile tests
against real devices in Bitbar cloud. Before continuing with
running with these scripts you should register with [Bitbar
service](https://cloud.bitbar.com/).

For more detailed guides on Appium please refer to the [documentation
online](http://appium.io/slate/en/master/?python#about-appium).

## Quick Start

* Install [Bundler](http://bundler.io/) to install all necessary gems
and dependencies.

* Install necessary gems and dependencies. Run in current directory:

    bundle install

* Update your test script (testdroid_*.rb) with necessary information
  (see below).

* Start test case run with eg.:

    rspec testdroid_android.rb

## Common Necessary Settings

There are some common settings that you need to set in all scripts
regardless of the app type that you are testing. Each testdroid_*.rb
file needs to be updated with the following values.

Here are all the values that you need to edit:

* *screen_shot_dir* - where should screenshots be stored on your local drive

* *testdroid_username* - your email that you registered with to
   Bitbar cloud.  **Rather use testdroid_apiKey.**

* *testdroid_password* - your Bitbar cloud password.  **Rather use
   testdroid_apiKey.**

* *testdroid_apiKey* - a personal unique key that allows you to
   connect to Bitbar cloud without the need to use your username
   and passwords in your tests. You can find your api key under "My
   account" in [Bitbar cloud](https://cloud.bitbar.com/) UI.

* *testdroid_project* - has a default value, but you might want to add
  your own name to this. Project name is visible in your project view
  in Bitbar cloud. Each project must have a unique name

* *testdroid_testrun* - name or number of this test run. Test run
  names can be modified even at every test run

* *testdroid_app* - should be the name of the app you uploaded to
  cloud. Eg. if you uploaded your app using a script this would look
  something like this:
  'f4660af0-10f3-46e9-932b-0622f497b0d2/Testdroid.apk' If you uploaded
  your app through the Bitbar cloud web UI, you can use here the
  value 'latest' that refers to the latest uploaded app file.

## Native iOS Specific Settings

Example script: testdroid_appiumdriver_ios.rb

To run your Appium tests against your native iOS application with real
devices you need to edit the testdroid_appiumdriver_ios.rb script.

In addition to the above mentioned Appium capabilities for iOS testing
you need set

* *bundleId* - this is your application's unique name

## Native Android Specific Settings

Example script: testdroid_android.rb

To configure this script for testing your own app, in addition to the
common settings done above, you need to change the following values.

* *appPackage* - Java package of the Android app you want to run

* *appActivity* - activity name for the Android activity you want to
  launch from your package. Typically this is the main activity.

## Web App Specific Settings

Example: testdroid_selendroid.rb and testdroid_webdriver_ios.rb

For a more complete explanation on testing hybrid application take a
look at Appium
[documentation](https://github.com/appium/appium/blob/master/docs/en/advanced-concepts/hybrid.md).


## Tips

* To run tests against your previously uploaded app you can simply set
  your *testdroid_app* desired capability value to 'latest'


* To find available free devices in Bitbar cloud, you can use this Ruby [tool](https://github.com/bootstraponline/testdroid_device_finder)

## License

See the
[LICENSE](https://github.com/bitbar/testdroid-samples/blob/master/LICENSE)
file.
