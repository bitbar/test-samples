#!/bin/bash

# Enable trace for eaier debuging of the script
set -x

# Install dependencies
npm install

# Run the app
npm start &
# Wait for the app to launch TODO: don't use sleep for it.
sleep 30

# Increase Cypress verify timeout to avoid timeout issues on Bitbar Cloud
export CYPRESS_VERIFY_TIMEOUT=100000

# Run tests.
# It is crucial to set junit as a reporter. In user's project it can be achieved also in cypress.config.js.
npx cypress run --reporter junit --reporter-options "mochaFile=cypress/results/results-[hash].xml" --browser "/opt/nodes/node-chrome-142/browser/Chrome.app/Contents/MacOS/Google Chrome"


# Using webkit as the browser name for experimental WebKit support (Safari).
# Requires playwright-webkit package to be installed.
#npm install --save-dev playwright-webkit
#npx cypress run --reporter junit --reporter-options "mochaFile=cypress/results/results-[hash].xml" --browser webkit

# Prepare TEST-all.xml needed by BitBar
npm install junit-report-merger --save-dev
./node_modules/.bin/jrm ./TEST-all.xml "cypress/results/*.xml"
