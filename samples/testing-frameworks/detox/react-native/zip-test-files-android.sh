#!/bin/bash

# android version

rm android-test-files.zip
rm run-tests.sh
cp run-tests-android.sh run-tests.sh
zip -r android-test-files run-tests.sh run-tests-android.sh android/app/build/outputs/apk app.js index.android.js index.js package.json e2e rn-cli.config.js