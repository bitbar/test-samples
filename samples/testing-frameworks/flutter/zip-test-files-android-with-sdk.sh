#!/bin/bash

# android version

rm android-test-files-with-sdk.zip
rm run-tests.sh
cp run-tests-android-with-sdk.sh run-tests.sh
zip -r android-test-files-with-sdk run-tests.sh my_app
