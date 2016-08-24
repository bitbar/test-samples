#!/bin/bash

cp run-tests-android.sh run-tests.sh
rm android-phone-test.zip
zip -r android-phone-test.zip run-tests.sh run_android.py libs resources tests-android screenshots