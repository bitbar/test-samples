#!/bin/bash

## This is the IOS version of run-tests.sh for Server side execution
## To use in cloud:
## 		Replace the TEST and JUNIT variables with your desired test suite
##    execute ./createiOSZip.sh to create the test zip for upload in cloud

# Name of the desired test suite and optionally specific test case, eg: iOSSample#mainPageTest
TEST=${TEST:="iOSSample#mainPageTest"}
# JUnit file wont have the #caseName ending
JUNIT="iOSSample"

export SCREENSHOT_FOLDER=target/reports/screenshots/ios/
export PLATFORM_NAME=iOS
export JAVA_HOME=$(/usr/libexec/java_home)
export DYLD_FALLBACK_LIBRARY_PATH=${DYLD_LIBRARY_PATH}:$PWD/lib/mac/opencv:/Users/Shared/libimobiledevice-binaries-master
#export DYLD_FALLBACK_LIBRARY_PATH=${DYLD_LIBRARY_PATH}:/Users/Shared/libimobiledevice-binaries-master
# Replace all spaces with escape: "\ "
export DYLD_FALLBACK_LIBRARY_PATH=${DYLD_FALLBACK_LIBRARY_PATH// /\\ }
echo "DYLD_FALLBACK_LIBRARY_PATH=${DYLD_FALLBACK_LIBRARY_PATH}"

## Cloud setup
echo "UDID is ${UDID}"
echo "Starting Appium ..."
appium-1.5 -U ${UDID} --command-timeout 120 --log-no-colors --log-timestamp >appium.log 2>&1 &
/opt/testdroid/appium-1.4.16-testdroid/bin/ios-webkit-debug-proxy-launcher.js -c ${UDID}:27753 -d >ios-webkit-debug-proxy.log 2>&1 &

ps -ef|grep appium

echo "Extracting tests.zip..."
unzip tests.zip

mvn --quiet install:install-file -Dfile=lib/mac/opencv/opencv-2413.jar -DgroupId=opencv -DartifactId=opencv -Dversion=2.4.13 -Dpackaging=jar
mkdir -p screenshots

ideviceinstaller -l

echo "xcrun --sdk iphonesimulator --show-sdk-version"
xcrun --sdk iphonesimulator --show-sdk-version

chmod +x lib/*

## Start test execution
echo "Running test ${TEST}"
mvn -Dtest=${TEST} --quiet clean test

## Post-processing
# JUnit results need to be at root as "TEST-all.xml"
ln -s target/reports/junit/TEST-${JUNIT}.xml TEST-all.xml
# Screenshots need to be at screenshots directory in root.
rm -r screenshots
ln -s target/reports/screenshots/ios screenshots