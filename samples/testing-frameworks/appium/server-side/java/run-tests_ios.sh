#!/bin/bash

# Important! This step is needed to make sure the test zip files are extracted
echo "Extracting tests.zip..."
unzip tests.zip

security unlock-keychain -p testdroid login.keychain

## This is the IOS version of run-tests.sh to be used as template for Server side execution
## To use in cloud:
##  Replace the TEST and TEST_CASE variables with your desired test class and cases
##  execute ./createiOSZip.sh to create the test zip for upload in cloud

# Name of the desired test suite and optionally specific test case, eg: iOSSample#mainPageTest
TEST=${TEST:="iOSSample"}
# OPTIONAL: add the name of TestCases to be used with the `mvn test` command
# Leave blank to test the whole class!
TEST_CASE="#mainPageTest"

## Environment variables setup
export SCREENSHOT_FOLDER=target/reports/screenshots/ios/
export PLATFORM_NAME=iOS
export JAVA_HOME=$(/usr/libexec/java_home)

export DYLD_FALLBACK_LIBRARY_PATH=${DYLD_LIBRARY_PATH}:$PWD/lib/mac/opencv:/Users/Shared/libimobiledevice-binaries-master
# Replace all spaces with escape: "\ "
export DYLD_FALLBACK_LIBRARY_PATH=${DYLD_FALLBACK_LIBRARY_PATH// /\\ }
echo "DYLD_FALLBACK_LIBRARY_PATH=${DYLD_FALLBACK_LIBRARY_PATH}"

export PLATFORM_VERSION=$(ideviceinfo --key ProductVersion)
echo "iOS Version: ${PLATFORM_VERSION}"

if [ -z ${UDID} ] ; then
  export UDID=${IOS_UDID}
fi
echo "UDID is ${UDID}"

## Appium server launch
echo "Starting Appium ..."
appium -U ${UDID} --log-no-colors --log-timestamp

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
