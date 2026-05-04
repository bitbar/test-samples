*** Settings ***
Library           AppiumLibrary    run_on_failure=Capture Screenshot On Failure
Library           OperatingSystem

*** Variables ***
${PROJECTROOT}                   ${CURDIR}${/}..
${PROJECT_ROOT}                  ${PROJECTROOT}
${SCREENSHOTS}                   ${PROJECTROOT}${/}screenshots


${REMOTE_URL}                   https://appiumstaging.bitbar.com/wd/hub
#${REMOTE_URL}                   http://localhost:4723/wd/hub    # Use this for Server-side execution in BitBar cloud
${APIKEY}
${AUTOMATION_NAME}              uiautomator2
${IOS_AUTOMATION_NAME}          XCUITest

${PLATFORM_NAME_ANDROID}        Android
${DEVICE_NAME_ANDROID}          Samsung
${APP_ANDROID}                  ${CURDIR}${/}app${/}BitbarSampleApp.apk
#${APP_ANDROID}                  ${PROJECTROOT}${/}application.apk    # Use this for Server-side execution in BitBar cloud

${PLATFORM_NAME_IOS}            iOS
${DEVICE_NAME_IOS}              iPhone
${APP_IOS}                      ${CURDIR}${/}app${/}BitbarIOSSample.ipa
#${APP_IOS}                      ${PROJECTROOT}${/}application.ipa    # Use this for Server-side execution in BitBar cloud
${AUTO_ACCEPT_ALERTS}           true

${BITBAR_PROJECT}               YourProjectName
${BITBAR_TESTRUN}               YourTestRunName


*** Keywords ***
Set Up And Open Android Application
    Open Application    ${REMOTE_URL}
    ...    platformName=${PLATFORM_NAME_ANDROID}
    ...    appium:automationName=${AUTOMATION_NAME}
    ...    bitbar:apiKey=${APIKEY}
    ...    bitbar:device=${DEVICE_NAME_ANDROID}
    ...    bitbar:app=${APP_ANDROID}
    ...    noReset=true
    ...    fullReset=false
    Wait Until Page Contains    What is the best way to test your application    5s

Set Up And Open Ios Application
    Open Application    ${REMOTE_URL}
    ...    platformName=${PLATFORM_NAME_IOS}
    ...    appium:automationName=${IOS_AUTOMATION_NAME}
    ...    bitbar:apiKey=${APIKEY}
    ...    bitbar:device=${DEVICE_NAME_ANDROID}
    ...    bitbar:app=${APP_IOS}
    ...    startIWDP=true
    Wait Until Page Contains    What is the best way to test your application    5s

Set Up And Open Chrome
    Open Application    ${REMOTE_URL}
    ...    platformName=${PLATFORM_NAME_ANDROID}
    ...    browserName=Chrome
    ...    appium:automationName=${AUTOMATION_NAME}
    ...    bitbar:apiKey=${APIKEY}
    ...    bitbar:device=${DEVICE_NAME_ANDROID}

Set Up And Open Safari
    Open Application    ${REMOTE_URL}
    ...    platformName=${PLATFORM_NAME_IOS}
    ...    browserName=Safari
    ...    appium:automationName=${IOS_AUTOMATION_NAME}
    ...    bitbar:apiKey=${APIKEY}
    ...    bitbar:device=${DEVICE_NAME_IOS}
    ...    startIWDP=true
    Wait Until Page Contains    Automation for Apps   10s

Capture Screenshot On Failure
    Run Keyword And Ignore Error    Create Directory    ${SCREENSHOTS}
    ${status}    ${message}=    Run Keyword And Ignore Error    Capture Page Screenshot    ${SCREENSHOTS}${/}${TEST NAME}.png
    Run Keyword If    '${status}' == 'FAIL'    Log    Could not capture screenshot: ${message}    WARN
F
