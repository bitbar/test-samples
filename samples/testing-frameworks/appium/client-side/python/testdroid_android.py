#
# For help on setting up your machine and configuring this TestScript go to
# http://docs.bitbar.com/testing/appium/
#

import os
import unittest
import subprocess
from time import sleep
from appium import webdriver
from device_finder import DeviceFinder
from selenium.common.exceptions import WebDriverException
from testdroid_utils import TDUtils


class TestdroidAndroid(unittest.TestCase):
    def setUp(self):

        #
        # IMPORTANT: Set the following parameters.
        # You can set the parameters outside the script with environment
        # variables.
        # If env var is not set the string after or is used.
        #
        testdroid_url = os.environ.get('TESTDROID_URL') or \
            "https://cloud.bitbar.com"
        appium_url = os.environ.get('TESTDROID_APPIUM_URL') or \
            'https://appium.bitbar.com/wd/hub'
        testdroid_apiKey = os.environ.get('TESTDROID_APIKEY') or ""
        testdroid_project_name = os.environ.get('TESTDROID_PROJECT') or \
            "Android sample project"
        testdroid_testrun_name = os.environ.get('TESTDROID_TESTRUN') or \
            "My testrun"
        testdroid_app = os.environ.get('TESTDROID_APP') or ""
        app_package = os.environ.get('TESTDROID_APP_PACKAGE') or \
            'com.bitbar.testdroid'
        app_activity = os.environ.get('TESTDROID_ACTIVITY') or \
            '.BitbarSampleApplicationActivity'
        new_command_timeout = os.environ.get('TESTDROID_CMD_TIMEOUT') or '60'
        testdroid_test_timeout = os.environ.get('TESTDROID_TEST_TIMEOUT') or '600'
        testdroid_find_device = os.environ.get('TESTDROID_FINDDEVICE') or "true"

        self.screenshot_dir = os.environ.get('TESTDROID_SCREENSHOTS') or \
            os.getcwd() + "/screenshots"
        self.screenshot_count = 1

        # Options to select device
        # 1) Set environment variable TESTDROID_DEVICE
        # 2) Set device name to this python script
        # 3) Do not set #1 and #2 and let DeviceFinder to find free device for
        #    you
        testdroid_device = os.environ.get('TESTDROID_DEVICE') or ""

        deviceFinder = DeviceFinder(url=testdroid_url)
        # Loop will not exit until free device is found
        while testdroid_device == "":
            testdroid_device = deviceFinder.available_android_device()

        if "localhost" in appium_url:
            self.api_level = subprocess.check_output(["adb", "shell", "getprop ro.build.version.sdk"])
        else:
            self.api_level = deviceFinder.device_API_level(testdroid_device)

        self.utils = TDUtils(self.screenshot_dir)
        self.utils.log("Will save screenshots at: " + self.screenshot_dir)

        self.utils.log("Device API level is %s" % self.api_level)
        self.utils.log("Starting Appium test using device '%s'" % testdroid_device)

        desired_capabilities_cloud = {}
        desired_capabilities_cloud['testdroid_apiKey'] = testdroid_apiKey
        desired_capabilities_cloud['testdroid_target'] = 'android'
        desired_capabilities_cloud['automationName'] = 'Appium'
        if self.api_level <= 16:
            desired_capabilities_cloud['testdroid_target'] = 'selendroid'
            desired_capabilities_cloud['automationName'] = 'Selendroid'

        desired_capabilities_cloud['testdroid_project'] = testdroid_project_name
        desired_capabilities_cloud['testdroid_testrun'] = testdroid_testrun_name
        desired_capabilities_cloud['testdroid_device'] = testdroid_device
        desired_capabilities_cloud['testdroid_app'] = testdroid_app
        desired_capabilities_cloud['platformName'] = 'Android'
        desired_capabilities_cloud['deviceName'] = 'Android Phone'
        desired_capabilities_cloud['appPackage'] = app_package
        desired_capabilities_cloud['appActivity'] = app_activity
        desired_capabilities_cloud['newCommandTimeout'] = new_command_timeout
        desired_capabilities_cloud['testdroid_testTimeout'] = testdroid_test_timeout
        desired_capabilities_cloud['testdroid_findDevice'] = testdroid_find_device

        # set up webdriver
        self.utils.log("WebDriver request initiated. Waiting for response, this typically takes 2-3 mins")
        self.driver = webdriver.Remote(appium_url, desired_capabilities_cloud)
        self.utils.log("WebDriver response received")
        self.utils.update_driver(self.driver)
        self.utils.log("Driver session id: " + self.driver.session_id)

    def tearDown(self):
        self.utils.log("Quitting")
        self.driver.quit()

    def testSample(self):
        self.utils.log("  Getting device screen size")
        self.utils.log("  " + str(self.driver.get_window_size()))

        isSelendroid = None
        if self.api_level < 17:
            isSelendroid = True

        self.utils.screenshot("app_launch")

        self.utils.log("  Typing in name")
        elems = self.driver.find_elements_by_class_name('android.widget.EditText')
        self.utils.log("  info: EditText:" + str(len(elems)))
        self.utils.log("  Filling in name")
        elems[0].send_keys("Testdroid User")
        sleep(2)
        self.utils.screenshot("name_typed")

        self.driver.orientation = "LANDSCAPE"
        self.utils.screenshot("landscape")
        self.driver.orientation = "PORTRAIT"
        self.utils.screenshot("portrait")

        try:
            self.utils.log("  Hiding keyboard")
            self.driver.hide_keyboard()
        except WebDriverException:
            pass  # pass exception, if keyboard isn't visible already
        self.utils.screenshot("name_typed_keyboard_hidden")

        self.utils.log("  Clicking element 'Buy 101 devices'")
        if isSelendroid:
            elem = self.driver.find_element_by_link_text('Buy 101 devices')
        else:
            elem = self.driver.find_element_by_android_uiautomator('new UiSelector().text("Buy 101 devices")')
        elem.click()

        self.utils.screenshot("clicked_button1")

        self.utils.log("  Clicking Answer")
        if isSelendroid:
            elem = self.driver.find_element_by_link_text('Answer')
        else:
            elem = self.driver.find_element_by_android_uiautomator('new UiSelector().text("Answer")')
        elem.click()

        self.utils.screenshot("answer")

        self.utils.log("  Navigating back to Activity-1")
        self.driver.back()
        self.utils.screenshot("main_activity")

        self.utils.log("  Clicking element 'Use Testdroid Cloud'")
        if isSelendroid:
            elem = self.driver.find_element_by_link_text('Use Testdroid Cloud')
        else:
            elem = self.driver.find_element_by_android_uiautomator('new UiSelector().text("Use Testdroid Cloud")')
        elem.click()

        self.utils.screenshot("clicked_button2")

        self.utils.log("  Clicking Answer")
        if isSelendroid:
            elem = self.driver.find_element_by_link_text('Answer')
        else:
            elem = self.driver.find_element_by_android_uiautomator('new UiSelector().text("Answer")')
        elem.click()

        self.utils.screenshot("answer")


def initialize():
    return TestdroidAndroid


if __name__ == "__main__":
    suite = unittest.TestLoader().loadTestsFromTestCase(TestdroidAndroid)
    unittest.TextTestRunner(verbosity=2).run(suite)
