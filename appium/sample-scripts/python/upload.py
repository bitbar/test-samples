#
# Example how to upload application to Testdroid Cloud before Appium test
#

import requests
import base64
import os
import json
import argparse


class UploadApp():
    def __init__(self):
        self.myfile = os.environ.get('TESTDROID_APP_PATH') or '../../../apps/builds/Testdroid.apk'
        self.upload_url = os.environ.get('TESTDROID_UPLOAD_URL') or 'http://appium.testdroid.com/upload'
        self.api_key = os.environ.get('TESTDROID_APIKEY')


    def parse_args(self):
        parser = argparse.ArgumentParser(description='Set needed environment variables')
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


    def build_headers(self):
        hdrs = {'Authorization' : 'Basic %s' % base64.b64encode(self.api_key+":"),
                'Accept' : 'application/json' }
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
