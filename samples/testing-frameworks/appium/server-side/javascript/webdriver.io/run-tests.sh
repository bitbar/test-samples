#!/usr/bin/env bash

echo "Starting Appium ..."
# Use appium-X.X, to choose a specific Appium server version. More about supported versions at
# https://support.smartbear.com/bitbar/docs/en/mobile-app-tests/automated-testing/appium-support.html
appium --log-no-colors --log-timestamp > /dev/null

echo "Preparing..."

echo "Extracting tests.zip..."
unzip tests.zip

echo "Installing dependencies..."
export SDKROOT=$(xcrun --sdk macosx --show-sdk-path)
npm ci

echo "Running tests..."
if [ "$(uname)" == "Darwin" ]; then
	npm run test-ios
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
	npm run test-android
else
	echo "Unknown OS system, exiting..."
	exit 1
fi
