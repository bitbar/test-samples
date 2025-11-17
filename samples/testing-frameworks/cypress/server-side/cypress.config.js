const { defineConfig } = require('cypress')
module.exports = defineConfig({
  e2e: {
    specPattern: 'cypress/e2e/**/*.cy.{js,jsx,ts,tsx}',
    supportFile: false
  },
  headless: true,
  reporter: 'junit',
  reporterOptions: {
    mochaFile: 'cypress/results/results-[hash].xml',
    toConsole: true
  },
  trashAssetsBeforeRuns: false,
  videoCompression: 32,
  viewportHeight: 720,
  viewportWidth: 1280,
  experimentalWebKitSupport: true
})
