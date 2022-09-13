# Testing parallel

## Test Execution

### Prerequisites  

1. Install Java

    This project is targeting Java 1.8. In order to run the test you'll need to download Java 1.8 or change source field in pom.xml

    ```xml
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.6.1</version>
        <configuration>
            <source>1.8</source>
            <target>1.8</target>
        </configuration>
    </plugin>
    ```

2. Get Maven

    http://maven.apache.org/

### Running tests

1. Specify target devices by adding .properties file to resources/properties directory.

    E.g. (GooglePixelXL.properties).
    ```properties
    bitbar_device = Google Pixel XL
    browser_name = chrome
    platform_name = Android
    device_name = Android Phone
    ```
2. Add devices to TestSuite.xml

    If you have already created a .properties file with choosen device now add it to TestSuite.xml file.

    E.g.
    ```xml
     <test name="Simple Google Test Google Pixel XL">
        <parameter name="Device" value="GooglePixelXL"/>
        <classes>
            <class name="SimpleGoogleTest"/>
        </classes>
    </test>
    ```
3. Run tests

    Run test with your Bitbar api key:
    ```
    mvn clean test -DBITBAR_API_KEY=<YOUR_BITBAR_API_KEY>
    ```