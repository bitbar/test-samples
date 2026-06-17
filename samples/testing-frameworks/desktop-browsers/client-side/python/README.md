Intro
=====

This folder contains a Python client-side Selenium sample for running a desktop browser test in BitBar Cloud.

Files
=====

* `bitbar_selenium.py` contains the sample unittest that opens the BitBar sample page, verifies the result text, and saves screenshots locally.
* `screenshots/` is created when the test runs and stores the captured images.

Prerequisites
=============

* Python 3
* Selenium for Python
* A valid BitBar API key

Setup
=====

Install Selenium with one of the following options:

```bash
python3 -m pip install selenium
```

If your Python installation is managed by Homebrew and blocks global installs, use either a virtual environment or the explicit override:

```bash
python3 -m venv .venv
source .venv/bin/activate
python3 -m pip install selenium
```

or

```bash
python3 -m pip install --break-system-packages selenium
```

For this sample to run without any code changes, pin Selenium and urllib3 to versions compatible with the current `desired_capabilities` usage:

```bash
python3 -m venv .venv_selenium3
source .venv_selenium3/bin/activate
python3 -m pip install "selenium==3.141.0"
python3 -m pip install "urllib3<2"
```

Then set your BitBar API key in the test capabilities in `bitbar_selenium.py`.

Run
===

From this folder, run:

```bash
source .venv_selenium3/bin/activate
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
* `TypeError: WebDriver.__init__() got an unexpected keyword argument 'desired_capabilities'` means Selenium 4 is being used with a Selenium 3 style sample. Activate `.venv_selenium3` and install pinned dependencies shown above.
* `ValueError: Timeout value connect was <object object ...>` after installing Selenium 3 means `urllib3` is too new. Reinstall with `python3 -m pip install "urllib3<2" --force-reinstall`.
* `DeprecationWarning` messages from Selenium 3 internals can appear with newer Python versions and are non-blocking.
* The sample saves screenshots into the local `screenshots` folder in the current working directory.