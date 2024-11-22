# Install dependencies
npm install

# Run the app
Start-Process -FilePath "pwsh" -ArgumentList "-Command", "npm", "start" -NoNewWindow
# Wait for the app to launch
Start-Sleep -Seconds 30

# Run tests.
# It is crucial to set junit as a reporter. In user's project ect it can be achieved also in cypress.config.js.
npm run cy:run --- --reporter junit --reporter-options "mochaFile=cypress/results/results-[hash].xml" --browser "C:\nodes\node-chrome-129\browser\chrome.exe"

# Prepare TEST-all.xml needed by BitBar
npm install junit-report-merger --save-dev
./node_modules/.bin/jrm ./TEST-all.xml "cypress/results/*.xml"
