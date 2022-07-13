# install the required gems with bundler by doing:
#   "bundle install"
# execute tests using rspec:
#  rspec bitbar_android.rb
require 'json'
require 'rspec'
require 'selenium-webdriver'
require 'appium_lib'
require 'curb'
require 'selenium/webdriver/remote/http/curb'
include Selenium


##
## IMPORTANT: Set the following parameters.
## Make sure that the screenshot directory already exists!
##
screen_shot_dir = "screenshot-folder"
bitbar_api_key = "Your Bitbar api key"
bitbar_device = "Motorola Nexus 6 7.1.1"
bitbar_app_file = "../../../../../apps/android/bitbar-sample-app.apk"

##
##  If your app is already uploaded assign its ID to the bitbar_app_id (can be found in bitbar files library)
##
bitbar_app_id = nil

def log(msg)
  puts "#{Time.now}: #{msg}"
end

##
##  Set other parameters if needed, see more on README
## 
desired_capabilities_cloud = {
    'device' => 'Android',
    'platformName' => 'Android',
    'deviceName' => 'Android',
    'fullReset' => false,
    'noReset' => true,
    'bitbar_apiKey' => bitbar_api_key,
    'bitbar_project' => 'Appium Ruby Demo',
    'bitbar_description' => 'Appium project description',
    'bitbar_testrun' => 'Test Run 1',
    'bitbar_device' => bitbar_device,
    'app-package' => 'com.bitbar.testdroid',
    'app-activity' => '.BitbarSampleApplicationActivity'
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
  bitbar_app_id= resp["id"]
end

describe "BitbarSampleApp testing" do
  before :all do
    if bitbar_app_id == nil
      log ("Upload application #{bitbar_app_file}")
      upload_application(bitbar_app_file, bitbar_api_key)
      log ("Uploaded file id #{@bitbar_app}")
    end
    desired_capabilities_cloud['bitbar_app'] = bitbar_app_id
    http_client = WebDriver::Remote::Http::Curb.new
    log ("Start Webdriver with [#{desired_capabilities_cloud}]")
    @driver = Appium::Driver.new ({:caps => desired_capabilities_cloud, :appium_lib => {:server_url => server_url}})
    @web_driver = @driver.start_driver()
    log ("WebDriver response received")
  end

  after :all do
    log ("Stop WebDriver")
    @driver.driver_quit()
  end

  it "should show failure page" do
    log ("view1: Clicking button - 'Buy 101 devices'")
    @driver.find_element(:id, 'com.bitbar.testdroid:id/radio0').click
    log ("view1: Typing in textfield[0]: Bitbar user")
    @driver.find_element(:id, 'com.bitbar.testdroid:id/editText1').send_keys("Bitbar user")
    @driver.navigate.back()
    log ("view1: Taking screenshot screenshot1.png")
    @driver.screenshot(screen_shot_dir + "/screenshot1.png")
    log ("view1: Clicking button Answer")
    @driver.find_element(:id, 'com.bitbar.testdroid:id/button1').click
    log ("view2: Taking screenshot screenshot2.png")
    @driver.screenshot(screen_shot_dir + "/screenshot2.png")
    @driver.find_element(:id, 'com.bitbar.testdroid:id/textView1').text == 'Wrong Answer!'
    sleep(2)
  end

  it "should click back button" do
    log ("view2: Go back")
    @driver.navigate.back()
  end

  it "should click 2nd radio button" do
    log ("view1: Clicking button - 'Use Testdroid Cloud'")
    @driver.find_element(:id, 'com.bitbar.testdroid:id/radio1').click
    log ("view1: Taking screenshot screenshot3.png")
    @driver.screenshot(screen_shot_dir + "/screenshot3.png")
    log ("view1: Clicking Answer")
    @driver.find_element(:id, 'com.bitbar.testdroid:id/button1').click
    log ("view2: Taking screenshot screenshot4.png")
    @driver.screenshot(screen_shot_dir + "/screenshot4.png")
    @driver.find_element(:id, 'com.bitbar.testdroid:id/textView1').text == 'You are right!'
    log ("view2: Sleeping 3 before quitting webdriver")
    sleep(3)
  end

end
