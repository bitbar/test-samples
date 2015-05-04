##
## For help on setting up your machine and configuring this TestScript go to
## http://help.testdroid.com/customer/portal/topics/631129-appium/articles
##
## IMPORTANT: Testdroid cloud doesn't currently support safari testing on
## iOS versions 8.0 or newer. Screenshots can't be taken yet as well.
## Newest iOS versions and screenshots will be supported soon.
##

import os
import sys
import time
from time import sleep
from appium import webdriver
from device_finder import DeviceFinder

def log(msg):
    print (time.strftime("%H:%M:%S") + ": " + msg)

appium_Url = 'http://appium.testdroid.com/wd/hub';

##
## IMPORTANT: Set the following parameters.
##
screenshotDir= "/absolute/path/to/desired/directory"
testdroid_username = "username@example.com"
testdroid_password = "p4s$w0rd"

## Device can be manually defined, by device name found from Testdroid Cloud
#testdroid_device = "iPhone 5 A1429 6.1.4"

## DeviceFinder can be used to find available freemium device for testing
deviceFinder = DeviceFinder(testdroid_username, testdroid_password)
testdroid_device = ""
# Safari testing iPad 3 freemium device not yet supported as it is iOS 8.2 device
while testdroid_device == "" or testdroid_device == "iPad 3 A1416 8.2":
    testdroid_device = deviceFinder.available_free_ios_device()
    
print "Starting Appium test using device '%s'" % testdroid_device

desired_capabilities_cloud = {}
desired_capabilities_cloud['testdroid_username'] = testdroid_username
desired_capabilities_cloud['testdroid_password'] = testdroid_password
desired_capabilities_cloud['testdroid_target'] = 'safari'
desired_capabilities_cloud['testdroid_project'] = 'Appium Safari Demo'
desired_capabilities_cloud['testdroid_testrun'] = 'TestRun A'
desired_capabilities_cloud['testdroid_device'] = testdroid_device
desired_capabilities_cloud['platformName'] = 'iOS'
desired_capabilities_cloud['deviceName'] = 'iOS Device'
desired_capabilities_cloud['browserName'] = 'Safari'

desired_caps = desired_capabilities_cloud;

log ("Will save screenshots at: " + screenshotDir)

# Set up webdriver
log ("WebDriver request initiated. Waiting for response, this typically takes 2-3 mins")
driver = webdriver.Remote(appium_Url, desired_caps)
driver.implicitly_wait(60)

# Need to change context?
log ("Contexts: " + str(driver.contexts))
#webview = driver.contexts[-1] # -1 = Use the last index
#driver.switch_to.context(webview)

log ("Loading page http://testdroid.com")
driver.get("http://testdroid.com")

# Sleep 5 seconds to make sure the device is ready
sleep(5)

# Finding element xpath, id or name is simple with Appium GUI application's inspector.
log ("Finding menu button")
elem = driver.find_element_by_xpath('//body[1]/header[1]/div[1]/div[1]/button[1]')
log ("elem: " + elem.text)

# This element isn't displayed on bigger screens
if elem.is_displayed():
    log ("Clicking menu button")
    elem.click()
else:
    log ("Menu button not found, skipping menu button.")

log ("Finding 'Products' button")
elem = driver.find_element_by_xpath('//body[1]/header[1]/div[1]/nav[1]/ul[1]/li[2]')
log ("Clicking 'Products' button")
elem.click()

log ("Finding 'Testdroid' banner")
elem = driver.find_element_by_xpath('//body[1]/header[1]/div[1]/div[1]/a[1]/img[1]')
log ("Clicking 'Testdroid' banner")
elem.click()

log ("quitting")
driver.quit()