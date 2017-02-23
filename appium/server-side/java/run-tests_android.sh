#!/bin/bash

# Important! This step is needed to make sure the test zip files are extracted
echo "Extracting tests.zip..."
unzip tests.zip

## This is the Android version of run-tests.sh to be used as template for Server side execution
## To use in cloud:
##  Replace the TEST variable with your desired test classes and cases
##  Examples at https://maven.apache.org/surefire/maven-surefire-plugin/examples/single-test.html
##  execute ./createAndroidZip.sh to create the test zip for upload in cloud

# Name of the desired test class and optionally specific test case, eg: AndroidSample#mainPageTest
TEST=${TEST:="AndroidSample"}

## Environment variables setup
export SCREENSHOT_FOLDER=target/reports/screenshots/android/
export PLATFORM_NAME=Android
export UDID=${ANDROID_SERIAL}

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
# Use appium-1.4 to target 1.4.x and appium-1.5 to target 1.5.x
echo "Starting Appium ..."
appium-1.4 --command-timeout 120 --log-no-colors --log-timestamp

# Check that the appium process exists
ps -ef|grep appium

## Start test execution
echo "Running tests: ${TEST}"
# Remove `-Dtest=${TEST}` to launch all tests in the project
mvn -Dtest=${TEST} --quiet clean test

## Post-processing
# Generate combined TEST-all.xml
mvn --quiet antrun:run@junitreport
# Make sure there's no pre-existing `screenshots` file blocking symbolic link creation
rm -rf screenshots
# Screenshots need to be available at root as directory `screenshots` .
ln -s ${SCREENSHOT_FOLDER} screenshots
