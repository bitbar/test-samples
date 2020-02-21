describe('Bitbar Sample App', () => {
    it('2nd radio button should be clicked ', async () => {
        let el;

        console.log("view1: Clicking button - 'Use Testdroid Cloud'");
        el = await $('//XCUIElementTypeButton[@name="answer2"]');
        el.click();

        console.log("view1: Typing in UIATextField: Bitbar user");
        el = await $('//UIATextField');
        el.setValue('Bitbar user'+'\n');

        console.log("view1: Taking screenshot screenshot1.png");
        await takeScreenshot('screenshot1');

        console.log("view1: Clicking button Answer");
        el = await $('//XCUIElementTypeButton[@name="sendAnswer"]');
        el.click();

        await sleep(2000);
        console.log("view2: Taking screenshot screenshot2.png");
        await takeScreenshot('screenshot2');
        
        el = await $('//XCUIElementTypeStaticText[@name="You are right!"]');
        let txt = await el.getText();
        txt.should.be.equal('You are right!');

    });
    const sleep = (milliseconds) => {
        return new Promise(resolve => setTimeout(resolve, milliseconds))
    };
});
