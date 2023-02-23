#!/usr/bin/env bash

echo "Starting Appium ..."
appium --log-no-colors --log-timestamp >/dev/null

echo "Preparing..."

# Make sure there's no pre-existing `screenshots` file blocking symbolic link creation
rm -rf screenshots

# Recreate screenshots dir
mkdir screenshots

echo "Extracting tests.zip..."
unzip tests.zip

echo "Installing dependencies..."
export SDKROOT=$(xcrun --sdk macosx --show-sdk-path)
npm ci

echo "Running tests..."
if [ "$(uname)" == "Darwin" ]; then
  npm run ios
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
  npm run android
else
  echo "Unknown OS system, exiting..."
  exit 1
fi
