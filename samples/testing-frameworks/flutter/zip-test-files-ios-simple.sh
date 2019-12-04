#!/bin/bash

# ios version

rm ios-test-files-simple.zip
rm run-tests.sh
cp run-tests-ios-simple.sh run-tests.sh
zip -r ios-test-files-simple run-tests.sh my_app
