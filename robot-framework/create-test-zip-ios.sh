#!/usr/bin/env bash

cp run-tests-ios.sh run-tests.sh
rm tests-robot-ios.zip
zip -r tests-robot-ios.zip run-tests.sh run_ios.py libs resources tests-ios
