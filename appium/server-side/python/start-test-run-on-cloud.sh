#!/bin/bash

#
# Copyright(C) 2018 Bitbar Technologies Oy
#
# NOTE: contributions are welcome
#
# __author__ = 'Niko Cankar <niko.cankar@bitbar.com>'


function is_valid_id_or_exit {
    #echo "is_valid_id_or_exit: checking ${1}"
    re='^[0-9]+$'
    if ! [[ "$1" =~ $re ]] ; then
        echo "Received error message:
$1" >&2; return 1
    fi
    # echo "'$1' is valid number"
    return 0
}

# params: 1) api-key, 2) project name to check id for
function get_project_id {
    eval key="${1}"
    eval name="${2}"
    echo "... ... get_project_id: 1) $key 2) $name"
    local project_id=$(curl -s -H "Accept: application/json" -u "${key}": -X GET https://staging.testdroid.com/api/v2/me/projects | jq '.data[] | if .name=="'"${name}"'" then .id else empty end')

    echo "... ... get_project_id: ${project_id}"
    is_valid_id_or_exit "${project_id}"
    local ok=$?
    echo "is valid id returned: ${ok}"

    if [ ${ok} -gt 0 ]; then
        echo "... let's create new project with name ${2}"
        # project_id=...
    fi
    echo "... project ID: ${project_id}"
    PROJECT_ID=${project_id}
}

# default values that may be overriden by command line params
ENV="ios"
API_KEY="iOdbfNRhApqRGT5KcJvGpmNmFblKKYie" 
PROJECT_NAME="ios appium"
DEVICE_GROUP_ID=68095
TEST_FILE=""
TEST_FILE_ID=40924162
PROJECT_NAME="ios appium"


# https://stackoverflow.com/questions/192249/how-do-i-parse-command-line-arguments-in-bash
while [[ $# -gt 0 ]]
do
    key="$1"

    case $key in
        -t|--test-file)
            TEST_FILE="$2"
            shift # past argument
            shift # past value
            ;;
        -d|--device-group-id)
            DEVICE_GROUP_ID="$2"
            shift
            shift
            ;;
        -k|--api-key)
            API_KEY="$2"
            shift
            shift
            ;;
        -p|--project-name)
            PROJECT_NAME="$2"
            shift
            shift
            ;;
        --default)
            shift # past argument
            ;;
        *)    # unknown option
            echo "Unknown option '${2}'"
            ;;
    esac
done


echo "... Getting project's (${PROJECT_NAME}) ID or create new project"
get_project_id "\${API_KEY}" "\${PROJECT_NAME}"


# if TEST_FILE is not empty, new test file needs to be updated and new TEST_FILE_ID created
if [ ! -z "${TEST_FILE}" ]; then
    echo "Creating test file for environment: ${ENV}"
    cp run-tests_${ENV}.sh run-tests.sh && zip -r "${TEST_FILE}" requirements.txt *py run-tests.sh


    #curl -H "Accept: application/json" -u ${API_KEY}: -X GET https://staging.testdroid.com/api/v2/me/projects?name="ios appium"

    echo "... Uploading  ${TEST_FILE} to project"
    TEST_FILE_ID=$(curl -s -H "Accept: application/json" -u "${API_KEY}": -X POST -F 'file=@"'"${TEST_FILE}"'"' "https://staging.testdroid.com/api/v2/me/projects/${PROJECT_ID}/files/test"  | jq '. | if .id then .id else .message end')

    is_valid_id_or_exit "${TEST_FILE_ID}"
else
    echo "... Using existing test file id: ${TEST_FILE_ID}"
fi

echo "... Starting new test run in cloud"
TESTRUN_ID=$(curl -s 'https://staging.testdroid.com/api/v2/me/runs' -H "Content-Type: application/json" -H "Accept: application/json" -u ${API_KEY}: -X POST --data '{"osType":"IOS","projectId":"'"${PROJECT_ID}"'","frameworkId":560,"files":[{"id":40548259},{"id":"'"${TEST_FILE_ID}"'"}],"deviceGroupId":"'"${DEVICE_GROUP_ID}"'"}'  | jq '. | if .id then .id else .message end')

is_valid_id_or_exit "${TESTRUN_ID}"


echo "Test run started: 
https://staging.testdroid.com/#testing/test-run/${PROJECT_ID}/${TESTRUN_ID}"
