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
##
testdroid_url = os.environ.get('TESTDROID_URL') or "https://cloud.testdroid.com"
appium_url = os.environ.get('TESTDROID_APPIUM_URL') or 'http://appium.testdroid.com/wd/hub'
testdroid_username = os.environ.get('TESTDROID_USERNAME') or "user@email.com"
testdroid_password = os.environ.get('TESTDROID_PASSWORD') or "password"
screenshot_dir = os.environ.get('TESTDROID_SCREENSHOTS') or "/absolute/path/to/desired/directory"
testdroid_project_name = os.environ.get('TESTDROID_PROJECT') or "Appium iOS demo"
testdroid_testrun_name = os.environ.get('TESTDROID_TESTRUN') or "My testrun"

# Options to select device
# 1) Set environment variable TESTDROID_DEVICE
# 2) Set device name to this python script
# 3) Do not set #1 and #2 and let DeviceFinder to find free device for you

deviceFinder = None
testdroid_device = os.environ.get('TESTDROID_DEVICE') or ""

if testdroid_device == "":
    deviceFinder = DeviceFinder(username=testdroid_username, password=testdroid_password, url=testdroid_url)
    # Loop will not exit until free device is found
    while testdroid_device == "":
        testdroid_device = deviceFinder.available_free_ios_device()

print "Starting Appium test using device '%s'" % testdroid_device

desired_capabilities_cloud={
        'testdroid_username': testdroid_username,
        'testdroid_password': testdroid_password,
        'testdroid_project': testdroid_project_name,
        'testdroid_target': 'ios',
        'testdroid_description': 'Appium project description',
        'testdroid_testrun': testdroid_testrun_name,
        'testdroid_device': testdroid_device,
        'testdroid_app': 'sample/BitbarIOSSample.ipa',
        'platformName': 'iOS',
        'deviceName': 'iPhone device',
        'bundleId': 'com.bitbar.testdroid.BitbarIOSSample',
}
    
# set up webdriver
log ("WebDriver request initiated. Waiting for response, this typically takes 2-3 mins")

driver = None

try:
    driver = webdriver.Remote(command_executor=appium_url,desired_capabilities=desired_capabilities_cloud)
    log ("WebDriver response received")

    # view1
    log ("view1: Finding buttons")
    buttons = driver.find_elements_by_class_name('UIAButton')
    log ("view1: Clicking button [0] - RadioButton 1")
    buttons[0].click()

    log ("view1: Typing in textfield[0]: Testdroid user")
    elem = driver.find_element_by_class_name('UIATextField')
    elem.clear()
    elem.send_keys('Testdroid user')

    y = 0.95
    log ("view1: Tapping at position (384, 0.95) - Estimated position of SpaceBar")
    driver.execute_script("mobile: tap",{"touchCount":"1","x":"0.5","y":y})

    log ("view1: Taking screenshot screenshot1.png")
    driver.save_screenshot(screenshot_dir + "/screenshot1.png")
    #driver.execute_script('UIATarget.localTarget().captureScreenWithName("screenshot1");') # takes screenshot at server-side

    log ("view1: Hiding Keyboard")
    driver.find_element_by_name("Return").click()
    #driver.execute_script('UIATarget.localTarget().frontMostApp().keyboard().buttons()["Hide keyboard"].tap();')

    log ("view1: Taking screenshot screenshot2.png")
    driver.save_screenshot(screenshot_dir + "/screenshot2.png")

    log ("view1: Clicking button[6] - OK  Button")
    buttons[6].click()

    log ("view2: Taking screenshot screenshot3.png")
    driver.save_screenshot(screenshot_dir + "/screenshot3.png")

    # view2
    log ("view2: Finding buttons")
    buttons = driver.find_elements_by_class_name('UIAButton')
    log ("view2: Clicking button[0] - Back/OK button")
    buttons[0].click()

    # view 1
    log ("view1: Finding buttons")
    buttons = driver.find_elements_by_class_name('UIAButton')
    log ("view1: Clicking button[2] - RadioButton 2")
    buttons[2].click()

    log ("view1: Clicking button[6] - OK Button")
    buttons[6].click()

    log ("view1: Taking screenshot screenshot4.png")
    driver.save_screenshot(screenshot_dir + "/screenshot4.png")

    log ("view1: Sleeping 3 before quitting webdriver")
    sleep(3)

finally:
    log ("view1: Quitting WebDriver")
    driver.quit()
