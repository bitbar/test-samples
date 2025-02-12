# Appium Client Side + Nightwatch

### Setup

* Install NPM Dependencies

    ```sh
    npm install
    ```

* Add your apiKey to `./.credentials.json`

   Create a file called ".credentials.json" in the project's root and add your bitbar apiKey to it as described below:

    ```json
    {
        "apiKey": "YOUR_BITBAR_CLOUD_APIKEY"
    }
    ```

* Modify the `nightwatch.json` file according to your project

### Run

* To run the test on iOS

    ```sh
    npm run test:ios
    ```

* To run the test on Android

    ```sh
    npm run test:android
    ```
