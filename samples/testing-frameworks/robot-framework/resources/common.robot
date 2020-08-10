*** Settings ***
Library           AppiumLibrary    run_on_failure=Capture Screenshot On Failure

*** Variables ***
${SCREENSHOTS}                  ${PROJECTROOT}${/}screenshots${/}

${REMOTE_URL}                   http://localhost:4723/wd/hub
${AUTOMATION_NAME}              appium
${IOS_AUTOMATION_NAME}          XCUITest

${PLATFORM_NAME_ANDROID}        Android
${PLATFORM_VERSION_ANDROID}     6.0.1   # Uncomment this from the 'Open Application' keyword if you want to be specific
${DEVICE_NAME_ANDROID}          Android Emulator
#${APP_ANDROID}                  ${CURDIR}${/}app${/}BitbarSampleApp.apk
${APP_ANDROID}                  ${PROJECTROOT}${/}application.apk    # Uncomment for cloud execution

${PLATFORM_NAME_IOS}            iOS
${PLATFORM_VERSION_IOS}         8.1.2   # Uncomment this from the 'Open Application' keyword if you want to be specific
${DEVICE_NAME_IOS}              Local Device
#${APP_IOS}                      ${CURDIR}${/}app${/}BitbarIOSSample.ipa
${APP_IOS}                      ${PROJECTROOT}${/}application.ipa    # Uncomment for cloud execution
${AUTO_ACCEPT_ALERTS}           true


*** Keywords ***

Set Up And Open Android Application
    Open Application    ${REMOTE_URL}    platformName=${PLATFORM_NAME_ANDROID}    #platformVersion=${PLATFORM_VERSION_ANDROID}
    ...    deviceName=${DEVICE_NAME_ANDROID}    app=${APP_ANDROID}    automationName=${AUTOMATION_NAME}    noReset=true    fullReset=false
    Wait Until Page Contains    What is the best way to test your application    5s

Set Up And Open Ios Application
    Open Application    ${REMOTE_URL}    platformName=${PLATFORM_NAME_IOS}    #platformVersion=${PLATFORM_VERSION_IOS}
    ...    deviceName=${DEVICE_NAME_IOS}    app=${APP_IOS}    automationName=${IOS_AUTOMATION_NAME}     startIWDP=true
    Wait Until Page Contains    What is the best way to test your application    5s

Set Up And Open Chrome
    Open Application    ${REMOTE_URL}    platformName=${PLATFORM_NAME_ANDROID}    #platformVersion=${PLATFORM_VERSION_ANDROID}
    ...    deviceName=${DEVICE_NAME_ANDROID}    browserName=Chrome    automationName=${AUTOMATION_NAME}

Set Up And Open Safari
    Open Application    ${REMOTE_URL}    platformName=${PLATFORM_NAME_IOS}    #platformVersion=${PLATFORM_VERSION_IOS}
    ...    deviceName=${DEVICE_NAME_IOS}    browserName=Safari    automationName=${IOS_AUTOMATION_NAME}     bundleid=com.apple.mobilesafari     startIWDP=true
    Wait Until Page Contains    Automation for Apps   10s

Capture Screenshot On Failure
    Capture Page Screenshot    ${SCREENSHOTS}${/}${TEST NAME}.png
