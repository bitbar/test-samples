*** Settings ***
Resource         ${PROJECTROOT}${/}resources${/}common.robot
Test Teardown    Close Application

*** Variables ***
${NAME}                John
${INPUT_NAME_FIELD}    xpath=//*[@name='userName']
${SUBMIT_BUTTON}       xpath=//*[@name='sendAnswer']

*** Test cases ***

Simple Smoke Test - Correct Answer
    [Tags]    cloud
    Set Up And Open Ios Application
    Input Name       ${NAME}
    Select Option    answer2
    Submit Selection
    Validate Correct Answer

Simple Smoke Test - Wrong Answer
    [Tags]    101
    Set Up And Open Ios Application
    Input Name       ${NAME}
    Select Option    answer1
    Submit Selection
    Validate Wrong Answer

Simple Smoke Test - Wrong Answer 2
    [Tags]    mom
    Set Up And Open Ios Application
    Input Name       ${NAME}
    Select Option    answer3
    Submit Selection
    Validate Wrong Answer

Simple Smoke Test - Fails Intentionally
    [Tags]    fail
    Set Up And Open Ios Application
    Input Name       ${NAME}
    Select Option    answer3
    Submit Selection
    Validate Correct Answer

*** Keywords ***

Input Name
    [Arguments]    ${name}
    Input Value    ${INPUT_NAME_FIELD}    ${name}
    Hide Keyboard    Return

Select Option
    [Arguments]    ${option}
    Click Element    xpath=//*[@name='${option}']

Submit Selection
    Click Element    ${SUBMIT_BUTTON}

Validate Correct Answer
    Wait Until Page Contains    Congratulations ${NAME}    5s

Validate Wrong Answer
    Wait Until Page Contains    Wrong Answer    5s
