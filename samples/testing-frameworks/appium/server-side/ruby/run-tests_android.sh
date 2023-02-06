#!/bin/bash

TEST=${TEST:="android_sample_spec.rb"} # Name of the test file

##### Cloud testrun dependencies start
echo "Extracting tests.zip..."
unzip tests.zip

## Environment variables setup
export SCREENSHOT_FOLDER=target/reports/screenshots/

echo "Starting Appium ..."
appium --log-no-colors --log-timestamp
##### Cloud testrun dependencies end.

## Run the test:
bundle install
echo "Running test ${TEST}"

# Make sure there's no pre-existing `screenshots` files
rm -rf screenshots
bundle exec rspec ${TEST} --format RspecJunitFormatter  --out TEST-all.xml

# Screenshots need to be available at root as directory `screenshots` .
cp -a ${SCREENSHOT_FOLDER}. screenshots/












