# Appium Client Side Ruby Samples

Bitbar can be used to run Appium tests against real devices to test
native Android or iOS applications, hybrid (Android & iOS) or for web
testing (Safari on iOS and Chrome on Android).

You'll find here all steps you need to start running your mobile tests
against real devices in Bitbar cloud. Before continuing with
running with these scripts you should register with [Bitbar
service](https://cloud.bitbar.com/).

For more detailed guides on Appium please refer to the [documentation
online](https://appium.io/docs/en/).

## Quick Start

* Install [Bundler](http://bundler.io/) to install all necessary gems
and dependencies.

* Install necessary gems and dependencies. Run in current directory:
```
    bundle install
```
* Update your test script (bitbar_*.rb) with necessary information
  (see below).

* Start test case run with eg.:
```
    rspec bitbar_android.rb
```
## Common Necessary Settings

There are some common settings that you need to set in all scripts
regardless of the app type that you are testing. Each bitbar_*.rb
file needs to be updated with the following values.

Here are all the values that you need to edit:

* *screen_shot_dir* - where should screenshots be stored on your local drive.
   Make sure that the directory already exists.

* *bitbar_apiKey* - a personal unique key that allows you to
   connect to Bitbar cloud without the need to use your username
   and passwords in your tests. You can find your api key under "My
   account" in [Bitbar cloud](https://cloud.bitbar.com/) UI.

* *bitbar_project* - has a default value, but you might want to add
  your own name to this. Project name is visible in your project view
  in Bitbar cloud. Each project must have a unique name

* *bitbar_testrun* - name or number of this test run. Test run
  names can be modified even at every test run

* *bitbar_app_id* - should be the id of the app you uploaded to cloud.

## Native iOS Specific Settings

Example script: bitbar_appiumdriver_ios.rb

To run your Appium tests against your native iOS application with real
devices you need to edit the bitbar_appiumdriver_ios.rb script.

In addition to the above mentioned Appium capabilities for iOS testing
you need set

* *bundleId* - this is your application's unique name

## Native Android Specific Settings

Example script: bitbar_android.rb

To configure this script for testing your own app, in addition to the
common settings done above, you need to change the following values.

* *appPackage* - Java package of the Android app you want to run

* *appActivity* - activity name for the Android activity you want to
  launch from your package. Typically this is the main activity.

## Web App Specific Settings

Example: bitbar_webdriver_ios.rb

For a more complete explanation on testing hybrid application take a
look at Appium
[documentation](https://appium.readthedocs.io/en/stable/en/writing-running-appium/web/hybrid/).
