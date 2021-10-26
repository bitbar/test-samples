import os
import unittest

from selenium import webdriver


class BitbarSeleniumSample(unittest.TestCase):

    def setUp(self):
        #
        # IMPORTANT: Set the following parameters according to your needs.
        # You can use Capabilities creator:
        # https://cloud.bitbar.com/#public/capabilities-creator
        # Please mind bitbar_apiKey is required and can be found at
        # https://cloud.bitbar.com/#user/my-account (My Integrations > API Access)
        #

        # user-customizable parameters start here
        capabilities = {
            'bitbar_apiKey': '<insert your Bitbar API key here>',
            'platform': 'Windows',
            'browserName': 'Chrome',
            'version': '94',
            'resolution': '1920x1080',
            'bitbar_project': 'Selenium sample project',
            'bitbar_testrun': 'Python sample test',
            'bitbar_testTimeout': '600'
        }
        # user-customizable parameters end here

        self.screenshot_dir = os.getcwd() + '/screenshots'

        if capabilities['platform'].lower() == 'windows':
            hub_url = 'https://westeurope-hub.bitbar.com/wd/hub'
        elif capabilities['platform'].lower() == 'linux':
            hub_url = 'https://broker-cloud.bitbar.com/wd/hub'
        else:
            raise Exception("Unsupported platform")

        self.driver = webdriver.Remote(command_executor=hub_url,
                                       desired_capabilities=capabilities)

    def tearDown(self):
        self.driver.quit()

    def test_sample(self):
        # check page title
        test_url = 'https://bitbar.github.io/web-testing-target/'
        self.driver.get(test_url)
        expected_title = 'Bitbar - Test Page for Samples'
        assert self.driver.title == expected_title, 'Wrong page title'
        print(self.driver.title)
        self.driver.get_screenshot_as_file(self.screenshot_dir + '/' + '1_home_page.png')

        # click "Click for answer" button
        button = self.driver.find_element_by_xpath('//button[contains(., "Click for answer")]')
        button.click()

        # check answer text
        self.driver.find_element_by_xpath('//p[@id="result_element" and contains(., "Bitbar")]')
        print(self.driver.find_element_by_id('result_element').text)

        # verify button changed color
        style = str(button.get_attribute('style'))
        expected_style = 'background-color: rgb(127, 255, 0);'
        assert expected_style == style, 'Wrong button styling'
        self.driver.get_screenshot_as_file(self.screenshot_dir + '/' + '2_button_clicked.png')


if __name__ == "__main__":
    unittest.main()
