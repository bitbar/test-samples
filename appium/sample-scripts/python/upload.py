#
# Example how to upload application to Testdroid Cloud before Appium test
#

import argparse
import base64
import os
import requests
import sys


class UploadApp():
    def __init__(self):
        self.myfile = os.environ.get('TESTDROID_APP_PATH') or \
            '../../../apps/builds/BitbarSampleApp.apk'
        self.upload_url = os.environ.get('TESTDROID_UPLOAD_URL') or \
            'https://appium.bitbar.com/upload'
        # Provide mandatory API key with this env var or with -k/--apikey flag
        self.api_key = os.environ.get("TESTDROID_APIKEY") or ""

    def parse_args(self):
        parser = argparse.ArgumentParser(description='Upload a mobile app to Testdroid Cloud and get a handle to it')
        parser.add_argument('-k', '--apikey', type=str, required=False, help="User's apiKey to identify to cloud, or set environment variable TESTDROID_APIKEY")
        parser.add_argument('-a', '--app_path', type=str, required=False, help="Path to app to upload or set environment variable TESTDROID_APP_PATH. Current value is: '{}'".format(self.myfile))
        parser.add_argument('-u', '--url', type=str, required=False, help="Testdroid Cloud url to upload app or set environment variable TESTDROID_UPLOAD_URL. Current value is: '{}'".format(self.upload_url))

        args = parser.parse_args()
        if args.app_path:
            self.myfile = args.app_path
        if args.url:
            self.upload_url = args.url
        if args.apikey:
            self.api_key = args.apikey

        # Sanity checks
        if len(self.api_key) == 0:
            print "ERROR: API key is missing. Provide TESTDROID_APIKEY env var or -k/--apikey <APIKEY> flag."
            sys.exit(1)

    def build_headers(self):
        hdrs = {'Authorization': 'Basic %s' % base64.b64encode(self.api_key + ":"),
                'Accept': 'application/json'}
        return hdrs

    def upload_app(self):
        self.parse_args()
        files = {'file': (os.path.basename(self.myfile), open(self.myfile, 'rb'), 'application/octet-stream')}
        r = requests.post(self.upload_url, files=files, headers=self.build_headers())

        try:
            print "Filename to use in testdroid capabilities in your test: {}".format(r.json()['value']['uploads']['file'])
        except ValueError:
            print "Upload response: \n{}".format(r)


if __name__ == '__main__':
    up = UploadApp()
    up.upload_app()
