# Appium Client Side C# Biometric Sample

This appium test was made for biometric featrue testing on [BitbarSampleApp(Android)](https://github.com/bitbar/test-samples/blob/master/apps/android/BitBarSampleApp.instrumented.apk) and [BitbarSampleApp(iOS)](https://github.com/bitbar/test-samples/blob/master/apps/ios/BitBarSampleApp.ipa). It can be used as a template for your automated test that includes biometric authentication. 

[_Read more about biometric authentication_](https://support.smartbear.com/bitbar/docs/testing-with-bitbar/biometric-authentication/index.html?sbsearch=biometry)


* Before running tests provide your API key to `BaseBiometricTest` class and replace `<APP_ID>` with your application ID in `TestBiometricsAndroid` class or `TestBiometriciOS` class depending on what platform you want to test. User's API key can be found in https://cloud.bitbar.com/#user/my-account under _My Integrations_. <br />
[_Read more about capabilites_](https://support.smartbear.com/bitbar/docs/testing-with-bitbar/automated-testing/appium/desired-capabilities.html?sbsearch=Capabilities)
* Set the `SCREENSHOT_FOLDER` to an existing directory on your system.

## Launching test

### Windows

Launch the `BitbarBiometricSample.sln` on Visual Studio and make sure that NUnit Test Adapter is installed through
the Extension Manager. Use Test Explorer to run the tests.

### UNIX

1. Download dependencies

    ```sh
    nuget restore BitbarBiometricSample.sln
    ```

1. Build Test Package

    ```sh
    dotnet msbuild
    ```

    or

    ```
    dotnet msbuild -property:Configuration=Release
    ```

    for _Relase_ build

1. Specify platform and run tests 
    
    Android:
    ```sh
    nunit-console BitbarBiometricSample/bin/<Release/Debug>/BitbarBiometricSample.dll --where "class == BitbarBiometricSample.TestBiometricsAndroid"
    ```

    iOS:
    ```sh
    nunit-console BitbarBiometricSample/bin/<Release/Debug>/BitbarBiometricSample.dll --where "class == BitbarBiometricSample.TestBiometricsIOS"    
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
        mono NUnit.ConsoleRunner.<VERSION>/tools/nunit3-console.exe BitbarBiometricSample/bin/<Release/Debug>/BitbarBiometricSample.dll --where "class == BitbarBiometricSample.<SPECIFIED_CLASS>" 
        ```
