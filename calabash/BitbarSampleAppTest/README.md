
# Calabash Android example for BitbarSampleApp

## Run locally

Run `run_local_tests.sh` to run the test on local device.

## Create test zip

Run `create_test_zip.sh` to create BitbarSampleAppTests.zip file.

## Run in Testdroid Cloud

Testdroid Cloud Python API client must be installed. Check out https://github.com/bitbar/testdroid-api-client-python for details.

1. Create test zip file with `create_test_zip.sh`
1. Insert credentials to `run_test_in_testdroid_cloud.py` or export `TESTDROID_USERNAME` and `TESTDROID_PASSWORD`
2. Run `run_test_in_testdroid_cloud.py`

