#!/bin/bash

export JAVA_HOME=$(/usr/libexec/java_home)

## Cloud setup
echo "Getting UDID..."
echo $UDID
#UDID="${echo $UDID}"
echo "UDID set to ${UDID}"

echo "Starting Appium ..."
/opt/appium/bin/appium.js -U ${UDID} --log-no-colors --log-timestamp --command-timeout 120 >appium.log 2>&1 &

# Wait for appium to fully launch
sleep 10

ps -ef|grep appium

echo "Extracting tests.zip..."
unzip tests.zip

echo "Installing pip"
curl -O https://bootstrap.pypa.io/get-pip.py
python get-pip.py --user

echo "Exporting path"
export PATH=$PATH:$HOME/Library/Python/2.7/bin

echo "Installing requirements"
pip install -r ./resources/requirements.txt --user

## Start test execution
echo "Running test"
python run_ios.py -x TEST-all