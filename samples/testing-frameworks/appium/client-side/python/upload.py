#
# Example how to upload application to Bitbar Cloud before Appium test
#

import argparse
import os
import requests
import sys
from requests.auth import HTTPBasicAuth

class UploadApp():
    def __init__(self):
        self.myfile = os.environ.get('BITBAR_APP_PATH') or \
            '../../../../../apps/android/bitbar-sample-app.apk'
        self.upload_url = os.environ.get('BITBAR_UPLOAD_URL') or \
            'https://cloud.bitbar.com/api/v2/me/files'
        # Provide mandatory API key with this env var or with -k/--apikey flag
        self.api_key = os.environ.get("BITBAR_APIKEY") or ""

    def parse_args(self):
        parser = argparse.ArgumentParser(description='Upload a mobile app to Bitbar Cloud and get a handle to it')
        parser.add_argument('-k', '--apikey', type=str, required=False, help="User's apiKey to identify to cloud, or set environment variable BITBAR_APIKEY")
        parser.add_argument('-a', '--app_path', type=str, required=False, help="Path to app to upload or set environment variable BITBAR_APP_PATH. Current value is: '{}'".format(self.myfile))
        parser.add_argument('-u', '--url', type=str, required=False, help="Bitbar Cloud url to upload app or set environment variable BITBAR_UPLOAD_URL. Current value is: '{}'".format(self.upload_url))

        args = parser.parse_args()
        if args.app_path:
            self.myfile = args.app_path
        if args.url:
            self.upload_url = args.url
        if args.apikey:
            self.api_key = args.apikey

        # Sanity checks
        if len(self.api_key) == 0:
            print("ERROR: API key is missing. Provide BITBAR_APIKEY env var or -k/--apikey <APIKEY> flag.")
            sys.exit(1)

    def upload_app(self):
        self.parse_args()
        files = {'file': (os.path.basename(self.myfile), open(self.myfile, 'rb'), 'application/octet-stream')}
        r = requests.post(self.upload_url, files=files, auth=HTTPBasicAuth(self.api_key, ''))

        try:
            print("File id to use in bitbar capabilities in your test: {}".format(r.json()['id']))
        except ValueError:
            print("Upload response: \n{}".format(r))


if __name__ == '__main__':
    up = UploadApp()
    up.upload_app()
