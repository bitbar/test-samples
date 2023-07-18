#
#  Example script for parallel Appium tests
#

import unittest
from time import sleep

import xmlrunner
from appium.webdriver.common.appiumby import AppiumBy
from selenium.common.exceptions import WebDriverException

from BitBarAppiumTest import BitBarAppiumTest, log


class BitBarSampleAppTest(BitBarAppiumTest):
    def setUp(self):
        # BitBarAppiumTest takes settings (local or cloud) from environment variables
        super(BitBarSampleAppTest, self).setUp()

    # Test start.
    def test_the_app(self):
        driver = self.get_driver()  # Initialize Appium connection to device

        sleep(10)  # Wait that the app loads
        log("Start!")
        # Use this to get detected screen hierarchy
        # print self.driver.page_source

        if self.isAndroid():
            try:
                log("Taking screenshot 0_appLaunch.png")
                driver.save_screenshot(self.screenshot_dir + "/0_appLaunch.png")
                log("Clicking element 'Use Testdroid Cloud'")
                elem = self.driver.find_element(AppiumBy.ANDROID_UIAUTOMATOR,
                                                'new UiSelector().text("Use Testdroid Cloud")')
                self.assertTrue(elem)
                elem.click()
                sleep(2)  # always sleep before taking screenshot to let transition animations finish
                log("Taking screenshot: 1_radiobuttonPressed.png")
                driver.save_screenshot(self.screenshot_dir + "/1_radiobuttonPressed.png")

                log("Sleeping 3 before quitting WebDriver")
                sleep(3)
            except WebDriverException:
                log("Android testrun failed..")
        else:  # iOS
            try:
                log("Taking screenshot 0_appLaunch.png")
                driver.save_screenshot(self.screenshot_dir + "/0_appLaunch.png")
                log("Finding buttons")
                buttons = driver.find_elements(AppiumBy.CLASS_NAME, 'UIAButton')
                log("Clicking button [2] - Radiobutton 2")
                buttons[2].click()

                log("Taking screenshot 1_radiobuttonPressed.png")
                driver.save_screenshot(self.screenshot_dir + "/1_radiobuttonPressed.png")

                log("Sleeping 3 before quitting WebDriver")
                sleep(3)
            except WebDriverException:
                log("iOS testrun failed..")
    # Test end.


if __name__ == '__main__':
    unittest.main(testRunner=xmlrunner.XMLTestRunner(output='test-reports'))
