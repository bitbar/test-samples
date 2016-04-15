##
## For help on setting up your machine and configuring this TestScript go to
## http://docs.testdroid.com/appium/
##

import os
import time
import unittest
from device_finder import DeviceFinder
from time import sleep
from selenium import webdriver

def log(msg):
    print (time.strftime("%H:%M:%S") + ": " + msg)

class TestdroidAndroid(unittest.TestCase):

    def screenshot(self, name):
        screenshotName = str(self.screenshot_count) + "_" + name + ".png" 
        log ("Taking screenshot: " + screenshotName)
        sleep(1) # wait for animations to complete before taking screenshot
        self.driver.save_screenshot(self.screenshot_dir + "/" + screenshotName)
        self.screenshot_count += 1

    
    def setUp(self):

        ##
        ## IMPORTANT: Set the following parameters.
        ## You can set the parameters outside the script with environment variables.
        ## If env var is not set the string after or is used.
        ##
        self.screenshot_dir = os.environ.get('TESTDROID_SCREENSHOTS') or "/absolute/path/to/desired/directory"
        testdroid_url = os.environ.get('TESTDROID_URL') or "https://cloud.testdroid.com"
        testdroid_apiKey = os.environ.get('TESTDROID_APIKEY') or ""
        testdroid_app = os.environ.get('TESTDROID_APP') or ""
        appium_url = os.environ.get('TESTDROID_APPIUM_URL') or 'http://appium.testdroid.com/wd/hub'
        app_package = os.environ.get('TESTDROID_APP_PACKAGE') or 'com.testdroid.sample.android'
        app_activity = os.environ.get('TESTDROID_ACTIVITY') or '.MA_MainActivity'
        testdroid_project = os.environ.get('TESTDROID_PROJECT') or 'Android hybrid sample project'
        testdroid_testrun = os.environ.get('TESTDROID_TESTRUN') or 'My testrun'
        new_command_timeout = os.environ.get('TESTDROID_CMD_TIMEOUT') or '60'
        testdroid_test_timeout = os.environ.get('TESTDROID_TEST_TIMEOUT') or '600'

        log ("Will save screenshots at: " + self.screenshot_dir)
        self.screenshot_count = 1


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

        apiLevel = deviceFinder.device_API_level(testdroid_device)
        print "Starting Appium test using device '%s'" % testdroid_device

        desired_capabilities_cloud = {}
        desired_capabilities_cloud['testdroid_apiKey'] = testdroid_apiKey
        if apiLevel > 16:
            desired_capabilities_cloud['testdroid_target'] = 'android'
        else:
            desired_capabilities_cloud['testdroid_target'] = 'selendroid'

        desired_capabilities_cloud['testdroid_apiKey'] = testdroid_apiKey
        desired_capabilities_cloud['testdroid_project'] = testdroid_project
        desired_capabilities_cloud['testdroid_testrun'] = testdroid_testrun
        desired_capabilities_cloud['testdroid_device'] = testdroid_device
        desired_capabilities_cloud['testdroid_app'] = testdroid_app
        desired_capabilities_cloud['platformName'] = 'Android'
        desired_capabilities_cloud['deviceName'] = 'Android Phone'
        desired_capabilities_cloud['automationName'] = 'selendroid'
        desired_capabilities_cloud['newCommandTimeout'] = new_command_timeout
        desired_capabilities_cloud['testdroid_testTimeout'] = testdroid_test_timeout

        # set up webdriver
        log ("WebDriver request initiated. Waiting for response, this typically takes 2-3 mins")
        self.driver = webdriver.Remote(appium_url, desired_capabilities_cloud)
        log ("WebDriver response received")

    def tearDown(self):
        log ("Quitting")
        self.driver.quit()

    def testSample(self):
        log ("Activity-1")
        log ("  Getting device screen size")
        print self.driver.get_window_size()

        self.screenshot("appLaunch")
        sleep(3)

        log('clicking button "hybrid app"')
        element=self.driver.find_element_by_id('com.testdroid.sample.android:id/mm_b_hybrid')
        element.click()
        self.screenshot('hybridActivity')

        log('Typing in the Url')
        element=self.driver.find_element_by_id('com.testdroid.sample.android:id/hy_et_url')
        element.send_keys("http://testdroid.com")
        self.screenshot('urlTyped')

        log('hiding keyboard')
        self.driver.back()
        self.screenshot('keyboardHidden')

        log('clicking Load url button')
        element=self.driver.find_element_by_id('com.testdroid.sample.android:id/hy_ib_loadUrl')
        element.click()
        log('waiting 10 seconds for url to load completely')
        sleep(10)
        self.screenshot('webPageLoaded')

        log('finding window handles')
        handles = self.driver.window_handles
        for handle in self.driver.window_handles:
            log("   > Window: " + handle)

        log('switching to webview')
        webview = handles[1]
        self.driver.switch_to_window(webview)

        log ("Clicking 'Products'")
        elem = self.driver.find_element_by_xpath('//*[@id="menu"]/ul/li[1]/a')
        elem.click()
        sleep(5)
        self.screenshot('webview_products')

        log ("Clicking 'Learn More'")
        elem = self.driver.find_element_by_xpath('//*[@id="products"]/div[1]/div/div[1]/div[3]/a')
        elem.click()
        sleep(5)
        self.screenshot('webview_learnmore')

        log ("Clicking 'Supported Frameworks'")
        elem = self.driver.find_element_by_xpath('//*[@id="topBox"]/div[1]/div/a[2]')
        elem.click()
        sleep(5)
        self.screenshot('webview_supportedframeworks')

        log('finding window handles')
        handles = self.driver.window_handles
        for handle in self.driver.window_handles:
            log("   > Window: " + handle)

        log('switching back to native app')
        nativeapp = handles[0]
        self.driver.switch_to_window(nativeapp)

        log('goint back')
        self.driver.back()
        self.screenshot('launchScreen')

def initialize():
    return TestdroidAndroid

if __name__ == "__main__":
    suite = unittest.TestLoader().loadTestsFromTestCase(TestdroidAndroid)
    unittest.TextTestRunner(verbosity=2).run(suite)
