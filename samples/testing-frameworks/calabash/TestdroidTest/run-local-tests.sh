#!/bin/bash
#
# Run Calabash Android tests on local device.
#
# Author: Jarno Tuovinen <jarno.tuovinen@bitbar.com>
#

APPLICATION_PATH=../../apps/builds/Testdroid.apk
# No need to set the device id if only one device is connected
DEVICE_ID=ABC123

# Create helper folders for screenshots and results
SCREENSHOTS="screenshots/"
RESULTS="results/"

if [ -d "${SCREENSHOTS}" ]; then
  rm -rf ${SCREENSHOTS}
fi
mkdir ${SCREENSHOTS}

if [ -d "${RESULTS}" ]; then
  rm -rf ${RESULTS}
fi
mkdir ${RESULTS}

# Run the test

# When running first time run apk resigning:
#calabash-android resign ${APPLICATION_PATH}
# You can add different profiles (configured in cucumber.yml) or tags here:

SCREENSHOT_PATH=${SCREENSHOTS} calabash-android run ${APPLICATION_PATH} -f junit -o ${RESULTS} -f pretty -v

# Use this command if multiple devices connected
#ADB_DEVICE_ARG=${DEVICE_ID} SCREENSHOT_PATH=${SCREENSHOTS} calabash-android run ${APPLICATION_PATH} -f junit -o ${RESULTS} -f pretty -v

