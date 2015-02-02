##
## For help on setting up your machine and configuring this TestScript go to
## http://help.testdroid.com/customer/portal/topics/631129-appium/articles
##

import os
import sys
import time
from time import sleep
from selenium import webdriver

def log(msg):
    print (time.strftime("%H:%M:%S") + ": " + msg)

appium_Url = 'http://appium.testdroid.com/wd/hub';

##
## IMPORTANT: Set the following parameters.
##
screenshotDir= "/absolute/path/to/desired/directory"
testdroid_username = "username@example.com"
testdroid_password = "p4s$w0rd"

testdroid_device = "iPad 3 A1416 7.0.4" # Example device. Change if you desire.

desired_capabilities_cloud={
        'testdroid_username': testdroid_username,
        'testdroid_password': testdroid_password,
        'testdroid_project': 'Appium iOS Project1',
        'testdroid_target': 'ios',
        'testdroid_description': 'Appium project description',
        'testdroid_testrun': 'Test Run 1',
        'testdroid_device': testdroid_device,
        'testdroid_app': 'sample/BitbarIOSSample.ipa',
        'platformName': 'iOS',
        'deviceName': 'iPhone device',
        'bundleId': 'com.bitbar.testdroid.BitbarIOSSample',
    }
    
# set up webdriver
log ("WebDriver request initiated. Waiting for response, this typically takes 2-3 mins")

driver = webdriver.Remote(command_executor=appium_Url,desired_capabilities=desired_capabilities_cloud)
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
driver.save_screenshot(screenshotDir + "/screenshot1.png")
#driver.execute_script('UIATarget.localTarget().captureScreenWithName("screenshot1");') # takes screenshot at server-side

log ("view1: Hiding Keyboard")
driver.find_element_by_name("Return").click()
#driver.execute_script('UIATarget.localTarget().frontMostApp().keyboard().buttons()["Hide keyboard"].tap();')

log ("view1: Taking screenshot screenshot2.png")
driver.save_screenshot(screenshotDir + "/screenshot2.png")

log ("view1: Clicking button[6] - OK  Button")
buttons[6].click()

log ("view2: Taking screenshot screenshot3.png")
driver.save_screenshot(screenshotDir + "/screenshot3.png")

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
driver.save_screenshot(screenshotDir + "/screenshot4.png")

log ("view1: Sleeping 3 before quitting webdriver")
sleep(3)

log ("view1: Quitting WebDriver")
driver.quit()