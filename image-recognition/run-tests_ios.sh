#!/bin/bash

## This is the IOS version of run-tests.sh for Server side execution
## To use in cloud:
## 		Replace the TEST and JUNIT variables with your desired test suite
##    execute ./createiOSZip.sh to create the test zip for upload in cloud

# Name of the desired test suite and optionally specific test case, eg: iOSSample#mainPageTest
TEST=${TEST:="iOSSample#mainPageTest"}
# JUnit file wont have the #caseName ending
JUNIT="iOSSample"

export JAVA_HOME=$(/usr/libexec/java_home)
export DYLD_LIBRARY_PATH=${DYLD_LIBRARY_PATH}:/opt/opencv249/lib

## Cloud setup
echo "UDID is ${UDID}"
echo "Starting Appium ..."
/opt/testdroid/appium/bin/appium.js -U ${UDID} --command-timeout 120 --log-no-colors --log-timestamp >appium.log 2>&1 &

# Wait for appium to fully launch
sleep 5

ps -ef|grep appium

echo "Extracting tests.zip..."
unzip tests.zip

## Prepare testdroid.properties
echo "Creating testdroid.properties..."
touch testdroid.properties

echo "appium.automationName=appium" >> testdroid.properties
echo "appium.appFile=application.ipa" >> testdroid.properties
echo "testdroid.appiumUrl=http://localhost:4723/wd/hub" >> testdroid.properties

## Start test execution
echo "Running test ${TEST}"
mvn -Dtest=${TEST} --quiet test

## Post-processing
# JUnit results need to be at root as "TEST-all.xml"
mv target/reports/junit/TEST-${JUNIT}.xml TEST-all.xml
# Screenshots need to be at screenshots directory in root.
mv target/reports/screenshots/ios screenshots
