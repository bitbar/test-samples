##
## Example script for parallel selenium tests
##
import unittest
import xmlrunner
from time import time, sleep
from TestdroidAppiumTest import TestdroidAppiumTest, log

class BitbarSampleAppTest(TestdroidAppiumTest):
    def setUp(self):
        # TestdroidAppiumTest takes settings (local or cloud) from environment variables
        super(BitbarSampleAppTest, self).setUp()

    # Test start.
    def test_the_app(self):
        driver = self.get_driver() # Initialize Appium connection to device

        starttime = time()
        counter = 1
        log("Start!")
        while True:
            driver.save_screenshot(self.screenshot_dir + "/%03d.png" % counter)
            sleep(5) # how long should we wait between each screenshot?
            counter += 1
            if int(time() - starttime) > 600: # how many seconds should the loop last?
                break

    # Test end.

if __name__ == '__main__':
    unittest.main(testRunner=xmlrunner.XMLTestRunner(output='test-reports'))
