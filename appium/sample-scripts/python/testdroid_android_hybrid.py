##
## For help on setting up your machine and configuring this TestScript go to
## http://docs.testdroid.com/appium/
##

import os
import time
import unittest
import subprocess
from time import sleep
from appium import webdriver
from device_finder import DeviceFinder
from selenium.common.exceptions import WebDriverException
from selenium.common.exceptions import NoSuchElementException
from appium.common.exceptions import NoSuchContextException
from testdroid_utils import TDUtils


class TestdroidAndroid(unittest.TestCase):

    def setUp(self):
        ##
        ## IMPORTANT: Set the following parameters.
        ## You can set the parameters outside the script with environment variables.
        ## If env var is not set the string after or is used.
        ##

        testdroid_url = os.environ.get('TESTDROID_URL') or "https://cloud.testdroid.com"
        appium_url = os.environ.get('TESTDROID_APPIUM_URL') or 'http://appium.testdroid.com/wd/hub'
        testdroid_apiKey = os.environ.get('TESTDROID_APIKEY') or ""
        testdroid_project = os.environ.get('TESTDROID_PROJECT') or 'Android hybrid sample project'
        testdroid_testrun = os.environ.get('TESTDROID_TESTRUN') or 'My testrun'
        testdroid_app = os.environ.get('TESTDROID_APP') or ""
        app_package = os.environ.get('TESTDROID_APP_PACKAGE') or 'com.testdroid.sample.android'
        app_activity = os.environ.get('TESTDROID_ACTIVITY') or '.MM_MainMenu'
        new_command_timeout = os.environ.get('TESTDROID_CMD_TIMEOUT') or '60'
        testdroid_test_timeout = os.environ.get('TESTDROID_TEST_TIMEOUT') or '600'

        self.screenshot_dir = os.environ.get('TESTDROID_SCREENSHOTS') or os.getcwd() + "/screenshots"
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
                testdroid_device = deviceFinder.available_android_device()

        self.utils = TDUtils(self.screenshot_dir)
        self.utils.log("Will save screenshots at: " + self.screenshot_dir)

        if "localhost" in appium_url:
            self.api_level = subprocess.check_output(["adb", "shell", "getprop ro.build.version.sdk"])
        else:
            self.api_level = deviceFinder.device_API_level(testdroid_device)

        self.utils.log("Device API level is %s" % self.api_level)
        self.utils.log("Starting Appium test using device '%s'" % testdroid_device)

        desired_capabilities_cloud = {}
        desired_capabilities_cloud['testdroid_apiKey'] = testdroid_apiKey
        if self.api_level > 16:
            desired_capabilities_cloud['testdroid_target'] = 'android'
            desired_capabilities_cloud['automationName'] = 'android'
        else:
            desired_capabilities_cloud['testdroid_target'] = 'selendroid'
            desired_capabilities_cloud['automationName'] = 'selendroid'

        desired_capabilities_cloud['testdroid_apiKey'] = testdroid_apiKey
        desired_capabilities_cloud['testdroid_project'] = testdroid_project
        desired_capabilities_cloud['testdroid_testrun'] = testdroid_testrun
        desired_capabilities_cloud['testdroid_device'] = testdroid_device
        desired_capabilities_cloud['testdroid_app'] = testdroid_app
        desired_capabilities_cloud['appPackage'] = app_package
        desired_capabilities_cloud['appActivity'] = app_activity
        desired_capabilities_cloud['platformName'] = 'Android'
        desired_capabilities_cloud['deviceName'] = 'Android Phone'

        desired_capabilities_cloud['newCommandTimeout'] = new_command_timeout
        desired_capabilities_cloud['testdroid_testTimeout'] = testdroid_test_timeout

        # set up webdriver
        self.utils.log("WebDriver request initiated. Waiting for response, this typically takes 2-3 mins")
        self.driver = webdriver.Remote(appium_url, desired_capabilities_cloud)
        self.utils.update_driver(self.driver)
        self.utils.log("WebDriver response received")

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

        self.utils.log("Checking API level. This test works only on API 19 and above.")
        self.utils.log("API level: " + str(self.api_level))
        if self.api_level < 19:
            raise Exception("The chosen device has API level under 19. The Hybrid view will crash.")

        self.utils.log('Clicking button "hybrid app"')
        element = self.driver.find_element_by_id('com.testdroid.sample.android:id/mm_b_hybrid')
        element.click()
        self.utils.screenshot('hybrid_activity')

        self.utils.log('Typing in the url http://docs.testdroid.com')
        element = self.driver.find_element_by_id('com.testdroid.sample.android:id/hy_et_url')
        element.send_keys("http://bitbar.github.io/testdroid-samples/")
        self.utils.screenshot('url_typed')

        try:
            self.utils.log("Hiding keyboard")
            self.driver.hide_keyboard()
        except WebDriverException:
            pass # pass exception, if keyboard isn't visible already
        self.utils.screenshot('keyboard_hidden')

        self.utils.log('Clicking Load url button')
        element = self.driver.find_element_by_id('com.testdroid.sample.android:id/hy_ib_loadUrl')
        element.click()
        self.utils.screenshot('webpage_loaded')

        contexts = "undefined"
        end_time = time.time() + 30
        while "undefined" in str(contexts) and time.time() < end_time:
            contexts = self.driver.contexts
            self.utils.log(str(contexts))
            sleep(5)

        context = str(contexts[-1])
        self.utils.log("Context will be " + context)
        self.driver.switch_to.context(context)
        self.utils.log("Context is " + self.driver.current_context)

        self.utils.log("Finding button with text 'Click for answer'")
        button = self.utils.wait_until_xpath_matches('//button[contains(., "Click for answer")]')


        self.utils.log("Clicking on button")
        button.click()
        self.utils.screenshot("answer")

        self.utils.log("Check answer text")
        elem = self.driver.find_element_by_xpath('//p[@id="result_element" and contains(., "Testdroid")]')

        self.utils.log("Verify button changed color")
        style = str(button.get_attribute('style'))
        expected_style = "rgb(127, 255, 0"
        self.assertTrue(expected_style in style)

        self.driver.switch_to.context("NATIVE_APP")

        self.utils.log('Going back')
        self.driver.back()
        self.utils.screenshot('launch_screen')

def initialize():
    return TestdroidAndroid

if __name__ == "__main__":
    suite = unittest.TestLoader().loadTestsFromTestCase(TestdroidAndroid)
    unittest.TextTestRunner(verbosity=2).run(suite)
