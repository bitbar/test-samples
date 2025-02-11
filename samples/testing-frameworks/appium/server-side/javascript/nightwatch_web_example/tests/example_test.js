
module.exports = {
    'Basic Test Example' : function (browser) {
        browser
            .url('https://bitbar.github.io/web-testing-target/')
            .pause(2000)
            .assert.title("Bitbar - Test Page for Samples")
            .end();
    }
};
