##
# For help on setting up your machine and configuring this TestScript go to
# http://docs.bitbar.com/testing/appium/
##

import os
import unittest
import subprocess
from appium import webdriver
from bitbar_utils import BitbarUtils
from device_finder import DeviceFinder


class BaseTest(unittest.TestCase):
    def setUp(self):
        self.appium_url = (
            os.environ.get("BITBAR_APPIUM_URL") or "https://appium.bitbar.com/wd/hub"
        )

        bitbar_url = os.environ.get("BITBAR_URL") or "https://cloud.bitbar.com"
        bitbar_apiKey = os.environ.get("BITBAR_APIKEY") or ""
        bitbar_testrun_name = os.environ.get("BITBAR_TESTRUN") or "My testrun"
        bitbar_app = os.environ.get("BITBAR_APP") or ""
        new_command_timeout = os.environ.get("BITBAR_CMD_TIMEOUT") or "60"
        bitbar_test_timeout = os.environ.get("BITBAR_TEST_TIMEOUT") or "600"
        bitbar_find_device = os.environ.get("BITBAR_FINDDEVICE") or True
        self.screenshot_dir = (
            os.environ.get("BITBAR_SCREENSHOTS") or os.getcwd() + "/screenshots"
        )
        self.screenshot_count = 1

        # Options to select device
        # 1) Set environment variable BITBAR_DEVICE
        # 2) Set device name to this python script
        # 3) Do not set #1 and #2 and let DeviceFinder to find free device for
        #    you
        self.bitbar_device = os.environ.get("BITBAR_DEVICE") or ""
        self.device_finder = DeviceFinder(url=bitbar_url)
        self.utils = BitbarUtils(self.screenshot_dir)
        self.utils.log("Will save screenshots at: " + self.screenshot_dir)

        self.desired_capabilities_cloud = {}
        self.desired_capabilities_cloud["bitbar_apiKey"] = bitbar_apiKey
        self.desired_capabilities_cloud["bitbar_testrun"] = bitbar_testrun_name
        self.desired_capabilities_cloud["bitbar_app"] = bitbar_app
        self.desired_capabilities_cloud["fullReset"] = False
        self.desired_capabilities_cloud["noReset"] = True
        self.desired_capabilities_cloud["newCommandTimeout"] = new_command_timeout
        self.desired_capabilities_cloud["bitbar_testTimeout"] = bitbar_test_timeout
        self.desired_capabilities_cloud["bitbar_findDevice"] = bitbar_find_device

    def test_sample(self):
        pass

    def tearDown(self):
        self.utils.log("Quitting")
        self.driver.quit()

    def _start_webdriver(self):
        self.utils.log(
            "WebDriver request initiated. Waiting for response, this typically takes 2-3 mins"
        )
        self.driver = webdriver.Remote(self.appium_url, self.desired_capabilities_cloud)
        self.utils.log("WebDriver response received")
        self.utils.update_driver(self.driver)
        self.utils.log("Driver session id: " + self.driver.session_id)

    def _find_device(self, os: str):
        if os == "Android":
            while self.bitbar_device == "":
                self.bitbar_device = self.device_finder.available_android_device()
            if "localhost" in self.appium_url:
                self.api_level = subprocess.check_output(
                    ["adb", "shell", "getprop ro.build.version.sdk"]
                )
            else:
                self.api_level = self.device_finder.device_API_level(self.bitbar_device)
            self.utils.log("Device API level is %s" % self.api_level)

        else:
            while self.bitbar_device == "":
                self.bitbar_device = self.device_finder.available_ios_device()

        self.utils.log("Starting Appium test using device '%s'" % self.bitbar_device)
