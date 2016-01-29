# -*- coding: utf-8 -*-

import requests, sys

class DeviceFinder:
    # Cloud URL (not including API path)
    url = None

    """ Full constructor with username and password
    """
    def __init__(self, url="https://cloud.testdroid.com", download_buffer_size=65536):
        self.cloud_url = url
        self.download_buffer_size = download_buffer_size

    """ Append dictionary items to header
    """
    def _build_headers(self, headers=None):
        hdrs = {}
        hdrs["Accept"] = "application/json"
        if headers != None:
            hdrs.update(headers)
        return hdrs

    """ Returns list of devices
    """
    def get_devices(self, limit=0):
        return self.get("devices?limit=%s" % (limit))

    """ GET from API resource
    """
    def get(self, path=None, get_headers=None):
        url = "%s/api/v2/%s" % (self.cloud_url, path)
        headers=self._build_headers(get_headers)
        res =  requests.get(url, headers=self._build_headers(get_headers))
        if res.ok:
            return res.json()
        else:
            print "Could not retrieve free devices."
            sys.exit -1

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
    """ Find out the API level of a Device
    """
    def device_API_level(self, deviceName):
        print "Searching for API level of device '%s'" % deviceName

        try:
            device = self.get(path="devices?search=%s" % deviceName)
            apiLevel = device['data'][0]['softwareVersion']['apiLevel']
            print "Found API level: %s" % apiLevel
            name = device['data'][0]['displayName']
            print "Device name: %s" % name
            deviceId = device['data'][0]['id']
            print "Device id: %s" % deviceId
            return apiLevel
        except Exception, e:
            print "Error: %s" % e
            return
