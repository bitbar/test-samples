#
# For help on setting up your machine and configuring this TestScript go to
# http://docs.bitbar.com/testing/appium/
#

import os
import time
import unittest
import subprocess
from appium import webdriver
from device_finder import DeviceFinder
from selenium.common.exceptions import WebDriverException
from bitbar_utils import BitbarUtils


class BitbarAndroid(unittest.TestCase):
    def setUp(self):
        #
        # IMPORTANT: Set the following parameters.
        # You can set the parameters outside the script with environment
        # variables.
        # If env var is not set the string after or is used.
        #

        bitbar_url = os.environ.get('BITBAR_URL') or \
            "https://cloud.bitbar.com"
        appium_url = os.environ.get('BITBAR_APPIUM_URL') or \
            'https://appium.bitbar.com/wd/hub'
        bitbar_apiKey = os.environ.get('BITBAR_APIKEY') or ""
        bitbar_project = os.environ.get('BITBAR_PROJECT') or \
            'Android hybrid sample project'
        bitbar_testrun = os.environ.get('BITBAR_TESTRUN') or \
            'My testrun'
        bitbar_app = os.environ.get('BITBAR_APP') or ""
        app_package = os.environ.get('BITBAR_APP_PACKAGE') or \
            'com.testdroid.sample.android'
        app_activity = os.environ.get('BITBAR_ACTIVITY') or \
            '.MM_MainMenu'
        new_command_timeout = os.environ.get('BITBAR_CMD_TIMEOUT') or '60'
        bitbar_test_timeout = os.environ.get('BITBAR_TEST_TIMEOUT') or '600'

        self.screenshot_dir = os.environ.get('BITBAR_SCREENSHOTS') or \
            os.getcwd() + "/screenshots"
        self.screenshot_count = 1

        # Options to select device
        # 1) Set environment variable BITBAR_DEVICE
        # 2) Set device name to this python script
        # 3) Do not set #1 and #2 and let DeviceFinder to find free device for
        #    you
        bitbar_device = os.environ.get('BITBAR_DEVICE') or ""

        deviceFinder = DeviceFinder(url=bitbar_url)
        if bitbar_device == "":
            # Loop will not exit until free device is found
            while bitbar_device == "":
                bitbar_device = deviceFinder.available_android_device()

        self.utils = BitbarUtils(self.screenshot_dir)
        self.utils.log("Will save screenshots at: " + self.screenshot_dir)

        if "localhost" in appium_url:
            self.api_level = subprocess.check_output(["adb", "shell", "getprop ro.build.version.sdk"])
        else:
            self.api_level = deviceFinder.device_API_level(bitbar_device)

        self.utils.log("Device API level is %s" % self.api_level)
        self.utils.log("Starting Appium test using device '%s'" % bitbar_device)

        desired_capabilities_cloud = {}
        desired_capabilities_cloud['bitbar_apiKey'] = bitbar_apiKey
        if self.api_level > 16:
            desired_capabilities_cloud['bitbar_target'] = 'android'
            desired_capabilities_cloud['automationName'] = 'Appium'
        else:
            desired_capabilities_cloud['bitbar_target'] = 'selendroid'
            desired_capabilities_cloud['automationName'] = 'Selendroid'

        desired_capabilities_cloud['bitbar_apiKey'] = bitbar_apiKey
        desired_capabilities_cloud['bitbar_project'] = bitbar_project
        desired_capabilities_cloud['bitbar_testrun'] = bitbar_testrun
        desired_capabilities_cloud['bitbar_device'] = bitbar_device
        desired_capabilities_cloud['bitbar_app'] = bitbar_app
        desired_capabilities_cloud['appPackage'] = app_package
        desired_capabilities_cloud['appActivity'] = app_activity
        desired_capabilities_cloud['platformName'] = 'Android'
        desired_capabilities_cloud['deviceName'] = 'Android Phone'
        desired_capabilities_cloud['fullReset'] = False
        desired_capabilities_cloud['noReset'] = True
        desired_capabilities_cloud['newCommandTimeout'] = new_command_timeout
        desired_capabilities_cloud['bitbar_testTimeout'] = bitbar_test_timeout

        # set up webdriver
        self.utils.log("WebDriver request initiated. Waiting for response, \
                        this typically takes 2-3 mins")
        self.driver = webdriver.Remote(appium_url, desired_capabilities_cloud)
        self.utils.update_driver(self.driver)
        self.utils.log("WebDriver response received")
        self.driver.implicitly_wait(5)

    def tearDown(self):
        self.utils.log("Quitting")
        self.driver.quit()

    def testSample(self):
        self.utils.log("  Getting device screen size")
        self.utils.log("  " + str(self.driver.get_window_size()))

        self.utils.screenshot("app_launch")

        self.utils.log("Checking API level. This test works only on API 19 \
                        and above.")
        self.utils.log("API level: " + str(self.api_level))
        if self.api_level < 19:
            raise Exception("The chosen device has API level under 19. \
                            Hybrid view will crash.")

        self.utils.log('Clicking button "hybrid app"')
        element = self.driver.find_element_by_id('com.testdroid.sample.android:id/mm_b_hybrid')
        element.click()
        self.utils.screenshot('hybrid_activity')

        url = "https://bitbar.github.io/web-testing-target/"
        self.utils.log('Typing in the url ' + url)
        element = self.driver.find_element_by_id('com.testdroid.sample.android:id/hy_et_url')
        element.send_keys(url)
        self.utils.screenshot('url_typed')

        try:
            self.utils.log("Hiding keyboard")
            self.driver.hide_keyboard()
        except WebDriverException:
            pass  # pass exception, if keyboard isn't visible already
        self.utils.screenshot('keyboard_hidden')

        self.utils.log('Clicking Load url button')
        element = self.driver.find_element_by_id('com.testdroid.sample.android:id/hy_ib_loadUrl')
        element.click()
        self.utils.screenshot('webpage_loaded')

        context = "undefined"
        end_time = time.time() + 30
        while "undefined" in context and time.time() < end_time:
            contexts = self.driver.contexts
            context = str(contexts[-1])
            self.utils.log("Available contexts: {}, picking: {}".format(contexts, context))

        self.utils.log("Context will be " + context)
        self.driver.switch_to.context(context)
        self.utils.log("Context is " + self.driver.current_context)

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

        self.driver.switch_to.context("NATIVE_APP")

        self.utils.log('Going back')
        self.driver.back()
        self.utils.screenshot('launch_screen')


def initialize():
    return BitbarAndroid


if __name__ == "__main__":
    suite = unittest.TestLoader().loadTestsFromTestCase(BitbarAndroid)
    unittest.TextTestRunner(verbosity=2).run(suite)
