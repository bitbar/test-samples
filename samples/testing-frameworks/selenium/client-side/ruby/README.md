# Selenium Ruby Sample

This folder contains a Ruby Selenium WebDriver sample that runs a desktop browser test on BitBar Cloud.

## Requirements

- Ruby
- Bundler
- A valid BitBar API key

## Configure

1. Open `bitbar_selenium.rb`.
2. Replace `<insert your BitBar API key here>` in `bitbar:options.apiKey` with your BitBar API key. You can find it in BitBar Cloud under **My Integrations** > **API Access**.
3. Adjust the browser, platform, operating-system version, resolution, project, or test-run values in `capabilities` as needed. The [Capabilities Creator](https://cloud.bitbar.com/#public/capabilities-creator) lists supported values.

## Run

From this directory, install the dependencies and run the sample:

```bash
bundle install
bundle exec ruby bitbar_selenium.rb
```

## Expected Output

A successful run prints:

```text
Bitbar - Test Page for Samples
Bitbar
```