# Template for Robot Framework

- Libs folder: Place for self made libraries.
- Tests folder: Contains test files. Resource ``${PROJECTROOT}${/}resources${/}common.robot`` should be included under ``*** settings ***`` on each test file so they can use common resources. Write all test files under this folder.
- Resources folder: Contains generic keywords and settings (common.robot), also other files required by the tests. Add all common keywords to common.robot and add all required extra features to this folder.


# How to install requirements:
- pip install -r ./resources/requirements.txt

# Running tests locally:
- ``python run.py``                  Runs the whole suite.
- ``python run.py -i <tag name>``    Runs tests that contains ``[Tags]    <tag name>``
- ``python run.py --test <name>``    Runs tests that contains name ``<name>`` in them.
- ``python run.py --suite <name>``   Runs the suite containing ``<name>``.
- ``python run.py --dryrun``         Runs everything as a dryrun
- ``-x <xunit_output_file>`` command line option can be used to generate report in xUnit compatible XML format. So for example ``python run.py -x xunit`` will run all the test and create ``xunit.xml`` file relatively to the output directory of other test results.

# Running tests on cloud:
- ``createAndroidTestZip.sh`` creates .zip file containing all necessary files for cloud execution on Android devices. Script outputs ``android-phone-test.zip`` file that can be uploaded to Testdroid Cloud for test execution.
- By default the tests will be run as server-side Appium and the driver is configured to use ``localhost`` address as: ``${REMOTE_URL}    http://localhost:4723/wd/hub``
- Framework can be configured also to run the tests as client-side Appium. To do this, the ``localhost`` address has to be replaced with ``appium.testdroid.com``. Also following additional capabilities has to be added:
	- ``testdroid_username``
	- ``testdroid_password``
	- ``testdroid_target``
	- ``testdroid_project``
	- ``testdroid_testrun``
	- ``testdroid_device``
	- ``testdroid_app``

	More information about desired capabilities for client-side Appium can be found from Testdroid [Appium Documentation](http://help.testdroid.com/customer/portal/articles/1507074-testdroid_-desired-capabilities)

# References
- [Robot Framework User Guide](http://robotframework.org/robotframework/latest/RobotFrameworkUserGuide.html)
- [Appium Library Keyword Documentation](http://jollychang.github.io/robotframework-appiumlibrary/doc/AppiumLibrary.html)
- [robotframework.org](http://robotframework.org/)