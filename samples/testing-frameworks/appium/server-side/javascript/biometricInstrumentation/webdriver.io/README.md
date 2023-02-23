# Appium Server Side with biometric instrumentation + Webdriver.io

This directory contains simple test scenario using
[Appium](http://appium.io/) + [Webdriver.io](https://webdriver.io/) + [Mocha](https://mochajs.org/).

## Structure

```text
.
├── README.md
├── android.wdio.conf.js
├── create-zip.sh
├── ios.wdio.conf.js
├── package-lock.json
├── package.json
├── run-tests.sh
└── test
    └── specs
        ├── android
        │   ├── biometricFail.e2e.js
        │   └── biometricPass.e2e.js
        └── iOS
            ├── biometricFail.e2e.js
            └── biometricPass.e2e.js
```

-   `package.json` and `package-lock.json` is obvious for everyone familiar with Node.js. Shortly for those who doesn't know:
    it contains information about project and what's more important - dependenencies.
-   `run-tests.sh` - starting point for BitBar Cloud executor
-   `test/specs/**/*e2e.js` - Tests themselves
-   `android.wdio.conf.js` - Config for android [Webdriver.io Testrunner](https://webdriver.io/docs/gettingstarted.html)
-   `ios.wdio.conf.js` - Config for ios [Webdriver.io Testrunner](https://webdriver.io/docs/gettingstarted.html)
-   `create-zip.sh` - script creating zip file needed to run tests on BitBar

### Dependencies

-   `@wdio/cli` - Webdriver.io (and Testrunner)
-   `@wdio/junit-reporter` - JUnit reporter, because Bitbar Cloud reads JUnit reports to fetch information about i.a. test cases
-   `@wdio/local-runner` - To run test "locally" (because from server perspective it's locally)
-   `@wdio/mocha-framework` - Mocha support
-   `@wdio/spec-reporter` - Default [spec reporter](https://webdriver.io/docs/spec-reporter.html)
-   `@wdio/sync` - To be able to enable [sync mode](https://webdriver.io/docs/sync-vs-async.html)
-   `chai` - Chai for Mocha (because I wanted to use [should](https://www.chaijs.com/guide/styles/#should) [BDD style])

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
    "test/specs/**/*" \
    "android.wdio.conf.js" \
    "ios.wdio.conf.js"
```

We've prepared script to make running that command easier. Just run `create-zip.sh` with output file name passed as parameter.

```bash
./create-zip.sh "sample-test"
```

### Upload Test and Create Test Run

The simplest way to do that would be using [Bitbar Cloud UI](https://cloud.bitbar.com/#testing/test-run-creator).
Especially if you don't know your API key.
[Click here to check out documentation](https://support.smartbear.com/bitbar/docs/testing-with-bitbar/organizing-tests-and-devices/managing-test-runs/create.html).

This test is using for Android [BitBarSampleApp.apk](../../../../../../../apps/android/BitBarSampleApp.apk) and for iOS [BitBarSampleApp.ipa](../../../../../../../apps/ios/BitBarSampleApp.ipa).
Download it and use it with prepared test package.

## FAQ

> To use Chai's `should` you need to run proper code. Where it's done?

It's in `*.conf.js`'s `before` hook.

> There is global function `takeScreenshot` in test - where it came from?

It's in `*.conf.js`'s `before` hook.

## Links

-   [Webdriver.io Testrunner Configuration](https://webdriver.io/docs/configurationfile.html) (helpful if you want to understand and modify `*.conf.js`)
-   [Webdriver.io Appium API](https://webdriver.io/docs/api/appium.html)
-   [Chai BDD API](https://www.chaijs.com/api/bdd/)
