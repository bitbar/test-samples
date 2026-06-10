import os
import unittest

from selenium import webdriver
from selenium.webdriver.common.by import By


class BitbarSeleniumSample(unittest.TestCase):

    def setUp(self):
        #
        # IMPORTANT: Set the following parameters according to your needs.
        # You can use Capabilities creator:
        # https://cloud.bitbar.com/#public/capabilities-creator
        # Please mind apiKey is required and can be found at
        # https://cloud.bitbar.com/#user/my-account (My Integrations > API Access)
        #

        # user-customizable parameters start here
        capabilities = {
            'platformName': 'Windows',
            'browserName': 'chrome',
            'browserVersion': 'latest',
            'bitbar:options': {
                'project': 'Selenium sample project',
                'testrun': 'Python sample test',
                'apiKey': '<insert your BitBar API key here>',
                'osVersion': '11',
                'resolution': '1920x1080'
            }
        }
        # user-customizable parameters end here

        self.screenshot_dir = os.getcwd() + '/screenshots'

        self.driver = webdriver.Remote(command_executor='https://us-west-desktop-hub.bitbar.com/wd/hub',
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
        button = self.driver.find_element(By.XPATH, '//button[contains(., "Click for answer")]')
        button.click()

        # check answer text
        self.driver.find_element(By.XPATH, '//p[@id="result_element" and contains(., "Bitbar")]')
        print(self.driver.find_element(By.ID, 'result_element').text)

        # verify button changed color
        style = str(button.get_attribute('style'))
        expected_style = 'background-color: rgb(127, 255, 0);'
        assert expected_style == style, 'Wrong button styling'
        self.driver.get_screenshot_as_file(self.screenshot_dir + '/' + '2_button_clicked.png')


if __name__ == "__main__":
    unittest.main()
