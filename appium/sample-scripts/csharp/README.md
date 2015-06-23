#Appium Testdroid Sample

The capabilities has been set to run tests for Bitbar Sample Android app. Before running tests modify Tests.cs and
replace TESTDROID_USERNAME and TESTDROID_PASSWORD with your user credentials.

### Download dependencies

`nuget install Test123/packages.config -OutputDirectory packages`

### Build Test Package

`xbuild`

### Run tests
`nunit-console Test123/bin/Debug/TestdroidAndroidSammple.dll`

