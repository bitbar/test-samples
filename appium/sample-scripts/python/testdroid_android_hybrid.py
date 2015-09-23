##
## For help on setting up your machine and configuring this TestScript go to
## http://help.testdroid.com/customer/portal/topics/631129-appium/articles
##

import os
import sys
import time
from time import sleep
from selenium import webdriver


appium_Url = 'http://appium.testdroid.com/wd/hub';

##
## IMPORTANT: Set the following parameters.
##
screenshotDir= "/absolute/path/to/desired/directory"
testdroid_username = "username@example.com"
testdroid_password = "p4s$w0rd"
testdroid_device = "Dell Venue 7 3730" # Example device. Change if you desire.

def log(msg):
    print (time.strftime("%H:%M:%S") + ": " + msg)

screenShotCount = 1
def screenshot(name):
    global screenShotCount
    screenshotName = str(screenShotCount) + "_" + name + ".png" 
    log ("Taking screenshot: " + screenshotName)
    sleep(1) # wait for animations to complete before taking screenshot
    driver.save_screenshot(screenshotDir + "/" + screenshotName)
    screenShotCount += 1

desired_capabilities_cloud = {}
desired_capabilities_cloud['testdroid_username'] = testdroid_username
desired_capabilities_cloud['testdroid_password'] = testdroid_password
desired_capabilities_cloud['testdroid_target'] = 'chrome'
desired_capabilities_cloud['testdroid_project'] = 'Appium Android Hybrid Demo'
desired_capabilities_cloud['testdroid_testrun'] = 'TestRun 1'
desired_capabilities_cloud['testdroid_device'] = testdroid_device
desired_capabilities_cloud['testdroid_app'] = 'sample/testdroid-sample.apk'
desired_capabilities_cloud['platformName'] = 'Android'
desired_capabilities_cloud['deviceName'] = 'Android Phone'
desired_capabilities_cloud['automationName'] = 'selendroid'
desired_capabilities_cloud['appPackage'] = 'com.testdroid.sample.android'
desired_capabilities_cloud['appActivity'] = '.MA_MainActivity'

desired_caps = desired_capabilities_cloud;

log ("Will save screenshots at: " + screenshotDir)

# set up webdriver
log ("WebDriver request initiated. Waiting for response, this typically takes 2-3 mins")
driver = webdriver.Remote(appium_Url, desired_caps)
log ("WebDriver response received")

log ("Activity-1")
log ("  Getting device screen size")
print driver.get_window_size()

screenshot("appLaunch")

log('clicking button "hybrid app"')
element=driver.find_element_by_id('com.testdroid.sample.android:id/mm_b_hybrid')
element.click()
screenshot('hybridActivity')

log('Typing in the Url')
element=driver.find_element_by_id('com.testdroid.sample.android:id/hy_et_url')
element.send_keys("http://testdroid.com")
screenshot('urlTyped')

log('hiding keyboard')
driver.back()
screenshot('keyboardHidden')

log('clicking Load url button')
element=driver.find_element_by_id('com.testdroid.sample.android:id/hy_ib_loadUrl')
element.click()
log('waiting 10 seconds for url to load completely')
sleep(10)
screenshot('webPageLoaded')

log('finding window handles')
handles = driver.window_handles
for handle in driver.window_handles:
    log("   > Window: " + handle)

log('switching to webview')
webview = handles[1]
driver.switch_to_window(webview)

log ("Clicking 'Products'")
elem = driver.find_element_by_xpath('//*[@id="menu"]/ul/li[1]/a')
elem.click()
sleep(5)
screenshot('webview_products')

log ("Clicking 'Learn More'")
elem = driver.find_element_by_xpath('//*[@id="products"]/div[1]/div/div[1]/div[3]/a')
elem.click()
sleep(5)
screenshot('webview_learnmore')

log ("Clicking 'Supported Frameworks'")
elem = driver.find_element_by_xpath('//*[@id="topBox"]/div[1]/div/a[2]')
elem.click()
sleep(5)
screenshot('webview_supportedframeworks')

log('finding window handles')
handles = driver.window_handles
for handle in driver.window_handles:
    log("   > Window: " + handle)

log('switching back to native app')
nativeapp = handles[0]
driver.switch_to_window(nativeapp)

log('goint back')
driver.back()
screenshot('launchScreen')

sys.exit()
