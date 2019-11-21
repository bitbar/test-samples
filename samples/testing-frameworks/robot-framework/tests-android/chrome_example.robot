*** Settings ***
Resource         ${PROJECTROOT}${/}resources${/}common.robot
Test Teardown    Close Application

*** Variables ***
${URL}                 https://bitbar.github.io/web-testing-target/
${ANSWER_BUTTON}       xpath = //button[contains(., "Click for answer")]
${RESULT_ELEMENT}      xpath = //p[@id="result_element" and contains(., "Bitbar")]
${EXPECTED_STYLE}      xpath = //button[contains(@style, "rgb(127, 255, 0)")]

*** Test cases ***

Chrome Smoke Test - Correct Style
    [Tags]    cloud
    Set Up And Open Chrome
    Go To Url   ${URL}
    Wait Until Page Contains Element    ${ANSWER_BUTTON}
    Click Element   ${ANSWER_BUTTON}
    Validate Style

Chrome Smoke Test - Fails Intentionally
    [Tags]    fail
    Set Up And Open Chrome
    Go To Url   ${URL}
    Wait Until Page Contains Element    ${ANSWER_BUTTON}
    Validate Style

*** Keywords ***

Validate Style
        Wait Until Page Contains Element    ${EXPECTED_STYLE}    5s
