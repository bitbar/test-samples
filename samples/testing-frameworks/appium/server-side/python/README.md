# Appium Server Side Python Samples

This folder contains a sample Appium server side test case for Android
and iOS environments. To read more about server and client Appium runs
in BitBar Cloud, check our online documentation from `https://support.smartbear.com/bitbar/docs/testing-with-bitbar/automated-testing/appium/index.html`.

## Apps

You will find the necessary applications in:
 - Android: [bitbar-sample-app.apk](../../../../../apps/android/bitbar-sample-app.apk)
 - iOS:  [bitbar-ios-sample.ipa](../../../../../apps/ios/bitbar-ios-sample.ipa)

## Folder Content

This folder contains the following files:

* `BitBarAppiumTest.py` and `BitBarSampleAppTest.py` are the actual
  test files written in Python. The test execution uses Python's
  unittest framework for test execution.

* `requirements.txt` lists the required Python packages that need to be
  installed for the test to be executable, e.g. AppiumPythonClient.

* `run-tests_ios.sh` and `run-tests_android.sh` are environment
  specific Bash shell scripts that need to be renamed to
  `run-tests.sh` so it will be executed by BitBar Cloud when the test
  is started on some device. Depending on environment you are testing,
  you'll need to use the iOS or Android version of this script.

* `create-zip.sh` is a script that packages the test files into a zip
  file. The output of this file needs to be uploaded to your server
  side test run. You need to provide parameter `ios` or `android`
  depending on which environment you want to use.
