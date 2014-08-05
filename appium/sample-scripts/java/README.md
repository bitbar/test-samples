Testdroid Appium Sample in Java
===========================

This folder includes sample Appium tests using Java for Android and iOS. Before runnign tests you need to customize scripts
for your application. 

In file SampleAppium(iOS)Test.java 
Add your user credentials:
  private static final String TESTDROID_USERNAME = "admin@localhost";
  private static final String TESTDROID_PASSWORD = "admin";
File path to your application binary (apk or ipa):
TARGET_APP_PATH=/files/myapp.ipa
Target device(see full list of devices in https://cloud.testdroid.com/web/devices#):
capabilities.setCapability("testdroid_device", "Samsung Galaxy S5");
