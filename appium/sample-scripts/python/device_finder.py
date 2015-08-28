# -*- coding: utf-8 -*-

import os, sys, requests, json, time, httplib
from optparse import OptionParser
from urlparse import urljoin
from datetime import datetime

class DeviceFinder:
    # Cloud URL (not including API path)
    url = None
    # Oauth access token
    access_token = None
    # Oauth refresh token
    refresh_token = None
    # Unix timestamp (seconds) when token expires
    token_expiration_time = None

    """ Full constructor with username and password
    """
    def __init__(self, username=None, password=None, url="https://cloud.testdroid.com", download_buffer_size=65536):
        self.username = username
        self.password = password
        self.cloud_url = url
        self.download_buffer_size = download_buffer_size

    """ Get Oauth2 token
    """
    def get_token(self):
        if not self.access_token:
            # TODO: refresh
            url = "%s/oauth/token" % self.cloud_url
            payload = {
                "client_id": "testdroid-cloud-api",
                "grant_type": "password",
                "username": self.username,
                "password": self.password
            }
            res = requests.post(
                                url,
                                data = payload,
                                headers = { "Accept": "application/json" }
                                )
            if res.status_code != 200:
                print "FAILED: Authentication or connection failure. Check Testdroid Cloud URL and your credentials."
                sys.exit(-1)

            reply = res.json()

            self.access_token = reply['access_token']
            self.refresh_token = reply['refresh_token']
            self.token_expiration_time = time.time() + reply['expires_in']
        elif self.token_expiration_time < time.time():
            url = "%s/oauth/token" % self.cloud_url
            payload = {
                "client_id": "testdroid-cloud-api",
                "grant_type": "refresh_token",
                "refresh_token": self.refresh_token
            }
            res = requests.post(
                                url,
                                data = payload,
                                headers = { "Accept": "application/json" }
                                )
            if res.status_code != 200:
                print "FAILED: Unable to get a new access token using refresh token"
                self.access_token = None
                return self.get_token()

            reply = res.json()

            self.access_token = reply['access_token']
            self.refresh_token = reply['refresh_token']
            self.token_expiration_time = time.time() + reply['expires_in']

        return self.access_token

    """ Helper method for getting necessary headers to use for API calls, including authentication
    """
    def _build_headers(self):
        return { "Authorization": "Bearer %s" % self.get_token(), "Accept": "application/json" }


    """ GET from API resource
    """
    def get(self, path=None, payload={}, headers={}):
        if path.find('v2/') >= 0:
            cut_path = path.split('v2/')
            path = cut_path[1]

        url = "%s/api/v2/%s" % (self.cloud_url, path)
        headers = dict(self._build_headers().items() + headers.items())
        res =  requests.get(url, params=payload, headers=headers)
        if headers['Accept'] == 'application/json':
            return res.json()
        else:
            return res.text

    """ Returns list of devices
    """
    def get_devices(self, limit=0):
        return self.get("devices?limit=%s" % (limit))

    """ Find available free Android device
    """
    def available_free_android_device(self, limit=0):
        print "Searching Available Free Android Device..."

        for device in self.get_devices(limit)['data']:
            if device['creditsPrice'] == 0 and device['locked'] == False and device['osType'] == "ANDROID" and device['softwareVersion']['apiLevel'] > 16:
                print "Found device '%s'" % device['displayName']
                print ""
                return str(device['displayName'])

        print "No available device found"
        print ""
        return ""

    """ Find available free iOS device
    """
    def available_free_ios_device(self, limit=0):
        print "Searching Available Free iOS Device..."

        for device in self.get_devices(limit)['data']:
            if device['creditsPrice'] == 0 and device['locked'] == False and device['osType'] == "IOS":
                print "Found device '%s'" % device['displayName']
                print ""
                return str(device['displayName'])

        print "No available device found"
        print ""
        return ""
