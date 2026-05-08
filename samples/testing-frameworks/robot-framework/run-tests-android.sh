#!/bin/bash


echo "Extracting tests.zip..."
unzip -o tests.zip

echo "Installing requirements"
chmod 0755 resources/requirements.txt
python3 -m pip install -r resources/requirements.txt

## start Appium server
echo "Starting Appium ..."

appium --log-no-colors --log-timestamp

ps -ef|grep appium

if [ -f application.apk ]; then
  export APPFILE=$PWD/application.apk
fi

echo "App file is ${APPFILE}"

## Start test execution
echo "Running test"
if [ -n "${APPFILE}" ]; then
  python3 run_android.py --variable "APPFILE:${APPFILE}" -x TEST-all
else
  python3 run_android.py -x TEST-all
fi

echo "Gathering results"
mkdir -p output-files
cp -r screenshots output-files
mv report.html log.html output-files
