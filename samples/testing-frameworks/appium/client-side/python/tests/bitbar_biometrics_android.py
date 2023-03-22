import os
import unittest
from base_test import BaseTest
from selenium.webdriver.common.by import By
from appium.webdriver.common.appiumby import AppiumBy


class BitbarBiometricsAndroid(BaseTest):
    def setUp(self):
        super().setUp()
        bitbar_project_name = (
            os.environ.get("BITBAR_PROJECT") or "Android biometrics sample project"
        )
        app_package = os.environ.get("BITBAR_APP_PACKAGE") or "com.bitbarsampleapp"
        app_activity = (
            os.environ.get("BITBAR_ACTIVITY") or "com.bitbarsampleapp.MainActivity"
        )
        if self.bitbar_device == "":
            self._find_device("Android")

        self.desired_capabilities_cloud["bitbar_project"] = bitbar_project_name
        self.desired_capabilities_cloud["bitbar_device"] = self.bitbar_device
        self.desired_capabilities_cloud["appPackage"] = app_package
        self.desired_capabilities_cloud["appActivity"] = app_activity
        self.desired_capabilities_cloud["platformName"] = "Android"
        self.desired_capabilities_cloud["deviceName"] = "Android device"
        self.desired_capabilities_cloud["bitbar_biometricInstrumentation"] = True
        if self.api_level <= 16:
            self.desired_capabilities_cloud["bitbar_target"] = "selendroid"
            self.desired_capabilities_cloud["automationName"] = "Selendroid"
        else:
            self.desired_capabilities_cloud["bitbar_target"] = "android"
            self.desired_capabilities_cloud["automationName"] = "Appium"

    def test_sample(self):
        super()._start_webdriver()
        self.utils.screenshot("start_screen")

        button = self.driver.find_element(
            By.XPATH,
            '//android.view.ViewGroup[@content-desc="Biometric authentication"]',
        )
        button.click()
        self.utils.log("view1: Clicking button - BIOMETRIC AUTHENTICATION")
        self.driver.implicitly_wait(10)
        self.utils.screenshot("second_screen")

        element = self.driver.find_element(
            AppiumBy.ACCESSIBILITY_ID, "Sensor type value"
        )
        assert element.text == "ANDROID BIOMETRICS", self.utils.log(
            "view2: Error in biometry injection"
        )
        self.utils.log("view2: Biometry injected properly")

        button = self.driver.find_element(
            By.XPATH,
            '//android.view.ViewGroup[@content-desc="Ask biometric authentication"]',
        )
        button.click()
        self.utils.log("view2: Accepting biometry prompt")
        self.utils.screenshot("authentication_prompt")

        button = self.driver.find_element(By.XPATH, '//*[@text="PASS"]')
        button.click()
        element = self.driver.find_element(
            AppiumBy.ACCESSIBILITY_ID, "Authentication status value"
        )
        assert element.text == "SUCCEEDED"
        self.utils.log("view2: Test passed")
        self.utils.screenshot("biometry_checked")


def initialize():
    return BitbarBiometricsAndroid


if __name__ == "__main__":
    suite = unittest.TestLoader().loadTestsFromTestCase(BitbarBiometricsAndroid)
    unittest.TextTestRunner(verbosity=2).run(suite)
