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
##
screen_shot_dir = "screenshot-folder"
bitbar_api_key = ENV["BITBAR_APIKEY"]
bitbar_device = "iPad 2 A1395 7.0.4" # Example device. Change if you desire.
bitbar_app_file = "BitbarIOSSample.ipa"


def log(msg)
  puts "#{Time.now}: #{msg}"
end

@bitbar_app = nil
desired_capabilities_cloud = {
    'device' => 'iphone',
    'bitbar_app' => nil,
    'bitbar_apiKey' => bitbar_api_key,
    'bitbar_project' => 'Appium iOS Project1',
    'bitbar_description' => 'Appium project description',
    'bitbar_testrun' => 'Test Run 1',
    'bitbar_device' => bitbar_device,
    'bitbar_target' => 'ios',
    'deviceName' => 'iPhone device',
    'platformName' => 'iOS',
    'bundleId' => 'com.bitbar.testdroid.BitbarIOSSample'
}


server_url = 'https://appium.bitbar.com/wd/hub'

def upload_application(file_path, bitbar_api_key)

  c = Curl::Easy.new("https://cloud.bitbar.com/api/v2/me/files")
  c.http_auth_types = :basic
  c.username = bitbar_api_key
  c.password = nil
  c.multipart_form_post = true
  c.verbose = true
  c.http_post(Curl::PostField.file("file", file_path))
  resp = JSON.parse(c.body_str)

  @bitbar_app = resp["id"]
end

describe "BitbarIOSSample testing" do
  before :all do

    FileUtils.mkdir_p 'screen_shot_dir'

    log ("Upload application #{bitbar_app_file}")
    # => upload_application(bitbar_app_file, bitbar_api_key)
    log ("Uploaded file id #{@bitbar_app}")

    desired_capabilities_cloud['bitbar_app'] = "latest"

    log ("Start Webdriver with [#{desired_capabilities_cloud}]")
    @appium_driver = Appium::Driver.new ({:caps => desired_capabilities_cloud, :appium_lib => {:server_url => server_url}})
    @web_driver = @appium_driver.start_driver()

    log ("WebDriver response received")
  end

  after :all do
    log ("Stop WebDriver")

    @appium_driver.driver_quit
  end

  it "should show failure page" do
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
