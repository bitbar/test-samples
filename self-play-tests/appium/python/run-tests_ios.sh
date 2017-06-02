#!/bin/bash

##
## Work in progress! The dependency installations need to be done to the
## container so that we don't need to install them here.
##
export JAVA_HOME=$(/usr/libexec/java_home)

TEST=${TEST:="BitbarSampleAppTest.py"} # Name of the test file

##### Cloud testrun dependencies start
echo "Extracting tests.zip..."
unzip tests.zip

echo "Installing pip for python"
curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py
sudo python get-pip.py

echo "Installing Appium Python Client 0.24 and xmlrunner 1.7.7"
chmod 0755 requirements.txt
sudo pip install -r requirements.txt

if [ -z ${UDID} ] ; then
  export UDID=${IOS_UDID}
fi
echo "UDID is ${UDID}"

echo "Starting Appium ..."
appium-1.6 -U ${UDID} --log-no-colors --log-timestamp --show-ios-log --command-timeout 120

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

ln -s test-reports/*.xml TEST-all.xml
