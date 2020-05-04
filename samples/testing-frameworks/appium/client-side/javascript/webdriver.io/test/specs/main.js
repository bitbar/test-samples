describe('Bitbar Sample App', () => {
    it('Should radio button be visible ', async () => {
        let el = await $('//android.widget.RadioButton[@text="Buy 101 devices"]');
        let visible = await el.isDisplayed();
        visible.should.be.true;
    });

    it('Should show failure page', async () => {
        let el;

        console.log("view1: Clicking button - 'Buy 101 devices'");
        el = await $('//android.widget.RadioButton[@text="Buy 101 devices"]');
        el.click();

        console.log("view1: Typing in textfield[0]: Bitbar user");
        el = await $('//android.widget.EditText[@resource-id="com.bitbar.testdroid:id/editText1"]');
        el.setValue('Bitbar user');

        driver.hideKeyboard();
        console.log("view1: Taking screenshot screenshot1.png");
        await takeScreenshot('screenshot1');

        console.log("view1: Clicking button Answer");
        el = await $('//android.widget.Button[@text="Answer"]');
        el.click();

        console.log("view2: Taking screenshot screenshot2.png");
        await takeScreenshot('screenshot2');

        el = await $('//android.widget.TextView[@text="Wrong Answer!"]');
        let txt = await el.getText();
        txt.should.be.equal('Wrong Answer!');
    });

});
