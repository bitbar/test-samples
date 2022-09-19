#!/bin/bash

export JAVA_HOME=$(/usr/libexec/java_home)
export APPIUM_PORT=${APPIUM_PORT:="4723"}

if [ -z ${UDID} ] ; then
  export UDID=${IOS_UDID}
fi
echo "UDID is ${UDID}"

# Create the screenshots directory, if it doesn't exist'
mkdir -p .screenshots

echo "Starting Appium ..."
appium -U ${UDID} --log-no-colors --log-timestamp

echo "Extracting tests.zip..."
unzip -o tests.zip

echo "Installing requirements"
chmod 0755 resources/requirements.txt
python3 -m pip install --user  --requirement resources/requirements.txt

## Start test execution
echo "Running test"
python3 run_ios.py -x TEST-all

echo "Gathering results"
mkdir -p output-files
cp -r screenshots output-files
mv report.html log.html output-files
zip -r output-files.zip output-files
