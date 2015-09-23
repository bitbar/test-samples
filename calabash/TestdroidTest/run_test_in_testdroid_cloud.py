#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
Run test in Testdroid Cloud using API.

Check out https://github.com/bitbar/testdroid-api-client-python/
how to install Testdroid Python client.

Author: Jarno Tuovinen <jarno.tuovinen@bitbar.com>
"""

import os
import sys
from testdroid import Testdroid


def find_project(project_name):
    """Find a project"""
    projects = TESTDROID.get_projects()['data']
    for project in projects:
        if project['name'] == project_name:
            return project['id']
    return None


def file_exists(path_to_file):
    """ Check file existence

    Exit the script with value 1 if the file does not exist.
    """
    try:
        file_to_check = open(path_to_file, 'rb')
        file_to_check.close()
    except IOError as exc:
        if exc.errno == 2:
            print "Could not find file {}".format(path_to_file)
        else:
            print "I/O error({0}): {1}".format(exc.errno, exc.strerror)
        sys.exit(1)


# Set credentials here or use environment variables.
# If environment variables are set they override these values.
USERNAME = None
PASSWORD = None

USERNAME = os.environ.get('TESTDROID_USERNAME') or USERNAME
PASSWORD = os.environ.get('TESTDROID_PASSWORD') or PASSWORD

# Project information
PROJECT_NAME = "Testdroid Calabash Android example"
PROJECT_TYPE = "CALABASH"

# Device group, 14 = Free Android devices
DEVICE_GROUP_ID = 14

# Application under the test
APPLICATION_PATH = "../../apps/builds/Testdroid.apk"

# Test zip path
TEST_PATH = "TestdroidTests.zip"

# Check the file existence before any lengthy operation
file_exists(APPLICATION_PATH)
file_exists(TEST_PATH)

# Authenticate
TESTDROID = Testdroid(USERNAME, PASSWORD)

# Search for existing project
PROJECT_ID = find_project(PROJECT_NAME)

# Create a new project if there is no existing project
if PROJECT_ID is None:
    print "Creating new project (name: '{0}', project type: '{1}')" \
            .format(PROJECT_NAME, PROJECT_TYPE)
    PROJECT_ID = TESTDROID.create_project(PROJECT_NAME, PROJECT_TYPE)
    # At the moment create_project does not return the project id
    # so let's search the id again
    PROJECT_ID = find_project(PROJECT_NAME)

# Upload application to your project
print "Upload application '{}'".format(APPLICATION_PATH)
TESTDROID.upload_application_file(PROJECT_ID, APPLICATION_PATH)
print "Application uploaded"

# Upload test to your project
print "Upload test '{}'".format(TEST_PATH)
TESTDROID.upload_test_file(PROJECT_ID, TEST_PATH)
print "Test uploaded"

# Use one of the following ways to run the test

# Just start the test run
TESTDROID.start_test_run(PROJECT_ID, DEVICE_GROUP_ID)

# Start the test run and wait for it to finish
# TESTDROID.start_wait_test_run(PROJECT_ID, DEVICE_GROUP_ID)

# Start the test run and wait for it to finish and download the results
# TESTDROID.start_wait_download_test_run(PROJECT_ID, DEVICE_GROUP_ID)
