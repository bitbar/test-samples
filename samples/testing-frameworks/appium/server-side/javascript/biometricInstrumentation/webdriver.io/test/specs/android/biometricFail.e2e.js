describe("Fail biometric authentication", () => {
  it("should click [BIOMETRIC AUTHENTICATION] button", async () => {
    const buttonBiometrics = await $(
      '//android.view.ViewGroup[@content-desc="Biometric authentication"]'
    );
    await buttonBiometrics.waitForDisplayed({ timeout: 10000 });
    const visable = await buttonBiometrics.isDisplayed();
    visable.should.be.true;
    await buttonBiometrics.click();
  });

  it("should click [ASK BIOMETRIC AUTHENTICATION] button", async () => {
    const buttonAskBiometrics = await $(
      '//android.view.ViewGroup[@content-desc="Ask biometric authentication"]'
    );
    await buttonAskBiometrics.waitForDisplayed({ timeout: 10000 });
    const visable = await buttonAskBiometrics.isDisplayed();
    visable.should.be.true;
    await buttonAskBiometrics.click();
  });

  it("should show biometric alert", async () => {
    const alertBiometrics = await $("id=android:id/alertTitle");
    await alertBiometrics.waitForDisplayed({ timeout: 10000 });
    const alertTitle = await alertBiometrics.getText();
    alertTitle.should.be.equal("Biometric Authentication");
  });

  it("should fail biometric authentication", async () => {
    const actionButton = await $('//*[@text="FAIL"]');
    await actionButton.waitForDisplayed({ timeout: 10000 });
    actionButton.click();
    const textResult = await $(
      "android=new UiScrollable(new UiSelector().scrollable(true))" +
        '.scrollIntoView(new UiSelector().descriptionContains("Authentication status value"))'
    );
    await driver.waitUntil(
      async () => (await textResult.getText()) === "FAILED",
      {
        timeout: 2000,
        timeoutMsg: "expected text to be 'FAILED' after 2s",
      }
    );
    const text = await textResult.getText();
    text.should.be.equal("FAILED");
  });
});
