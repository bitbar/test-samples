#!/bin/bash


echo "Extracting tests.zip..."
unzip -o tests.zip

echo "Installing pip"
curl -O https://bootstrap.pypa.io/get-pip.py
python get-pip.py --user

echo "Installing requirements"
~/.local/bin/pip install -r ./resources/requirements.txt --target .

## start Appium server
echo "Starting Appium ..."
appium-1.6 --log-no-colors --log-timestamp --command-timeout 120

## Start test execution
echo "Running test"
python run_android.py -x TEST-all

echo "Gathering results"
mkdir -p output-files
cp -r screenshots output-files
mv report.html log.html output-files
