const webdriver = require('selenium-webdriver');

//
// IMPORTANT: Set the following parameters according to your needs.
// You can use Capabilities creator:
// https://cloud.bitbar.com/#public/capabilities-creator
// Please mind apiKey is required and can be found at
// https://cloud.bitbar.com/#user/profile (My Integrations > API Access)
//

// user-customizable parameters start here
const hubUrl = 'https://us-west-desktop-hub.bitbar.com/wd/hub';
const capabilities = {
  'platformName': 'Windows',
  'browserName': 'chrome',
  'browserVersion': 'latest',
  'bitbar:options': {
    'project': 'Selenium sample project',
    'testrun': 'NodeJS sample test',
    'apiKey': '<insert your BitBar API key here>',
    'osVersion': '11',
    'resolution': '1920x1080',
  }
};
// user-customizable parameters end here

async function nodeJsSample() {
  const chai = await import('chai');
  const expect = chai.expect;

  let driver = new webdriver.Builder()
    .usingServer(hubUrl)
    .withCapabilities(capabilities)
    .build();

  try {
    // check page title
    await driver.get('https://bitbar.github.io/web-testing-target/');
    const expected_title = 'Bitbar - Test Page for Samples'
    expect(await driver.getTitle()).to.equal(expected_title, 'Wrong page title');
    await driver.getTitle().then(function (title) {
      console.log(title)
    });

    // click "Click for answer" button
    const button = await driver.findElement(webdriver.By.xpath('//button[contains(., "Click for answer")]'));
    await button.click();

    // check answer text
    await driver.findElement(webdriver.By.xpath('//p[@id="result_element" and contains(., "Bitbar")]'));
    await driver.findElement(webdriver.By.id('result_element')).getText().then(function (result) {
      console.log(result)
    });

    // verify button changed color
    const style = await button.getAttribute('style');
    const expected_style = 'background-color: rgb(127, 255, 0);';
    expect(style).to.equal(expected_style, 'Wrong button styling');
  } catch (err) {
    console.error('An error occured:\n', err.stack, '\n');
  } finally {
    driver.quit();
  }
}

nodeJsSample();
