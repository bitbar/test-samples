# Selenium NodeJS Sample

This folder contains a sample client-side Selenium test for desktop browsers in BitBar Cloud.

## Files

- `bitbar_selenium.js` runs a Selenium WebDriver session against the BitBar desktop hub, opens the sample page, clicks the button, and validates the page title, result text, and button style.
- `package.json` defines the Node.js dependencies for the sample.

## Requirements

- Node.js
- npm
- A valid BitBar API key

## Dependencies

This sample uses the dependencies declared in `package.json`:

- `selenium-webdriver` `^4.28.1`
- `chai` `^5.1.2`

## Before Running

Open `bitbar_selenium.js` and replace the `apiKey` value with your own BitBar API key.

Install the project dependencies from this folder:

```bash
npm install
```

## Run Locally

From this folder, run:

```bash
node bitbar_selenium.js
```

## Expected Output

A successful run prints output similar to:

Bitbar - Test Page for Samples
Bitbar


## Troubleshooting

If `npm install` fails, verify that `node` and `npm` are installed and available in your shell.

If the test starts but fails to create a remote session, check that the BitBar `apiKey` in `bitbar_selenium.js` is valid.