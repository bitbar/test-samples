import time
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common import by


class BitbarUtils:
    def __init__(self, dir):
        self.screenshot_count = 1
        self.screenshot_dir = dir
        self.driver = None

    def screenshot(self, name):
        """
        Take screenshot and store files to defined location, with numbering
        prefix

        :Args:
        - name - files are stored as #_name
        """
        screenshot_name = str(self.screenshot_count) + "_" + name + ".png"
        self.log("Taking screenshot: " + screenshot_name)
        # on Android, switching context to NATIVE_APP for screenshot taking to
        # get screenshots also stored to Bitbar device run view. After
        # screenshot switching back to WEBVIEW. Works ok for Safari too.
        orig_context = self.driver.current_context
        self.driver.switch_to.context("NATIVE_APP")
        self.driver.save_screenshot(self.screenshot_dir + "/" + screenshot_name)
        # only change context if originally context was WEBVIEW
        if orig_context is not None and orig_context not in self.driver.current_context:
            self.driver.switch_to.context(orig_context)

        self.screenshot_count += 1

    def wait_until_xpath_matches(self, xpath, timeout=10, step=1):
        """
        Search for specified xpath for defined period

        :Args:

        - xpath - the xpath to search for
        - timeout - duration in seconds to search for given xpath
        - step - how often to search run the search

        :Usage:
        self.wait_until_xpath_matches("//div[@id='example']", 15, 2)"
        """
        end_time = time.time() + timeout
        found = False
        while time.time() < end_time and not found:
            self.log("  Looking for xpath {}".format(xpath))
            try:
                elem = self.driver.find_element(by.By.XPATH, value=xpath)
                found = True
            except NoSuchElementException:
                found = False
            time.sleep(step)
        if not found:
            raise NoSuchElementException(
                "Element wiht xpath: '{}' not found in {}s".format(xpath, timeout)
            )
        return elem

    def update_driver(self, driver):
        self.driver = driver

    def log(self, msg):
        print(time.strftime("%H:%M:%S") + ": " + msg)
        return

    def sleep(self, duration):
        time.sleep(duration)
        return
