# Appium Client Side Java Sample

This folder includes sample Appium tests using Java for Android and iOS, which can be run
in [Bitbar Cloud](https://cloud.bitbar.com/).

This example can be run either using client side execution or server side execution.
To find more information about these possibilities, visit https://docs.bitbar.com/testing/appium/

## Test Execution

### Prerequisites

1. Install Java

    Currently the example project is targeting Java 1.7. To change that, modify the target and source field in pom.xml

    ```xml
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.6.0</version>
        <configuration>
            <source>1.7</source>
            <target>1.7</target>
        </configuration>
    </plugin>
    ```

1. Install Maven

    http://maven.apache.org/

### Android

1.  Download sample application [bitbar-sample-app.apk](../../../../../apps/android/bitbar-sample-app.apk)

2.  Run the following command in the root directory of the project:

        ```sh
        mvn clean test \
            -Dtest=AndroidAppiumExampleTest \
            -DexecutionType=clientside \
            -DapiKey=<your_bitbar_apiKey> \
            -DapplicationPath=</path/to/bitbar-sample-app.apk>
        ```

        Where:

        - `<your_bitbar_apiKey>` is your API key to Bitbar Cloud. You can find API key in

    https://cloud.bitbar.com/#user/my-account under _My Integrations_. - `<path/to/bitbar-sample-app.apk>` is the path to the downloaded sample application

### Android with biometry

1.  Download sample application [BitBarSampleApp.apk](../../../../../apps/android/BitBarSampleApp.apk)

2.  Run the following command in the root directory of the project:

        ```sh
        mvn clean test \
            -Dtest=AndroidBiometryAppiumExampleTest \
            -DexecutionType=clientsideWithBiometry \
            -DapiKey=<your_bitbar_apiKey> \
            -DapplicationPath=</path/to/BitBarSampleApp.apk>
        ```

        Where:

        - `<your_bitbar_apiKey>` is your API key to Bitbar Cloud. You can find API key in

    https://cloud.bitbar.com/#user/my-account under _My Integrations_. - `<path/to/BitBarSampleApp.apk>` is the path to the downloaded sample application

### iOS

1.  Download Sample Application [bitbar-ios-sample.ipa](../../../../../apps/ios/bitbar-ios-sample.ipa)

2.  Run the following command in the root directory of the project:

        ```sh
        mvn clean test \
            -Dtest=IosAppiumExampleTest \
            -DexecutionType=clientside \
            -DapiKey=<your_bitbar_apiKey> \
            -DapplicationPath=</path/to/bitbar-ios-sample.ipa>
        ```

        Where:

        - `<your_bitbar_apiKey>` is your API key to Bitbar Cloud. You can find API key in

    https://cloud.bitbar.com/#user/my-account under _My Integrations_. - `<path/to/bitbar-ios-sample.ipa>` is the path to the downloaded sample application

### iOS With Biometry

1.  Download Sample Application [BitBarSampleApp.ipa](../../../../../apps/ios/BitBarSampleApp.ipa)

2.  Run the following command in the root directory of the project:

        ```sh
        ./run_client_side_test_and_export_results.sh \
            -Dtest=IosBiometryAppiumExampleTest \
            -DexecutionType=clientsideWithBiometry \
            -DapiKey=<your_bitbar_apiKey> \
            -DapplicationPath=<path/to/BitBarSampleApp.ipa>
        ```

        Where:

        - `<your_bitbar_apiKey>` is your API key to Bitbar Cloud. You can find API key in

    https://cloud.bitbar.com/#user/my-account under _My Integrations_. - `<path/to/BitBarSampleApp.ipa>` is the path to the downloaded sample application

### Notes

#### applicationPath-argument

The applicationPath-argument is only required if the application has not yet been uploaded to Bitbar cloud project. 
When the applicationPath-argument is provided, the application will be automatically uploaded to Bitbar cloud before 
the actual test execution starts. Otherwise, id or path for downloading the application has to be set in 
desired capabilities file.

### Upload Test Results

When using Client Side test execution, the test results have to be uploaded to Bitbar in order for it to correctly visualize the test run's success and test cases that have been run.

#### OSX, Linux and Windows with Cygwin

On OSX, Linux and Windows machines with Cygwin this process can be automated by running
the `run_client_side_test_and_export_results.sh` script. When this script is used, it replaces the `mvn clean test`
part of the test execution command. For example for android you could run it as follows:

```sh
./run_client_side_test_and_export_results.sh \
    -Dtest=AndroidAppiumExampleTest \
    -DexecutionType=clientside \
    -DapiKey=<your_bitbar_apiKey> \
    -DapplicationPath=</path/to/bitbar-sample-app.apk>
```

#### Windows

On Windows machines not running Cygwin this process can be automated by running
the `windows_client_side_test_and_export_results.bat` script. [Curl](https://curl.haxx.se/download.html) has to
be installed and in the Path in order for the `.bat` script to work.

```sh
windows_client_side_test_and_export_results.bat ^
    -Dtest=AndroidAppiumExampleTest ^
    -DexecutionType=clientside ^
    -DapiKey=<your_bitbar_apiKey> ^
    -DapplicationPath=</path/to/bitbar-sample-app.apk>
```

## Server Side Test Execution

Create a zip file containing the project, which will be uploaded to [Bitbar Cloud](https://cloud.bitbar.com/).

-   On OSX/Linux machines you can just run the following command at the project's root directory:

    ```sh
    ./zip_project.sh
    ```

    This creates a zip package called `server_side_test_package.zip`

-   You can also manually zip the project's sources. You have to include at least the following files in the zip package.
    Note that these files have to be at the root of the zip file, i.e. not inside any additional directory.

    -   `run-tests.sh`
    -   `pom.xml`
    -   `src/`

## Project Structure

### Test Cases

This is where the (Test)Magic happens. The test logic of the example test cases is located in:

-   _Android:_ `src/test/java/com/testdroid/appium/android/sample/AndroidAppiumExampleTest.java`

-   _iOS:_ `src/test/java/com/testdroid/appium/ios/sample/IosAppiumExampleTest.java`

-   _Android With Biometry:_ `src/test/java/com/testdroid/appium/android_biometrics/AndroidBiometricsAppiumExampleTest.java`

-   _iOS With Biometry:_ `src/test/java/com/testdroid/appium/ios_biometrics/IosBiometryAppiumExampleTest.java`

_These are the files you want to edit when testing your own application based on this template._

### Session initialization

The logic related to setting up the test session is located in:

-   `src/test/java/com/testdroid/appium/BaseTest.java`

This functionality is inherited by the test cases as follows:

-   _Android:_ BaseTest.java --> BaseAndroidTest.java --> AndroidAppiumExampleTest.java
-   _iOS:_ BaseTest.java --> BaseIOSTest.java --> IosAppiumExampleTest.java
-   _Android With Biometry:_ BaseTest.java --> BaseAndroidTest.java --> AndroidBiometryAppiumExampleTest.java
-   _iOS With Biometry:_ BaseTest.java --> BaseIOSTest.java --> IosBiometryAppiumExampleTest.java

_Most likely you won't have to edit these files at all._

### Desired Capabilities

The desired capabilities are fetched from a properties-file.
The properties-files are located in `src/test/resources/` and are specific to the test execution type
and OS version that is under test:

-   _Android Server Side_: `desiredCapabilities.android.serverside.properties`
-   _Android Client Side_: `desiredCapabilities.android.clientside.properties`
-   _Android Client Side With Biometry Instrumentation_: `desiredCapabilities.androidBiometrics.clientside.properties`
-   _iOS Server Side_: `desiredCapabilities.ios.serverside.properties`
-   _iOS Client Side_: `desiredCapabilities.ios.clientside.properties`
-   _iOS Client Side With Biometry Instrumentation_: `desiredCapabilities.iosBiometry.clientside.properties`

The properties-files are in the format `<desired_capability_name>=<desired_capability_value>`.

The desired capabilities can be divided to Appium-specific desired capabilities
(such as `platformName` and `deviceName`) and Bitbar-specific desired capabilities
(such as `testdroid_device` or `testdroid_project`).

For Test run with biometry instrumentation you have to add `bitbar_biometricInstrumentation=true` capability.

The Bitbar-specific desired capabilities have to only be defined for Client Side Test Execution.

For more information about Bitbar specific capabilites, please refer to
<http://docs.bitbar.com/testing/appium/desired-caps/>

## Helpful Resources

-   [Complete list of available devices](https://cloud.bitbar.com/#public/devices)
-   [Bitbar Documentation](http://docs.bitbar.com/)
