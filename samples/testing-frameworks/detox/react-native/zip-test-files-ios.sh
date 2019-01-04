#!/bin/bash

# ios version

rm ios-test-files.zip
rm run-tests.sh
cp run-tests-ios.sh run-tests.sh
zip -r ios-test-files run-tests.sh run-tests-ios.sh ios/build/Build/Products/Release-iphonesimulator/sampleproject.app app.js index.js package.json e2e rn-cli.config.js