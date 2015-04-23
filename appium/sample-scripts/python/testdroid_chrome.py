##
## For help on setting up your machine and configuring this TestScript go to
## http://help.testdroid.com/customer/portal/topics/631129-appium/articles
##

import os
import sys
import time
from time import sleep
from selenium import webdriver
from device_finder import DeviceFinder

def log(msg):
    print (time.strftime("%H:%M:%S") + ": " + msg)

appium_Url = 'http://appium.testdroid.com/wd/hub';
#appium_Url = 'http://10.0.1.115:10010/wd/hub';

##
## IMPORTANT: Set the following parameters.
##
screenshotDir= "/absolute/path/to/desired/directory"
testdroid_username = "user@example.com"
testdroid_password = "p4s$w0rd"

# Device can be manually defined, by device name found from Testdroid Cloud
# testdroid_device = "LG Google Nexus 5 D821 4.4"
#
# DeviceFinder can be used to find available free device for testing
deviceFinder = DeviceFinder(testdroid_username, testdroid_password)
testdroid_device = ""
while testdroid_device == "":
    testdroid_device = deviceFinder.available_free_android_device()

print "Starting Appium test using device '%s'" % testdroid_device

desired_capabilities_cloud = {}
desired_capabilities_cloud['testdroid_username'] = testdroid_username
desired_capabilities_cloud['testdroid_password'] = testdroid_password
desired_capabilities_cloud['testdroid_target'] = 'chrome'
desired_capabilities_cloud['testdroid_project'] = 'Appium Chrome Demo'
desired_capabilities_cloud['testdroid_testrun'] = 'TestRun A'
desired_capabilities_cloud['testdroid_device'] = testdroid_device
desired_capabilities_cloud['platformName'] = 'Android'
desired_capabilities_cloud['deviceName'] = 'AndroidDevice'
desired_capabilities_cloud['browserName'] = 'chrome'

desired_caps = desired_capabilities_cloud;

log ("Will save screenshots at: " + screenshotDir)

# set up webdriver
log ("WebDriver request initiated. Waiting for response, this typically takes 2-3 mins")
driver = webdriver.Remote(appium_Url, desired_caps)

log ("Loading page http://testdroid.com")
driver.get("http://testdroid.com")

log ("Taking screenshot of home page: '1_home.png'")
driver.save_screenshot(screenshotDir + "/1_home.png")

log ("Finding 'Products'")
elem = driver.find_element_by_xpath('//*[@id="menu"]/ul/li[1]/a')
log ("Clicking 'Products'")
elem.click()

log ("Taking screenshot of 'Products' page: '2_products.png'")
driver.save_screenshot(screenshotDir + "/2_products.png")

log ("Finding 'Learn More'")
elem = driver.find_element_by_xpath('//*[@id="products"]/div[1]/div/div[1]/div[3]/a')
log ("Clicking 'Learn More'")
elem.click()

log ("Taking screenshot of 'Learn More' page: '3_learnmore.png'")
driver.save_screenshot(screenshotDir + "/3_learnmore.png")

log ("Finding 'Supported Frameworks'")
elem = driver.find_element_by_xpath('//*[@id="topBox"]/div[1]/div/a[2]')
log ("Clicking 'Supported Frameworks'")
elem.click()

log ("Taking screenshot of 'Supported Framworks' page: '4_supportedframeworks.png'")
driver.save_screenshot(screenshotDir + "/4_supportedframeworks.png")

log ("quitting")
driver.quit()
sys.exit()
