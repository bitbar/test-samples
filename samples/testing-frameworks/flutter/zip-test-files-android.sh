#!/bin/bash

# android version

rm android-test-files.zip
rm run-tests.sh
cp run-tests-android.sh run-tests.sh
zip -r android-test-files run-tests.sh my_app
