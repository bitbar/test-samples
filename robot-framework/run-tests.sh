#!/bin/bash

export APPIUM_PORT=${APPIUM_PORT:="4723"}

## Cloud setup
echo "Starting Appium ..."

appium-1.6 --log-no-colors --log-timestamp --command-timeout 120 >appium.log 2>&1 &

TIMEOUT=20

echo "Waiting service to launch on {$APPIUM_PORT}..."

while ! nc -z localhost {$APPIUM_PORT}; do   
  sleep 1 # wait for 1 second before check again
  TIMEOUT=$((TIMEOUT-1))
  info "Waited $TIMEOUT"
  if [ $TIMEOUT -le 0 ]; then
    info "Service is not responding.."
    break
  fi 
done

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
python run_android.py -x TEST-all

echo "Gathering results"
zip -r robot_results.zip report.html log.html screenshots