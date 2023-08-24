# Appium Client Side Python Samples

Bitbar can be used to run Appium tests against real devices to test
native Android or iOS applications, hybrid (Android & iOS) or for web
testing (Safari on iOS and Chrome on Android).

You'll find here all steps you need to start running your mobile tests
against real devices in Bitbar. Before continuing with running these
scripts you should register with [Bitbar
service](https://cloud.bitbar.com/).

For more detailed guides on Appium please refer to their
[documentation online](https://appium.github.io/python-client-sphinx/).

## Uploading Your App To Cloud

Uploading an app is easiest using the `upload.py` script and using
apiKey identification. The apiKey is found under "My Account" in
Bitbar cloud. For the upload to be successful the full path to the
app (apk or ipa) needs to be provided.

If no app is provided on command line, then the Bitbar Sample Android app is
uploaded.

Upload a mobile app to Bitbar cloud and get a handle to it

```bash
$ python upload.py -h
usage: upload.py [-h] [-k APIKEY] [-a APP_PATH] [-u URL]
```

optional arguments:

```bash
  -h, --help            show this help message and exit
  -k APIKEY, --apikey APIKEY
                        User's apiKey to identify to cloud, or set environment
                        variable BITBAR_APIKEY
  -a APP_PATH, --app_path APP_PATH
                        Path to app to upload or set environment variable
                        BITBAR_APP_PATH. Current value is:
                        '../../../../../apps/android/testdroid-sample-app.apk'
  -u URL, --url URL     Bitbar cloud url to upload app or set environment
                        variable BITBAR_UPLOAD_URL. Current value is:
                        'https://cloud.bitbar.com/api/v2/me/files'
```

The below example shows how to upload a hybrid Android demo app to Bitbar Cloud.

```bash
$ python upload.py -k xg8x...YXto -a ../../../../../apps/android/bitbar-sample-app.apk
File id to use in Bitbar capabilities in your test: 127314812
```

The response message provides the application's cloud id that
should be used with `bitbar_app` capability.

## Common Settings in Example Tests

There are some common settings needed to run any of the example tests,
regardless of the app type being tested. Each `bitbar_*.py` file
needs to have the following values set as environment variables or set
in the files. Values can also be given as command line parameters to
`run-test.py` script.

### Values used in tests:

* *screenshot_dir* - where should screenshots be stored on your local drive

* *bitbar_username* - your email that you registered with to
   Bitbar cloud.  **Rather use bitbar_apiKey.**

* *bitbar_password* - your Bitbar cloud password. **Rather use bitbar_apiKey.**

* *bitbar_apiKey* - a personal unique key allowing you to connect
   to Bitbar cloud without using username and passwords in
   tests. Api key is found under "My account" in [Bitbar cloud](https://cloud.bitbar.com/) UI.

* *bitbar_project* - the project name in Bitbar cloud. Each
  project's name is unique. A project's name can be modified later on if needed.

* *bitbar_testrun* - name of this test run inside of
  `bitbar_project`. Each test run can have the same name, but it is
  recommended to set it dynamically (e.g. with timestamp or device
  name). If no test run name is given, the system automatically names
  it in "Test Run x".

* *bitbar_app* - id of the app uploaded using `upload.py`
  script or path for downloading an application. Example id could be '127314812'.
* *bitbar_biometricInstrumentation* - boolean value, that informs whether
  biometry should be injected or not.

### `run-test.py` paramaters:

```
  -h, --help            show this help message and exit
  -k APIKEY, --apikey APIKEY
                        User's apiKey to identify to cloud
  -s SCREENSHOT_DIR, --screenshot_dir SCREENSHOT_DIR
                        Path to screenshot directory
  -t {bitbar_chrome,bitbar_android,bitbar_safari,bitbar_ios,bitbar_biometrics_ios,bitbar_biometrics_android}, --test {bitbar_chrome,bitbar_android,bitbar_safari,bitbar_ios,bitbar_biometrics_ios,bitbar_biometrics_android}
                        The test file to be run
  -a APP, --app APP     Id of app uploaded to cloud using upload.py script or path for downloading an app. Mandatory for app testing
  --device DEVICE       Full name of device to use
  --device_group_id DEVICE_GROUP_ID
                        The id of the Bitbar device group from where available devices are to be searched from
  -p PROJECT, --project PROJECT
                        The name of the cloud project
  -r RUN_NAME, --run_name RUN_NAME
                        The name of the test run
  -u URL, --url URL     Bitbar url where test project and devices are found
  -i APPIUM_URL, --appium_url APPIUM_URL
                        Bitbar Appium url
  --bundle_id BUNDLE_ID
                        Mandatory bundleID when running iOS tests
  --app_package APP_PACKAGE
                        Mandatory app package path for native Android tests
  --app_activity APP_ACTIVITY
                        Mandatory main activity for native Android tests
  --cmd_timeout CMD_TIMEOUT
                        New command timeout value, default is 60s
  --test_timeout TEST_TIMEOUT
                        Maximum test duration, defaults to 600s
```

## Example Run

Remember to download pip packages listed in `python3-requirements.txt`:

```bash
python3 -m pip install -r python3-requirements.txt
```

```bash
$ python run-test.py -k xYY5...PeOA6 -s ./screenshots -p "iOS" -t bitbar_ios -a "127314812"
testSample (bitbar_ios.TestdroidIOS) ... Searching Available Free iOS Device...
Found device 'Apple iPad Mini A1432 9.2.1'

Starting Appium test using device 'Apple iPad Mini A1432 9.2.1'
15:59:00: WebDriver request initiated. Waiting for response, this typically takes 2-3 mins
15:59:58: WebDriver response received
15:59:58: view1: Finding buttons
15:59:59: view1: Clicking button [0] - RadioButton 1
16:00:00: view1: Typing in textfield[0]: Testdroid user
16:00:08: view1: Tapping at position (384, 0.95) - Estimated position of SpaceBar
16:00:10: view1: Taking screenshot screenshot1.png
16:00:14: view1: Hiding Keyboard
16:00:17: view1: Taking screenshot screenshot2.png
16:00:21: view1: Clicking button[6] - OK  Button
16:00:22: view2: Taking screenshot screenshot3.png
16:00:26: view2: Finding buttons
16:00:28: view2: Clicking button[0] - Back/OK button
16:00:29: view1: Finding buttons
16:00:30: view1: Clicking button[2] - RadioButton 2
16:00:31: view1: Clicking button[6] - OK Button
16:00:32: view1: Taking screenshot screenshot4.png
16:00:36: view1: Sleeping 3 before quitting webdriver.
16:00:39: Quitting
ok

```

## Native iOS Example

Example script: `bitbar_ios.py`

To run iOS native app tests additional parameter is required to be provided:

* **bundleId** - this is your application's unique name

```bash
$ python run-test.py -k xYY5...PeOA6 -s ./screenshots -p "iOS" -r `date +%R` -a "127314812" --bundle_id "com.bitbar.testdroid.BitbarIOSSample" -t bitbar_ios  
```

This parameter is not needed if running against the sample BitbarIOSSample.ipa application, as it's set inside of the sample script.

## Native Android Example

Example script: `bitbar_android.py`

To run an Appium test against a native Android application Appium needs to the
following additional information:

* **app_package** - Java package of the Android app you want to run

* **app_activity** - activity name for the Android activity you want to
  launch from your package. Typically this is the main activity.

For running the sample applications and tests these do not need to be set as they are set inside of the sample scripts if no parameter is given.

```bash
python run-test.py -k xYY5...PeOA6 -s ./screenshots -a '127314812' -p "Android Native" -r  `date +%R` -t bitbar_android
```

## Safari Browser Testing

Does not need any specific settings.

Example: `bitbar_safari.py`

```bash
python run-test.py -k xYY5hc8PXAXsBBd1G3ijnb18wlqPeOA6 -s ./screenshots -t bitbar_safari -p "Safari browser"  -r `date +%R`
```

## Chrome Browser Testing

Does not need any special settings.

Example: `bitbar_chrome.py`

```bash
python run-test.py -k xYY5hc8PXAXsBBd1G3ijnb18wlqPeOA6 -s ./screenshots -t bitbar_chrome -p "Chrome browser"  -r `date +%R`
```
