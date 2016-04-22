# -*- coding: utf-8 -*-

import os
import requests, sys
from random import choice


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
    def get_devices(self, limit=0, query_param=""):
        # https://cloud.testdroid.com/api/v2/devices?filter=b_online_eq_true;b_locked_eq_false;b_enabled_eq_true;n_creditsPrice_eq_0;s_osType_eq_ios
        query_str = "devices?limit=%s" % (limit)
        if self.device_group:
            query_str += "&device_group_id[]=%s" % (self.device_group)
        # add extra query params, defaults to all
        query_str += "&filter=b_online_eq_true;b_locked_eq_false;b_enabled_eq_true"
        # device os specific part
        query_str += query_param
        return self.get(query_str)

    """ GET from API resource
    """
    def get(self, path=None, get_headers=None):
        self.url_query = "%s/api/v2/%s" % (self.cloud_url, path)
        headers=self._build_headers(get_headers)
        print "  Device query url: {}".format(self.url_query)
        res =  requests.get(self.url_query, headers=self._build_headers(get_headers))
        if res.ok:
            return res.json()
        else:
            print "Could not retrieve devices."
            sys.exit(-1)

    """ Find available free Android device
    """
    def available_free_android_device(self, limit=0):
        print "Searching available Android device..."

        free = ";n_creditsPrice_eq_0"
        if self.device_group: 
            free = ""

        json_resp = self.get_devices(limit, ";s_osType_eq_android" + free)
        all_devices = json_resp['data']
        devices = []
        # get only Android devices with apiLevel above 16
        for device in all_devices:
            if device['softwareVersion']['apiLevel'] > 16:
                devices.append(device)
        device_name = str(choice(devices)['displayName'])
        print "Found device: '{}'".format(device_name)
        return device_name

    """ Find available free iOS device
    """
    def available_free_ios_device(self, limit=0):
        print "Searching available iOS device..."
        
        free = ";n_creditsPrice_eq_0"
        if self.device_group: 
            free = ""

        devices = self.get_devices(limit, ";s_osType_eq_ios" + free)['data']
        device_name = str(choice(devices)['displayName'])
        print "Found device: '{}'".format(device_name)
        return device_name



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
than directly from command line
"""
if __name__ == '__main__':
    df = DeviceFinder()
    print "DeviceFinder: {}".format(df.available_free_android_device())
