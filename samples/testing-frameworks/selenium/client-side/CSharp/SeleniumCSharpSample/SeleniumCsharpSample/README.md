# BitBar Selenium C# Sample

This folder contains a C# NUnit Selenium sample 

## Prerequisites

- .NET SDK installed (current project targets `net10.0`/`net7.0`)
- A valid BitBar API key


## Files

- `UnitTest1.cs`: Main sample test
- `BitbarSeleniumSampleCSharp.csproj`: Project file and dependencies

## Run the test

From this folder:

```bash
dotnet test BitbarSeleniumSampleCSharp.csproj
```

## Configure BitBar

Update capabilities in `UnitTest1.cs` as needed:

- platformName
- browserVersion
- bitbar:options (project, testrun, apiKey, osVersion, resolution)


## Corner Cases / Troubleshooting

- SDK version mismatch (`NETSDK1045`):
	If your local SDK does not support the target framework, install a compatible SDK or change the target framework in `BitbarSeleniumSampleCSharp.csproj`.

	Example to install/check SDK:

	```bash
	dotnet --list-sdks
	```

- Need to update Selenium WebDriver version:
	If tests fail after browser or API-side changes, update `Selenium.WebDriver` to a newer compatible version.

	```bash
	dotnet add BitbarSeleniumSampleCSharp.csproj package Selenium.WebDriver --version 4.33.0
	dotnet restore
	```

- Package restore/build issues after version change:
	Clean and restore dependencies before running tests again.

	```bash
	dotnet clean BitbarSeleniumSampleCSharp.csproj
	dotnet restore BitbarSeleniumSampleCSharp.csproj
	dotnet test BitbarSeleniumSampleCSharp.csproj
	```