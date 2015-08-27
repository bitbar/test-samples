# Appium Python Samples

Testdroid can be used to run Appium tests against real devices to test
native Android or iOS applications, hybrid (Android & iOS) or for web
testing (Safari on iOS and Chrome on Android).

You'll find here all steps you need to start running your mobile tests
against real devices in Testdroid Cloud. Before continuing with running with
these scripts you should register with [Testdroid service](https://cloud.testdroid.com/).

For more detailed guides on Appium please refer to their excellent
[documentation
online](http://appium.io/slate/en/master/?python#about-appium).

## Upload Your App To Cloud

Before testing can start you need to upload you mobile application to
the cloud. To do so open and configure the upload.py script. You will
need to configure your username (email) and password that you registered
with to Testdroid cloud. Also you need to set the full path to your
mobile app. This can be an Android or iOS application.

```sh
$ python update.py 
{"status":0,"sessionId":"f4660af0-10f3-46e9-932b-0622f497b0d2","value":{"message":"uploads successful","uploadCount":1,"rejectCount":0,"expiresIn":1800,"uploads":{"file":"f4660af0-10f3-46e9-932b0622f497b0d2/Testdroid.apk"},"rejects":{}}}
```

From the response message you need to store the application's file
name in the cloud. In this example upload it is
'f4660af0-10f3-46e9-932b-0622f497b0d2/Testdroid.apk'.

## Common Settings

There are some common settings that you need to set in all scripts
regardless of the app type that you are testing. Each testdroid_*.py
file needs to be updated with the following values.

Here are all the values that you need to edit:

* screenshotDir - where should screenshots be stored on your local drive

* testdroid_username - your email that you registered with to Testdroid Cloud

* testdroid_password - your Testdroid Cloud password

* testdroid_project - you can set this value as an environment variable
  or set in the script. This is the name you want to give to your
  project in Testdroid Cloud. Each project must have a unique name, 
  which can also be modified later

* testdroid_testrun - name of this test run. Is set here or read from
  environment variable. Test run names can also be modified, even for
  every test run

* testdroid_app - should be the name of the app you uploaded to
  cloud. Eg. if you uploaded your app using the upload.py script this
  would look like
  'f4660af0-10f3-46e9-932b-0622f497b0d2/Testdroid.apk'


## Native iOS Specific Settings

Example script: testdroid_ios.py

To run your Appium tests against your native iOS application with real
devices you need to edit the testdroid_ios.py script.

In addition to the above mentioned Appium capabilities for iOS testing
you need set

* bundleId - this is your application's unique name

## Native Android Specific Settings

Example script: testdroid_android.py

To configure this script for testing your own app, in addition to the
common settings done above, you need to change the following values.

* appPackage - Java package of the Android app you want to run

* appActivity - activity name for the Android activity you want to
  launch from your package. Typically this is the main activity.

## Tips

* To run tests against your previous app you can simply set your
  testdroid_app desired capability value to 'latest'


## License

See the [LICENSE](https://github.com/bitbar/testdroid-samples/blob/master/LICENSE) file.
