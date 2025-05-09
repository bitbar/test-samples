describe("Pass biometric authentication", () => {
  it("should wait for injection to be ready", async () => {
    const alertBiometrics = await $(
      '//XCUIElementTypeAlert[@name="Biometric Authentication"]'
    );
    await alertBiometrics.waitForDisplayed({ timeout: 60000 });
    const visible = await alertBiometrics.isDisplayed();
    visible.should.be.true;
    await browser.execute("mobile: alert", {
      action: "accept",
      buttonLabel: "OK",
    });
  });

  it("should click [BIOMETRIC AUTHENTICATION] button", async () => {
    const buttonBiometrics = await $(
      '(//XCUIElementTypeOther[@name="Biometric authentication"])[3]'
    );
    await buttonBiometrics.waitForDisplayed({ timeout: 10000 });
    const visible = await buttonBiometrics.isDisplayed();
    visible.should.be.true;
    await buttonBiometrics.click();
  });

  it("should click [ASK BIOMETRIC AUTHENTICATION] button", async () => {
    const buttonAskBiometrics = await $(
      '//XCUIElementTypeOther[@name="Ask biometric authentication"]'
    );
    await buttonAskBiometrics.waitForDisplayed({ timeout: 10000 });
    const visible = await buttonAskBiometrics.isDisplayed();
    visible.should.be.true;
    await buttonAskBiometrics.click();
  });

  it("should show biometric alert", async () => {
    const alertBiometrics = await $(
      '//XCUIElementTypeAlert[@name="Biometric Authentication"]'
    );
    await alertBiometrics.waitForDisplayed({ timeout: 10000 });
    const visible = await alertBiometrics.isDisplayed();
    visible.should.be.true;
  });

  it("should pass biometric authentication", async () => {
    const result = await driver.execute("mobile: alert", {
      action: "accept",
      buttonLabel: "Pass",
    });
    const textResult = await $(
      '//XCUIElementTypeStaticText[@name="Authentication status value"]'
    );
    await driver.waitUntil(
      async () => (await textResult.getText()) === "SUCCEEDED",
      {
        timeout: 2000,
        timeoutMsg: "expected text to be 'SUCCEEDED' after 2s",
      }
    );
    const text = await textResult.getText();
    text.should.be.equal("SUCCEEDED");
  });
});
