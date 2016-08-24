#!/bin/bash

cp run-tests-ios.sh run-tests.sh
rm ios-phone-test.zip
zip -r ios-phone-test.zip run-tests.sh run_ios.py libs resources tests-ios screenshots