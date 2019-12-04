#!/bin/bash

# ios version

rm ios-test-files.zip
rm run-tests.sh
cp run-tests-ios.sh run-tests.sh
zip -r ios-test-files run-tests.sh my_app
