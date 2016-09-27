#!/bin/bash

## This is the Android version of run-tests.sh for Server side execution
## To use in cloud:
## 		Replace the TEST and JUNIT variables with your desired test suite
##    execute ./createAndroidZip.sh to create the test zip for upload in cloud

# Name of the desired test suite and optionally specific test case, eg: iOSSample#mainPageTest
TEST=${TEST:="AndroidSample#mainPageTest"}
# JUnit file wont have the #caseName ending
JUNIT="AndroidSample"

export SCREENSHOT_FOLDER=target/reports/screenshots/android/
export PLATFORM_NAME=Android
export UDID=${ANDROID_SERIAL}
#export LD_LIBRARY_PATH=/opt/OpenCV/opencv-2.4.9/build/lib

echo "Starting Appium ..."
appium-1.4 --command-timeout 120 --log-no-colors --log-timestamp >appium.log 2>&1 &

ps -ef|grep appium

echo "Extracting tests.zip..."
unzip tests.zip

mvn --quiet install:install-file -Dfile=lib/linux/opencv/java7/opencv-2413.jar -DgroupId=opencv -DartifactId=opencv -Dversion=2.4.13 -Dpackaging=jar

APILEVEL=$(adb shell getprop ro.build.version.sdk)
APILEVEL="${APILEVEL//[$'\t\r\n']}"
echo "API level is: ${APILEVEL}"
if [ "$APILEVEL" -gt "16" ]; then
	export AUTOMATION_NAME=appium
else
	export AUTOMATION_NAME=selendroid
fi

chmod +x lib/*

## Start test execution
echo "Running test ${TEST}"
mvn -Dtest=${TEST} --quiet clean test

## Post-processing
# JUnit results need to be at root as "TEST-all.xml"
#mv target/reports/junit/TEST-${JUNIT}.xml TEST-all.xml
ln -s target/reports/junit/TEST-${JUNIT}.xml TEST-all.xml
# Screenshots need to be at screenshots directory in root.
rm -r screenshots
ln -s target/reports/screenshots/android screenshots
