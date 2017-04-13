
module.exports = {
    'Basic Test Example' : function (browser) {
        browser
            .url('http://testdroid.com/')
            .waitForElementVisible('body', 4000)
            .pause(2000)
            .assert.title('Mobile App Testing | Testdroid Technology by Bitbar')
            .end();
    }
};