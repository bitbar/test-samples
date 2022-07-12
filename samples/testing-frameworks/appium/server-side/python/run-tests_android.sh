#!/bin/bash

##
## Work in progress! The dependency installations need to be done to the
## container so that we don't need to install them here.
##
TEST=${TEST:="BitbarSampleAppTest.py"} #Name of the test file

##### Cloud testrun dependencies start
echo "Extracting tests.zip..."
unzip tests.zip

echo "Installing pip for python"
curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py
python get-pip.py

echo "Installing Appium Python Client 0.24 and xmlrunner 1.7.7"
chmod 0755 requirements.txt
pip install -r requirements.txt

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
