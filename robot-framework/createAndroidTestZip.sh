#!/bin/bash

cp run-tests-android.sh run-tests.sh
rm android-test.zip
zip -r android-test.zip run-tests.sh run_android.py libs resources tests-android
