##
## For help on setting up your machine and configuring this TestScript go to
## http://help.testdroid.com/customer/portal/topics/631129-appium/articles
##

import os
import time
import unittest
from time import sleep
from selenium import webdriver
from device_finder import DeviceFinder

def log(msg):
    print (time.strftime("%H:%M:%S") + ": " + msg)

class TestdroidAndroid(unittest.TestCase):

    def screenshot(self, name):
        self.screenShotCount = 1
        screenshotName = str(self.screenShotCount) + "_" + name + ".png" 
        log ("Taking screenshot: " + screenshotName)
        sleep(1) # wait for animations to complete before taking screenshot
        self.driver.save_screenshot(self.screenshotDir + "/" + screenshotName)
        self.screenShotCount += 1

    
    def setUp(self):

        ##
        ## IMPORTANT: Set the following parameters.
        ## You can set the parameters outside the script with environment variables.
        ## If env var is not set the string after or is used.
        ##
        self.screenshotDir = os.environ.get('TESTDROID_SCREENSHOTS') or "/absolute/path/to/desired/directory"
        testdroid_url = os.environ.get('TESTDROID_URL') or "https://cloud.testdroid.com"
        testdroid_apiKey = os.environ.get('TESTDROID_APIKEY') or ""
#        testdroid_app = os.environ.get('TESTDROID_APP') or ""
        appium_url = os.environ.get('TESTDROID_APPIUM_URL') or 'http://appium.testdroid.com/wd/hub'

        # Options to select device
        # 1) Set environment variable TESTDROID_DEVICE
        # 2) Set device name to this python script
        # 3) Do not set #1 and #2 and let DeviceFinder to find free device for you

        deviceFinder = None
        testdroid_device = os.environ.get('TESTDROID_DEVICE') or "Samsung Galaxy Tab 3 10.1 GT-P5210 4.4.2"

        deviceFinder = DeviceFinder(url=testdroid_url)
        if testdroid_device == "":
            # Loop will not exit until free device is found
            while testdroid_device == "":
                testdroid_device = deviceFinder.available_free_android_device()

        
        desired_capabilities_cloud = {}
        desired_capabilities_cloud['testdroid_apiKey'] = testdroid_apiKey
        desired_capabilities_cloud['testdroid_target'] = 'chrome'
        desired_capabilities_cloud['testdroid_project'] = 'Appium Chrome Demo'
        desired_capabilities_cloud['testdroid_testrun'] = 'TestRun A'
        desired_capabilities_cloud['testdroid_device'] = testdroid_device
#        desired_capabilities_cloud['testdroid_app'] = testdroid_app
        desired_capabilities_cloud['platformName'] = 'Android'
        desired_capabilities_cloud['deviceName'] = 'AndroidDevice'
        desired_capabilities_cloud['browserName'] = 'chrome'

        desired_caps = desired_capabilities_cloud;

        log ("Will save screenshots at: " + self.screenshotDir)

        # set up webdriver
        log ("WebDriver request initiated. Waiting for response, this typically takes 2-3 mins")
        self.driver = webdriver.Remote(appium_url, desired_caps)

        log ("Loading page http://testdroid.com")
        self.driver.get("http://testdroid.com")

    def tearDown(self):
        log ("Quitting")
        self.driver.quit()

    def testSample(self):
        log ("Taking screenshot of home page: '1_home.png'")
        self.driver.save_screenshot(self.screenshotDir + "/1_home.png")

        log ("Finding 'Products'")
        elem = self.driver.find_element_by_xpath('//*[@id="menu"]/ul/li[1]/a')
        log ("Clicking 'Products'")
        elem.click()
    
        log ("Taking screenshot of 'Products' page: '2_products.png'")
        self.driver.save_screenshot(self.screenshotDir + "/2_products.png")
        
        log ("Finding 'Learn More'")
        elem = self.driver.find_element_by_xpath('//*[@id="products"]/div[1]/div/div[1]/div[3]/a')
        log ("Clicking 'Learn More'")
        elem.click()

        log ("Taking screenshot of 'Learn More' page: '3_learnmore.png'")
        self.driver.save_screenshot(self.screenshotDir + "/3_learnmore.png")

        log ("Finding 'Supported Frameworks'")
        elem = self.driver.find_element_by_xpath('//*[@id="topBox"]/div[1]/div/a[2]')
        log ("Clicking 'Supported Frameworks'")
        elem.click()

        log ("Taking screenshot of 'Supported Framworks' page: '4_supportedframeworks.png'")
        self.driver.save_screenshot(self.screenshotDir + "/4_supportedframeworks.png")

        log ("quitting")


if __name__ == "__main__":
    suite = unittest.TestLoader().loadTestsFromTestCase(TestdroidAndroid)
    unittest.TextTestRunner(verbosity=2).run(suite)
