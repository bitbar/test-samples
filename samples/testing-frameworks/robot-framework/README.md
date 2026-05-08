# Robot Framework sample

This sample contains Robot Framework tests for Android and iOS, including app and mobile browser coverage for BitBar cloud.

## Project structure

- `libs/`: custom Python libraries used by tests.
- `resources/`: shared test assets.
  - `common.robot`: shared variables and setup keywords.
  - `requirements.txt`: Python dependencies.
  - `app/`: sample binaries (`BitbarSampleApp.apk`, `BitbarIOSSample.ipa`).
- `tests-android/`: Android app (`android_example.robot`) and Chrome (`chrome_example.robot`) suites.
- `tests-ios/`: iOS app (`ios_example.robot`) and Safari (`safari_example.robot`) suites.
- `run_android.py`, `run_ios.py`: local/client-side runners.
- `create-test-zip-android.sh`, `create-test-zip-ios.sh`: package tests for BitBar server-side runs.
- `run-tests-android.sh`, `run-tests-ios.sh`: server-side entrypoint scripts.

## Install requirements

### macOS/Linux

```bash
python3 -m pip install -r resources/requirements.txt
```

### Windows (PowerShell)

```powershell
py -3 -m pip install -r .\resources\requirements.txt
```

## Configure `resources/common.robot`

Update these variables before running tests.

| Variable | Client-side cloud run | Server-side BitBar run |
| --- | --- | --- |
| `${REMOTE_URL}` | `https://eu-mobile-hub.bitbar.com/wd/hub` | `http://localhost:4723/wd/hub` |
| `${APIKEY}` | Your BitBar API key | Not required |
| `${APP_ANDROID}` | BitBar app id (for example `123456789`) | `${APP_FILE}` and use `appium:app` capability |
| `${APP_IOS}` | BitBar app id (or cloud app reference) | `${APP_FILE}` |

Notes:
- `run-tests-android.sh` and `run-tests-ios.sh` set `APPFILE` from `application.apk` / `application.ipa` when running server-side.
- The Android setup keyword currently uses `bitbar:app`; for server-side packaging, switch it to `appium:app=${APP_ANDROID}` as already noted in `common.robot`.
- iOS setup already uses `appium:app=${APP_IOS}`.

## Client-side execution

Runner scripts append `--variable PROJECTROOT:<repo path>` and execute all suites in the platform folder.

### Run all Android or iOS suites

```bash
python3 run_android.py
python3 run_ios.py
```

```powershell
py -3 .\run_android.py
py -3 .\run_ios.py
```

### Useful filters

```bash
python3 run_android.py -i cloud
python3 run_android.py -e fail
python3 run_android.py --test "Simple Smoke Test - Correct Answer"
python3 run_ios.py --suite safari_example
python3 run_ios.py --dryrun
python3 run_android.py -x xunit
```

```powershell
py -3 .\run_android.py -i cloud
py -3 .\run_android.py -e fail
py -3 .\run_android.py --test "Simple Smoke Test - Correct Answer"
py -3 .\run_ios.py --suite safari_example
py -3 .\run_ios.py --dryrun
py -3 .\run_android.py -x xunit
```

## Server-side execution in BitBar cloud

### 1) Create the test package

macOS/Linux:

```bash
./create-test-zip-android.sh
./create-test-zip-ios.sh
```

Windows PowerShell equivalent:

```powershell
Copy-Item .\run-tests-android.sh .\run-tests.sh
if (Test-Path .\tests-robot-android.zip) { Remove-Item .\tests-robot-android.zip }
Compress-Archive -Path .\run-tests.sh, .\run_android.py, .\libs, .\resources, .\tests-android -DestinationPath .\tests-robot-android.zip

Copy-Item .\run-tests-ios.sh .\run-tests.sh
if (Test-Path .\tests-robot-ios.zip) { Remove-Item .\tests-robot-ios.zip }
Compress-Archive -Path .\run-tests.sh, .\run_ios.py, .\libs, .\resources, .\tests-ios -DestinationPath .\tests-robot-ios.zip
```

### 2) Upload package(s)

- `tests-robot-android.zip`
- `tests-robot-ios.zip`

### 3) What runs in BitBar server-side scripts

`run-tests-<platform>.sh` does the following:
- unzips `tests.zip`
- installs dependencies from `resources/requirements.txt`
- starts Appium
- runs `python3 run_<platform>.py -x TEST-all` (with `APPFILE` when available)
- collects `screenshots/`, `report.html`, and `log.html` into `output-files/`

## Test suites included

- Android app: `tests-android/android_example.robot`
- Android browser: `tests-android/chrome_example.robot`
- iOS app: `tests-ios/ios_example.robot`
- iOS browser: `tests-ios/safari_example.robot`

Some cases are intentionally failing and tagged with `fail` for pipeline validation.

## References

- [Robot Framework User Guide](http://robotframework.org/robotframework/latest/RobotFrameworkUserGuide.html)
- [AppiumLibrary keyword docs](http://jollychang.github.io/robotframework-appiumlibrary/doc/AppiumLibrary.html)
- [Bitbar Appium desired capabilities](http://docs.bitbar.com/testing/appium/desired-caps/)
