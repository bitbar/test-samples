Testdroid Appium Sample in Java
===========================

This folder includes sample Appium tests using Java for Android and iOS. Before running tests you need to customize scripts
for your application. 

####Configuring testdroid specific settings

*File  [SampleAppium(iOS)Test.java](https://github.com/bitbar/testdroid-samples/blob/master/appium/sample-scripts/java/src/test/java/com/testdroid/appium/android/sample/SampleAppiumTest.java)* 
- Add your user credentials:
```
  private static final String TESTDROID_USERNAME = "admin@localhost";
  private static final String TESTDROID_PASSWORD = "admin";
```
- File path to your application binary (apk or ipa):
```
TARGET_APP_PATH=/files/myapp.ipa
```
- Target device - see full list of real device in [Testdroid Cloud](https://cloud.testdroid.com/web/devices#):
```
...
capabilities.setCapability("testdroid_device", "Asus Memo Pad 8 K011");
...
```

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
