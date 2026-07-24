# Selenium Python Sample

1. Open `bitbar_selenium.py` and update `apiKey` in `capabilities`.
2. Install compatible Selenium:
3. Run:

```bash
python3 bitbar_selenium.py
```

## Common Error

Error:

```text
TypeError: WebDriver.__init__() got an unexpected keyword argument 'desired_capabilities'
```

Cause:
- Installed Selenium version is too new for this sample code.

Fix:

```bash
python -m pip install "selenium==4.9.1" 
(any selenium version that is compatible)
```
