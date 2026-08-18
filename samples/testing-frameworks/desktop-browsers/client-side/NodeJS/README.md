Intro
=====

This folder contains a Node.js client-side Selenium sample for running a desktop browser test in BitBar Cloud.

Files
=====

* `bitbar_selenium.js` contains the sample test flow that opens the BitBar sample page, validates title/text, and verifies button style.
* `package.json` defines the Node.js dependencies for this sample.
* `package-lock.json` locks dependency versions.

Prerequisites
=============

* Node.js 18+
* npm
* A valid BitBar API key

Setup
=====

Install dependencies from `package.json`:

```bash
npm install
```

Then update your API key in `bitbar_selenium.js`:

```js
'apiKey': '<insert your BitBar API key here>'
```

You can also customize browser/platform values in the same capabilities block.

Run
===

From this folder, run:

```bash
node bitbar_selenium.js
```

Successful output includes:

```text
Bitbar - Test Page for Samples
Bitbar
```

Troubleshooting
===============

* `SessionNotCreatedException: Full authentication is required to access this resource` usually means the BitBar API key is missing, invalid, or expired.
* `Error [ERR_MODULE_NOT_FOUND]` for `chai` or `Cannot find module 'selenium-webdriver'` means dependencies were not installed. Run `npm install` again in this folder.
* `Failed to submit test run! No desktop browser matching Desired Capabilities` means selected browser/platform/options are not available in the BitBar environment.

