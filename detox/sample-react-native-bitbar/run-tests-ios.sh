#!/bin/bash

##### Cloud testrun dependencies start
echo "[Testdroid-ssa-client] Extracting tests.zip..."
unzip tests.zip

echo "[Testdroid-ssa-client] NOT Starting Appium, start it in your test script if needed!"

##### Cloud testrun dependencies end.

export APPIUM_APPFILE=$PWD/application.ipa #dummy app, not the actual app

## Run the test:
echo "[Testdroid-ssa-client] Running test ${TEST}"

export APPFILE=$PWD/ios/build/Build/Products/Release-iphonesimulator/sampleproject.app

# put local tools to path
export PATH=$PATH:$PWD

export SCREENSHOTSFOLDER=$PWD/screenshots

#################################

echo "using APPFILE: ${APPFILE}"

cd "$(dirname "$0")" || exit


echo "Before install $(date)"

if [ -z ${UDID} ] ; then
	export UDID=${IOS_UDID}
fi
	echo "UDID is ${UDID}"


rm -rf "$SCREENSHOTSFOLDER"
rm ./*.xml
mkdir -p "$SCREENSHOTSFOLDER"

echo $SCREENSHOTSFOLDER

brew tap wix/brew
brew install applesimutils

node --version
npm -version
watchman --version

echo "Npm install"
npm install

#build project
#"${PWD}/node_modules/.bin/detox" build --configuration ios.sim.release

#echo "Launching Detox server"
# no need to start server for iOS simulator
#"${PWD}/node_modules/.bin/detox" run-server &
#"${PWD}/node_modules/.bin/detox" run-server > detox-server.log 2>&1 &

cat package.json

"${PWD}/node_modules/.bin/detox" test --configuration ios.sim.release --loglevel verbose > detox.log 2>&1
#"${PWD}/node_modules/.bin/detox" test --configuration ios.sim.debug --loglevel verbose > detox.log 2>&1
scriptExitStatus=$?

ls -la detox.log

echo "detox-test-log"
cat detox.log

echo "detox-server-log"
cat detox-server.log

echo "Test has been run $(date), exit status: '${scriptExitStatus}'"

mv ./*.xml TEST-all.xml

exit $scriptExitStatus
