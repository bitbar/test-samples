
# install the required gems with bundler by doing:
#   "bundle install"
# execute tests using rspec:
#  rspec testdroid_android.rb
require 'json'
require 'rspec'
require 'selenium-webdriver'
require 'curb'
require 'selenium/webdriver/remote/http/curb'
include Selenium


##
## IMPORTANT: Set the following parameters.
##
screen_shot_dir= "screenshot-folder"
testdroid_username = ENV["TESTDROID_USERNAME"]
testdroid_password = ENV["TESTDROID_PASSWORD"]
testdroid_device = "Asus Memo Pad 8 K011" # Example device. Change if you desire.
testdroid_app_file = "BitbarAndroidSample.apk" 


def log(msg)
    puts "#{Time.now}: #{msg}"
end
@testdroid_app=nil
desired_capabilities_cloud={
        'device'=> 'Android',
        'testdroid_app'=> nil,
        'testdroid_username'=> testdroid_username,
        'testdroid_password'=> testdroid_password,
        'testdroid_target'=> 'Android',
        'testdroid_project'=> 'Appium Android demo',
        'testdroid_description'=> 'Appium project description',
        'testdroid_testrun'=> 'Test Run 1',
        'testdroid_device'=> testdroid_device,
        'app-package' => 'com.bitbar.testdroid',
        'app-activity' => '.BitbarSampleApplicationActivity',
    }


server_url = 'http://appium.testdroid.com/wd/hub'

def upload_application(file_path, username, password)
  
  c = Curl::Easy.new("http://appium.testdroid.com/upload")
  c.http_auth_types = :basic
  c.username = username
  c.password = password
  c.multipart_form_post = true
  c.verbose = true
  c.http_post(Curl::PostField.file('BitbarAndroidSample.apk', file_path))
  resp = JSON.parse(c.body_str)

  @testdroid_app = resp["value"]["uploads"]["BitbarAndroidSample.apk"]
end

describe "BitbarAndroidSample testing" do
  before :all do
 
    log ("Upload application #{testdroid_app_file}")
    upload_application(testdroid_app_file, testdroid_username , testdroid_password)
    log ("Uploaded file uuid #{@testdroid_app}")

    desired_capabilities_cloud['testdroid_app']=@testdroid_app
    http_client = WebDriver::Remote::Http::Curb.new
    http_client.timeout = nil #not timeout for Webdriver calls
    log ("Start Wbdriver with [#{desired_capabilities_cloud}]")
    @driver = Selenium::WebDriver.for(:remote, :desired_capabilities => desired_capabilities_cloud, :url => server_url, :http_client => http_client)

    log ("WebDriver response received")
   end

  after :all do
    log ("Stop WebDriver")

    @driver.quit
  end

  it "should show failure page"  do
    log ("view1: Clicking button [0] - 'Buy 101 devices'")
    @driver.find_elements(:name, 'Buy 101 devices')[0].click()
    
    log ("view1: Typing in textfield[0]: Testdroid user")
    @driver.find_elements(:tag_name,  :EditText)[0].send_keys("Testdroid user")
    @driver.navigate.back()
    log ("view1: Taking screenshot screenshot1.png")
    @driver.save_screenshot(screen_shot_dir + "/screenshot1.png")
    
  
    log ("view1: Clicking button Answer")
    @driver.find_elements(:name, :Answer)[0].click()

    log ("view2: Taking screenshot screenshot2.png")
    @driver.save_screenshot(screen_shot_dir + "/screenshot2.png")
    @driver.find_elements(:name, 'Wrong Answer!') != nil
    sleep(2)
  end

  it "should click back button" do
     # view2
    log ("view2: Go back")
    @driver.navigate.back()
  end

  it "should click 2nd radio button" do
    log ("view1: Clicking button [0] - 'Use Testdroid Cloud'")
    @driver.find_elements(:name, 'Use Testdroid Cloud')[0].click()
    @driver.save_screenshot(screen_shot_dir + "/screenshot3.png")
    log ("view1: Clicking Answer")
    @driver.find_elements(:name, :Answer)[0].click()

    log ("view1: Taking screenshot screenshot4.png")
    @driver.save_screenshot(screen_shot_dir + "/screenshot4.png")
    @driver.find_elements(:name, 'You are right!') != nil
    log ("view1: Sleeping 3 before quitting webdriver")
    sleep(3)

  end

  it "should get window size" do
    size = @driver.manage.window.size
    size.width.should eq(1080)
    size.height.should eq(1920)
  end

end