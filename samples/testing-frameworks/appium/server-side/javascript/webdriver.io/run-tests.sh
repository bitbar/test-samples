#!/usr/bin/env bash

echo "Preparing..."

# Make sure there's no pre-existing `screenshots` file blocking symbolic link creation
rm -rf screenshots

# Recreate screenshots dir
mkdir screenshots

echo "Extracting tests.zip..."
unzip tests.zip

echo "Installing dependencies..."
npm install

echo "Running tests..."
if [ "$(uname)" == "Darwin" ]; then
		./node_modules/.bin/wdio ios.conf.js
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
		./node_modules/.bin/wdio android.conf.js
else
		echo "Unknown OS system, exiting..."
		exit 1
fi
