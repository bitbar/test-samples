#!/bin/bash

# ios version

rm ios-test-files-simulator.zip
rm run-tests.sh
cp run-tests-ios-simulator.sh run-tests.sh
zip -r ios-test-files-simulator run-tests.sh my_app
