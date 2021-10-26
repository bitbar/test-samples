var assert = require('assert')
var webdriver = require('selenium-webdriver');

//
// IMPORTANT: Set the following parameters according to your needs.
// You can use Capabilities creator:
// https://cloud.bitbar.com/#public/capabilities-creator
// Please mind bitbar_apiKey is required and can be found at
// https://cloud.bitbar.com/#user/my-account (My Integrations > API Access)
//

// user-customizable parameters start here
var capabilities = {
    'bitbar_apiKey': '<insert your Bitbar API key here>',
    'platform': 'Windows',
    'browserName': 'Chrome',
    'version': '94',
    'resolution': '1920x1080',
    'bitbar_project': 'Selenium sample project',
    'bitbar_testrun': 'NodeJS sample test',
    'bitbar_testTimeout': '600'
}
// user-customizable parameters end here

if (capabilities['platform'].toLowerCase() == 'windows') {
    var hub_url = 'https://westeurope-hub.bitbar.com/wd/hub';
} else if (capabilities['platform'].toLowerCase() == 'linux') {
    var hub_url = 'https://broker-cloud.bitbar.com/wd/hub';
} else {
    throw 'Unsupported platform';
}

async function nodeJsSample() {
    try {
        var driver = new webdriver.Builder()
            .usingServer(hub_url)
            .withCapabilities(capabilities)
            .build();

        // check page title
        test_url = 'https://bitbar.github.io/web-testing-target/';
        await driver.get(test_url);
        expected_title = 'Bitbar - Test Page for Samples'
        assert.equal(await driver.getTitle(), expected_title, 'Wrong page title')
        await driver.getTitle().then(function (title) {
            console.log(title)
        });

        // click "Click for answer" button
        var button = await driver.findElement(webdriver.By.xpath('//button[contains(., "Click for answer")]'));
        await button.click();

        // check answer text
        await driver.findElement(webdriver.By.xpath('//p[@id="result_element" and contains(., "Bitbar")]'));
        await driver.findElement(webdriver.By.id('result_element')).getText().then(function (result) {
            console.log(result)
        });

        // verify button changed color
        style = await button.getAttribute('style');
        expected_style = 'background-color: rgb(127, 255, 0);';
        assert.equal(style, expected_style, 'Wrong button styling');
    }

    catch (err) {
        console.error('An error occured:\n', err.stack, '\n');
    } finally {
        driver.quit();
    }

};

nodeJsSample();
