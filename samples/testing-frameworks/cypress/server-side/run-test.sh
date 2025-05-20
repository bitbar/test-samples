#!/bin/bash

# Enable trace for eaier debuging of the script
set -x

# Install dependencies
npm install

# Run the app
npm start &
# Wait for the app to launch TODO: don't use sleep for it.
sleep 30

# Run tests.
# It is crucial to set junit as a reporter. In user's project it can be achieved also in cypress.config.js.
npm run cy:run -- --reporter junit --reporter-options "mochaFile=cypress/results/results-[hash].xml" --browser "/opt/nodes/node-chrome-134/browser/Chrome.app/Contents/MacOS/Google Chrome"

# Prepare TEST-all.xml needed by BitBar
npm install junit-report-merger --save-dev
./node_modules/.bin/jrm ./TEST-all.xml "cypress/results/*.xml"
