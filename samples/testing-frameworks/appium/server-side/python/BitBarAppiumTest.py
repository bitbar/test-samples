# -*- coding: UTF-8 -*-

#
# Copyright(C) 2023 SmartBear Software
#
#
# NOTE: This is very much work in progress
#
__author__ = 'Henri Kivelä <henri.kivela@bitbar.com>, Lasse Häll <lasse.hall@bitbar.com>'


import os
import pprint
import sys
import time
import unittest

from appium import webdriver


def log(msg):
    header = ''
    if os.environ.get('APPIUM_DEVICE'):
        header = f"[{os.environ.get('APPIUM_DEVICE')}]"
    print(f'{header} {time.strftime("%H:%M:%S")}: {msg}')
    sys.stdout.flush()


class BitBarAppiumTest(unittest.TestCase):
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

    def setUp(self, appium_url='http://localhost:4723/wd/hub', platform_name=None, bundle_id=None,
              application_file=None, browser_name=None, application_package=None, screenshot_dir=None,
              application_activity=None, automation_name=None, noReset=None, fullReset=None):
        self.appium_url = os.environ.get('APPIUM_URL') or appium_url
        self.platform_name = platform_name or os.environ.get('APPIUM_PLATFORM') or 'Android'

        self.bundle_id = bundle_id or os.environ.get('APPIUM_BUNDLEID')

        self.automation_name = automation_name or os.environ.get('APPIUM_AUTOMATION')

        self.application_file = application_file or os.environ.get('APPIUM_APPFILE')
        self.browser_name = browser_name or os.environ.get('APPIUM_BROWSER')

        self.application_package = application_package or os.environ.get('APPIUM_PACKAGE')
        self.application_activity = application_activity or os.environ.get('APPIUM_ACTIVITY')

        self.device_name = os.environ.get('APPIUM_DEVICE') or self.device_name

        if screenshot_dir:
            self.set_screenshot_dir(screenshot_dir)
        else:
            self.set_screenshot_dir(f'{os.getcwd()}/screenshots')
        self.fullReset = fullReset or False
        self.noReset = noReset or True
        # Initialize WebDriver
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
        log(f'Will save screenshots at: {screenshot_dir}')
        self.screenshot_dir = screenshot_dir
        if not os.path.exists(screenshot_dir):
            log(f'Creating directory {screenshot_dir}')
            os.mkdir(self.screenshot_dir)

    def get_desired_capabilities(self):
        capabilities = {
            'platformName': self.platform_name,
            'appium:automationName': self.automation_name,
            'appium:deviceName': self.device_name
        }
        if self.bundle_id:
            log(f'Using bundleId {self.bundle_id}')
            capabilities['appium:bundleId'] = self.bundle_id
        if self.application_file:
            log(f'Using application file {self.application_file}')
            capabilities['appium:app'] = self.application_file
        if self.browser_name:
            log(f'Using mobile browser {self.browser_name}')
            capabilities['browserName'] = self.browser_name
        if self.application_package:
            capabilities['appium:appPackage'] = self.application_package
        if self.application_activity:
            capabilities['appium:appActivity'] = self.application_activity

        log(pprint.pformat(capabilities))
        return capabilities

    def get_driver(self):
        if self.driver:
            return self.driver
            # set up WebDriver
        log(f'Connecting WebDriver to {self.appium_url}')
        self.driver = webdriver.Remote(self.appium_url, self.get_desired_capabilities())
        # Wait max 30 seconds for elements
        self.driver.implicitly_wait(30)

        log('WebDriver response received')
        return self.driver

    def isAndroid(self):
        return self.platform_name and self.platform_name.upper() == 'ANDROID'

    def isIOS(self):
        return self.platform_name and self.platform_name.upper() == 'IOS'
