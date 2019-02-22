# install the required gems with bundler by doing:
#   "bundle install"
# execute tests using rspec:
#  rspec bitbar_selendroid.rb
require 'json'
require 'rspec'
require 'selenium-webdriver'
require 'curb'
require 'selenium/webdriver/remote/http/curb'
include Selenium


##
## IMPORTANT: Set the following parameters.
##
screen_shot_dir = "screenshot-folder"
bitbar_api_key = ENV["BITBAR_APIKEY"]
bitbar_device = "Samsung Galaxy Nexus GT-I9250 4.2.2" # Example device. Change if you desire.
bitbar_app_file = "BitbarAndroidSample.apk"


def log(msg)
  puts "#{Time.now}: #{msg}"
end

@bitbar_app = nil
desired_capabilities_cloud = {
    'device' => 'Selendroid',
    'platformName' => 'Android',
    'deviceName' => 'Android',
    'bitbar_app' => nil,
    'bitbar_apiKey' => bitbar_api_key,
    'bitbar_target' => 'selendroid',
    'bitbar_project' => 'Appium Android(Selendroid) demo',
    'bitbar_description' => 'Appium project description',
    'bitbar_testrun' => 'Selendroid Run 1',
    'bitbar_device' => bitbar_device,
    'app-package' => 'com.bitbar.testdroid',
    'app-activity' => '.BitbarSampleApplicationActivity',
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

describe "BitbarAndroidSample testing" do
  before :all do

    log ("Upload application #{bitbar_app_file}")
    upload_application(bitbar_app_file, bitbar_api_key)
    log ("Uploaded file id #{@bitbar_app}")

    desired_capabilities_cloud['bitbar_app'] = @bitbar_app
    http_client = WebDriver::Remote::Http::Curb.new
    http_client.timeout = nil #not timeout for Webdriver calls
    log ("Start Webdriver with [#{desired_capabilities_cloud}]")
    @driver = Selenium::WebDriver.for(:remote, :desired_capabilities => desired_capabilities_cloud, :url => server_url, :http_client => http_client)

    log ("WebDriver response received")
  end
  after(:each) do
    if example.exception != nil
      @driver.save_screenshot(screen_shot_dir + "#{example.description.gsub(' ', '_')}") unless @driver.nil?
    end
  end
  after :all do
    log ("Stop WebDriver")

    @driver.quit
  end

  it "should show failure page" do
    log ("view1: Clicking RadioButton 0 - 'Buy 101 devices'")
    @driver.find_elements(:id, 'radio0')[0].click()

    log ("view1: Typing in EditText[0]: Testdroid user")
    @driver.find_elements(:tag_name, :EditText)[0].send_keys("Testdroid user")

    log ("view1: Taking screenshot screenshot1.png")
    @driver.save_screenshot(screen_shot_dir + "/screenshot1.png")
    sleep(2)
    @driver.navigate.back()

    log ("view1: Clicking button Answer")
    @driver.find_elements(:tag_name, :Button)[0].click()

    log ("view2: Taking screenshot screenshot2.png")
    @driver.save_screenshot(screen_shot_dir + "/screenshot2.png")
    find_elements(:link_text, 'Wrong Answer!').size == 1
    sleep(2)
  end

  it "should click back button" do
    # view2
    log ("view2: Go back")
    @driver.navigate.back()
  end

  it "should click 2nd radio button" do
    log ("view1: Clicking RadioButton 1 - 'Use Testdroid Cloud'")
    @driver.find_elements(:id, 'radio1')[0].click()
    @driver.save_screenshot(screen_shot_dir + "/screenshot3.png")
    log ("view1: Clicking Answer")
    @driver.find_elements(:tag_name, :Button)[0].click()


    log ("view1: Taking screenshot screenshot4.png")
    @driver.save_screenshot(screen_shot_dir + "/screenshot4.png")
    @driver.find_elements(:link_text, 'You are right!').size == 1

    log ("view1: Sleeping 3 before quitting webdriver")
    sleep(3)

  end

end
