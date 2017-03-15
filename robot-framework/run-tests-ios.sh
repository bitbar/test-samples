#!/bin/bash

export JAVA_HOME=$(/usr/libexec/java_home)
export APPIUM_PORT=${APPIUM_PORT:="4723"}

if [ -z ${UDID} ] ; then
  export UDID=${IOS_UDID}
fi
echo "UDID is ${UDID}"

echo "Starting Appium ..."
node /opt/testdroid/appium -U ${UDID} --log-no-colors --log-timestamp --command-timeout 120 >appium.log 2>&1 &
node /opt/testdroid/appium/bin/ios-webkit-debug-proxy-launcher.js -c ${UDID}:27753 -d >ios-webkit-debug-proxy.log 2>&1 &
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


echo "Installing requirements"
pip install -r ./resources/requirements.txt --user

## Start test execution
echo "Running test"
python run_ios.py -x TEST-all

echo "Gathering results"
mkdir -p output-files
cp -r screenshots output-files
mv report.html log.html output-files
zip -r output-files.zip output-files
