# Bitbar Appium Sample


The capabilities has been set to run tests for Bitbar Sample Android
app. Before running tests modify Tests.cs and
 replace
TESTDROID_USERNAME and TESTDROID_PASSWORD with your user
credentials. Set the SCREENSHOT_FOLDER to an existing directory on
your system.

**Note:** Instead of using *testdroid_username* and
  *testdroid_password* desired capabilities to identify to Bitbar
  Cloud you should rather use *testdroid_apiKey*. Your personal apiKey is
  found in cloud.bitbar.com under 'My account'.


#### Windows

Launch the AppiumTest.sln on Visual Studio and make sure that NUnit Test Adapter is installed through the Extension Manager. Use Test Explorer to run the test.

#### UNIX

### Download dependencies

`nuget install Test123/packages.config -OutputDirectory packages`

### Build Test Package

`xbuild`

### Run tests
`nunit-console Test123/bin/Debug/TestdroidAndroidSample.dll`
