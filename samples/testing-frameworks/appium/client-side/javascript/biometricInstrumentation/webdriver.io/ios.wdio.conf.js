const path = require("path");

const credentials = require("./.credentials.json");
const apiKey = credentials["apiKey"];
const appId = credentials["iosAppId"];

exports.config = {
  framework: "mocha",
  mochaOpts: {
    ui: "bdd",
    timeout: 60000,
  },
  maxInstances: 1,

  logLevel: "silent",
  deprecationWarnings: true,

  bail: 0,
  waitforTimeout: 10000,
  connectionRetryTimeout: 500000,
  connectionRetryCount: 0,

  reporters: ["spec"],

  protocol: "https",
  hostname: "appium.bitbar.com",
  port: 443,
  path: "/wd/hub",

  specs: ["./test/specs/ios/*.e2e.js"],

  capabilities: [
    {
      "platformName": "iOS",
      "appium:deviceName": "iPhone device",
      "appium:automationName": "XCUITest",
      "appium:bundleId": "org.reactjs.native.example.BitBarSampleApp",
      "bitbar:options": {
        app: appId,
        apiKey: apiKey,
        device: "Apple iPhone 11 Pro A2215 13.1.2",
        biometricInstrumentation: true,
        project: "WebdriverIO_iOS_biometric_sample",
        multiSessionWait: 10,
        //appiumVersion: "1.22.3" //launch tests on appium 1
      }
    },
  ],

  //
  // =====
  // Hooks
  // =====
  // WebdriverIO provides several hooks you can use to interfere with the test process in order to enhance
  // it and to build services around it. You can either apply a single function or an array of
  // methods to it. If one of them returns with a promise, WebdriverIO will wait until that promise got
  // resolved to continue.

  /**
   * Gets executed once before all workers get launched.
   * @param {Object} config wdio configuration object
   * @param {Array.<Object>} capabilities list of capabilities details
   */
  onPrepare: function (config, capabilities) {
    // clean screenshots dir
    const rimraf = require("rimraf");
    rimraf.sync("./screenshots/*", {
      glob: {
        ignore: "./screenshots/.gitkeep",
      },
    });
  },

  /**
   * Gets executed just before initialising the webdriver session and test framework. It allows you
   * to manipulate configurations depending on the capability or spec.
   * @param {Object} config wdio configuration object
   * @param {Array.<Object>} capabilities list of capabilities details
   * @param {Array.<String>} specs List of spec file paths that are to be run
   */
  // beforeSession: function (config, capabilities, specs) {
  // },

  /**
   * Gets executed before test execution begins. At this point you can access to all global
   * variables like `browser`. It is the perfect place to define custom commands.
   * @param {Array.<Object>} capabilities list of capabilities details
   * @param {Array.<String>} specs List of spec file paths that are to be run
   */
  // before: function (capabilities, specs) {
  // },
  before: function () {
    const chai = require("chai");
    global.expect = chai.expect;
    chai.should();

    const fs = require("fs");

    global.takeScreenshot = async (fileName) => {
      let screenshot = await driver.takeScreenshot();
      screenshot = screenshot.replace(/^data:image\/png;base64,/, "");
      let filePath = path.resolve(`./screenshots/${fileName}.png`);
      fs.writeFileSync(filePath, screenshot, "base64");
    };
  },

  /**
   * Runs before a WebdriverIO command gets executed.
   * @param {String} commandName hook command name
   * @param {Array} args arguments that command would receive
   */
  // beforeCommand: function (commandName, args) {
  // },

  /**
   * Hook that gets executed before the suite starts
   * @param {Object} suite suite details
   */
  // beforeSuite: function (suite) {
  // },

  /**
   * Function to be executed before a test (in Mocha/Jasmine) starts.
   */
  // beforeTest: function (test, context) {
  // },

  /**
   * Hook that gets executed _before_ a hook within the suite starts (e.g. runs before calling
   * beforeEach in Mocha)
   */
  // beforeHook: function (test, context) {
  // },

  /**
   * Hook that gets executed _after_ a hook within the suite starts (e.g. runs after calling
   * afterEach in Mocha)
   */
  // afterHook: function (test, context, { error, result, duration, passed, retries }) {
  // },

  /**
   * Function to be executed after a test (in Mocha/Jasmine).
   */
  // afterTest: function(test, context, { error, result, duration, passed, retries }) {
  // },

  /**
   * Hook that gets executed after the suite has ended
   * @param {Object} suite suite details
   */
  // afterSuite: function (suite) {
  // },

  /**
   * Runs after a WebdriverIO command gets executed
   * @param {String} commandName hook command name
   * @param {Array} args arguments that command would receive
   * @param {Number} result 0 - command success, 1 - command error
   * @param {Object} error error object if any
   */
  // afterCommand: function (commandName, args, result, error) {
  // },

  /**
   * Gets executed after all tests are done. You still have access to all global variables from
   * the test.
   * @param {Number} result 0 - test pass, 1 - test fail
   * @param {Array.<Object>} capabilities list of capabilities details
   * @param {Array.<String>} specs List of spec file paths that ran
   */
  // after: function (result, capabilities, specs) {
  // },

  /**
   * Gets executed right after terminating the webdriver session.
   * @param {Object} config wdio configuration object
   * @param {Array.<Object>} capabilities list of capabilities details
   * @param {Array.<String>} specs List of spec file paths that ran
   */
  // afterSession: function (config, capabilities, specs) {
  // },

  /**
   * Gets executed after all workers got shut down and the process is about to exit. An error
   * thrown in the onComplete hook will result in the test run failing.
   * @param {Object} exitCode 0 - success, 1 - fail
   * @param {Object} config wdio configuration object
   * @param {Array.<Object>} capabilities list of capabilities details
   * @param {<Object>} results object containing test results
   */
  // onComplete: function(exitCode, config, capabilities, results) {
  // },

  /**
   * Gets executed when a refresh happens.
   * @param {String} oldSessionId session ID of the old session
   * @param {String} newSessionId session ID of the new session
   */
  //onReload: function(oldSessionId, newSessionId) {
  //}
};
