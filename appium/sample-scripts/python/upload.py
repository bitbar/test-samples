#
# Example how to upload application to Testdroid Cloud before Appium test
#

import requests
import base64

username = 'USERNAME'
password = 'PASSWORD'
upload_url = 'http://appium.testdroid.com/upload'
myfile = '../../../apps/builds/Testdroid.apk'

def build_headers():
  return { 'Authorization' : 'Basic %s' % base64.b64encode(username+":"+password) }

files = {'file': ('Testdroid.apk', open(myfile, 'rb'), 'application/octet-stream')}
r = requests.post(upload_url, files=files, headers=build_headers())
print r.text

