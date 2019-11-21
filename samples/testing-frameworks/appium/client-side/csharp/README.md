# Appium Client Side C# Sample

The capabilities has been set to run tests for Bitbar Sample Android app.
Before running tests modify Tests.cs and replace `TESTDROID_USERNAME` and `TESTDROID_PASSWORD` with your user
credentials. Set the `SCREENSHOT_FOLDER` to an existing directory on your system.

**Note:** Instead of using `testdroid_username` and `testdroid_password` desired capabilities to identify to
Bitbar Cloud you should rather use `testdroid_apiKey`. You can find API key in
https://cloud.bitbar.com/#user/my-account under _My Integrations_.

## Windows

Launch the `AppiumTest.sln` on Visual Studio and make sure that NUnit Test Adapter is installed through
the Extension Manager. Use Test Explorer to run the test.

## UNIX

1. Download dependencies

    ```sh
    nuget install Test123/packages.config -OutputDirectory packages
    ```

1. Build Test Package

    ```sh
    xbuild
    ```

1. Run tests

    ```sh
    nunit-console Test123/bin/Debug/TestdroidAndroidSample.dll
    ```
