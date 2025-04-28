# -*- coding: UTF-8 -*-

#
# Copyright(C) 2019 Bitbar Technologies Oy
#

import os
import sys
import time
import unittest
from selenium import webdriver

def log(msg):
    print ("%s: %s" % (time.strftime("%H:%M:%S"), msg))
    sys.stdout.flush()

class BitbarSeleniumTest(unittest.TestCase):
    driver = None

    hub_url = 'https://us-west-desktop-hub.bitbar.com/wd/hub'
    screenshot_dir = None
    desired_caps = {
        'platform': None,
        'osVersion': None,
        'browserName': None,
        'version': None,
        'resolution': None,
        'bitbar_apiKey': '<insert your BitBar API key here>',
        'bitbar_project': None,
        'bitbar_testrun': None,
    }

    def setUp(self, hub_url=hub_url, desired_caps = desired_caps, screenshot_dir=screenshot_dir):
        self.hub_url = os.environ.get('HUB_URL') or hub_url

        self.desired_caps = desired_caps

        if screenshot_dir:
            self.set_screenshot_dir(screenshot_dir)
        else:
            self.set_screenshot_dir('%s/screenshots/' % (os.getcwd()))

    def tearDown(self):
        self.driver.quit()

    def set_screenshot_dir(self, screenshot_dir):
        self.screenshot_dir = screenshot_dir
        if not os.path.exists(screenshot_dir):
            log('Creating directory %s' % screenshot_dir)
            os.mkdir(self.screenshot_dir)

    def get_driver(self):
        if self.driver:
            self.driver = getattr(webdriver, self.driver)()
            return self.driver
        log("Connecting WebDriver to %s" % self.hub_url)
        self.driver = webdriver.Remote(self.hub_url, self.desired_caps)
        self.driver.implicitly_wait(30)
        log("WebDriver response received")
        return self.driver
