import os
import argparse
import unittest
import importlib


class TestdroidTestRunner():
    def __init__(self):
        self.variables = {"TESTDROID_APIKEY":"", 
                               "TESTDROID_SCREENSHOTS":"",
                               "TESTDROID_APP":"",
                               "TESTDROID_DEVICE":""}
        self.available_tests = ['testdroid_chrome', 'testdroid_android',
                                    'testdroid_safari', 'testdroid_ios',
                                    'testdroid_android_hybrid']


    def parse_args(self):
        parser = argparse.ArgumentParser(description='Set needed environment variables for sample Testdroid tests')
        parser.add_argument('-k', '--apikey', type=str, required=True, help="User's apiKey to identify to cloud")
        parser.add_argument('-s', '--screenshot_dir', type=str, required=True, help="Path to screenshot directory")
        parser.add_argument('-t', '--test', type=str, required=True, choices=self.available_tests, help="The test file to be run")
        parser.add_argument('-a', '--app', type=str, required=False, help="Name of app uploaded to cloud using upload.py script. Or ""latest"" to reuse last used app. Mandatory for app testing")
        group_device = parser.add_mutually_exclusive_group()
        group_device.add_argument('--device', type=str, required=False, help="Full name of device to use")
        group_device.add_argument('--device_group_id', type=str, required=False, help="The id of the Testdroid Cloud device group from where available devices are to be searched from")
        parser.add_argument('-p', '--project', type=str, required=False, help="The name of the cloud project")
        parser.add_argument('-r', '--run_name', type=str, required=False, help="The name of the test run")
        parser.add_argument('-u', '--url', type=str, required=False, help="Testdroid Cloud url where test project and devices are found")
        parser.add_argument('-i', '--appium_url', type=str, required=False, help="Testdroid Appium url")
        parser.add_argument('--bundle_id', type=str, required=False, help="Mandatory bundleID when running iOS tests")
        parser.add_argument('--app_package', type=str, required=False, help="Mandatory app package path for native Android tests")
        parser.add_argument('--app_activity', type=str, required=False, help="Mandatory main activity for native Android tests")
        parser.add_argument('--cmd_timeout', type=str, required=False, help="New command timeout value, default is 60s")
        parser.add_argument('--test_timeout', type=str, required=False, help="Maximum test duration, defaults to 600s")



        args = parser.parse_args()
        # mandatory params
        self.variables['TESTDROID_APIKEY'] = str(args.apikey)
        self.variables['TESTDROID_SCREENSHOTS'] = str(args.screenshot_dir)
        self.selected_test = args.test

        # optional parameters
        if args.app:
            self.variables['TESTDROID_APP'] = str(args.app)

        if args.device:
            self.variables['TESTDROID_DEVICE'] = str(args.device)

        if args.device_group_id:
            self.variables['TESTDROID_DEVICE_GROUP'] = str(args.device_group_id)
        if args.project:
            self.variables["TESTDROID_PROJECT"] = args.project
            
        if args.run_name:
            self.variables["TESTDROID_TESTRUN"] = args.run_name

        if args.url:
            self.variables["TESTDROID_URL"] = args.url

        if args.appium_url:
            self.variables["TESTDROID_APPIUM_URL"] = args.appium_url

        if args.bundle_id:
            self.variables["TESTDROID_BUNDLE_ID"] = args.bundle_id

        if args.app_package:
            self.variables["TESTDROID_APP_PACKAGE"] = args.app_package

        if args.app_activity:
            self.variables["TESTDROID_ACTIVITY"] = args.app_activity

        if args.cmd_timeout:
            self.variables["TESTDROID_CMD_TIMEOUT"] = args.cmd_timeout

        if args.test_timeout:
            self.variables["TESTDROID_TEST_TIMEOUT"] = args.test_timeout


        # export variables + values
        for key in self.variables:
            # print "  {} : {}".format(key, self.variables[key])
            os.environ[key] = str(self.variables[key])


    def print_values(self):
        print "Stored environment variables:"
        for key in self.variables.keys():
            print "  {} : {}".format(key, os.environ.get(key, 'Not set'))


    def run_selected_test(self):
        module = importlib.import_module(self.selected_test)

        test = module.initialize()
        suite = unittest.TestLoader().loadTestsFromTestCase(test)
        unittest.TextTestRunner(verbosity=2).run(suite)


if __name__ == '__main__':
    runner = TestdroidTestRunner()
    runner.parse_args()
#    runner.print_values()
    runner.run_selected_test()
