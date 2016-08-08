#!/bin/bash

rm android-phone-test.zip
zip -r android-phone-test.zip run-tests.sh run.py libs resources tests screenshots
