#
# For help on setting up your machine and configuring this TestScript go to
# http://docs.bitbar.com/testing/appium/
#

import os
import unittest
from appium import webdriver
from device_finder import DeviceFinder
from testdroid_utils import TDUtils


class TestdroidSafari(unittest.TestCase):
    def setUp(self):
        #
        # IMPORTANT: Set the following parameters.
        #
        testdroid_url = os.environ.get('TESTDROID_URL') or "https://cloud.bitbar.com"
        appium_url = os.environ.get('TESTDROID_APPIUM_URL') or 'https://appium.bitbar.com/wd/hub'
        testdroid_apiKey = os.environ.get('TESTDROID_APIKEY') or ""
        testdroid_project_name = os.environ.get('TESTDROID_PROJECT') or "Safari sample project"
        testdroid_testrun_name = os.environ.get('TESTDROID_TESTRUN') or "My testrun"
        new_command_timeout = os.environ.get('TESTDROID_CMD_TIMEOUT') or '60'
        testdroid_test_timeout = os.environ.get('TESTDROID_TEST_TIMEOUT') or '600'
        testdroid_find_device = os.environ.get('TESTDROID_FINDDEVICE') or "true"
        automation_name = os.environ.get('TESTDROID_AUTOMATION_NAME') or "XCUITest"

        self.screenshot_dir = os.environ.get('TESTDROID_SCREENSHOTS') or os.getcwd() + "/screenshots"
        self.screenshot_count = 1

        # Options to select device
        # 1) Set environment variable TESTDROID_DEVICE
        # 2) Set device name to this python script
        # 3) Do not set #1 and #2 and let DeviceFinder find a free device
        testdroid_device = os.environ.get('TESTDROID_DEVICE') or ""

        deviceFinder = DeviceFinder(url=testdroid_url)
        while testdroid_device == "":
            testdroid_device = deviceFinder.available_ios_device()

        print "Starting Appium test using device '%s'" % testdroid_device

        self.utils = TDUtils(self.screenshot_dir)
        self.utils.log("Will save screenshots at: " + self.screenshot_dir)

        desired_capabilities_cloud = {}
        desired_capabilities_cloud['testdroid_apiKey'] = testdroid_apiKey
        desired_capabilities_cloud['testdroid_target'] = 'safari'
        desired_capabilities_cloud['testdroid_project'] = testdroid_project_name
        desired_capabilities_cloud['testdroid_testrun'] = testdroid_testrun_name
        desired_capabilities_cloud['testdroid_device'] = testdroid_device
        desired_capabilities_cloud['platformName'] = 'iOS'
        desired_capabilities_cloud['deviceName'] = 'iOS Device'
        desired_capabilities_cloud['browserName'] = 'Safari'
        desired_capabilities_cloud['newCommandTimeout'] = new_command_timeout
        desired_capabilities_cloud['testdroid_testTimeout'] = testdroid_test_timeout
        desired_capabilities_cloud['testdroid_findDevice'] = testdroid_find_device
        desired_capabilities_cloud['automationName'] = automation_name

        # Set up webdriver
        self.utils.log("WebDriver request initiated. Waiting for response, this typically takes 2-3 mins")
        self.driver = webdriver.Remote(appium_url, desired_capabilities_cloud)
        self.utils.update_driver(self.driver)
        test_url = "http://bitbar.github.io/testdroid-samples/"
        self.utils.log("Loading page " + test_url)
        self.driver.get(test_url)

    def tearDown(self):
        self.utils.log("Quitting")
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
    return TestdroidSafari


if __name__ == "__main__":
    suite = unittest.TestLoader().loadTestsFromTestCase(TestdroidSafari)
    unittest.TextTestRunner(verbosity=2).run(suite)
