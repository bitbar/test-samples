#!/bin/bash

export JAVA_HOME=$(/usr/libexec/java_home)
export APPIUM_PORT=${APPIUM_PORT:="4723"}

## Cloud setup
echo "Getting UDID..."
echo $UDID
#UDID="${echo $UDID}"
echo "UDID set to ${UDID}"

echo "Starting Appium ..."
/opt/appium/build/lib/main.js -U ${UDID} --log-no-colors --log-timestamp --command-timeout 120 >appium.log 2>&1 &

TIMEOUT=30

echo "Waiting for Appium to be ready at port ${APPIUM_PORT}..."

while ! nc -z localhost ${APPIUM_PORT}; do
  sleep 1 # wait for 1 second before check again
  TIMEOUT=$((TIMEOUT-1))
  if [ $TIMEOUT -le 0 ]; then
    echo "Appium failed to start! Aborting."
    exit 1
    break
  fi
done

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

echo "Gathering results"
zip -r robot_results.zip report.html log.html screenshots