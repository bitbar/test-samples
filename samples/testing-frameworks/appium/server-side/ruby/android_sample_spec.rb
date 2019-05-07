require 'rspec'
require 'appium_lib'
require_relative 'setup_appium'

describe "TestSuite" do
  before :all do
    @log = SetupAppium.new
    SetupAppium.new.set_screenshot_dir
    @driver = SetupAppium.new.get_android_driver
  end

  after :all do
    @log.log("Stop WebDriver")
    @driver.quit_driver
  end

  it "should show failure page"  do
    @log.log("view1: Clicking button - 'Buy 101 devices'")
    @driver.find_element(:id, 'com.bitbar.testdroid:id/radio0').click

    @log.log("view1: Typing in textfield[0]: Bitbar user")
    @driver.find_element(:id, 'com.bitbar.testdroid:id/editText1').send_keys("Bitbar user")
    @driver.hide_keyboard
    @log.log("view1: Taking screenshot screenshot1.png")
    @driver.screenshot("#{SetupAppium.screenshot_dir}screenshot1.png")

    @log.log("view1: Clicking button Answer")
    @driver.find_element(:id, 'com.bitbar.testdroid:id/button1').click

    @log.log("view2: Taking screenshot screenshot2.png")
    @driver.screenshot("#{SetupAppium.screenshot_dir}screenshot2.png")
    expect(@driver.find_element(:id, 'com.bitbar.testdroid:id/textView1').text).to match('Wrong Answer!')
    sleep(2)
  end

  it "should click back button" do
    # view2
    @log.log("view2: Go back")
    @driver.back
  end

  it "should click 2nd radio button" do
    @log.log("view1: Clicking button - 'Use Testdroid Cloud'")
    @driver.find_element(:id, 'com.bitbar.testdroid:id/radio1').click
    @driver.screenshot("#{SetupAppium.screenshot_dir}screenshot3.png")
    @log.log("view1: Clicking Answer")
    @driver.find_element(:id, 'com.bitbar.testdroid:id/button1').click

    @log.log("view1: Taking screenshot screenshot4.png")
    @driver.screenshot("#{SetupAppium.screenshot_dir}screenshot4.png")
    expect(@driver.find_element(:id, 'com.bitbar.testdroid:id/textView1').text).to match('You are right!')
    @log.log("view1: Sleeping 3 before quitting webdriver")
    sleep(3)
  end

end