#!/bin/bash

TEST=${TEST:="ios_sample_spec.rb"} # Name of the test file

##### Cloud testrun dependencies start
echo "Extracting tests.zip..."
unzip tests.zip

## Environment variables setup
export SCREENSHOT_FOLDER=target/reports/screenshots/

echo "Setting UDID..."
echo "UDID set to ${IOS_UDID}"

echo "Starting Appium ..."
appium -U ${IOS_UDID}  --log-no-colors --log-timestamp --command-timeout 120
ps -ef|grep appium
##### Cloud testrun dependencies end.

## Run the test:
bundle install
echo "Running test ${TEST}"

bundle exec rspec ${TEST} --format RspecJunitFormatter  --out result.xml

## Post-processing
ln -s *.xml TEST-all.xml

# Make sure there's no pre-existing `screenshots` file blocking symbolic link creation
rm -rf screenshots
# Screenshots need to be available at root as directory `screenshots` .
ln -s ${SCREENSHOT_FOLDER} screenshots












