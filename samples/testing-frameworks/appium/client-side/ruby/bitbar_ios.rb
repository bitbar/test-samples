# install the required gems with bundler by doing:
#   "bundle install"
# execute tests using rspec:
#  rspec bitbar_ios.rb
require 'json'
require 'rspec'
require 'selenium-webdriver'
require 'curb'
require 'appium_lib'
require 'selenium/webdriver/remote/http/curb'
require 'fileutils'


##
## IMPORTANT: Set the following parameters.
## Make sure that the screenshot directory already exists!
##
screen_shot_dir = "screenshot-folder"
bitbar_api_key = ENV["BITBAR_APIKEY"] #Your bitbar api key
bitbar_device = "Apple iPhone 7 A1778 15.4.1" # Example device. Change if you desire.
bitbar_app_file = "../../../../../apps/ios/bitbar-ios-sample.ipa"

##
##  If your app is already uploaded assign its ID to the bitbar_app_id (can be found in bitbar files library)
##
@bitbar_app_id = nil


##
##  Set other parameters if needed, see more on README
## 
desired_capabilities_cloud = {
    'platformName' => 'iOS',
    'appium:automationName' => 'XCUITest',
    'appium:bundleId' => 'com.bitbar.testdroid.BitbarIOSSample',
    'appium:deviceName' => 'iPhone device',
    'bitbar:options' => {
      'apiKey' => bitbar_api_key,
      'project' => 'Appium iOS Project1',
      'description' => 'Appium project description',
      'testrun' => 'Test Run 1',
      'device' => bitbar_device,
      'app' => @bitbar_app_id
    }
}


server_url = 'https://appium.bitbar.com/wd/hub'

def log(msg)
  puts "#{Time.now}: #{msg}"
end

def upload_application(file_path, bitbar_api_key)

  c = Curl::Easy.new("https://cloud.bitbar.com/api/v2/me/files")
  c.http_auth_types = :basic
  c.username = bitbar_api_key
  c.password = nil
  c.multipart_form_post = true
  c.verbose = true
  c.http_post(Curl::PostField.file("file", file_path))
  resp = JSON.parse(c.body_str)

  @bitbar_app_id = resp["id"]
end

describe "BitbarIOSSample testing" do
  before :all do
    if @bitbar_app_id == nil
      log ("Upload application #{bitbar_app_file}")
      upload_application(bitbar_app_file, bitbar_api_key)
      log ("Uploaded file id #{@bitbar_app_id}")
      desired_capabilities_cloud['bitbar:options']['app'] = @bitbar_app_id
    end
    log ("Start Webdriver with [#{desired_capabilities_cloud}]")
    @driver = Appium::Driver.new ({:caps => desired_capabilities_cloud, :appium_lib => {:server_url => server_url}})
    @web_driver = @driver.start_driver(:read_timeout => 999_999)

    log ("WebDriver response received")
  end

  after :all do
    log ("Stop WebDriver")
    @driver.driver_quit()
  end

  it "should show failure page" do
    log ("view1: Finding buttons")
    @driver.find_element(:name, "answer1").click

    log ("view1: Typing in edit field: Bitbar user")
    @driver.find_element(:name, "userName").send_keys("Bitbar user\n")
    @driver.hide_keyboard('return', :pressKey)
    sleep(2)

    log ("view1: Taking screenshot screenshot1.png")
    @driver.screenshot(screen_shot_dir + "/screenshot1.png")
    sleep(2)

    log ("view1: Clicking Send Answer  Button")
    @driver.find_element(:name, "sendAnswer").click
    sleep(2)

    log ("view2: Taking screenshot screenshot2.png")
    @driver.screenshot(screen_shot_dir + "/screenshot2.png")
    sleep(2)
  end

  it "should click back button" do
    # view2
    log ("view2: Clicking button - Back/OK button")
    @driver.find_element(:name, "back").click
    sleep(2)
    log ("view2: Taking screenshot screenshot3.png")
    @driver.screenshot(screen_shot_dir + "/screenshot3.png")
  end

  it "should click 2nd radio button" do

    log ("view1: Clicking answer2 - RadioButton 2")

    @driver.find_element(:name, "answer2").click
    sleep(1)
    log ("view1: Taking screenshot screenshot4.png")
    @driver.screenshot(screen_shot_dir + "/screenshot4.png")

    log ("view1: Clicking Send Answer Button")
    @driver.find_element(:name, "sendAnswer").click

    log ("view1: Taking screenshot screenshot5.png")
    @driver.screenshot(screen_shot_dir + "/screenshot5.png")

    log ("view1: Sleeping 3 before quitting webdriver")
    sleep(3)

  end

end

