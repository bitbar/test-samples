
require "parallel_tests"
require 'selenium-webdriver'
require 'curb'
require 'selenium/webdriver/remote/http/curb'

#make sure that credentials has been set
bitbar_api_key = ENV["BITBAR_APIKEY"]


server_url = 'https://appium.bitbar.com/wd/hub'


RSpec.configure do |config|
  #require 'parallel_tests'
require 'selenium-webdriver'
  config.before(:suite) do
    #Define all the devices where tests will be executed 
  	device = ["Asus Google Nexus 7 ME370T 4.2.2", "Asus Google Nexus 7 ME370T 4.3 JWR66Y", "Asus Google Nexus 7 ME370T 4.4.2"]
    File.delete("upload.completed") if File.exist?('upload.completed')
    #Only the first process should do the app uploading - now it returns "latest" which means that use earlier uploaded file
    bitbar_app = ParallelTests.first_process? ? upload_file() : sleep_until_upload_completed()
    index = 0
    #TEST_ENV_NUMBER - has the current process number (1st = "" , 2nd = 1, 3rd = 2..etc)
    if (ENV['TEST_ENV_NUMBER'] == "" )
      index = 0
    else 
      index = ENV['TEST_ENV_NUMBER'].to_i-1
    end
    puts "Current device:"+device[index];
   
    #Set Testdroid cloud appium settings
    desired_capabilities_cloud = {
        'device' => 'Android',
        'bitbar_app' => bitbar_app,
        'bitbar_apiKey' => bitbar_api_key,
        'bitbar_project' => 'Appium Android demo',
        'bitbar_description' => 'Appium project description',
        'bitbar_testrun' => 'Test Run 1',
        'bitbar_device' => device[index],
        'appPackage' => 'com.bitbar.testdroid',
        'appActivity' => '.BitbarSampleApplicationActivity',
        'bitbar_target' => 'Android',
        'deviceName' => 'Android Phone',
        'platformName' => 'Android',
    }

    http_client = WebDriver::Remote::Http::Curb.new
    http_client.timeout = nil #not timeout for Webdriver calls
    log ("Start Webdriver with [#{desired_capabilities_cloud}]")

    driver = Selenium::WebDriver.for(:remote, :desired_capabilities => desired_capabilities_cloud, :url => server_url, :http_client => http_client)

    #Adding webdriver to Rspec configuration so we can access it from our tests
    config.add_setting :web_driver
    config.web_driver = driver


  end

  config.after(:suite) do
    puts "Test suite done"
    #make sure quit webdriver
    config.web_driver.quit if defined? config.web_driver 
  end

end

#Do the test uploading here - now this is just static sleep
def upload_file
  puts "uploading"
  
  sleep 20
  File.open("upload.completed", "w") {}
  puts "done"
  return "latest"
end

def sleep_until_upload_completed
  puts "Sleeping until upload completed"
  sleep(1) until  File.exist?('upload.completed')
  puts "File was uploaded"
  return "latest"
end

