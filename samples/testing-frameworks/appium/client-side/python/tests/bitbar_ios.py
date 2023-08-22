import os
import unittest
from time import sleep
from base_test import BaseTest
from selenium.webdriver.common.by import By


class BitbarIOS(BaseTest):
    def setUp(self):
        super().setUp()
        bitbar_project_name = os.environ.get("BITBAR_PROJECT") or "iOS sample project"
        bitbar_bundle_id = (
            os.environ.get("BITBAR_BUNDLE_ID") or "com.bitbar.testdroid.BitbarIOSSample"
        )
        automation_name = os.environ.get("BITBAR_AUTOMATION_NAME") or "XCUITest"
        if self.bitbar_device == "":
            self._find_device("iOS")

        self.capabilities["platformName"] = "iOS"
        self.capabilities["bitbar:options"]["project"] = bitbar_project_name
        self.capabilities["bitbar:options"]["device"] = self.bitbar_device
        self.capabilities["bitbar:options"]["target"] = "ios"
        self.capabilities["appium:bundleId"] = bitbar_bundle_id
        self.capabilities["appium:automationName"] = automation_name
        self.capabilities["appium:deviceName"] = "iPhone device"

    def test_sample(self):
        super()._start_webdriver()
        # view1
        self.utils.log("view1: Finding buttons")
        buttons = self.driver.find_elements(By.CLASS_NAME, "UIAButton")
        self.utils.log("view1: Clicking button [0] - RadioButton 1")
        buttons[0].click()

        self.utils.log("view1: Typing in textfield[0]: Bitbar user")
        elem = self.driver.find_element(By.CLASS_NAME, "UIATextField")
        elem.clear()
        elem.send_keys("Bitbar user")

        self.utils.log("view1: Typing into text field")
        self.utils.screenshot("text-in-field")

        self.utils.log("view1: Hiding Keyboard")
        self.driver.find_element(By.XPATH, "//*[contains(@name, 'Return')]").click()

        self.utils.log("view1: Hiding keyboard")
        self.utils.screenshot("keyboard-hidden")

        self.utils.log("view1: Clicking button[6] - OK  Button")
        buttons[6].click()

        self.utils.log("view2: Ok button clicked")
        self.utils.screenshot("new-view")

        # view2
        self.utils.log("view2: Finding buttons")
        buttons = self.driver.find_elements(By.CLASS_NAME, "UIAButton")
        self.utils.log("view2: Clicking button[0] - Back/OK button")
        buttons[0].click()
        self.utils.screenshot("back-to-view1")

        # view1
        self.utils.log("view1: Finding buttons")
        buttons = self.driver.find_elements(By.CLASS_NAME, "UIAButton")
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
