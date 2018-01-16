
# install the required gems with bundler by doing:
#   "bundle install"
# execute tests using rspec:
#  rspec testdroid_ios.rb
require 'json'
require 'rspec'
require 'selenium-webdriver'
require 'curb'
require 'appium_lib'
require 'selenium/webdriver/remote/http/curb'
require 'fileutils'



##
## IMPORTANT: Set the following parameters.
##
screen_shot_dir= "screenshot-folder"
testdroid_username = ENV["TESTDROID_USERNAME"]
testdroid_password = ENV["TESTDROID_PASSWORD"]
testdroid_device = "iPad 2 A1395 7.0.4" # Example device. Change if you desire.
testdroid_app_file = "BitbarIOSSample.ipa" 


def log(msg)
    puts "#{Time.now}: #{msg}"
end
@testdroid_app=nil
desired_capabilities_cloud={
        'device'=> 'iphone',
        'testdroid_app'=> nil,
        'testdroid_username'=> testdroid_username,
        'testdroid_password'=> testdroid_password,
        'testdroid_project'=> 'Appium iOS Project1',
        'testdroid_description'=> 'Appium project description',
        'testdroid_testrun'=> 'Test Run 1',
        'testdroid_device'=> testdroid_device,
        'testdroid_target' => 'ios',
        'deviceName' => 'iPhone device',
        'platformName' => 'iOS',
        'bundleId' => 'com.bitbar.testdroid.BitbarIOSSample'
    }


server_url = 'https://appium.bitbar.com/wd/hub'

def upload_application(file_path, username, password)
  
  c = Curl::Easy.new("https://appium.bitbar.com/upload")
  c.http_auth_types = :basic
  c.username = username
  c.password = password
  c.multipart_form_post = true
  c.verbose = true
  c.http_post(Curl::PostField.file('BitbarIOSSample.ipa', file_path))
  resp = JSON.parse(c.body_str)

  @testdroid_app = resp["value"]["uploads"]["BitbarIOSSample.ipa"]
end

describe "BitbarIOSSample testing" do
  before :all do
    
    FileUtils.mkdir_p 'screen_shot_dir'

    log ("Upload application #{testdroid_app_file}")
    # => upload_application(testdroid_app_file, testdroid_username , testdroid_password)
    log ("Uploaded file uuid #{@testdroid_app}")

    desired_capabilities_cloud['testdroid_app']="latest"

    log ("Start Webdriver with [#{desired_capabilities_cloud}]")
    @appium_driver = Appium::Driver.new  ({:caps => desired_capabilities_cloud, :appium_lib =>{ :server_url => server_url}})
    @web_driver = @appium_driver.start_driver()

    log ("WebDriver response received")
   end

  after :all do
    log ("Stop WebDriver")

    @appium_driver.driver_quit
  end

  it "should show failure page"  do
    log ("view1: Finding buttons")
    buttons = @appium_driver.find_elements(:xpath, "//UIAApplication[1]/UIAWindow[1]/UIAButton")
    log ("view1: Clicking button [0] - RadioButton 1")
    buttons[0].click()

    log ("view1: Typing in textfield[0]: Testdroid user")
    @appium_driver.find_element(:name, "userName").send_keys("Testdroid user\n")

    log ("view1: Taking screenshot screenshot1.png")
    @appium_driver.screenshot(screen_shot_dir + "/screenshot1.png")
    sleep(5)

    log ("view1: Taking screenshot screenshot2.png")
    @appium_driver.screenshot(screen_shot_dir + "/screenshot2.png")
    sleep(5)
    log ("view1: Clicking button[6] - Answer  Button")
    buttons[6].click()

    log ("view2: Taking screenshot screenshot3.png")
    @appium_driver.screenshot(screen_shot_dir + "/screenshot3.png")
    sleep(5)
  end

  it "should click back button" do
     # view2
    log ("view2: Finding buttons")
    buttons = @appium_driver.find_elements(:xpath, "//UIAApplication[1]/UIAWindow[1]/UIAButton")
    log ("view2: Clicking button[0] - Back/OK button")
    buttons[0].click()
  end

  it "should click 2nd radio button" do

    log ("view1: Clicking button[2] - RadioButton 2")

    buttons = @appium_driver.find_elements(:xpath, "//UIAApplication[1]/UIAWindow[1]/UIAButton")
    buttons[2].click()

    log ("view1: Clicking button[6] - Answer Button")
    buttons[6].click()

    log ("view1: Taking screenshot screenshot4.png")
    @appium_driver.screenshot(screen_shot_dir + "/screenshot4.png")

    log ("view1: Sleeping 3 before quitting webdriver")
    sleep(3)

  end

  it "should get window size" do
    size = @appium_driver.manage.window.size
    size.width.should eq(768)
    size.height.should eq(1024)
  end

end
