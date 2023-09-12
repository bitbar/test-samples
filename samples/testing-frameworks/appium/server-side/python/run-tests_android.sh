#!/bin/bash

##
## Work in progress! The dependency installations need to be done to the
## container so that we don't need to install them here.
##
TEST=${TEST:="BitBarSampleAppTest.py"} #Name of the test file

##### Cloud testrun dependencies start
echo "Extracting tests.zip..."
unzip tests.zip

echo "Installing Appium Python Client and xmlrunner"
chmod 0444 requirements.txt
pip3 install -r requirements.txt

echo "Starting Appium ..."

appium --log-no-colors --log-timestamp

ps -ef|grep appium
##### Cloud testrun dependencies end.

#App file is at current working folder
if [ -f application.apk ]; then
  export APPIUM_APPFILE=$PWD/application.apk
elif [ -f application.apks ]; then
  export APPIUM_APPFILE=$PWD/application.apks
fi

## Desired capabilities:

export APPIUM_URL="http://localhost:4723/wd/hub" # Local & Cloud
export APPIUM_DEVICE="Local Device"
export APPIUM_PLATFORM="android"

APILEVEL=$(adb shell getprop ro.build.version.sdk)
APILEVEL="${APILEVEL//[$'\t\r\n']}"
echo "API level is: ${APILEVEL}"

## APPIUM_AUTOMATION
if [ "$APILEVEL" -gt "16" ]; then
  echo "Setting APPIUM_AUTOMATION=Appium"
  export APPIUM_AUTOMATION="uiautomator2"
else
  echo "Setting APPIUM_AUTOMATION=selendroid"
  export APPIUM_AUTOMATION="Selendroid"
fi

## Run the test:
echo "Running test ${TEST}"
rm -rf screenshots

python ${TEST}

mv test-reports/*.xml TEST-all.xml
