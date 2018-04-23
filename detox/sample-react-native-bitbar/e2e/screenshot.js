// copied from Unitys test run
const spawn = require('child-process-promise').spawn;
const log = require('npmlog');

let takeScreenshot = function(screenshotName) {
  const screenshotDestination = `screenshots/${screenshotName}.png`;

  if (device.getPlatform() === 'ios') {
    return spawn('sh', ['-c', `/usr/bin/xcrun simctl io ${device._deviceId} screenshot "${screenshotDestination}"`]);
  } else {
    return spawn('sh', ['-c', `adb -s ${device._deviceId} shell /system/bin/screencap -p /sdcard/screenshot.png && adb -s ${device._deviceId} pull /sdcard/screenshot.png "${screenshotDestination}"`]);
  }
};

module.exports = takeScreenshot;
