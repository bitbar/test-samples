# -*- coding: UTF-8 -*-

#
# Copyright(C) 2014 Bitbar Technologies Oy
#
#
# NOTE: This is very much work in progress
#
__author__ = 'Henri Kivelä <henri.kivela@bitbar.com>, Lasse Häll <lasse.hall@bitbar.com>'


import os
import sys
import time
import subprocess
import unittest
from appium import webdriver
from selenium.common.exceptions import WebDriverException
import pprint
import tempfile

def log(msg):
    header = ''
    if os.environ.get('APPIUM_DEVICE'):
        header = '[%s] ' % os.environ.get('APPIUM_DEVICE')
    print ("%s%s: %s" % (header, time.strftime("%H:%M:%S"),msg))
    sys.stdout.flush()

class TestdroidAppiumTest(unittest.TestCase):

    # Appium
    driver = None
    platform_name = None
    automation_name = None
    appium_url = None
    application_file = None
    device_name = None
    browser_name = None

    screenshot_dir = None
    # iOS
    bundle_id = None
    # Android
    application_package = None
    application_activity = None


    # Automatically resolved
    resolution = None

    def setUp(self, appium_url='http://localhost:4723/wd/hub', platform_name=None, bundle_id = None, application_file=None, browser_name=None, application_package=None, screenshot_dir=None,
                 application_activity=None, automation_name=None):
        self.appium_url = os.environ.get('APPIUM_URL') or appium_url
        self.platform_name = platform_name or os.environ.get('APPIUM_PLATFORM') or 'Android'

        self.bundle_id = bundle_id or os.environ.get('APPIUM_BUNDLEID')

        self.automation_name = automation_name or os.environ.get('APPIUM_AUTOMATION')

        self.application_file = application_file or os.environ.get('APPIUM_APPFILE')
        self.browser_name = browser_name or os.environ.get('APPIUM_BROWSER')

        self.application_package = application_package or os.environ.get('APPIUM_PACKAGE')
        self.application_activity = application_activity or os.environ.get('APPIUM_ACTIVITY')
        
        self.device_name = os.environ.get('APPIUM_DEVICE') or device_name

        if screenshot_dir:
            self.set_screenshot_dir(screenshot_dir)
        else:
            self.set_screenshot_dir('%s/screenshots' % (os.getcwd()))
        # Initialize webdriver
        self.get_driver()

    def tearDown(self):
        self.driver.quit()

    def set_application_file(self, file):
        self.application_file = file

    def set_application_package(self, application_package):
        self.application_package = application_package

    def set_application_activity(self, application_activity):
        self.application_activity = application_activity

    def set_screenshot_dir(self, screenshot_dir):
        log ("Will save screenshots at: " + screenshot_dir)
        self.screenshot_dir = screenshot_dir
        if not os.path.exists(screenshot_dir):
            log('Creating directory %s' % screenshot_dir)
            os.mkdir(self.screenshot_dir)

    def get_desired_capabilities(self):
        desired_caps = {}
        desired_caps['platformName'] = self.platform_name
        desired_caps['automationName'] = self.automation_name
        if self.bundle_id:
            log('Using bundleId %s' % self.bundle_id)
            desired_caps['bundleId'] = self.bundle_id
        if self.application_file:
            log('Using application file %s' % self.application_file)
            desired_caps['app'] = self.application_file
        if self.browser_name:
            log('Using mobile browser %s' % self.browser_name)
            desired_caps['browserName'] = self.browser_name
        desired_caps['deviceName'] = self.device_name
        if self.application_package:
            desired_caps['appPackage'] = self.application_package
        if self.application_activity:
            desired_caps['appActivity'] = self.application_activity

        log(pprint.pformat(desired_caps))
        return desired_caps

    def get_driver(self):
        if self.driver:
            return self.driver
            # set up webdriver
        log("Connecting WebDriver to %s" % self.appium_url)
        self.driver = webdriver.Remote(self.appium_url, self.get_desired_capabilities())
        # Wait max 30 seconds for elements
        self.driver.implicitly_wait(30)

        log("WebDriver response received")
        return self.driver


    def isAndroid(self):
        return self.platform_name and self.platform_name.upper() == 'ANDROID'

    def isIOS(self):
        return self.platform_name and self.platform_name.upper() == 'IOS'

    def isSelendroid(self):
        if self.automation_name:
            return self.automation_name.upper() == 'SELENDROID'
        return False
