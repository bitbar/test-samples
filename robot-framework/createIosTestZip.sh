#!/bin/bash

cp run-tests-ios.sh run-tests.sh
rm ios-test.zip
zip -r ios-test.zip run-tests.sh run_ios.py libs resources tests-ios
