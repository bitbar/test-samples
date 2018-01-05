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


def log(msg):
    print (time.strftime("%H:%M:%S") + ": " + msg)


class TestdroidIOS(unittest.TestCase):
    """
    Take screenshot and store files to defined location, with numbering prefix

    :Args:
    - name - files are stored as #_name
    """

    def screenshot(self, name):
        screenshot_name = str(self.screenshot_count) + "_" + name + ".png"
        log("Taking screenshot: " + screenshot_name)
        self.driver.save_screenshot(self.screenshot_dir + "/" + screenshot_name)
        self.screenshot_count += 1

    def setUp(self):

        ##
        ## IMPORTANT: Set the following parameters.
        ##
        testdroid_url = os.environ.get('TESTDROID_URL') or "https://cloud.bitbar.com"
        appium_url = os.environ.get('TESTDROID_APPIUM_URL') or 'https://appium.bitbar.com/wd/hub'
        testdroid_apiKey = os.environ.get('TESTDROID_APIKEY') or ""
        testdroid_project_name = os.environ.get('TESTDROID_PROJECT') or "iOS sample project"
        testdroid_testrun_name = os.environ.get('TESTDROID_TESTRUN') or "My testrun"
        testdroid_app = os.environ.get('TESTDROID_APP') or ""
        testdroid_bundle_id = os.environ.get('TESTDROID_BUNDLE_ID') or "com.bitbar.testdroid.BitbarIOSSample"
        new_command_timeout = os.environ.get('TESTDROID_CMD_TIMEOUT') or '60'
        testdroid_test_timeout = os.environ.get('TESTDROID_TEST_TIMEOUT') or '600'
        testdroid_find_device = os.environ.get('TESTDROID_FINDDEVICE') or "true"
        automation_name = os.environ.get('TESTDROID_AUTOMATION_NAME') or "XCUITest"

        self.screenshot_dir = os.environ.get('TESTDROID_SCREENSHOTS') or os.getcwd() + "/screenshots"
        log("Will save screenshots at: " + self.screenshot_dir)
        self.screenshot_count = 1

        # Options to select device
        # 1) Set environment variable TESTDROID_DEVICE
        # 2) Set device name to this python script
        # 3) Do not set #1 and #2 and let DeviceFinder to find free device for you
        testdroid_device = os.environ.get('TESTDROID_DEVICE') or ""

        deviceFinder = DeviceFinder(url=testdroid_url)
        if testdroid_device == "":
            # Loop will not exit until free device is found
            while testdroid_device == "":
                testdroid_device = deviceFinder.available_ios_device()

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
        desired_capabilities_cloud['newCommandTimeout'] = new_command_timeout
        desired_capabilities_cloud['testdroid_testTimeout'] = testdroid_test_timeout
        desired_capabilities_cloud['testdroid_findDevice'] = testdroid_find_device
        desired_capabilities_cloud['automationName'] = automation_name
        desired_capabilities_cloud['app'] = testdroid_bundle_id

        # set up webdriver
        log("WebDriver request initiated. Waiting for response, this typically takes 2-3 mins")

        self.driver = webdriver.Remote(command_executor=appium_url, desired_capabilities=desired_capabilities_cloud)
        log("WebDriver response received")

    def tearDown(self):
        log("Quitting")
        self.driver.quit()

    def testSample(self):
        # view1
        log("view1: Finding buttons")
        buttons = self.driver.find_elements_by_class_name('UIAButton')
        log("view1: Clicking button [0] - RadioButton 1")
        buttons[0].click()

        log("view1: Typing in textfield[0]: Testdroid user")
        elem = self.driver.find_element_by_class_name('UIATextField')
        elem.clear()
        elem.send_keys('Testdroid user')

        log("view1: Taking screenshot screenshot1.png")
        self.screenshot("screenshot1")

        log("view1: Hiding Keyboard")
        self.driver.find_element_by_xpath("//*[contains(@name, 'Return')]").click()

        log("view1: Taking screenshot screenshot2.png")
        self.screenshot("screenshot2")

        log("view1: Clicking button[6] - OK  Button")
        buttons[6].click()

        log("view2: Taking screenshot screenshot3.png")
        self.screenshot("screenshot3")

        # view2
        log("view2: Finding buttons")
        buttons = self.driver.find_elements_by_class_name('UIAButton')
        log("view2: Clicking button[0] - Back/OK button")
        buttons[0].click()

        # view1
        log("view1: Finding buttons")
        buttons = self.driver.find_elements_by_class_name('UIAButton')
        log("view1: Clicking button[2] - RadioButton 2")
        buttons[2].click()

        log("view1: Clicking button[6] - OK Button")
        buttons[6].click()

        log("view1: Taking screenshot screenshot4.png")
        self.screenshot("screenshot4")

        log("view1: Sleeping 3 before quitting webdriver.")
        sleep(3)


def initialize():
    return TestdroidIOS


if __name__ == "__main__":
    suite = unittest.TestLoader().loadTestsFromTestCase(TestdroidIOS)
    unittest.TextTestRunner(verbosity=2).run(suite)
