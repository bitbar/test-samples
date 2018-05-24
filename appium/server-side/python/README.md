Intro
=====


This folder contains a sample Appium server side test case for Android
and iOS environments. To read more about server and client Appium runs
in Bitbar Cloud, check our online documentation from `http://docs.bitbar.com/testing/appium/index.html`.

Folder Content
--------------

This folder contains the following files:

* `TestdroidAppiumTest.py` and `BitbarSampleAppTest.py` are the actual
  test files written in Python. The test execution uses Python's
  unittest framework for test execution.

* `requirements.txt` lists the required Python packagesthat need to be
  installed for the test to be executable, eg. AppiumPythonClient.

* `run-tests_ios.sh` and `run-tests_android.sh` are environment
  specific Bash shell scripts that need to be renamed to
  `run-tests.sh` so it will be executed by Bitbar Cloud when the test
  is started on some device. Depending on environment you are testing,
  you'll need to use the iOS or Android version of this script.

* `create-zip.sh` is a script that packages the test files into a zip
  file. The output of this file needs to be uploaded to your server
  side test run.

* `start-test-run-on-cloud.sh` - a script to launch server side test
  runs from the command line. You'll need to set some environment
  parameters to get this working. These are the cloud URL, you api key
  for identification etc. To get more help on how the script works
  type `./start-test-run-on-cloud.sh --help`
  