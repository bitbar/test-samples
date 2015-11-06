#
# Example how to upload application to Testdroid Cloud before Appium test
#

import requests
import base64
import os
import json

# update your apiKey value here or export it to your environment
# and you app path if you want to upload your own app instead of demo app
api_key = os.environ.get('TESTDROID_APIKEY') or "<api key value here>"
upload_url = 'http://appium.testdroid.com/upload'
myfile = os.environ.get('TESTDROID_APP_PATH') or '../../../apps/builds/Testdroid.apk'

def build_headers():
    hdrs = {'Authorization' : 'Basic %s' % base64.b64encode(api_key+":"),
            'Accept' : 'application/json' }
    return hdrs

files = {'file': ('Testdroid.apk', open(myfile, 'rb'), 'application/octet-stream')}
r = requests.post(upload_url, files=files, headers=build_headers())

if  "successful" in r.json()['value']['message']:
    print "Filename to use in testdroid capabilities in your test: {}".format(r.json()['value']['uploads']['file'])
else:
    print "Upload response: \n{}".format(r.json())
