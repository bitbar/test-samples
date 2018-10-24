#!/usr/bin/env bash

cp run-tests-android.sh run-tests.sh
rm tests-robot-android.zip
zip -r tests-robot-android.zip run-tests.sh run_android.py libs resources tests-android
