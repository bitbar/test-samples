
# install the required gems with bundler by doing:
#   "bundle install"
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
testdroid_device = "iPad 3 A1416 7.0.4" # Example device. Change if you desire.
testdroid_app_file = "BitbarIOSSample.ipa" 


def log(msg)
    puts "#{Time.now}: #{msg}"
end
@testdroid_app=nil
desired_capabilities_cloud={
        'app'=> 'com.bitbar.testdroid.BitbarIOSSample',
        'device'=> 'iphone',
        'testdroid_app'=> nil,
        'testdroid_username'=> testdroid_username,
        'testdroid_password'=> testdroid_password,
        'testdroid_project'=> 'Appium iOS Project1',
        'testdroid_description'=> 'Appium project description',
        'testdroid_testrun'=> 'Test Run 1',
        'testdroid_device'=> testdroid_device,
    }


server_url = 'http://appium.testdroid.com/wd/hub'

def upload_application(file_path, username, password)
  
  c = Curl::Easy.new("http://appium.testdroid.com/upload")
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
    log ("view1: Finding buttons")
    buttons = @driver.find_elements(:tag_name, :button)
    log ("view1: Clicking button [0] - RadioButton 1")
    buttons[0].click()

    log ("view1: Typing in textfield[0]: Testdroid user")
    @driver.find_elements(:tag_name,  :textField)[0].send_keys("Testdroid user")

    y = 0.95
    x = 0.5
    log ("view1: Tapping at position (384, 0.95) - Estimated position of SpaceBar")
    @driver.execute_script("mobile: tap",{"touchCount" => "1","x" => x,"y" => y})

    log ("view1: Taking screenshot screenshot1.png")
    @driver.save_screenshot(screen_shot_dir + "/screenshot1.png")
    
    log ("view1: Hiding Keyboard")
    @driver.execute_script('UIATarget.localTarget().frontMostApp().keyboard().buttons()["Hide keyboard"].tap();')

    log ("view1: Taking screenshot screenshot2.png")
    @driver.save_screenshot(screen_shot_dir + "/screenshot2.png")

    log ("view1: Clicking button[6] - OK  Button")
    buttons[6].click()

    log ("view2: Taking screenshot screenshot3.png")
    @driver.save_screenshot(screen_shot_dir + "/screenshot3.png")
  end

  it "should click back button" do
     # view2
    log ("view2: Finding buttons")
    buttons = @driver.find_elements(:tag_name, :button)
    log ("view2: Clicking button[0] - Back/OK button")
    buttons[0].click()
  end

  it "should click 2nd radio button" do
    log ("view1: Finding buttons")
    buttons = @driver.find_elements(:tag_name, :button)
    log ("view1: Clicking button[2] - RadioButton 2")
    buttons[2].click()

    log ("view1: Clicking button[6] - OK Button")
    buttons[6].click()
    log ("view1: Taking screenshot screenshot4.png")
    @driver.save_screenshot(screen_shot_dir + "/screenshot4.png")

    log ("view1: Sleeping 3 before quitting webdriver")
    sleep(3)

  end

  it "should get window size" do
    size = @driver.manage.window.size
    size.width.should eq(768)
    size.height.should eq(1024)
  end

end