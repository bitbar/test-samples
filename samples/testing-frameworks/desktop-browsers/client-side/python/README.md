Intro
=====

This folder contains a Python client-side Selenium sample for running a desktop browser test in Bitbar Cloud.

Files
=====

* `bitbar_selenium.py` contains the sample unittest that opens the Bitbar sample page, verifies the result text, and saves screenshots locally.
* `screenshots/` is created when the test runs and stores the captured images.

Prerequisites
=============

* Python 3
* Selenium for Python
* A valid Bitbar API key

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

Then set your Bitbar API key in the test capabilities in `bitbar_selenium.py`.

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
* `SessionNotCreatedException: Full authentication is required to access this resource` usually means the Bitbar API key is missing, invalid, or expired.
* The sample saves screenshots into the local `screenshots` folder in the current working directory.