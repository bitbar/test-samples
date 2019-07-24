##
## For help on setting up your machine and configuring this TestScript go to
## http://docs.bitbar.com/testing/appium/
##

import os
import time
import unittest
from time import sleep
from appium import webdriver
from device_finder import DeviceFinder
from bitbar_utils import BitbarUtils

class BitbarIOS(unittest.TestCase):
    """
    Take screenshot and store files to defined location, with numbering prefix

    :Args:
    - name - files are stored as #_name
    """

    def setUp(self):

        ##
        ## IMPORTANT: Set the following parameters.
        ##
        bitbar_url = os.environ.get('BITBAR_URL') or "https://cloud.bitbar.com"
        appium_url = os.environ.get('BITBAR_APPIUM_URL') or 'https://appium.bitbar.com/wd/hub'
        bitbar_apiKey = os.environ.get('BITBAR_APIKEY') or ""
        bitbar_project_name = os.environ.get('BITBAR_PROJECT') or "iOS sample project"
        bitbar_testrun_name = os.environ.get('BITBAR_TESTRUN') or "My testrun"
        bitbar_app = os.environ.get('BITBAR_APP') or ""
        bitbar_bundle_id = os.environ.get('BITBAR_BUNDLE_ID') or "com.bitbar.testdroid.BitbarIOSSample"
        new_command_timeout = os.environ.get('BITBAR_CMD_TIMEOUT') or '60'
        bitbar_test_timeout = os.environ.get('BITBAR_TEST_TIMEOUT') or '600'
        bitbar_find_device = os.environ.get('BITBAR_FINDDEVICE') or "true"
        automation_name = os.environ.get('BITBAR_AUTOMATION_NAME') or "XCUITest"

        self.screenshot_dir = os.environ.get('BITBAR_SCREENSHOTS') or os.getcwd() + "/screenshots"

        # Options to select device
        # 1) Set environment variable BITBAR_DEVICE
        # 2) Set device name to this python script
        # 3) Do not set #1 and #2 and let DeviceFinder to find free device for you
        bitbar_device = os.environ.get('BITBAR_DEVICE') or ""

        deviceFinder = DeviceFinder(url=bitbar_url)
        if bitbar_device == "":
            # Loop will not exit until free device is found
            while bitbar_device == "":
                bitbar_device = deviceFinder.available_ios_device()

        print("Starting Appium test using device '%s'" % bitbar_device)

        self.utils = BitbarUtils(self.screenshot_dir)
        self.utils.log("Will save screenshots at: " + self.screenshot_dir)
        
        desired_capabilities_cloud = {}
        desired_capabilities_cloud['bitbar_apiKey'] = bitbar_apiKey
        desired_capabilities_cloud['bitbar_target'] = 'ios'
        desired_capabilities_cloud['bitbar_project'] = bitbar_project_name
        desired_capabilities_cloud['bitbar_testrun'] = bitbar_testrun_name
        desired_capabilities_cloud['bitbar_device'] = bitbar_device
        desired_capabilities_cloud['bitbar_app'] = bitbar_app
        desired_capabilities_cloud['platformName'] = 'iOS'
        desired_capabilities_cloud['deviceName'] = 'iPhone device'
        desired_capabilities_cloud['newCommandTimeout'] = new_command_timeout
        desired_capabilities_cloud['bitbar_testTimeout'] = bitbar_test_timeout
        desired_capabilities_cloud['bitbar_findDevice'] = bitbar_find_device
        desired_capabilities_cloud['automationName'] = automation_name
        desired_capabilities_cloud['app'] = bitbar_bundle_id

        # set up webdriver
        self.utils.log("WebDriver request initiated. Waiting for response, this typically takes 2-3 mins")

        self.driver = webdriver.Remote(command_executor=appium_url, desired_capabilities=desired_capabilities_cloud)
        self.utils.update_driver(self.driver)
        self.utils.log("WebDriver response received")

    def tearDown(self):
        self.utils.log("Quitting")
        self.driver.quit()

    def testSample(self):
        # view1
        self.utils.log("view1: Finding buttons")
        buttons = self.driver.find_elements_by_class_name('UIAButton')
        self.utils.log("view1: Clicking button [0] - RadioButton 1")
        buttons[0].click()

        self.utils.log("view1: Typing in textfield[0]: Bitbar user")
        elem = self.driver.find_element_by_class_name('UIATextField')
        elem.clear()
        elem.send_keys('Bitbar user')

        self.utils.log("view1: Typing into text field")
        self.utils.screenshot("text-in-field")

        self.utils.log("view1: Hiding Keyboard")
        self.driver.find_element_by_xpath("//*[contains(@name, 'Return')]").click()
        
        self.utils.log("view1: Hiding keyboard")
        self.utils.screenshot("keyboard-hidden")

        self.utils.log("view1: Clicking button[6] - OK  Button")
        buttons[6].click()

        self.utils.log("view2: Ok button clicked")
        self.utils.screenshot("new-view")

        # view2
        self.utils.log("view2: Finding buttons")
        buttons = self.driver.find_elements_by_class_name('UIAButton')
        self.utils.log("view2: Clicking button[0] - Back/OK button")
        buttons[0].click()
        self.utils.screenshot("back-to-view1")
        
        # view1
        self.utils.log("view1: Finding buttons")
        buttons = self.driver.find_elements_by_class_name('UIAButton')
        self.utils.log("view1: Clicking button[2] - RadioButton 2")
        buttons[2].click()
        self.utils.screenshot("radio-button-clicked")
        self.utils.log("view1: Clicking button[6] - OK Button")
        buttons[6].click()

        self.utils.log("view1: OK button clicked again")
        self.utils.screenshot("last-screenshot")

        self.utils.log("view1: Sleeping 3 before quitting webdriver.")
        sleep(3)


def initialize():
    return BitbarIOS


if __name__ == "__main__":
    suite = unittest.TestLoader().loadTestsFromTestCase(BitbarIOS)
    unittest.TextTestRunner(verbosity=2).run(suite)
