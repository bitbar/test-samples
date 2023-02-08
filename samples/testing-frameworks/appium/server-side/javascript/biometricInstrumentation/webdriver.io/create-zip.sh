#!/usr/bin/env bash

# You can pass zip name as argument - default: <test.zip>
ZIP_NAME=${1:-"test"}

zip "${ZIP_NAME}".zip \
  package.json \
  package-lock.json \
  run-tests.sh \
  test/specs/**/* \
  android.wdio.conf.js \
  ios.wdio.conf.js
