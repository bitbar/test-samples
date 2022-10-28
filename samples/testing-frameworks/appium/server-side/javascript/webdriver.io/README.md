# Appium Server Side + Webdriver.io

This directory contains simple test scenario using
[Appium](http://appium.io/) + [Webdriver.io](https://webdriver.io/) + [Mocha](https://mochajs.org/).

## Structure

```text
.
├── package.json
├── package-lock.json
├── run-tests.sh
├── test
│   └── specs
│       ├─── android.js
│       └─── ios.js
├─── android.conf.js
├─── ios.conf.js
└─── create-zip.sh
```

* `package.json` and `package-lock.json` is obvious for everyone familiar with Node.js. Shortly for those who doesn't know:
it contains information about project and what's more important - dependenencies.
* `run-tests.sh` - starting point for BitBar Cloud executor
* `test/specs/android.js` or `test/specs/ios.js` - Test itself
* `android.conf.js` - Config for android [Webdriver.io Testrunner](https://webdriver.io/docs/gettingstarted.html)
* `ios.conf.js` - Config for ios [Webdriver.io Testrunner](https://webdriver.io/docs/gettingstarted.html)
* `create-zip.sh` - script creating zip file needed to run tests on BitBar

### Dependencies

* `@wdio/appium-service` - [Appium Service](https://webdriver.io/docs/appium-service.html)
* `@wdio/cli` - Webdriver.io (and Testrunner)
* `@wdio/junit-reporter` - JUnit reporter, because Bitbar Cloud reads JUnit reports to fetch information about i.a. test cases
* `@wdio/local-runner` - To run test "locally" (because from server perspective it's locally)
* `@wdio/mocha-framework` - Mocha support
* `@wdio/spec-reporter` - Default [spec reporter](https://webdriver.io/docs/spec-reporter.html)
* `@wdio/sync` - To be able to enable [sync mode](https://webdriver.io/docs/sync-vs-async.html)
* `chai` - Chai for Mocha (because I wanted to use [should](https://www.chaijs.com/guide/styles/#should) [BDD style])

## Usage

### Prepare package

As always for server side execution you need to create zip package. Zip all files mentioned in [Structure](#structure)
section above.

You can do it either manually (with your OS file explorer) or by command line. Linux example:

```bash
zip "sample-test.zip" \
    "package.json" \
    "package-lock.json" \
    "run-tests.sh" \
    "test/specs/android.js" \
    "test/specs/ios.js" \
    "android.conf.js" \
    "ios.conf.js"
```

We've prepared script to make running that command easier. Just run `create-zip.sh` with output file name passed as parameter.

```bash
./create-zip.sh "sample-test"
```

### Upload Test and Create Test Run

The simplest way to do that would be using [Bitbar Cloud UI](https://cloud.bitbar.com/#testing/test-run-creator).
Especially if you don't know your API key.
[Click here to check out documentation](https://docs.bitbar.com/testing/user-manuals/projects/index.html#create-a-new-test-run).

This test is using for Android [bitbar-sample-app.apk](../../../../../../apps/android/bitbar-sample-app.apk) and for iOS [bitbar-ios-sample.ipa](../../../../../../apps/ios/bitbar-ios-sample.ipa).
Download it and use it with prepared test package.

## FAQ

> To use Chai's `should` you need to run proper code. Where it's done?

It's in `*.conf.js`'s `before` hook.

> There is global function `takeScreenshot` in test - where it came from?

It's in `*.conf.js`'s `before` hook.

## Links

* [Webdriver.io Testrunner Configuration](https://webdriver.io/docs/configurationfile.html) (helpful if you want to understand and modify `*.conf.js`)
* [Webdriver.io Appium API](https://webdriver.io/docs/api/appium.html)
* [Chai BDD API](https://www.chaijs.com/api/bdd/)
