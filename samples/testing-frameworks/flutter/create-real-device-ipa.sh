#!/bin/bash

# create ipa from Runner.app and "clean" build/ios directory
rm app.zip
rm app.ipa
mkdir my_app/build/ios/iphoneos/Payload
mv my_app/build/ios/iphoneos/Runner.app my_app/build/ios/iphoneos/Payload/Runner.app
mv my_app/build/ios/iphoneos/Payload Payload
zip -r app.zip Payload
mv app.zip app.ipa
rm -rf Payload
rm -rf my_app/build/ios/iphoneos/
rm -rf my_app/build/ios/Debug-iphoneos/
mkdir my_app/build/ios/iphoneos
