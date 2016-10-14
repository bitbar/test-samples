#!/bin/bash

#
# Script for uploading the test project to Testdroid Cloud as a Server side Appium test
#
# Requires the user's API key, device group name and test type (android or ios).
# Creates the test zip, uploads it to the set project and launches the test.
#
# @author lasse.hall@bitbar.com
#

function help() {
    echo
    echo "$0 - create and upload test project to Testdroid Cloud and run it"
    echo
    echo "Usage: $0 -a/i -g <DEVICE_GROUP_NAME> -k <API_KEY>"
    echo " -a for Android test"
    echo " -i for iOS test"
    echo "Optional: -p <PROJECT_NAME> to choose a specific profile. If not given, a new project will be created"
    echo "Optional: -t for creating and uploading a new test zip file"
    echo "Optional: -f <APP_FILE_PATH> for uploading a new app file"
    echo "Optional: -e <API_ENDPOINT> for private cloud instances"
    exit
}

while getopts "aitg:k:p:f:e:h" opt; do
    case $opt in
        a) ANDROID=true
          ;;
        i) IOS=true
          ;;
        t) UPLOAD_TEST=true
          ;;
        g) DEVICE_GROUP_NAME=${OPTARG}
          ;;
        k) API_KEY=${OPTARG}
          ;;
        p) PROJECT_NAME=${OPTARG}
          ;;
        f) APP_FILE=${OPTARG}
          ;;
        e) API_ENDPOINT=${OPTARG}
          ;;
        h) help
          ;;
        \?) echo "Invalid option: -${OPTARG}" >&2
          ;;
        :) echo "Option -${OPTARG} requires an argument." >&2
          exit 1
          ;;
    esac
done

# Set the Public Cloud as API_ENDPOINT, if not set to something else already
if [ -z ${API_ENDPOINT} ]; then
    API_ENDPOINT=https://cloud.testdroid.com
fi

# Check that either -a or -i flag was given
if [[ ( "${ANDROID}" = true && "${IOS}" = true ) || ( -z ${ANDROID} && -z ${IOS} ) ]] ; then
    echo "Either -a or -i flag must be provided, but not both!" >&2
    help
fi

# Check that -k was given
if [ -z ${API_KEY} ] ; then
    echo "API_KEY with -k not given!" >&2
    help
else
    MAIN_USER_ID="$(curl -H "Accept: application/json" -u ${API_KEY}: "${API_ENDPOINT}/api/v2/me" | python -m json.tool | sed -n -e '/"mainUserId": / s/^.*"mainUserId": \(.*\)"*,/\1/p')"
    echo "MAIN_USER_ID: ${MAIN_USER_ID}"
    if [ -z ${MAIN_USER_ID} ] ; then
        echo "Authentication failed, check apikey given in -k: "${API_KEY}""
        help
    else
        echo "Authentication succeeded."
        echo "mainUserId: ${MAIN_USER_ID}"
    fi
fi

# Check if -p <PROJECT_NAME> was given
if [ -z ${PROJECT_NAME} ] ; then
    echo "No -p <PROJECT_NAME> given, creating a new project"
    if [ ${ANDROID} ] ; then
        echo "Creating Android project.."
        PROJECT_NAME="$(curl -H "Accept: application/json" -u ${API_KEY}: -X POST -F 'type=APPIUM_ANDROID_SERVER_SIDE' "${API_ENDPOINT}/api/v2/me/projects" | python -m json.tool | sed -n -e '/"name":/ s/^.*"name": "\(.*\)".*/\1/p')"
    elif [ ${IOS} ] ; then
        echo "Creating iOS project.."
        PROJECT_NAME="$(curl -H "Accept: application/json" -u ${API_KEY}: -X POST -F 'type=APPIUM_IOS_SERVER_SIDE' "${API_ENDPOINT}/api/v2/me/projects" | python -m json.tool | sed -n -e '/"name":/ s/^.*"name": "\(.*\)".*/\1/p')"
    else
        echo "$ANDROID and $IOS were both false.. exiting" >&2
        exit 1
    fi
    echo "Created project with name: ${PROJECT_NAME}"
    PROJECT_ID="$(curl -G -s -H "Accept: application/json" -u ${API_KEY}: "${API_ENDPOINT}/api/v2/me/projects?limit=1" --data-urlencode "search=${PROJECT_NAME}" | python -m json.tool | sed -n -e '/"id":/ s/^.* \(.*\),.*/\1/p')"
    echo "PROJECT_ID: ${PROJECT_ID}"
else
    # Replace all spaces in PROJECT_NAME with + signs
    #PROJECT_NAME=${PROJECT_NAME// /+}
    echo "Checking if project with name ${PROJECT_NAME} exists (spaces escaped with +)"

    # Check if Project exists
    PROJECT_ID="$(curl -G -s -H "Accept: application/json" -u ${API_KEY}: "${API_ENDPOINT}/api/v2/me/projects?limit=1" --data-urlencode "search=${PROJECT_NAME}" | python -m json.tool | sed -n -e '/"id":/ s/^.* \(.*\),.*/\1/p')"
    if [ -z ${PROJECT_ID} ] ; then
        #echo "Project not found, creating it now with name ${PROJECT_NAME} (spaces escaped with +)"
        if [ ${ANDROID} ] ; then
            echo "Creating Android project.."
            curl -H "Accept: application/json" -u ${API_KEY}: -X POST -F 'type=APPIUM_ANDROID_SERVER_SIDE' -F "name=${PROJECT_NAME}" "${API_ENDPOINT}/api/v2/me/projects"
        elif [ ${IOS} ] ; then
            echo "Creating iOS project.."
            curl -H "Accept: application/json" -u ${API_KEY}: -X POST -F 'type=APPIUM_IOS_SERVER_SIDE' -F "name=${PROJECT_NAME}" "${API_ENDPOINT}/api/v2/me/projects"
        else
            echo "$ANDROID and $IOS were both false.. exiting" >&2
            exit 1
        fi
        echo
        PROJECT_ID="$(curl -s -H "Accept: application/json" -u ${API_KEY}: "${API_ENDPOINT}/api/v2/me/projects?limit=1" --data-urlencode "search=${PROJECT_NAME}" | python -m json.tool | sed -n -e '/"id":/ s/^.* \(.*\),.*/\1/p')"
        echo "Project created with ID: ${PROJECT_ID} and name: ${PROJECT_NAME}"
    else
        echo "Project found with ID: ${PROJECT_ID}"
    fi
fi

# Check that the used project is of correct type with -a/i flag
PROJECT_TYPE="$(curl -s -H "Accept: application/json" -u ${API_KEY}: "${API_ENDPOINT}/api/v2/me/projects/${PROJECT_ID}" | python -m json.tool | sed -n -e '/"type":/ s/^.*"type": "\(.*\)".*/\1/p')"
echo "PROJECT_TYPE: ${PROJECT_TYPE}"
PROJECT_FRAMEWORK="$(curl -s -H "Accept: application/json" -u ${API_KEY}: "${API_ENDPOINT}/api/v2/me/projects/${PROJECT_ID}" | python -m json.tool | sed -n -e '/"frameworkId":/ s/^.* \(.*\),.*/\1/p')"
echo "PROJECT_FRAMEWORK: ${PROJECT_FRAMEWORK}"
if [[ ( "${ANDROID}" = true && "${PROJECT_TYPE}" -eq "CALABASH" && "${PROJECT_FRAMEWORK}" -eq 541 ) \
   || ( "${IOS}" = true && "${PROJECT_TYPE}" -eq "CALABASH_IOS" && "${PROJECT_FRAMEWORK}" -eq 542 ) \
   || ( "${ANDROID}" = true && "${PROJECT_TYPE}" -eq "APPIUM_ANDROID_SERVER_SIDE" ) \
   || ( "${IOS}" = true && "${PROJECT_TYPE}" -eq "APPIUM_IOS_SERVER_SIDE" ) ]] ; then
    :
else
    echo "Mismatch: -a:${ANDROID}/-i:${IOS} PROJECT_TYPE:${PROJECT_TYPE} & PROJECT_FRAMEWORK:${PROJECT_FRAMEWORK}"
    exit
fi

# Check that Device Group exists
echo "DEVICE_GROUP_NAME: ${DEVICE_GROUP_NAME}"
DEVICE_GROUP_ID="$(curl -G -s -H "Accept: application/json" -u ${API_KEY}: "${API_ENDPOINT}/api/v2/me/device-groups?withPublic=true" --data-urlencode "limit=1" --data-urlencode "search=${DEVICE_GROUP_NAME}" | python -m json.tool | sed -n -e '/"id":/ s/^.* \(.*\),.*/\1/p')"
echo "DEVICE_GROUP_ID: ${DEVICE_GROUP_ID}"
if [ -z ${DEVICE_GROUP_ID} ]; then
    echo "No DEVICE_GROUP_ID found; Device group with name \"${DEVICE_GROUP_NAME}\" doesn't seem to exist."
    exit
fi

# Check that Device Group is of correct type
DEVICE_GROUP_TYPE="$(curl -s -H "Accept: application/json" -u ${API_KEY}: "${API_ENDPOINT}/api/v2/me/device-groups/${DEVICE_GROUP_ID}" | python -m json.tool | sed -n -e '/"osType":/ s/^.*"osType": "\(.*\)".*/\1/p')"
echo "DEVICE_GROUP_TYPE: ${DEVICE_GROUP_TYPE}"
if [[ ( "${ANDROID}" = true && "${DEVICE_GROUP_TYPE}" -eq "ANDROID" ) || ( "${IOS}" = true && "${DEVICE_GROUP_TYPE}" -eq "IOS" ) ]] ; then
    :
else
    echo "Mismatch: -a:${ANDROID}/-i:${IOS} DEVICE_GROUP_TYPE:${DEVICE_GROUP_TYPE}"
    exit
fi

# Create and upload zip file if -t was given and if project has no zip uploaded yet
ZIP_EXISTS="$(curl -H "Accept: application/json" -u ${API_KEY}: "${API_ENDPOINT}/api/v2/me/projects/${PROJECT_ID}/files" | python -m json.tool | sed -n -e '/"test":/ s/^.*"test": \(.*\)"*/\1/p')"
if [[ ( -z ${UPLOAD} ) && ( ${ZIP_EXISTS} -ne "null" ) ]]; then
    :
else
    # Set the name of zip and copy correct sh template
    if [[ ( "${ANDROID}" = true ) ]] ; then
        OUT_ZIP=$(./createAndroidZip.sh)
    elif [[ ( "${IOS}" = true ) ]] ; then
        OUT_ZIP=$(./createiOSZip.sh)
    else
        echo "$ANDROID and $IOS were both false.. exiting"
        exit
    fi

    # Upload zip to Testdroid Cloud.
    echo "Uploading ${OUT_ZIP} to Project with ID ${PROJECT_ID}"
    curl -H "Accept: application/json" -u ${API_KEY}: -X POST -F "file=@${OUT_ZIP}" "${API_ENDPOINT}/api/v2/me/projects/${PROJECT_ID}/files/test"
    echo
fi

APP_EXISTS="$(curl -H "Accept: application/json" -u ${API_KEY}: "${API_ENDPOINT}/api/v2/me/projects/${PROJECT_ID}/files" | python -m json.tool | sed -n -e '/"app":/ s/^.*"app": \(.*\)"*,/\1/p')"
# Upload APP_FILE if -f was given or if project has no app uploaded yet
if [[ ( -z ${APP_FILE} ) && ( ${APP_EXISTS} -ne "null" ) ]]; then
    :
else
    if [[ -z ${APP_FILE} ]]; then
        echo "No app file given in -f and no app has been previously uploaded to the project! Exiting."
        exit
    else
        echo "-f was given, uploading the file given: ${APP_FILE}"
        curl -H "Accept: application/json" -u ${API_KEY}: -X POST -F "file=@${APP_FILE}" "${API_ENDPOINT}/api/v2/me/projects/${PROJECT_ID}/files/application"
        echo
    fi
fi

echo
echo "Launching test in Testdroid!"
TESTRUN_ID="$(curl -s -H "Accept: application/json" -u ${API_KEY}: -X POST "${API_ENDPOINT}/api/v2/me/projects/${PROJECT_ID}/runs?usedDeviceGroupId=${DEVICE_GROUP_ID}" | python -m json.tool | sed -n -e '/"id":/ s/^.* \(.*\),.*/\1/p')"

if [ -z ${TESTRUN_ID} ] ; then
    echo "TESTRUN_ID not gotten, the test probably wasn't launched properly.. exiting."
    exit
else
    echo "Testrun ID: ${TESTRUN_ID}"
    TEST_STATE="WAITING"
    while [ ${TEST_STATE} != "\"FINISHED\"" ] ; do
        TEST_STATE="$(curl -s -H "Accept: application/json" -u ${API_KEY}: "${API_ENDPOINT}/api/v2/me/projects/${PROJECT_ID}/runs/${TESTRUN_ID}" | python -m json.tool | sed -n -e '/"state":/ s/^.* \(.*\),.*/\1/p')"
        echo "TEST_STATE = ${TEST_STATE}"
        sleep 10
    done
fi

# Replace 'com/cloud' with 'com' from the end, if it exists due to private cloud API endpoint.
API_ENDPOINT=${API_ENDPOINT//com\/cloud/com}
echo "TEST DONE! The test results are available at ${API_ENDPOINT}/#service/testrun/${PROJECT_ID}/${TESTRUN_ID}"