#!/bin/bash

# Name of the test file
TEST=${TEST:="BitBarSampleAppTest.py"}

echo "Extracting tests.zip..."
unzip -o tests.zip


#########################################################
#
# Installing required Python libraries
#  - required libraries are in requirement.txt file that
#    is uploaded with this script file
#
#########################################################

echo "Installing requirements from requirements.txt"
chmod 0444 requirements.txt
pip3 install -r requirements.txt

#########################################################
#
# Preparing to start Appium
# - UDID is the device ID on which test will run and
#   required parameter on iOS test runs
# - appium - is a wrapper tha calls the latest installed
#   Appium server. Additional parameters can be passed
#   to the server here.
#
#########################################################

echo "UDID set to ${IOS_UDID}"
echo "Starting Appium ..."
appium -U ${IOS_UDID}  --log-no-colors --log-timestamp --command-timeout 120


#########################################################
#
# Setting of environment variables used later in test
# - used for Appium desired capabilities
# - note, APPIUM_URL is same for local and cloud server
#   runs
#
#########################################################
export APPIUM_APPFILE="$PWD/application.ipa"
export APPIUM_URL="http://localhost:4723/wd/hub"
export APPIUM_DEVICE="Local Device"
export APPIUM_PLATFORM="IOS"
export APPIUM_AUTOMATION="XCUITest"

## Clean local screenshots directory
rm -rf screenshots

## Start test execution
echo "Running test ${TEST}"
python3 ${TEST}

#########################################################
#
# Get test report
# - do any test result post processing your test results
#   need here
# - also any additional files can be retrieved here
# - retrieve files from device
#
#########################################################
mv test-reports/*.xml TEST-all.xml
