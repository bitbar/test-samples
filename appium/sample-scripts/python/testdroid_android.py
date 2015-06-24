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

##
## IMPORTANT: Set the following parameters.
## You can set the parameters outside the script with environment variables.
## If env var is not set the string after or is used.
##
screenshotDir= os.environ.get('TESTDROID_SCREENSHOTS') or "/absolute/path/to/desired/directory"
testdroid_url = os.environ.get('TESTDROID_URL') or "https://cloud.testdroid.com"
testdroid_username = os.environ.get('TESTDROID_USERNAME') or "user@email.com"
testdroid_password = os.environ.get('TESTDROID_PASSWORD') or "password"
appium_Url = os.environ.get('TESTDROID_APPIUM_URL') or 'http://appium.testdroid.com/wd/hub'

# Device can be manually defined, by device name found from Testdroid Cloud
# testdroid_device = "Asus Memo Pad 8 K011"

#DeviceFinder can be used to find available free device for testing
deviceFinder = DeviceFinder(username=testdroid_username, password=testdroid_password, url=testdroid_url)
testdroid_device = ""
while testdroid_device == "":
    testdroid_device = deviceFinder.available_free_android_device()

print "Starting Appium test using device '%s'" % testdroid_device

desired_capabilities_cloud = {}
desired_capabilities_cloud['testdroid_username'] = testdroid_username
desired_capabilities_cloud['testdroid_password'] = testdroid_password
desired_capabilities_cloud['testdroid_target'] = 'Android'
desired_capabilities_cloud['testdroid_project'] = os.environ.get('TESTDROID_PROJECT') or 'Appium Android demo'
desired_capabilities_cloud['testdroid_testrun'] = os.environ.get('TESTDROID_TESTRUN') or 'My testrun'
desired_capabilities_cloud['testdroid_device'] = testdroid_device
desired_capabilities_cloud['testdroid_app'] = 'sample/BitbarSampleApp.apk'
#desired_capabilities_cloud['app'] = '/absolut/path/to/your/application.apk'
desired_capabilities_cloud['platformName'] = 'Android'
desired_capabilities_cloud['deviceName'] = 'Android Phone'
desired_capabilities_cloud['appPackage'] = 'com.bitbar.testdroid'
desired_capabilities_cloud['appActivity'] = '.BitbarSampleApplicationActivity'

desired_caps = desired_capabilities_cloud;

log ("Will save screenshots at: " + screenshotDir)

# set up webdriver
log ("WebDriver request initiated. Waiting for response, this typically takes 2-3 mins")
driver = webdriver.Remote(appium_Url, desired_caps)
log ("WebDriver response received")

log ("Activity-1")
log ("  Getting device screen size")
print driver.get_window_size()

sleep(2) # always sleep before taking screenshot to let transition animations finish
log ("  Taking screenshot: 1_appLaunch.png")
driver.save_screenshot(screenshotDir + "/1_appLaunch.png")

log ("  Typing in name")
elems=driver.find_elements_by_class_name('android.widget.EditText')
log ("  info: EditText:" + `len(elems)`)
log ("  Filling in name")
elems[0].click()
elems[0].send_keys("Testdroid User")
sleep(2)
log ("  Taking screenshot: 2_nameTyped.png")
driver.save_screenshot(screenshotDir + "/2_nameTyped.png")
log ("  Hiding keyboard")
driver.back()
sleep(2)
log ("  Taking screenshot: 3_nameTypedKeyboardHidden.png")
driver.save_screenshot(screenshotDir + "/3_nameTypedKeyboardHidden.png")

log ("  Clicking element 'Buy 101 devices'")
elem = driver.find_element_by_name('Buy 101 devices')
elem.click()
sleep(2)
log ("  Taking screenshot: 4_clickedButton1.png")
driver.save_screenshot(screenshotDir + "/4_clickedButton1.png")

log ("  Clicking Answer")
elem = driver.find_element_by_name('Answer')
elem.click()
sleep(2)
log ("  Taking screenshot: 5_answer.png")
driver.save_screenshot(screenshotDir + "/5_answer.png")

log ("Navigating back to Activity-1")
driver.back()
sleep(2)
log ("  Taking screenshot: 6_mainActivity.png")
driver.save_screenshot(screenshotDir + "/6_mainActivity.png")

log ("  Clicking element 'Use Testdroid Cloud'")
elem = driver.find_element_by_name('Use Testdroid Cloud')
elem.click()
sleep(2)
log ("  Taking screenshot: 7_clickedButton2.png")
driver.save_screenshot(screenshotDir + "/7_clickedButton2.png")

log ("  Clicking Answer")
elem = driver.find_element_by_name('Answer')
elem.click()
sleep(2)
log ("  Taking screenshot: 8_answer.png")
driver.save_screenshot(screenshotDir + "/8_answer.png")

log ("Quitting")
driver.quit()
