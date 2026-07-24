Intro
=====

This folder contains a Python client-side Selenium sample for running a desktop browser test in BitBar Cloud.

Files
=====

* `bitbar_selenium.py` contains the sample unittest that opens the BitBar sample page, verifies the result text, and saves screenshots locally.
* `requirements.txt` contains the required Python package versions for this sample.
* `screenshots/` is created when the test runs and stores the captured images.

Prerequisites
=============

* Python 3
* Selenium for Python
* A valid BitBar API key

Setup
=====

Install dependencies from `requirements.txt`:

```bash
python3 -m pip install -r requirements.txt
```

If your Python installation is managed by Homebrew and blocks global installs, use:

```bash
python3 -m pip install --break-system-packages --user -r requirements.txt
```

For this sample to run without any code changes, use the package versions defined in `requirements.txt`.

Then set your BitBar API key in the test capabilities in `bitbar_selenium.py`.

Run
===

From this folder, run:

```bash
python3 bitbar_selenium.py
```

Successful output looks like this:

```text
Bitbar - Test Page for Samples
Bitbar
.
----------------------------------------------------------------------
Ran 1 test in 32.714s

OK
```

Troubleshooting
===============

* `ModuleNotFoundError: No module named 'selenium'` means Selenium is not installed for the Python interpreter you used to launch the test.
* `SessionNotCreatedException: Full authentication is required to access this resource` usually means the BitBar API key is missing, invalid, or expired.
* `TypeError: WebDriver.__init__() got an unexpected keyword argument 'desired_capabilities'` means incompatible package versions are installed. Reinstall dependencies from `requirements.txt`.
* `ValueError: Timeout value connect was <object object ...>` usually means `urllib3` is too new for this sample. Reinstall dependencies from `requirements.txt`.
* `DeprecationWarning` messages from Selenium 3 internals can appear with newer Python versions and are non-blocking.
* The sample saves screenshots into the local `screenshots` folder in the current working directory.