#!/bin/bash

## This is the Android version of run-tests.sh for Server side execution
## To use in cloud:
## 		Replace the TEST and JUNIT variables with your desired test suite
##    execute ./createAndroidZip.sh to create the test zip for upload in cloud

# Name of the desired test suite and optionally specific test case, eg: iOSSample#mainPageTest
TEST=${TEST:="AndroidSample#mainPageTest"}
# JUnit file wont have the #caseName ending
JUNIT="AndroidSample"

export LD_LIBRARY_PATH=/opt/OpenCV/opencv-2.4.9/build/lib

ls opt

echo "Starting Appium ..."
/opt/appium/appium/bin/appium.js --command-timeout 120 --log-no-colors --log-timestamp >appium.log 2>&1 &

# Wait for appium to fully launch
sleep 5

ps -ef|grep appium

echo "Extracting tests.zip..."
unzip tests.zip

## Prepare testdroid.properties
echo "Creating testdroid.properties..."
touch testdroid.properties

APILEVEL=$(adb shell getprop ro.build.version.sdk)
APILEVEL="${APILEVEL//[$'\t\r\n']}"
echo "API level is: ${APILEVEL}"
if [ "$APILEVEL" -gt "16" ]; then
	echo "appium.automationName=appium" >> testdroid.properties
else
	echo "appium.automationName=selendroid" >> testdroid.properties
fi
echo "appium.appFile=application.apk" >> testdroid.properties
echo "testdroid.appiumUrl=http://localhost:4723/wd/hub" >> testdroid.properties

## Start test execution
echo "Running test ${TEST}"
mvn -Dtest=${TEST} --quiet test

## Post-processing
# JUnit results need to be at root as "TEST-all.xml"
mv target/reports/junit/TEST-${JUNIT}.xml TEST-all.xml
# Screenshots need to be at screenshots directory in root.
mkdir -p screenshots
cp -R target/reports/screenshots/android/ screenshots
