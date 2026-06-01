# -*- coding: utf-8 -*-
from robot.api import logger
from robot.libraries.BuiltIn import BuiltIn
from selenium.webdriver.common.keys import Keys
import time


class ExampleLibraryException(Exception):
    """It is a good practice to throw library-specific exceptions so
    that you know where the exception is coming from"""
    pass

class ExampleLibrary(object):
    """Libraries should be documented according to Robot Framework User Guide"""

    def library_keyword(self):
        """Document keywords as well."""
        return True

    def _driver(self):
        appium = BuiltIn().get_library_instance('AppiumLibrary')
        return getattr(appium, '_current_application')()

    def hide_ios_keyboard(self):
        """Dismiss iOS keyboard reliably across iOS versions (incl. iOS 26+)."""
        driver = self._driver()

        def keyboard_visible():
            try:
                result = driver.execute_script('mobile: isKeyboardShown')
                result = bool(result)
                logger.debug('mobile: isKeyboardShown returned: %s', result)
                return result
            except Exception as e:
                logger.warning('mobile: isKeyboardShown failed: %s', e)
                return None

        def is_hidden():
            visible = keyboard_visible()
            return visible is False

        logger.info('Checking if iOS keyboard is visible...')
        visible = keyboard_visible()

        if visible is False:
            logger.info('Keyboard is not visible, nothing to do.')
            return True

        logger.info('Keyboard visibility: %s. Trying dismiss strategies.', visible)

        logger.info('Sending Enter/Return to active element...')
        try:
            active = driver.switch_to.active_element
            active.send_keys(Keys.RETURN)
            time.sleep(0.4)
            if is_hidden():
                logger.info('Keyboard dismissed after sending Enter/Return to active element.')
                return True
        except Exception as e:
            logger.warning('Sending Enter/Return to active element failed: %s', e)

        logger.warning('Keyboard is still visible or visibility could not be confirmed: %s', visible)
        return False
