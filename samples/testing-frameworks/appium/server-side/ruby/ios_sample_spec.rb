require 'rspec'
require 'appium_lib'
require_relative 'setup_appium'

describe "TestSuite" do
  before :all do
    @log = SetupAppium.new
    SetupAppium.new.set_screenshot_dir
    @driver = SetupAppium.new.get_ios_driver
  end

  after :all do
    @log.log("Stop WebDriver")
    @driver.quit_driver
  end

  it "should show failure page"  do
    @log.log("view1: Clicking button  - RadioButton 1")
    @driver.find_element(:name, "answer1").click

    @log.log("view1: Typing in textfield: Bitbar user")
    @driver.find_element(:name, "your name").send_keys("Bitbar user")
    @driver.hide_keyboard('return', :pressKey)

    @log.log("view1: Taking screenshot screenshot1.png")
    @driver.screenshot("#{SetupAppium.screenshot_dir}screenshot1.png")
    sleep(5)

    @log.log("view1: Clicking button - Answer  Button")
    @driver.find_element(:name, "sendAnswer").click

    @log.log("view2: Taking screenshot screenshot2.png")
    @driver.screenshot("#{SetupAppium.screenshot_dir}screenshot2.png")
    expect(@driver.find_element(:name, 'Wrong Answer!').text).to match('Wrong Answer!')
    sleep(5)
  end

  it "should click back button" do
    @log.log("view2: Clicking button - Back/OK button")
    @driver.find_element(:name, "back").click
  end

  it "should click 2nd radio button" do
    @log.log("view1: Clicking button - RadioButton 2")
    @driver.find_element(:name, "answer2").click

    @log.log("view1: Clicking button - Answer  Button")
    @driver.find_element(:name, "sendAnswer").click

    @log.log("view1: Taking screenshot screenshot4.png")
    @driver.screenshot("#{SetupAppium.screenshot_dir}screenshot4.png")
    expect(@driver.find_element(:name, 'You are right!').text).to match('You are right!')
    @log.log("view1: Sleeping 3 before quitting webdriver")
    sleep(3)
  end

end
