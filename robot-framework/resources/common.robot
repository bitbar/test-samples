*** Settings ***
Library           AppiumLibrary    run_on_failure=Capture Screenshot On Failure

*** Variables ***
${SCREENSHOTS}                  ${PROJECTROOT}${/}screenshots${/}

${REMOTE_URL}                   http://localhost:4723/wd/hub
${APP_PACKAGE}                  com.bitbar.testdroid
${AUTOMATION_NAME}              appium

${PLATFORM_NAME_ANDROID}        Android
${PLATFORM_VERSION_ANDROID}     6.0.1   # Uncomment this from the 'Open Application' keyword if you want to be spesific
${DEVICE_NAME_ANDROID}          Android Emulator    # IP address?
${APP_ANDROID}                  ${CURDIR}${/}app${/}BitbarSampleApp.apk
#${APP_ANDROID}                  ${PROJECTROOT}${/}application.apk

${PLATFORM_NAME_IOS}            ios
${PLATFORM_VERSION_IOS}         9.0.2   # Uncomment this from the 'Open Application' keyword if you want to be spesific
${DEVICE_NAME_IOS}              iOS Emulator    # IP address?
${APP_IOS}                      ${CURDIR}${/}app${/}BitbarIOSSample.ipa
${AUTO_ACCEPT_ALERTS}           true


*** Keywords ***

Set Up And Open Android Application
    Open Application    ${REMOTE_URL}    platformName=${PLATFORM_NAME_ANDROID}    #platformVersion=${PLATFORM_VERSION_ANDROID}
    ...    deviceName=${DEVICE_NAME_ANDROID}    app=${APP_ANDROID}    automationName=${AUTOMATION_NAME}    appPackage=${APP_PACKAGE}
    Wait Until Page Contains    What is the best way to test your application    5s

Set Up And Open Ios Application
    Open Application    ${REMOTE_URL}    platformName=${PLATFORM_NAME_IOS}    #platformVersion=${PLATFORM_VERSION_IOS}
    ...    deviceName=${DEVICE_NAME_IOS}    app=${APP_IOS}    automationName=${AUTOMATION_NAME}    appPackage=${APP_PACKAGE}
    ...    autoAcceptAlerts=${AUTO_ACCEPT_ALERTS}
    Wait Until Page Contains    What is the best way to test your application    5s

Capture Screenshot On Failure
    Capture Page Screenshot    ${SCREENSHOTS}${/}${TEST NAME}.png