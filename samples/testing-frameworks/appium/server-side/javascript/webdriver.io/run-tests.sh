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
./node_modules/.bin/wdio wdio.conf.js
