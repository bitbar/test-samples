# -*- coding: utf-8 -*-

import os
import requests
import sys
import base64
from random import shuffle


class DeviceFinder:
    """ Constructor
    """
    def __init__(self, url="https://cloud.bitbar.com", api_key="", download_buffer_size=65536):
        self.cloud_url = os.environ.get('BITBAR_URL') or url
        self.api_key = os.environ.get('BITBAR_APIKEY') or api_key
        self.download_buffer_size = download_buffer_size
        self.url_query = None
        # to use devices from particular device group set the device
        # group id as environment variable. Otherwise free devices are
        # looked for
        self.device_group = os.environ.get('BITBAR_DEVICE_GROUP') or ""

    """ Append dictionary items to header
    """
    def _build_headers(self, headers=None):
        hdrs = {'Authorization' : 'Basic %s' % base64.b64encode((self.api_key+":").encode(encoding='utf_8')).decode(), 'Accept' : 'application/json' }
        if headers is not None:
            hdrs.update(headers)
        return hdrs

    """ GET from API resource
    """
    def get(self, path=None, get_headers=None):
        self.url_query = "%s/api/v2/%s" % (self.cloud_url, path)
        query_headers = self._build_headers(get_headers)
        res = requests.get(self.url_query, headers=query_headers)
        if res.ok:
            return res.json()
        else:
            print("Failed query: {}\nusing headers: {}".format(self.url_query,
                                                               query_headers))
            sys.exit(-1)

    """ Returns list of devices
    """
    def get_devices(self, limit=0):
        query_str = "devices?limit=%s" % (limit)
        if self.device_group:
            query_str = "me/device-groups/%s/devices?limit=0" % (self.device_group)
        devices = self.get(query_str)['data']
        shuffle(devices)
        return devices

    """ Find available Android device
    """
    def available_android_device(self, limit=0):
        print("Searching available Android device...")

        for device in self.get_devices(limit):
            if self.device_group:
                if (device['online'] is True and
                        device['locked'] is False and
                        device['osType'] == "ANDROID" and
                        device['softwareVersion']['apiLevel'] > 16):
                    print("Found device '%s'" % device['displayName'])
                    print("")
                    return str(device['displayName'])
            else:
                if (device['online'] is True and
                        device['creditsPrice'] == 0 and
                        device['locked'] is False and
                        device['osType'] == "ANDROID" and
                        device['softwareVersion']['apiLevel'] > 16):
                    print("Found device '%s'" % device['displayName'])
                    print("")
                    return str(device['displayName'])

        print("No available device found")
        print("")
        return ""

    """ Find available iOS device
    """
    def available_ios_device(self, limit=0):
        print("Searching available iOS device...")

        for device in self.get_devices(limit):
            if self.device_group:
                if (device['online'] is True and
                        device['locked'] is False and
                        device['osType'] == "IOS"):
                    print("Found device '%s'" % device['displayName'])
                    print("")
                    return str(device['displayName'])
            else:
                if (device['online'] is True and
                        device['creditsPrice'] == 0 and
                        device['locked'] is False and
                        device['osType'] == "IOS"):
                    print("Found device '%s'" % device['displayName'])
                    print("")
                    return str(device['displayName'])

        print("No available device found")
        print("")
        return ""

    """ Find out the API level of a Device
    """
    def device_API_level(self, deviceName):
        print("Searching for API level of device '%s'" % deviceName)

        try:
            devices = self.get(path="devices?search=%s" % deviceName)['data']
            picked_device = devices[0]
            print("Selected device '{}' has API level '{}'".format(picked_device['displayName'],
                                                                   picked_device['softwareVersion']['apiLevel']))
            return picked_device['softwareVersion']['apiLevel']
        except Exception as e:
            print("Error: %s" % e)
            return


""" DeviceFinder should rather be used from an other script rather
than directly from command line
"""
if __name__ == '__main__':
    df = DeviceFinder()
    print("DeviceFinder: {}".format(df.available_android_device()))
