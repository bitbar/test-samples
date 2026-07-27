# Ruby Selenium Sample

This folder contains a Ruby Selenium example script for testing web applications:

- `bitbar_selenium.rb`

## Description

This script demonstrates Selenium WebDriver testing with Ruby by connecting to the BitBar cloud testing platform. It retrieves and displays the title and content of the BitBar test page to verify successful browser automation and connectivity to the cloud testing service.

## Prerequisites

- Ruby installed
- Gems installed (from this folder):

```bash
bundle install
```

## Configuration

The script connects to BitBar cloud platform. You may need to configure:

- **API Key**: Update the `bitbar_api_key` variable if running against BitBar services
- **Target URL**: The script currently tests against public BitBar demo pages
- **Browser**: Chrome is used by default

## Run

From the repository root:

```bash
ruby samples/testing-frameworks/desktop-browsers/client-side/ruby/bitbar_selenium.rb
```

Or from this folder:

```bash
ruby bitbar_selenium.rb
```

## Expected Output

When successful, the script prints page title text similar to:

- Bitbar - Test Page for Samples
- Bitbar

## Troubleshooting

### Bundle Install Issues
If `bundle install` fails, ensure you have Ruby and Bundler installed:
```bash
ruby --version
gem install bundler
```

### Gem Installation Problems
If gems fail to install, try:
```bash
bundle update
bundle install
```

