import os
import unittest
from base_test import BaseTest
from selenium.webdriver.common.by import By


class BitbarChrome(BaseTest):
    def setUp(self):
        super().setUp()
        bitbar_project_name = (
            os.environ.get("BITBAR_PROJECT") or "Chrome sample project"
        )
        if self.bitbar_device == "":
            self._find_device("Android")

        self.desired_capabilities_cloud["bitbar_project"] = bitbar_project_name
        self.desired_capabilities_cloud["bitbar_device"] = self.bitbar_device
        self.desired_capabilities_cloud["platformName"] = "Android"
        self.desired_capabilities_cloud["deviceName"] = "Android device"
        self.desired_capabilities_cloud["browserName"] = "chrome"
        self.desired_capabilities_cloud["bitbar_target"] = "chrome"

    def test_sample(self):
        super()._start_webdriver()
        test_url = "https://bitbar.github.io/web-testing-target/"
        self.utils.log("Loading page " + test_url)
        self.driver.get(test_url)

        self.utils.screenshot("home_screen")

        self.utils.log("  Switching to landscape")
        self.driver.orientation = "LANDSCAPE"
        self.utils.screenshot("home_landscape")
        self.utils.log("  Switching to portrait")
        self.driver.orientation = "PORTRAIT"
        self.utils.screenshot("home_portrait")

        self.utils.log("Finding button with text 'Click for answer'")
        button = self.utils.wait_until_xpath_matches(
            '//button[contains(., "Click for answer")]'
        )

        self.utils.log("Clicking on button")
        button.click()
        self.utils.screenshot("answer")

        self.utils.log("Check answer text")
        self.driver.find_element(
            By.XPATH, '//p[@id="result_element" and contains(., "Bitbar")]'
        )

        self.utils.log("Verify button changed color")
        style = str(button.get_attribute("style"))
        expected_style = "rgb(127, 255, 0"
        self.assertTrue(expected_style in style)


def initialize():
    return BitbarChrome


if __name__ == "__main__":
    suite = unittest.TestLoader().loadTestsFromTestCase(BitbarChrome)
    unittest.TextTestRunner(verbosity=2).run(suite)
