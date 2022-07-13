# Appium Client Side + Webdriver.io

This directory contains simple test scenario using
[Appium](http://appium.io/) + [Webdriver.io](https://webdriver.io/) + [Mocha](https://mochajs.org/).

Note: For now it's Android only example.

## Structure

```
.
├── package.json
├── package-lock.json
├── .npmrc
├── screenshots
│   └── .gitkeep
├── test
│   └── specs
│       └── main.js
├── wdio.conf.js
└── .credentials.json
```

* `package.json` and `package-lock.json` is obvious for everyone familiar with Node.js. Shortly for those who doesn't know:
it contains information about project and what's more important - dependenencies.
* `.npmrc` - Config for npm
* `screenshots` - Here will appear all taken screenshots. Note: Directory will be cleared before each test run.
* `test/specs/main.js` - Test itself
* `wdio.conf.js` - Config for [Webdriver.io Testrunner](https://webdriver.io/docs/gettingstarted.html)
* .`credentials.json` - This file isn't included in the repository, however it's required to run sample code. You need to create it by yourself according to instructions below. It is used to store sensitive data.

### Dependencies

* `@wdio/cli` - Webdriver.io (and Testrunner)
* `@wdio/local-runner` - To run test "locally" (because from server perspective it's locally)
* `@wdio/mocha-framework` - Mocha support
* `@wdio/spec-reporter` - Default [spec reporter](https://webdriver.io/docs/spec-reporter.html)
* `@wdio/sync` - To be able to enable [sync mode](https://webdriver.io/docs/sync-vs-async.html)
* `chai` - Chai for Mocha (because I wanted to use [should](https://www.chaijs.com/guide/styles/#should) [BDD style])

## Usage

### Install Dependencies

```
npm ci
```
### Add your apiKey to `./.credentials.json`

Create a file called ".credentials.json" in the project's root and add your testdroid apiKey to it as described below:

```json
{
    "apiKey": "YOUR_BITBAR_CLOUD_APIKEY"
}
```

### Add sample application ID to `./.credentials.json`
Download [sample application](https://github.com/bitbar/test-samples/blob/master/apps/android/bitbar-sample-app.apk) and upload it to your BitBar Files Library. Copy applications ID and add it to  `.credentials.json` file:

```json
{
    "apiKey": "YOUR_BITBAR_CLOUD_APIKEY",
    "appId": "YOUR_APPLICATION_ID"
}
```

### Prepare Webdriver.io Configuration

You need to edit `wdio.conf.js` and set capabilities. You can either edit existing one (if you set API key then it should
work out of the box) or you can use [Capabilities Creator](https://cloud.bitbar.com/#public/capabilities-creator) (you just need
to switch mode to "Appium" and language to "Node.js").

### Run

```
npm run test
```

## FAQ

> To use Chai's `should` you need to run proper code. Where it's done?

It's in `wdio.conf.js`'s `before` hook.

> There is global function `takeScreenshot` in test - where it came from?

It's in `wdio.conf.js`'s `before` hook.

## Links

* [Webdriver.io Testrunner Configuration](https://webdriver.io/docs/configurationfile.html) (helpful if you want to understand and modify `wdio.conf.js`)
* [Webdriver.io Appium API](https://webdriver.io/docs/api/appium.html)
* [Chai BDD API](https://www.chaijs.com/api/bdd/)
