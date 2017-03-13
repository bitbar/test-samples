##
## Example script for parallel selenium tests
##
import unittest
import xmlrunner
from time import sleep
from TestdroidAppiumTest import TestdroidAppiumTest, log
from selenium.common.exceptions import WebDriverException

class BitbarSampleAppTest(TestdroidAppiumTest):
    def setUp(self):
        # TestdroidAppiumTest takes settings (local or cloud) from environment variables
        super(BitbarSampleAppTest, self).setUp()
    
    # Test start.    
    def test_the_app(self):
        driver = self.get_driver() # Initialize Appium connection to device
        
        sleep(10) # Wait that the app loads
        log("Start!")
        # Use this to get detected screen hierarchy
        # print self.driver.page_source
        
        if self.isAndroid():
            #try:
                log("Taking screenshot 0_appLaunch.png")
                driver.save_screenshot(self.screenshot_dir + "/0_appLaunch.png")
                log("Clicking element 'Use Testdroid Cloud'")
                if self.isSelendroid():
                    elem = driver.find_element_by_xpath("//LinearLayout[1]/FrameLayout[1]/ScrollView[1]/LinearLayout[1]/LinearLayout[1]/RadioGroup[1]/RadioButton[2]")
                else:
                    elem = self.driver.find_element_by_android_uiautomator('new UiSelector().text("Use Testdroid Cloud")')
                self.assertTrue(elem)
                elem.click()
                sleep(2) # always sleep before taking screenshot to let transition animations finish
                log("Taking screenshot: 1_radiobuttonPressed.png")
                driver.save_screenshot(self.screenshot_dir + "/1_radiobuttonPressed.png")
                
                log("Sleeping 3 before quitting webdriver")
                sleep(3)
            #except WebDriverException:
                #log("Testrun failed..")
        else: # iOS
            try:
                log("Taking screenshot 0_appLaunch.png")
                driver.save_screenshot(self.screenshot_dir + "/0_appLaunch.png")
                log("Finding buttons")
                buttons = driver.find_elements_by_class_name('UIAButton')
                log("Clicking button [2] - Radiobutton 2")
                buttons[2].click()

                log("Taking screenshot 1_radiobuttonPressed.png")
                driver.save_screenshot(self.screenshot_dir + "/1_radiobuttonPressed.png")

                log("Sleeping 3 before quitting webdriver")
                sleep(3)
            except WebDriverException:
                log("Testrun failed..")
                
    # Test end.

if __name__ == '__main__':
    unittest.main(testRunner=xmlrunner.XMLTestRunner(output='test-reports'))
