#!/bin/bash

## Cloud setup
echo "Starting Appium ..."

/opt/appium/appium/bin/appium.js --log-no-colors --log-timestamp --command-timeout 120 >appium.log 2>&1 &

# Wait for appium to fully launch
sleep 10

ps -ef|grep appium

echo "Extracting tests.zip..."
unzip tests.zip

echo "Installing pip"
curl -O https://bootstrap.pypa.io/get-pip.py
python get-pip.py --user

echo "Installing requirements"
~/.local/bin/pip install -r ./resources/requirements.txt --target .

## Start test execution
echo "Running test"
python run.py -x TEST-all