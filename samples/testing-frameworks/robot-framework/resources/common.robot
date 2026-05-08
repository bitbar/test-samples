*** Settings ***
Library           AppiumLibrary    run_on_failure=Capture Screenshot On Failure
Library           OperatingSystem

*** Variables ***
${PROJECTROOT}                   ${CURDIR}${/}..
${PROJECT_ROOT}                  ${PROJECTROOT}
${SCREENSHOTS}                   ${PROJECTROOT}${/}screenshots
${APP_FILE}                      %{APPFILE=}

${REMOTE_URL}                   https://eu-mobile-hub.bitbar.com/wd/hub
#${REMOTE_URL}                   http://localhost:4723/wd/hub    # Use this for server-side execution in BitBar
${APIKEY}                        # your_bitbar_api_key_here    # for client-side execution only

${PLATFORM_NAME_ANDROID}        Android
${AUTOMATION_NAME}              uiautomator2
${DEVICE_NAME_ANDROID}          Google Pixel
${APP_ANDROID}                  app_id_here    # BitBar app ID for BitbarSampleApp.apk application
#${APP_ANDROID}                  ${APP_FILE}    # Use this for server-side execution in BitBar

${PLATFORM_NAME_IOS}            iOS
${IOS_AUTOMATION_NAME}          XCUITest
${DEVICE_NAME_IOS}              Apple iPhone
${APP_IOS}                      app_id_here    # BitBar app ID for BitbarSampleApp.apk application
#${APP_IOS}                      ${APP_FILE}    # Use this for server-side execution in BitBar
${AUTO_ACCEPT_ALERTS}           true

*** Keywords ***
Set Up And Open Android Application
    Open Application    ${REMOTE_URL}
    ...    platformName=${PLATFORM_NAME_ANDROID}
    ...    appium:automationName=${AUTOMATION_NAME}
    ...    bitbar:apiKey=${APIKEY}
    ...    bitbar:device=${DEVICE_NAME_ANDROID}
    ...    bitbar:app=${APP_ANDROID}    # change to appium:app for server-side execution in BitBar
    ...    noReset=false
    ...    fullReset=false
    Wait Until Page Contains    What is the best way to test your application    5s

Set Up And Open Ios Application
    Open Application    ${REMOTE_URL}
    ...    platformName=${PLATFORM_NAME_IOS}
    ...    appium:automationName=${IOS_AUTOMATION_NAME}
    ...    bitbar:apiKey=${APIKEY}
    ...    bitbar:device=${DEVICE_NAME_IOS}
    ...    bitbar:app=${APP_IOS}    # change to appium:app for server-side execution in BitBar
    ...    autoAcceptAlerts=${AUTO_ACCEPT_ALERTS}
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

Capture Screenshot On Failure
    Run Keyword And Ignore Error    Create Directory    ${SCREENSHOTS}
    ${status}    ${message}=    Run Keyword And Ignore Error    Capture Page Screenshot    ${SCREENSHOTS}${/}${TEST NAME}.png
    Run Keyword If    '${status}' == 'FAIL'    Log    Could not capture screenshot: ${message}    WARN
