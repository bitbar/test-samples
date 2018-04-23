#!/bin/bash

TEST=${TEST:="BitbarSampleAppTest.py"} # Name of the test file

##### Cloud testrun dependencies start
echo "Extracting tests.zip..."
unzip tests.zip

echo "Setting UDID..."
echo "UDID set to ${UDID}"

echo "Starting Appium ..."
appium-1.6 -U ${UDID}  --log-no-colors --log-timestamp --show-ios-log --command-timeout 120

ps -ef|grep appium
##### Cloud testrun dependencies end.

export APPIUM_APPFILE=$PWD/application.ipa #App is at current working folder

## Desired capabilities:

export APPIUM_URL="http://localhost:4723/wd/hub" # Local & Cloud
export APPIUM_DEVICE="Local Device"
export APPIUM_PLATFORM="IOS"
export APPIUM_AUTOMATION="XCUITest"

## Run the test:
echo "Running test ${TEST}"
rm -rf screenshots

python ${TEST}

mv test-reports/*.xml TEST-all.xml
