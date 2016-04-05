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
        testdroid_project_name = os.environ.get('TESTDROID_PROJECT') or 'Appium Chrome Demo'
        testdroid_testrun_name = os.environ.get('TESTDROID_TESTRUN') or "My testrun"

        # Options to select device
        # 1) Set environment variable TESTDROID_DEVICE
        # 2) Set device name to this python script
        # 3) Do not set #1 and #2 and let DeviceFinder to find free device for you

        deviceFinder = None
        testdroid_device = os.environ.get('TESTDROID_DEVICE') or ""

        deviceFinder = DeviceFinder(url=testdroid_url)
        if testdroid_device == "":
            # Loop will not exit until free device is found
            while testdroid_device == "":
                testdroid_device = deviceFinder.available_free_android_device()

        desired_capabilities_cloud = {}
        desired_capabilities_cloud['testdroid_apiKey'] = testdroid_apiKey
        desired_capabilities_cloud['testdroid_target'] = 'chrome'
        desired_capabilities_cloud['testdroid_project'] = testdroid_project_name
        desired_capabilities_cloud['testdroid_testrun'] = testdroid_testrun_name
        desired_capabilities_cloud['testdroid_device'] = testdroid_device
        desired_capabilities_cloud['platformName'] = 'Android'
        desired_capabilities_cloud['deviceName'] = 'AndroidDevice'
        desired_capabilities_cloud['browserName'] = 'chrome'

        log ("Will save screenshots at: " + self.screenshotDir)

        # set up webdriver
        log ("WebDriver request initiated. Waiting for response, this typically takes 2-3 mins")
        self.driver = webdriver.Remote(appium_url, desired_capabilities_cloud)

        log ("Loading page http://docs.testdroid.com")
        self.driver.get("http://docs.testdroid.com")

    def tearDown(self):
        log ("Quitting, closing connection")
        self.driver.quit()

    def testSample(self):
        log ("Taking screenshot of home page: '1_home.png'")
        self.driver.save_screenshot(self.screenshotDir + "/1_home.png")

        log ("Finding 'search button'")
        elem = self.driver.find_element_by_xpath('//input[@id="search"]')

        log ("Clicking 'Search'")
        elem.send_keys("appium")
        log ("Taking screenshot of 'Search' with search text")
        self.driver.save_screenshot(self.screenshotDir + "/2_search_text.png")

        log ("Click search")
        elem = self.driver.find_element_by_xpath('//input[@class="search-button"]')
        elem.click()

        log ("  Switching to landscape")
        self.driver.orientation = "LANDSCAPE"
        self.driver.save_screenshot(self.screenshotDir + "/3_results_landscape.png")
        log ("  Switching to portrait: 2_portrait.png")
        self.driver.orientation = "PORTRAIT"
        self.driver.save_screenshot(self.screenshotDir + "/3_results_portrait.png")

        log ("Look for result text")
        # loop for 20s or until expected result is found
        for i in range(10):
            elem = self.driver.find_element_by_xpath('//h1[text()]')
            log ("Found text: '{}'".format(elem.text))
            if "for \"appium\"" in str(elem.text):
                log ("Element has text: '{}'".format(elem.text))
                break
            sleep(2)
            i = i + 1

        log ("End of test")

def initialize():
    return TestdroidAndroid

if __name__ == "__main__":
    suite = unittest.TestLoader().loadTestsFromTestCase(TestdroidAndroid)
    unittest.TextTestRunner(verbosity=2).run(suite)

