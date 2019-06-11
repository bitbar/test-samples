# -*- coding: UTF-8 -*-

#
# Copyright(C) 2019 Bitbar Technologies Oy
#

import codecs
import os
import unittest
import xmlrunner
from selenium import webdriver
from selenium.common.exceptions import WebDriverException
from selenium.webdriver.common.by import By
from BitbarSeleniumTest import BitbarSeleniumTest, log

class BitbarSampleWebTest(BitbarSeleniumTest):
    def setUp(self):
        # BitbarSampleWebTest loads settings/capabilities from environment variables
        super(BitbarSampleWebTest, self).setUp()

    def testSample(self):
        driver = self.get_driver()
        log("Start!")

        try:
            driver.get("https://bitbar.com/")
            print driver.title

            driver.get_screenshot_as_file(self.screenshot_dir+'frontpage.png')
            log("Finish!")
        except WebDriverException:
            log("Failed!")

if __name__ == '__main__':
    unittest.main(testRunner=xmlrunner.XMLTestRunner(output='test-reports'))
