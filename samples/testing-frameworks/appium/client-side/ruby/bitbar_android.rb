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
bitbar_api_key = ENV["BITBAR_APIKEY"]
bitbar_device = "Motorola Nexus 6 7.1.1" # Example device. Change if you desire.
bitbar_app_file = "../../../../../apps/android/bitbar-sample-app.apk"

##
##  If your app is already uploaded assign its ID to the bitbar_app_id (can be found in bitbar files library) or pass the
##  path for downloading your application in order to upload it to the cloud
##
@bitbar_app_id = nil

def log(msg)
  puts "#{Time.now}: #{msg}"
end

##
##  Set other parameters if needed, see more on README
##
desired_capabilities_cloud = {
    'platformName' => 'Android',
    'appium:options' => {
      'automationName' => 'uiautomator2',
      'fullReset' => false,
      'noReset' => true,
      'app-package' => 'com.bitbar.testdroid',
      'app-activity' => '.BitbarSampleApplicationActivity'
    },
    'bitbar:options' => {
      'apiKey' => bitbar_api_key,
      'project' => 'Appium Ruby Demo',
      'description' => 'Appium project description',
      'testrun' => 'Test Run 1',
      'device' => bitbar_device,
      'app' => @bitbar_app_id
      #'appiumVersion' => "1.22.3", # launch on appium 1
    },
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
  @bitbar_app_id = resp["id"]
end

describe "BitbarSampleApp testing" do
  before :all do
    if @bitbar_app_id == nil
      log ("Upload application #{bitbar_app_file}")
      upload_application(bitbar_app_file, bitbar_api_key)
      log ("Uploaded file #{@bitbar_app_id}")
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
    log ("view1: Clicking button - 'Buy 101 devices'")
    @driver.find_element(:id, 'com.bitbar.testdroid:id/radio0').click
    log ("view1: Typing in textfield[0]: Bitbar user")
    @driver.find_element(:class, 'android.widget.EditText').send_keys("Bitbar user")
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

