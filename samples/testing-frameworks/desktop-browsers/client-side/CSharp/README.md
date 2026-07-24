# Desktop Browsers - Client Side (C# Selenium Sample)

This sample runs a Selenium test in C# using NUnit against BitBar Cloud.

## Project Location

- Solution: `SeleniumCSharpSample/SeleniumCSharpSample.sln`
- Test project: `SeleniumCSharpSample/SeleniumCSharpSample/BitbarSeleniumSampleCSharp.csproj`
- Main test file: `SeleniumCSharpSample/SeleniumCSharpSample/UnitTest1.cs`

## Prerequisites

- .NET SDK that supports `net10.0`
- Internet access to BitBar hub: `https://eu-desktop-hub.bitbar.com/wd/hub`
- A BitBar API key

## Configure Test Credentials

Open `SeleniumCSharpSample/SeleniumCsharpSample/UnitTest1.cs` and update the API key:

bitbar_options.Add("apiKey", "<your BitBar API key>");

You can also customize browser settings in the same `Setup()` method, for example:

- `capabilities.PlatformName`
- `capabilities.BrowserVersion`
- `bitbar_options` values like `osVersion`, `resolution`, and `seleniumVersion`

## Restore Dependencies

From this folder (`CSharp`):

cd SeleniumCSharpSample
dotnet restore SeleniumCsharpSample.sln

## Run Tests

dotnet test SeleniumCsharpSample.sln


## What The Test Does

- Starts a remote WebDriver session on BitBar
- Navigates to `https://bitbar.github.io/web-testing-target/`
- Verifies page title: `Bitbar - Test Page for Samples`
- Closes the session

