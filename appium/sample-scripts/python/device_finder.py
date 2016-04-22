# -*- coding: utf-8 -*-

import os
import requests, sys


class DeviceFinder:

    """ Constructor
    """
    def __init__(self, url="https://cloud.testdroid.com", download_buffer_size=65536):
        self.cloud_url = url
        self.download_buffer_size = download_buffer_size
        self.url_query = None
        # to use devices from particular device group set the device
        # group id as environment variable. Otherwise free devices are
        # looked for
        self.device_group = os.environ.get('TESTDROID_DEVICE_GROUP') or ""


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
        query_str = "devices?limit=%s" % (limit)
        if self.device_group:
            query_str += "&device_group_id[]=%s" % (self.device_group)
#        print "Device query: {}".format(query_str)
        return self.get(query_str)

    """ GET from API resource
    """
    def get(self, path=None, get_headers=None):
        self.url_query = "%s/api/v2/%s" % (self.cloud_url, path)
        headers=self._build_headers(get_headers)
        res =  requests.get(self.url_query, headers=self._build_headers(get_headers))
        if res.ok:
            return res.json()
        else:
            print "Could not retrieve free devices."
            sys.exit(-1)

    """ Find available free Android device
    """
    def available_free_android_device(self, limit=0):
        print "Searching Available Free Android Device..."

        for device in self.get_devices(limit)['data']:
            if self.device_group:
                if device['online'] == True and device['locked'] == False and device['osType'] == "ANDROID" and device['softwareVersion']['apiLevel'] > 16:
                    print "Found device '%s'" % device['displayName']
                    print ""
                    return str(device['displayName'])
            else:
                if device['online'] == True and device['creditsPrice'] == 0 and device['locked'] == False and device['osType'] == "ANDROID" and device['softwareVersion']['apiLevel'] > 16:
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
            if self.device_group:
                print "Search free device from device group: {}".format(self.device_group)
                if device['online'] == True and device['locked'] == False and device['osType'] == "IOS":
                    print "Found device '%s'" % device['displayName']
                    print ""
                    return str(device['displayName'])
            else:
                if device['online'] == True and device['creditsPrice'] == 0 and device['locked'] == False and device['osType'] == "IOS":
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
            return apiLevel
        except Exception, e:
            print "Error: %s" % e
            return

""" DeviceFinder should rather be used from an other script rather
than directly from command lin
"""
if __name__ == '__main__':
    df = DeviceFinder()
    print "DeviceFinder: {}".format(df.available_free_android_device())
