Intro
=====

This folder contains a sample client side Selenium test for desktop browsers in BitBar Cloud.
To run the sample, go to `https://cloud.bitbar.com/` and use a valid Bitbar API key.

Folder Content
--------------

This folder contains the following files:

* `src/test/java/com/bitbar/selenium/BitbarSelenium.java` is the Java sample test.

* `pom.xml` defines the Maven project and pulls the Selenium Java dependency used by the sample.

Requirements
------------

* Java 8 or newer
* Maven 3.6 or newer
* A valid Bitbar API key

Before Running
--------------

Add your BitBar Key here in BitbarSelenium.java:-
* `bitbarOptions.put("apiKey", "<insert your Bitbar API key here>");`

Run Locally
-----------

From this folder, run:

```bash
mvn -q -DskipTests test-compile org.codehaus.mojo:exec-maven-plugin:3.6.0:java \
  -Dexec.mainClass=com.bitbar.selenium.BitbarSelenium \
  -Dexec.classpathScope=test
```

Expected Output
---------------

A successful run prints output similar to:

```text
Bitbar - Test Page for Samples
Bitbar
```
