#!/bin/bash

# Important! This step is needed to make sure the test zip files are extracted
echo "Extracting tests.zip..."
unzip tests.zip

## This is the Android version of run-tests.sh to be used as template for Server side execution
## To use in cloud:
##  Replace the TEST and TEST_CASE variables with your desired test class and cases
##  execute ./createAndroidZip.sh to create the test zip for upload in cloud

# Name of the desired test class and optionally specific test case, eg: AndroidSample#mainPageTest
TEST=${TEST:="AndroidSample"}
# OPTIONAL: add the name of TestCases to be used with the `mvn test` command
# Leave blank to test the whole class!
TEST_CASE="#mainPageTest"

## Environment variables setup
export SCREENSHOT_FOLDER=target/reports/screenshots/android/
export PLATFORM_NAME=Android
export UDID=${ANDROID_SERIAL}
export PATH=${PATH}:$ANDROID_HOME/tools/ #needed for screenshot2 command

# Check Android device API level to set Appium automationName
APILEVEL=$(adb shell getprop ro.build.version.sdk)
APILEVEL="${APILEVEL//[$'\t\r\n']}"
export PLATFORM_VERSION=${APILEVEL}
echo "API level is: ${APILEVEL}"
if [ "$APILEVEL" -gt "16" ]; then
	export AUTOMATION_NAME=appium
else
	export AUTOMATION_NAME=selendroid
fi

## Appium server launch
# Use appium-1.4, appium-1.5 or appium-1.6 to choose Appium server version
echo "Starting Appium ..."
appium --log-no-colors --log-timestamp

## Dependency installation
mvn --quiet install -DskipTests

## Start test execution
echo "Running tests ${TEST}${TEST_CASE}"
# Remove `-Dtest=${TEST}${TEST_CASE}` to launch all tests in the project
# More examples at https://maven.apache.org/surefire/maven-surefire-plugin/examples/single-test.html
mvn -Dtest=${TEST}${TEST_CASE} clean test

## Post-processing
# JUnit results need to be available at root as "TEST-all.xml"
mv target/reports/junit/TEST-${TEST}.xml TEST-all.xml

# Make sure there's no pre-existing `screenshots` file blocking symbolic link creation
rm -rf screenshots
# Screenshots need to be available at root as directory `screenshots` .
ln -s ${SCREENSHOT_FOLDER} screenshots
