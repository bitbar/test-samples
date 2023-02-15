# Appium Client Side C# Sample

The capabilities has been set to run tests for Bitbar Sample Android app.
Before running tests modify Tests.cs and replace `BITBAR_APIKEY` with your api key. You can find it in
https://cloud.bitbar.com/#user/my-account under _My Integrations_. Set the `SCREENSHOT_FOLDER` to an existing directory on your system.

## Windows

Launch the `AppiumTest.sln` on Visual Studio and make sure that NUnit Test Adapter is installed through
the Extension Manager. Use Test Explorer to run the test.

## UNIX

1. Download dependencies

    ```sh
    nuget install Test123/packages.config -OutputDirectory packages
    ```

    or

    ```sh
    nuget restore AppiumTest.sln
    ```

1. Build Test Package

    ```sh
    msbuild
    ```

1. Run tests

    ```sh
    nunit-console Test123/bin/Debug/TestdroidAndroidSample.dll
    ```

    *If you're having problems with getting `nunit-console` to work try following steps:*
    1. Download [Nunit.ConsoleRunner](https://www.nuget.org/packages/NUnit.ConsoleRunner)
        ```sh
        nuget install NUnit.ConsoleRunner
        ```
    2. Download [NUnit.Extension.NUnitV2Driver](https://www.nuget.org/packages/NUnit.Extension.NUnitV2Driver)
        ```sh
        nuget install NUnit.Extension.NUnitV2Driver
        ```
    3. Run produced .dll file with downloaded `nunit-console` (available in downloaded `ConsoleRunner` package)  using mono
        ```sh
        mono NUnit.ConsoleRunner.<VERSION>/tools/nunit3-console.exe Test123/bin/Debug/TestdroidAndroidSample.dll
        ```
