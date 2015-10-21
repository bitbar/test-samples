Testdroid Appium Sample in Java
===========================

This folder includes sample Appium tests using Java for Android and iOS. Before running tests you need to customize scripts
for your application. 

####Configuring testdroid specific settings

*File  [SampleAppium(iOS)Test.java](https://github.com/bitbar/testdroid-samples/blob/master/appium/sample-scripts/java/src/test/java/com/testdroid/appium/android/sample/SampleAppiumTest.java)* 

- Add your testdroid user credentials: Set environment variable
TESTDROID_USERNAME and TESTDROID_PASSWORD, or edit
*testdroid_username* and *testdroid_password* in source file.
**NOTE** A prefered approach is to use the *testdroid_apiKey*
capability instead of username and password. Simply replace
*testdroid_username* and *testdroid_password* with *testdroid_apiKey*. Your
api key is available under 'My account' in [Testdroid
Cloud](https://cloud.testdroid.com/).

- If want to run test against your application make sure to change file path to your application binary (apk or ipa):
```
TARGET_APP_PATH=/files/myapp.ipa
```
- Target device - see full list of real device in [Testdroid Cloud](https://cloud.testdroid.com/web/devices#):
```
...
capabilities.setCapability("testdroid_device", "Asus Memo Pad 8 K011");
...
```

For more information about Testdroid specific capabilites =>
http://help.testdroid.com/customer/portal/articles/1507074-testdroid_-desired-capabilities


####Running tests in Testdroid Cloud

You can run test from your IDE or directly from command line using using maven:
``` 
>mvn clean test -Dtest=SampleAppiumTest
```
or to be more precise: 
```
>mvn clean test -Dtest=com.testdroid.appium.android.sample.SampleAppiumTest
```
or run all the tests:
``` 
>mvn clean test 
```
