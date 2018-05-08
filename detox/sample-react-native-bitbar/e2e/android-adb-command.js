// send input command to device, for example keyevent 4 = back button
const spawn = require('child-process-promise').spawn;
const log = require('npmlog');

let sendADBInputCommand = function(commandName) {
  const inputCommand = `${commandName}`;
  return spawn('sh', ['-c', `adb -s ${device._deviceId} shell input "${inputCommand}"`]);
};

module.exports = sendADBInputCommand;
