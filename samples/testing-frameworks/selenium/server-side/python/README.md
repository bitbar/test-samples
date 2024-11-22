Intro
=====

This folder contains a sample server side test case for desktop browsers. 
To run tests in Bitbar Cloud, go to `https://cloud.bitbar.com/`.

Folder Content
--------------

This folder contains the following files:

* `BitbarSeleniumTest.py` and `BitbarSampleWebTest.py` are the actual test files 
  written in Python. The test execution uses Python's unittest framework for 
  test execution.

* `requirements.txt` lists the required Python packages that need to be installed 
  for the test to be executable, eg. Selenium Python Bindings.

* `run-tests.sh` is Bash shell script that will be executed by Bitbar Cloud when the test
  is started on chosen device.

* `create-test-zip.sh` is a script that packages the test files into a zip file. 
  The output zip needs to be uploaded to your server side test run.
  