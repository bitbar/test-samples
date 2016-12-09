@echo off

call mvn clean test -DexportResults=true %*

for /f "delims=" %%i in (target\sessionid.txt) do (
	for %%f in (target\surefire-reports\junitreports\TEST-*.xml) do (
	    echo %%f
	    curl -i -F result=@"%%f" "http://appium.testdroid.com/upload/result?sessionId=%%i"
		GOTO:EOF
    )
)
