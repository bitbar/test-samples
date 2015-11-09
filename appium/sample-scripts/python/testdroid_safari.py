##
## For help on setting up your machine and configuring this TestScript go to
## http://help.testdroid.com/customer/portal/topics/631129-appium/articles
##
## IMPORTANT: Testdroid cloud doesn't currently support safari testing on
## iOS versions 8.0 or newer. Screenshots can't be taken yet as well.
## Newest iOS versions and screenshots will be supported soon.
##

import os
import sys, traceback
import time
import unittest
from time import sleep
from appium import webdriver
from device_finder import DeviceFinder
from selenium.common.exceptions import WebDriverException

def log(msg):
    print (time.strftime("%H:%M:%S") + ": " + msg)

class TestdroidSafari(unittest.TestCase):
    
    def setUp(self):
        appium_Url = 'http://appium.testdroid.com/wd/hub'
        #appium_Url = 'http://localhost:4723/wd/hub'

        ##
        ## IMPORTANT: Set the following parameters.
        ##
        self.screenshot_dir = os.environ.get('TESTDROID_SCREENSHOTS') or "/absolute/path/to/desired/directory"
        testdroid_apiKey = os.environ.get('TESTDROID_APIKEY') or ""

        # Options to select device
        # 1) Set environment variable TESTDROID_DEVICE
        # 2) Set device name to this python script
        # 3) Do not set #1 and #2 and let DeviceFinder to find free device for you

        deviceFinder = None
        testdroid_device = os.environ.get('TESTDROID_DEVICE') or "iPhone 5 A1429 6.1.4"

        if testdroid_device == "":
            deviceFinder = DeviceFinder(url=testdroid_url)
            # Loop will not exit until free device is found
            while testdroid_device == "":
                testdroid_device = deviceFinder.available_free_ios_device()

        print "Starting Appium test using device '%s'" % testdroid_device

        desired_capabilities_cloud = {}
        desired_capabilities_cloud['testdroid_apiKey'] = testdroid_apiKey
        desired_capabilities_cloud['testdroid_target'] = 'safari'
        desired_capabilities_cloud['testdroid_project'] = 'Appium Safari Demo'
        desired_capabilities_cloud['testdroid_testrun'] = 'TestRun A'
        desired_capabilities_cloud['testdroid_device'] = testdroid_device
        desired_capabilities_cloud['platformName'] = 'iOS'
        desired_capabilities_cloud['deviceName'] = 'iOS Device'
        desired_capabilities_cloud['browserName'] = 'Safari'
        desired_caps = desired_capabilities_cloud;
        
        log ("Will save screenshots at: " + self.screenshot_dir)
        
        # Set up webdriver
        log ("WebDriver request initiated. Waiting for response, this typically takes 2-3 mins")
        self.driver = webdriver.Remote(appium_Url, desired_caps)
        self.driver.implicitly_wait(60)
    
    def tearDown(self):
        self.driver.quit()

    def test_get(self):
        try:
            # Need to change context?
            log ("Contexts: " + str(self.driver.contexts))
            #webview = self.driver.contexts[-1] # -1 = Use the last index
            #self.driver.switch_to.context(webview)

            log ("Loading page http://testdroid.com")
            self.driver.get("http://testdroid.com")
            
            sleep(3)
            #log("Taking a screenshot")
            #self.driver.save_screenshot(self.screenshot_dir + '/testdroidcom.png')
            
            # Finding element xpath, id or name is simple with Appium GUI application's inspector.
            # Safari Web Inspector can also be used on iOS devices.
            log ("Finding menu button")
            elem = self.driver.find_element_by_xpath('//body[1]/header[1]/div[1]/div[1]/button[1]')
            log ("elem: " + elem.text)
            
            # This element isn't displayed on bigger screens
            if elem.is_displayed():
                log ("Clicking menu button")
                elem.click()
                sleep(2) # Sleep 2 seconds so that the page has time to load
            else:
                log ("Menu button not found, skipping menu button.")
            
            log ("Finding 'Products' button")
            elem = self.driver.find_element_by_xpath('//body[1]/header[1]/div[1]/nav[1]/ul[1]/li[2]/a[1]')
            log ("Clicking 'Products' button")
            elem.click()
            sleep(2) # Sleep 2 seconds so that the page has time to load
            
            log ("Finding 'Testdroid' banner")
            elem = self.driver.find_element_by_xpath('//body[1]/header[1]/div[1]/div[1]/a[1]')
            log ("Clicking 'Testdroid' banner")
            elem.click()
            sleep(2) # Sleep 2 seconds so that the page has time to load
        except WebDriverException, e:
            print "Unexpected error:", sys.exc_info()[0]
            print '-'*60
            traceback.print_exc(file=sys.stdout)
            print '-'*60

if __name__ == "__main__":
    suite = unittest.TestLoader().loadTestsFromTestCase(TestdroidSafari)
    unittest.TextTestRunner(verbosity=2).run(suite)
