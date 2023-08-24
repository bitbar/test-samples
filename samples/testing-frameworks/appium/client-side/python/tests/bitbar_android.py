import os
import unittest
from time import sleep
from base_test import BaseTest
from selenium.webdriver.common.by import By
from appium.webdriver.common.appiumby import AppiumBy
from selenium.common.exceptions import WebDriverException


class BitbarAndroid(BaseTest):
    def setUp(self):
        super().setUp()
        bitbar_project_name = (
            os.environ.get("BITBAR_PROJECT") or "Android sample project"
        )
        app_package = os.environ.get("BITBAR_APP_PACKAGE") or "com.bitbar.testdroid"
        app_activity = (
            os.environ.get("BITBAR_ACTIVITY") or ".BitbarSampleApplicationActivity"
        )
        if self.bitbar_device == "":
            self._find_device("Android")

        self.capabilities["platformName"] = "Android"
        self.capabilities["bitbar:options"]["project"] = bitbar_project_name
        self.capabilities["bitbar:options"]["device"] = self.bitbar_device
        #self.capabilities["bitbar:options"]["appiumVersion"] = "1.22.3" #launch tests on appium 1
        self.capabilities["appium:automationName"] = "uiautomator2"
        self.capabilities["appium:appPackage"] = app_package
        self.capabilities["appium:appActivity"] = app_activity

    def test_sample(self):
        super()._start_webdriver()
        self.utils.log("  Getting device screen size")
        self.utils.log("  " + str(self.driver.get_window_size()))

        self.utils.screenshot("app_launch")
        self.utils.log("  Typing in name")
        elems = self.driver.find_elements(By.CLASS_NAME, "android.widget.EditText")
        self.utils.log("  info: EditText:" + str(len(elems)))
        self.utils.log("  Filling in name")
        elems[0].send_keys("Bitbar User")
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
        elem = self.driver.find_element(
            AppiumBy.ANDROID_UIAUTOMATOR, 'new UiSelector().text("Buy 101 devices")'
        )
        elem.click()

        self.utils.screenshot("clicked_button1")

        self.utils.log("  Clicking Answer")
        elem = self.driver.find_element(
            AppiumBy.ANDROID_UIAUTOMATOR, 'new UiSelector().text("Answer")'
        )
        elem.click()

        self.utils.screenshot("answer")

        self.utils.log("  Navigating back to Activity-1")
        self.driver.back()
        self.utils.screenshot("main_activity")

        self.utils.log("  Clicking element 'Use Testdroid Cloud'")
        elem = self.driver.find_element(
            AppiumBy.ANDROID_UIAUTOMATOR,
            'new UiSelector().text("Use Testdroid Cloud")',
        )
        elem.click()

        self.utils.screenshot("clicked_button2")

        self.utils.log("  Clicking Answer")
        elem = self.driver.find_element(
            AppiumBy.ANDROID_UIAUTOMATOR, 'new UiSelector().text("Answer")'
        )
        elem.click()

        self.utils.screenshot("answer")


def initialize():
    return BitbarAndroid


if __name__ == "__main__":
    suite = unittest.TestLoader().loadTestsFromTestCase(BitbarAndroid)
    unittest.TextTestRunner(verbosity=2).run(suite)
