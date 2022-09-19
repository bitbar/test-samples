# Template for Robot Framework

- Libs folder: Place for self made libraries.
- Tests folder: Contains test files. Resource ``${PROJECTROOT}${/}resources${/}common.robot`` should be included under ``*** settings ***`` on each test file so they can use common resources. Write all test files under this folder.
- Resources folder: Contains generic keywords and settings (common.robot), also other files required by the tests. Add all common keywords to common.robot and add all required extra features to this folder.


# How to install requirements:
- python3 -m pip install -r ./resources/requirements.txt

# Running tests locally:
There are two runner scripts for running the tests locally, ``run_android.py`` and ``run_ios.py``

- ``python run_<OS>.py`` Runs the whole suite. For example ``python run_android.py`` runs all the Android tests.
- ``python run_<OS>.py -i <tag name>`` Runs tests that contains ``[Tags]    <tag name>``
- ``python run_<OS>.py --test <name>`` Runs tests that contains name ``<name>`` in them.
- ``python run_<OS>.py --suite <name>`` Runs the suite containing ``<name>``.
- ``python run_<OS>.py --dryrun`` Runs everything as a dryrun
- ``-x <xunit_output_file>`` command line option can be used to generate report in xUnit compatible XML format. So for example ``python run_<OS>.py -x xunit`` will run all the test and create ``xunit.xml`` file relatively to the output directory of other test results.

# Running tests on the Cloud:
- From ``common.robot`` choose the correct ``${APP_ANDROID}`` or ``${APP_IOS}`` path by uncommenting the one with ``application.apk`` or ``application.ipa`` and commenting out other paths.
- ``create-test-zip-android.sh`` and ``create-test-zip-ios.sh`` will create .zip files containing all necessary files for cloud execution. Scripts output ``tests-robot-android.zip`` and ``tests-robot-ios.zip`` files that can be uploaded to Testdroid Cloud for test execution.
- By default the tests will be run as server-side Appium and the driver is configured to use ``localhost`` address as: ``${REMOTE_URL}    http://localhost:4723/wd/hub``
- Framework can be configured also to run the tests as client-side Appium. To do this, the ``localhost`` address has to be replaced with ``appium.bitbar.com``. Also following additional capabilities have to be added:
	- ``testdroid_username``
	- ``testdroid_password``
	- ``testdroid_target``
	- ``testdroid_project``
	- ``testdroid_testrun``
	- ``testdroid_device``
	- ``testdroid_app``
	- ``BundleID``

	More information about desired capabilities for client-side Appium can be found from Testdroid [Appium Documentation](http://docs.bitbar.com/testing/appium/desired-caps/)

# References
- [Robot Framework User Guide](http://robotframework.org/robotframework/latest/RobotFrameworkUserGuide.html)
- [Appium Library Keyword Documentation](http://jollychang.github.io/robotframework-appiumlibrary/doc/AppiumLibrary.html)
- [robotframework.org](http://robotframework.org/)
