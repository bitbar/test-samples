#!/bin/bash

echo "Extracting tests.zip..."
unzip tests.zip

echo "Installing pip for python"
curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py
sudo python3 get-pip.py

echo "Installing requirements specified by requirements.txt"
chmod 0755 requirements.txt
sudo pip3 install -r requirements.txt
pip3 install selenium

python3 BitbarSampleWebTest.py

mv test-reports/*.xml TEST-all.xml
