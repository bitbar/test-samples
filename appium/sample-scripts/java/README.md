Testdroid Appium Sample in Java
===========================

This folder includes sample Appium tests using Java for Android and iOS, which can be run in [Testdroid Cloud](https://cloud.testdroid.com/).

This example can be run either using client side execution or server side execution. To find more information about this, visit <http://docs.testdroid.com/appium/client-server/>

#Clientside Test Exectuion
##Prerequisites
1. Install Java
2. Install Maven
	-	<http://maven.apache.org/>

## Android
Download Sample Application [BitbarSampleApp.apk](https://github.com/bitbar/testdroid-samples/blob/master/apps/builds/BitbarSampleApp.apk)

Run the following command in the root directory of the project:

```
mvn clean test \
-Dtest=AndroidAppiumExampleTest \
-DexecutionType=clientside \
-DapiKey=<your_testdroid_apiKey> \
-DapplicationPath=</path/to/BitbarSampleApp.apk>
```

in which 

- \<your\_testdroid\_apiKey\> is your testdroid apiKey. The apiKey is available under 'My account' in [Testdroid Cloud](https://cloud.testdroid.com/).
- \</path/to/BitbarSampleApp.apk\> is the path to the downloaded sample application



##iOS
Download Sample Application  [BitbarIOSSample.ipa](https://github.com/bitbar/testdroid-samples/blob/master/apps/builds/BitbarIOSSample.ipa)

Run the following command in the root directory of the project:

```
mvn clean test \
-Dtest=IosAppiumExampleTest \
-DexecutionType=clientside \
-DapiKey=<your_testdroid_apiKey> \
-DapplicationPath=</path/to/BitbarIOSSample.ipa>
```

in which 

- \<your\_testdroid\_apiKey\> is your testdroid apiKey. The apiKey is available under 'My account' in [Testdroid Cloud](https://cloud.testdroid.com/).
- \</path/to/BitbarIOSSample.ipa\> is the path to the downloaded sample application

## Notes
### applicationPath-argument
The applicationPath-argument is only required if the application has not yet been uploaded to the Testdroid Cloud project. When the applicationPath-argument is provided, the application will be automatically uploaded to Testdroid Cloud before the actual test execution starts. If the application has already been uploaded, you can omit the parameter. In this case Testdroid Cloud will use the latest application that has been uploaded.

## Upload Test Results
When using Client Side test execution, the test results have to be uploaded to Testdroid Cloud in order for it to correctly visualize the test run's success and test cases that have been run.

On OSX and Linux, this process can be automated by running the <i>run_client_side_test_and_export_results.sh</i> script. When this script is used, it replaces the "mvn clean test" part of the test execution command. For example for android you could run it as follows:

```
./run_client_side_test_and_export_results.sh \
-Dtest=AndroidAppiumExampleTest \
-DexecutionType=clientside \
-DapiKey=<your_testdroid_apiKey> \
-DapplicationPath=</path/to/BitbarSampleApp.apk>
```


#Server Side Test Execution
Create a zip file containing the project, which will be uploaded to [Testdroid Cloud](https://cloud.testdroid.com/).

* On OSX/Linux machines you can just run the following command at the project's root directory:

	`./zip_project.sh` This creates a zip package called: <b>server\_side\_test\_package.zip</b>

* You can also manually zip the project's sources. You have to include at least the following files in the zip package. Note that these files have to be at the root of the zip file, i.e. not inside any additional directory.
 	* run-tests.sh 
	* pom.xml
	* src/


# Project Structure

## Test Cases
This is where the (Test)Magic happens. The test logic of the example test cases is located in:

- <b>Android:</b> <i>src/test/java/com/testdroid/appium/android/sample/AndroidAppiumExampleTest.java</i>

- <b>iOS:</b> <i>src/test/java/com/testdroid/appium/ios/sample/IosAppiumExampleTest.java</i>

<b>These are the files you want to edit when testing your own application based on this template.</b>

## Session initialization

The logic related to setting up the test session is located in:

- <i>src/test/java/com/testdroid/appium/BaseTest.java</i>

This functionality is inherited by the test cases as follows:

- <b>Android:</b> BaseTest.java --> BaseAndroidTest.java --> AndroidAppiumExampleTest.java
- <b>iOS:</b> BaseTest.java --> BaseIOSTest.java --> IosAppiumExampleTest.java

<b>Most likely you won't have to edit these files at all.</b>

## Desired Capabilities
The desired capabilities are fetched from a properties-file. The properties-files are located in <i>src/test/resources/</i> and are specific to the test execution type and OS version that is under test:

- <b>Android Server Side</b>: desiredCapabilities.android.serverside.properties
- <b>Android Client Side</b>: desiredCapabilities.android.clientside.properties
- <b>iOS Server Side</b>: desiredCapabilities.ios.serverside.properties
- <b>iOS Client Side</b>: desiredCapabilities.ios.clientside.properties

The properties-files are in the following format:
```
<desired_capability_name>=<desired_capability_value>
```
The desired capabilities can be divided to Appium-specific desired capabilities (such as <i>platformName</i> and <i>deviceName</i>) and Testdroid-specific desired capabilities (such as <i>testdroid_device</i> or <i>testdroid_project</i>).

The Testdroid-specific desired capabilities have to only be defined for Client Side Test Execution.

For more information about Testdroid specific capabilites, please refer to
<http://help.testdroid.com/customer/portal/articles/1507074-testdroid_-desired-capabilities>

# Helpful Resources
- [Complete list of available devices](https://cloud.testdroid.com/#service/devicegroups)
- [Testdroid Documentation](http://docs.testdroid.com/)



