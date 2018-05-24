#!/bin/bash

#
# Copyright(C) 2018 Bitbar Technologies Oy
#
# NOTE: contributions are welcome
#
# __author__ = 'Niko Cankar <niko.cankar@bitbar.com>'


#######################################################################
# Verify the provided ID looks like id and is not string message
#######################################################################
function is_valid_id_or_exit (){
    # echo "is_valid_id_or_exit checking: ${1}"
    re='^[0-9]+$'
    if ! [[ "$1" =~ $re ]] ; then
        echo "Received error message:
$1" >&2;
        exit 1
    fi
    # echo "is_valid_id_or_exit is valid number: ${1}"
    return 0
}

#######################################################################
# Check if project exists and get project files if they exist
#
# Params:
# $1 - api key to cloud
# $2 - project name to get ID for
#
# Return - exit value
#######################################################################
function get_project_information () {
    local project_id=$(curl -s -H "Accept: application/json" -u "${API_KEY}": -X GET "${CLOUD_URL}api/v2/me/projects" | jq '.data[] | if .name=="'"${PROJECT_NAME}"'" then .id else empty end')

    echo "... ... get_project_information: ${project_id}"
    is_valid_id_or_exit "${project_id}"
    local ok=$?

    if [ ${ok} -gt 0 ]; then
        echo "... ... didn't find project ${PROJECT_NAME}"
        # create_new_project
        exit 0
    else
        echo "... ... project existed, getting last app and test file IDs"
        PROJECT_ID=${project_id}
        # GET previous app and test files
        local app_and_test_ids=($(curl -s -H "Accept: application/json" -u "${API_KEY}": -X GET "${CLOUD_URL}api/v2/projects/${PROJECT_ID}/runs-extended?sort=createTime_d" | jq -r '.data[0].files.data[] | .id'))
        APP_FILE_ID=${app_and_test_ids[0]}
        TEST_FILE_ID=${app_and_test_ids[1]}
        echo "... ... found latest app ($APP_FILE_ID) and test ($TEST_FILE_ID) files, will use these if none were given as params"
    fi
}

#######################################################################
# Create a new project and set the new project ID to global PROJECT_ID
# variable.
#
# Params:
# $1 - project name to create
#
# Return - exit value and PROJECT_ID updated with 
#######################################################################
function create_new_project () {
    echo "... create new project with name '${PROJECT_NAME}'"
    project_id=$(curl -s -H "Accept: application/json" -u "${API_KEY}": -X POST -d "name=${PROJECT_NAME}"  "${CLOUD_URL}api/me/projects" | jq 'if .name=="'"${PROJECT_NAME}"'" then .id else empty end')
    is_valid_id_or_exit "${project_id}"
    echo "... project ID: ${project_id}"
    PROJECT_ID=${project_id}
}

#######################################################################
# Upload application file to existing project (ID) and updates the
# global variable APP_FILE_ID
#######################################################################
function upload_application_file() {
    echo "... ...Uploading  ${APP_FILE} to project"
    local app_file_id=$(curl -s -H "Accept: application/json" -u "${API_KEY}": -X POST -F 'file=@"'"${APP_FILE}"'"' "${CLOUD_URL}api/v2/me/projects/${PROJECT_ID}/files/application"  | jq '. | if .id then .id else .message end')
    
    is_valid_id_or_exit "${app_file_id}"
    echo "... ...Updated app file id to: ${app_file_id}"
    APP_FILE_ID=${app_file_id}
}

#######################################################################
# Upload test file to existing project (ID) and updates the global
# variable TEST_FILE_ID
#######################################################################
function upload_test_file () {
    echo "... ... Uploading  ${TEST_FILE} to project"
    local test_file_id=$(curl -s -H "Accept: application/json" -u "${API_KEY}": -X POST -F 'file=@"'"${TEST_FILE}"'"' "${CLOUD_URL}api/v2/me/projects/${PROJECT_ID}/files/test"  | jq '. | if .id then .id else .message end')

    is_valid_id_or_exit "${test_file_id}"
    echo "... ...Updated test file id to: ${test_file_id}"
    TEST_FILE_ID=${test_file_id}
}

#######################################################################
# Print usage and  input params
#######################################################################
function print_help_and_die() {
    echo "Usage: $0 
    -t|--test-file <file>
    -a|--app-file <application>
    -d|--device-group-id <device group id>
    -k|--api-key <api key>, Mandatory to do anything else
    -u|--url <cloud url>, Web URL for cloud endpoint eg. 'https://cloud.bitbar.com/'
    -p|--project <project name for test run> Mandatory, if project does not exist a new one is created
    -h|--help, print this help message"
    
    exit 0
}

#######################################################################
# Default values for global variables
#######################################################################
OS_TYPE="IOS" # UNDEFINED, IOS, ANDROID
FRAMEWORKID=542 
CLOUD_URL="https://cloud.bitbar.com/"
API_KEY=""
DEVICE_GROUP_ID=-1
APP_FILE=""
APP_FILE_ID=-1
TEST_FILE=""
TEST_FILE_ID=-1
PROJECT_NAME=""
PROJECT_ID=-1

#######################################################################
# Parse inputs and store variables
#######################################################################
while [[ $# -gt 0 ]]
do
    key="$1"
    case $key in
        -p|--project)
            PROJECT_NAME="$2"
            shift
            shift
            ;;
        -t|--test-file)
            TEST_FILE="$2"
            shift # past argument
            shift # past value
            ;;
        -a|--app-file)
            APP_FILE="${2}"
            # uploade app file to cloud
            shift
            shift
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
        -u|--url)
            CLOUD_URL="$2"
            shift
            shift
            ;;
        -h|--help)
            print_help_and_die
            ;;
        --default)
            shift # past argument
            ;;
        *)    # unknown option
            echo "Unknown option '${1}'"
            print_help_and_die
            ;;
    esac
done


#######################################################################
# Check required minimum params were given
# 1) project name is required
# 2) if project does not exist - then test and app files required as
#    parameters
# 3) 
#######################################################################
if [[ -z "${PROJECT_NAME}" ]] ||  [[ -z "${DEVICE_GROUP_ID}" ]] ||  [[ -z "${API_KEY}" ]]; then
    print_help_and_die
fi


#######################################################################
# Get project id and project files (app and test)
#######################################################################
echo "... Getting project's (${PROJECT_NAME}) ID and possible app and test data"
get_project_information "\${API_KEY}" "\${PROJECT_NAME}"


#######################################################################
# if APP_FILE given as param, then upload new app file to project
#######################################################################
if [ ! -z "${APP_FILE}" ]; then
    upload_application_file ${APP_FILE}
else  
    echo "... Using existing app file id: ${APP_FILE_ID}"
fi


#######################################################################
# if TEST_FILE given as param, then upload new test file to project
#######################################################################
if [ ! -z "${TEST_FILE}" ]; then
    upload_test_file ${TEST_FILE}
else
    echo "... Using existing test file id: ${TEST_FILE_ID}"
fi

#######################################################################
# Check test and app files are set
#######################################################################
if [[ ${TEST_FILE_ID} -eq -1 ]] ||  [[ ${APP_FILE_ID} -eq -1 ]]; then
    echo "... No TEST or APP files given or found from project"
fi

#######################################################################
# Starting the test run with provided params
#######################################################################
echo "... Starting new test run in cloud with params: project: ${PROJECT_ID}, app file: ${APP_FILE_ID} and test file: ${TEST_FILE_ID}"
TESTRUN_ID=$(curl -s "${CLOUD_URL}"'api/v2/me/runs' -H "Content-Type: application/json" -H "Accept: application/json" -u "${API_KEY}": -X POST --data '{"osType":"'"${OS_TYPE}"'","projectId":"'"${PROJECT_ID}"'","frameworkId":"'"${FRAMEWORKID}"'","files":[{"id":"'"${APP_FILE_ID}"'", "action":"INSTALL"},{"id":"'"${TEST_FILE_ID}"'", "action":"RUN_TEST"}],"deviceGroupId":"'"${DEVICE_GROUP_ID}"'"}'  | jq '. | if .id then .id else .message end')

is_valid_id_or_exit "${TESTRUN_ID}"


echo "Test run started: 
"${CLOUD_URL}"#testing/test-run/${PROJECT_ID}/${TESTRUN_ID}"
