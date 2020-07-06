##
## For help on setting up your machine and configuring this TestScript go to
## http://docs.bitbar.com/testing/appium/
##

import os
import unittest
from appium import webdriver
from device_finder import DeviceFinder
from bitbar_utils import BitbarUtils


class BitbarAndroid(unittest.TestCase):
    def setUp(self):
        ##
        ## IMPORTANT: Set the following parameters.
        ## You can set the parameters outside the script with environment variables.
        ## If env var is not set the string after 'or' is used.
        ##
        bitbar_url = os.environ.get('BITBAR_URL') or \
            "https://cloud.bitbar.com"
        appium_url = os.environ.get('BITBAR_APPIUM_URL') or \
            'https://appium.bitbar.com/wd/hub'
        bitbar_apiKey = os.environ.get('BITBAR_APIKEY') or ""
        bitbar_project_name = os.environ.get('BITBAR_PROJECT') or \
            'Appium Chrome Demo'
        bitbar_testrun_name = os.environ.get('BITBAR_TESTRUN') or \
            "My testrun"
        new_command_timeout = os.environ.get('BITBAR_CMD_TIMEOUT') or '60'
        bitbar_test_timeout = os.environ.get('BITBAR_TEST_TIMEOUT') or '600'
        bitbar_find_device = os.environ.get('BITBAR_FINDDEVICE') or True

        self.screenshot_dir = os.environ.get('BITBAR_SCREENSHOTS') or \
            os.getcwd() + "/screenshots"
        self.screenshot_count = 1

        # Options to select device
        # 1) Set environment variable BITBAR_DEVICE
        # 2) Set device name to this python script
        # 3) Do not set #1 and #2 and let DeviceFinder to find free device for you

        bitbar_device = os.environ.get('BITBAR_DEVICE') or ""

        deviceFinder = DeviceFinder(url=bitbar_url)
        if bitbar_device == "":
            # Loop will not exit until free device is found
            while bitbar_device == "":
                bitbar_device = deviceFinder.available_android_device()

        print("Starting Appium test using device '%s'" % bitbar_device)

        self.utils = BitbarUtils(self.screenshot_dir)
        self.utils.log("Will save screenshots at: " + self.screenshot_dir)

        desired_capabilities_cloud = {}
        desired_capabilities_cloud['bitbar_apiKey'] = bitbar_apiKey
        desired_capabilities_cloud['bitbar_target'] = 'chrome'
        desired_capabilities_cloud['bitbar_project'] = bitbar_project_name
        desired_capabilities_cloud['bitbar_testrun'] = bitbar_testrun_name
        desired_capabilities_cloud['bitbar_device'] = bitbar_device
        desired_capabilities_cloud['platformName'] = 'Android'
        desired_capabilities_cloud['deviceName'] = 'AndroidDevice'
        desired_capabilities_cloud['browserName'] = 'chrome'
        desired_capabilities_cloud['newCommandTimeout'] = new_command_timeout
        desired_capabilities_cloud['bitbar_testTimeout'] = bitbar_test_timeout
        desired_capabilities_cloud['bitbar_findDevice'] = bitbar_find_device

        # set up webdriver
        self.utils.log("WebDriver request initiated. Waiting for response, this typically takes 2-3 mins")
        self.driver = webdriver.Remote(appium_url, desired_capabilities_cloud)

        self.utils.update_driver(self.driver)
        test_url = "https://bitbar.github.io/web-testing-target/"
        self.utils.log("Loading page " + test_url)
        self.driver.get(test_url)

    def tearDown(self):
        self.utils.log("Quitting, closing connection")
        self.driver.quit()

    def testSample(self):
        self.utils.screenshot("home_screen")

        self.utils.log("  Switching to landscape")
        self.driver.orientation = "LANDSCAPE"
        self.utils.screenshot("home_landscape")
        self.utils.log("  Switching to portrait")
        self.driver.orientation = "PORTRAIT"
        self.utils.screenshot("home_portrait")

        self.utils.log("Finding button with text 'Click for answer'")
        button = self.utils.wait_until_xpath_matches('//button[contains(., "Click for answer")]')

        self.utils.log("Clicking on button")
        button.click()
        self.utils.screenshot("answer")

        self.utils.log("Check answer text")
        self.driver.find_element_by_xpath('//p[@id="result_element" and contains(., "Bitbar")]')

        self.utils.log("Verify button changed color")
        style = str(button.get_attribute('style'))
        expected_style = "rgb(127, 255, 0"
        self.assertTrue(expected_style in style)


def initialize():
    return BitbarAndroid


if __name__ == "__main__":
    suite = unittest.TestLoader().loadTestsFromTestCase(BitbarAndroid)
    unittest.TextTestRunner(verbosity=2).run(suite)
