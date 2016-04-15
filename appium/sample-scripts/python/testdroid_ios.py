##
## For help on setting up your machine and configuring this TestScript go to
## http://docs.testdroid.com/appium/
##

import os
import sys
import time
import unittest
from time import sleep
from selenium import webdriver
from device_finder import DeviceFinder

def log(msg):
    print (time.strftime("%H:%M:%S") + ": " + msg)

class TestdroidIOS(unittest.TestCase):
    def setUp(self):

        ##
        ## IMPORTANT: Set the following parameters.
        ##
        testdroid_url = os.environ.get('TESTDROID_URL') or "https://cloud.testdroid.com"
        appium_url = os.environ.get('TESTDROID_APPIUM_URL') or 'http://appium.testdroid.com/wd/hub'
        testdroid_apiKey = os.environ.get('TESTDROID_APIKEY') or ""
        testdroid_app = os.environ.get('TESTDROID_APP') or ""
        self.screenshot_dir = os.environ.get('TESTDROID_SCREENSHOTS') or "/absolute/path/to/desired/directory"
        testdroid_project_name = os.environ.get('TESTDROID_PROJECT') or "Appium iOS demo"
        testdroid_testrun_name = os.environ.get('TESTDROID_TESTRUN') or "My testrun"
        testdroid_bundle_id = os.environ.get('TESTDROID_BUNDLE_ID') or "com.bitbar.testdroid.BitbarIOSSample"
        new_command_timeout = os.environ.get('TESTDROID_CMD_TIMEOUT') or '60'
        testdroid_test_timeout = os.environ.get('TESTDROID_TEST_TIMEOUT') or '600'

        # Options to select device
        # 1) Set environment variable TESTDROID_DEVICE
        # 2) Set device name to this python script
        # 3) Do not set #1 and #2 and let DeviceFinder to find free device for you

        deviceFinder = None
        testdroid_device = os.environ.get('TESTDROID_DEVICE') or ""

        if testdroid_device == "":
            deviceFinder = DeviceFinder(url=testdroid_url)
            # Loop will not exit until free device is found
            while testdroid_device == "":
                testdroid_device = deviceFinder.available_free_ios_device()

        print "Starting Appium test using device '%s'" % testdroid_device

        desired_capabilities_cloud = {}
        desired_capabilities_cloud['testdroid_apiKey'] = testdroid_apiKey
        desired_capabilities_cloud['testdroid_target'] = 'ios'
        desired_capabilities_cloud['testdroid_project'] = testdroid_project_name
        desired_capabilities_cloud['testdroid_testrun'] = testdroid_testrun_name
        desired_capabilities_cloud['testdroid_device'] = testdroid_device
        desired_capabilities_cloud['testdroid_app'] = testdroid_app
        desired_capabilities_cloud['platformName'] = 'iOS'
        desired_capabilities_cloud['deviceName'] = 'iPhone device'
        desired_capabilities_cloud['bundleId'] = testdroid_bundle_id
        desired_capabilities_cloud['newCommandTimeout'] = new_command_timeout
        desired_capabilities_cloud['testdroid_testTimeout'] = testdroid_test_timeout

        # set up webdriver
        log ("WebDriver request initiated. Waiting for response, this typically takes 2-3 mins")

        self.driver = webdriver.Remote(command_executor=appium_url, desired_capabilities=desired_capabilities_cloud)
        log ("WebDriver response received")


    def tearDown(self):
        log ("Quitting")
        self.driver.quit()

    def testSample(self):
        # view1
        log ("view1: Finding buttons")
        buttons = self.driver.find_elements_by_class_name('UIAButton')
        log ("view1: Clicking button [0] - RadioButton 1")
        buttons[0].click()

        log ("view1: Typing in textfield[0]: Testdroid user")
        elem = self.driver.find_element_by_class_name('UIATextField')
        elem.clear()
        elem.send_keys('Testdroid user')

        y = 0.95
        log ("view1: Tapping at position (384, 0.95) - Estimated position of SpaceBar")
        self.driver.execute_script("mobile: tap",{"touchCount":"1","x":"0.5","y":y})

        log ("view1: Taking screenshot screenshot1.png")
        self.driver.save_screenshot(self.screenshot_dir + "/screenshot1.png")

        log ("view1: Hiding Keyboard")
        self.driver.find_element_by_name("Return").click()

        log ("view1: Taking screenshot screenshot2.png")
        self.driver.save_screenshot(self.screenshot_dir + "/screenshot2.png")

        log ("view1: Clicking button[6] - OK  Button")
        buttons[6].click()

        log ("view2: Taking screenshot screenshot3.png")
        self.driver.save_screenshot(self.screenshot_dir + "/screenshot3.png")

        # view2
        log ("view2: Finding buttons")
        buttons = self.driver.find_elements_by_class_name('UIAButton')
        log ("view2: Clicking button[0] - Back/OK button")
        buttons[0].click()

        # view 1
        log ("view1: Finding buttons")
        buttons = self.driver.find_elements_by_class_name('UIAButton')
        log ("view1: Clicking button[2] - RadioButton 2")
        buttons[2].click()

        log ("view1: Clicking button[6] - OK Button")
        buttons[6].click()

        log ("view1: Taking screenshot screenshot4.png")
        self.driver.save_screenshot(self.screenshot_dir + "/screenshot4.png")

        log ("view1: Sleeping 3 before quitting webdriver.")
        sleep(3)


def initialize():
    return TestdroidIOS


if __name__ == "__main__":
    suite = unittest.TestLoader().loadTestsFromTestCase(TestdroidIOS)
    unittest.TextTestRunner(verbosity=2).run(suite)

