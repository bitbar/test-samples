##
## For help on setting up your machine and configuring this TestScript go to
## http://help.testdroid.com/customer/portal/topics/631129-appium/articles
##

import os
import time
import unittest
from time import sleep
from appium import webdriver
from device_finder import DeviceFinder
from selenium.common.exceptions import WebDriverException

def log(msg):
    print (time.strftime("%H:%M:%S") + ": " + msg)

class TestdroidAndroid(unittest.TestCase):
    def setUp(self):

        ##
        ## IMPORTANT: Set the following parameters.
        ## You can set the parameters outside the script with environment variables.
        ## If env var is not set the string after or is used.
        ##
        self.screenshotDir = os.environ.get('TESTDROID_SCREENSHOTS') or "/absolute/path/to/desired/directory"
        testdroid_url = os.environ.get('TESTDROID_URL') or "https://cloud.testdroid.com"
        testdroid_apiKey = os.environ.get('TESTDROID_APIKEY') or ""
        testdroid_app = os.environ.get('TESTDROID_APP') or ""
        appium_url = os.environ.get('TESTDROID_APPIUM_URL') or 'http://appium.testdroid.com/wd/hub'

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
        if apiLevel > 17:
            desired_capabilities_cloud['testdroid_target'] = 'Android'
        else:
            desired_capabilities_cloud['testdroid_target'] = 'Selendroid'
        desired_capabilities_cloud['testdroid_project'] = os.environ.get('TESTDROID_PROJECT') or 'Android demo'
        desired_capabilities_cloud['testdroid_testrun'] = os.environ.get('TESTDROID_TESTRUN') or 'My testrun'
        desired_capabilities_cloud['testdroid_device'] = testdroid_device
        desired_capabilities_cloud['testdroid_app'] = testdroid_app
        #desired_capabilities_cloud['app'] = '/absolute/path/to/your/application.apk'
        desired_capabilities_cloud['platformName'] = 'Android'
        desired_capabilities_cloud['deviceName'] = 'Android Phone'
        desired_capabilities_cloud['appPackage'] = 'com.bitbar.testdroid'
        desired_capabilities_cloud['appActivity'] = '.BitbarSampleApplicationActivity'

        desired_caps = desired_capabilities_cloud;

        log ("Will save screenshots at: " + self.screenshotDir)
        # set up webdriver
        log ("WebDriver request initiated. Waiting for response, this typically takes 2-3 mins")
        self.driver = webdriver.Remote(appium_url, desired_caps)
        log ("WebDriver response received")

    def tearDown(self):
        log ("Quitting")
        self.driver.quit()

    def testSample(self):
        log ("Test: testSample")
        log ("  Getting device screen size")
        print self.driver.get_window_size()
        isSelendroid = None
        if 'automationName' in self.driver.capabilities:
            if self.driver.capabilities['automationName'] == 'selendroid':
                isSelendroid = True

        log ("  Taking screenshot: 1_appLaunch.png")
        self.driver.save_screenshot(self.screenshotDir + "/1_appLaunch.png")

        log ("  Typing in name")
        elems=self.driver.find_elements_by_class_name('android.widget.EditText')
        log ("  info: EditText:" + `len(elems)`)
        log ("  Filling in name")
        elems[0].send_keys("Testdroid User")
        sleep(2)
        log ("  Taking screenshot: 2_nameTyped.png")
        self.driver.save_screenshot(self.screenshotDir + "/2_nameTyped.png")

        try:
            log ("  Hiding keyboard")
            self.driver.hide_keyboard()
        except:
            pass # pass exception, if keyboard isn't visible already
        log ("  Taking screenshot: 3_nameTypedKeyboardHidden.png")
        self.driver.save_screenshot(self.screenshotDir + "/3_nameTypedKeyboardHidden.png")

        log ("  Clicking element 'Buy 101 devices'")
        if isSelendroid:
            elem = self.driver.find_element_by_link_text('Buy 101 devices')
        else:
            elem = self.driver.find_element_by_name('Buy 101 devices')
        elem.click()

        log ("  Taking screenshot: 4_clickedButton1.png")
        self.driver.save_screenshot(self.screenshotDir + "/4_clickedButton1.png")

        log ("  Clicking Answer")
        if isSelendroid:
            elem = self.driver.find_element_by_link_text('Answer')
        else:
            elem = self.driver.find_element_by_name('Answer')
        elem.click()

        log ("  Taking screenshot: 5_answer.png")
        self.driver.save_screenshot(self.screenshotDir + "/5_answer.png")

        log ("Navigating back to Activity-1")
        self.driver.back()
        log ("  Taking screenshot: 6_mainActivity.png")
        self.driver.save_screenshot(self.screenshotDir + "/6_mainActivity.png")

        log ("  Clicking element 'Use Testdroid Cloud'")
        if isSelendroid:
            elem = self.driver.find_element_by_link_text('Use Testdroid Cloud')
        else:
            elem = self.driver.find_element_by_name('Use Testdroid Cloud')
        elem.click()

        log ("  Taking screenshot: 7_clickedButton2.png")
        self.driver.save_screenshot(self.screenshotDir + "/7_clickedButton2.png")

        log ("  Clicking Answer")
        if isSelendroid:
            elem = self.driver.find_element_by_link_text('Answer')
        else:
            elem = self.driver.find_element_by_name('Answer')
        elem.click()

        log ("  Taking screenshot: 8_answer.png")
        self.driver.save_screenshot(self.screenshotDir + "/8_answer.png")

if __name__ == "__main__":
    suite = unittest.TestLoader().loadTestsFromTestCase(TestdroidAndroid)
    unittest.TextTestRunner(verbosity=2).run(suite)
