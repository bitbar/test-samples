# Robot Framework sample

This sample contains Robot Framework tests for Android and iOS, with scripts for client-side execution and BitBar cloud packaging.

## Project structure

- `libs/`: custom Python libraries for tests.
- `resources/`: shared assets and settings.
  - `common.robot`: common keywords.
  - `requirements.txt`: Python dependencies.
  - `app/`: sample app binaries.
- `tests-android/`: Android app + Chrome test suites.
- `tests-ios/`: iOS app + Safari test suites.
- `run_android.py`, `run_ios.py`: local Robot Framework runners.
- `create-test-zip-android.sh`, `create-test-zip-ios.sh`: package tests for cloud runs.
- `run-tests-android.sh`, `run-tests-ios.sh`: entrypoint scripts used inside cloud execution.

## Install requirements

**macOS/Linux**

```bash
python3 -m pip install -r resources/requirements.txt
```

**Windows (PowerShell)**

```powershell
py -3 -m pip install -r .\resources\requirements.txt
```

## Client-side execution

The runner scripts automatically set `PROJECTROOT` and execute all suites under the platform-specific test folder.

**macOS/Linux**

```bash
python3 run_android.py
python3 run_ios.py
```

**Windows (PowerShell)**

```powershell
py -3 .\run_android.py
py -3 .\run_ios.py
```

Common filtering options are passed directly to Robot Framework.

**macOS/Linux**

```bash
python3 run_android.py -i cloud
python3 run_android.py --test "Simple Smoke Test - Correct Answer"
python3 run_ios.py --suite safari_example
python3 run_ios.py --dryrun
python3 run_android.py -x xunit
```

**Windows (PowerShell)**

```powershell
py -3 .\run_android.py -i cloud
py -3 .\run_android.py --test "Simple Smoke Test - Correct Answer"
py -3 .\run_ios.py --suite safari_example
py -3 .\run_ios.py --dryrun
py -3 .\run_android.py -x xunit
```

Notes:
- `-x xunit` creates `xunit.xml` under Robot Framework output directory.
- Current defaults in `resources/common.robot` are cloud-oriented (`REMOTE_URL` and `bitbar:*` capabilities).
- To run against local Appium, update `REMOTE_URL` and desired capabilities in `resources/common.robot` to match your local setup and app paths.

## Server-side execution in BitBar cloud

1. Package test zip.

**macOS/Linux**

```bash
./create-test-zip-android.sh
./create-test-zip-ios.sh
```

**Windows (PowerShell)**

```powershell
Copy-Item .\run-tests-android.sh .\run-tests.sh
if (Test-Path .\tests-robot-android.zip) { Remove-Item .\tests-robot-android.zip }
Compress-Archive -Path .\run-tests.sh, .\run_android.py, .\libs, .\resources, .\tests-android -DestinationPath .\tests-robot-android.zip

Copy-Item .\run-tests-ios.sh .\run-tests.sh
if (Test-Path .\tests-robot-ios.zip) { Remove-Item .\tests-robot-ios.zip }
Compress-Archive -Path .\run-tests.sh, .\run_ios.py, .\libs, .\resources, .\tests-ios -DestinationPath .\tests-robot-ios.zip
```

2. Upload generated package:
- `tests-robot-android.zip`
- `tests-robot-ios.zip`

What the package contains:
- `run-tests.sh` (copied from platform-specific `run-tests-<platform>.sh`)
- runner script (`run_android.py` or `run_ios.py`)
- `libs/`, `resources/`, and platform test folder

Inside cloud execution, `run-tests.sh`:
- unzips `tests.zip`
- installs Python requirements
- starts Appium
- runs tests (`python3 run_<platform>.py -x TEST-all`)
- collects outputs to `output-files/` (screenshots, `report.html`, `log.html`)
- all the data is automatically collected by BitBar and available in the test run details.

## References

- [Robot Framework User Guide](http://robotframework.org/robotframework/latest/RobotFrameworkUserGuide.html)
- [AppiumLibrary keyword docs](http://jollychang.github.io/robotframework-appiumlibrary/doc/AppiumLibrary.html)
- [Bitbar Appium desired capabilities](http://docs.bitbar.com/testing/appium/desired-caps/)
