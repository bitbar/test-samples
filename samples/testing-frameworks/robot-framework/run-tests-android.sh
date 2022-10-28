#!/bin/bash


echo "Extracting tests.zip..."
unzip -o tests.zip

echo "Installing requirements"
chmod 0755 resources/requirements.txt
python3 -m pip install -r resources/requirements.txt

## start Appium server
echo "Starting Appium ..."
appium --log-no-colors --log-timestamp --command-timeout 120

## Start test execution
echo "Running test"
python3 run_android.py -x TEST-all

echo "Gathering results"
mkdir -p output-files
cp -r screenshots output-files
mv report.html log.html output-files
