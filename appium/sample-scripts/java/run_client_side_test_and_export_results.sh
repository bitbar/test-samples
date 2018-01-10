#!/bin/bash

file=target/sessionid.txt
if [ -f $file ]
then 
    rm $file
fi 

mvn clean test -DexportResults=true ${@} 

sessionid=$(head -n 1 target/sessionid.txt)
echo "Uploading results file for "$sessionid
cp target/surefire-reports/junitreports/TEST-*.xml target/TEST-all.xml
address="https://appium.bitbar.com/upload/result?sessionId="$sessionid
results=$PWD"/target/TEST-all.xml"
curl -i -F result=@$results $address
