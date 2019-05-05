const detox = require('detox');
const config = require('../package.json').detox;
const takeScreenshot = require('./screenshot');
const sendADBInputCommand = require('./android-adb-command');

before(async () => {
	console.log('before');
  await detox.init(config);
  device.takeScreenshot = takeScreenshot;
  device.sendADBInputCommand = sendADBInputCommand;
});

after(async () => {
	console.log('after');
  await detox.cleanup();
});