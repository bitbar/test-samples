require 'fileutils'

class SetupAppium

  @@screenshot_dir = './target/reports/screenshots/'
  app_file_android = Dir.glob 'application.apk'
  app_file_ios = Dir.glob 'application.ipa'
  app_path = Dir.pwd
  @@app_file_android = File.join(app_path, app_file_android)
  @@app_file_ios = File.join(app_path, app_file_ios)
  @@udid = ENV['IOS_UDID']


  def self.screenshot_dir
    # Return the value of this variable
    @@screenshot_dir
  end

  def log(msg)
    puts "#{Time.now}: #{msg}"
  end

  def get_android_driver
    desired_caps = {}
    desired_caps['platformName'] = 'Android'
    desired_caps['automationName'] = 'Appium'
    desired_caps['appPackage'] = 'com.bitbar.testdroid'
    desired_caps['appActivity'] = '.BitbarSampleApplicationActivity'
    desired_caps['deviceName'] = 'Android device'
    desired_caps['app'] = "#{@@app_file_android}"
    server_url = 'http://localhost:4723/wd/hub'
    @driver = Appium::Driver.new({:caps => desired_caps, :appium_lib => {:server_url => server_url}, :global_driver => true})
    @driver.start_driver()
    # Wait max 30 seconds for elements
    @driver.set_implicit_wait(30)

    log("WebDriver response received")
    return @driver
  end

  def get_ios_driver
    desired_caps = {}
    desired_caps['platformName'] = 'iOS'
    desired_caps['automationName'] = 'XCUITest'
    desired_caps['bundleId'] = 'com.bitbar.testdroid.BitbarIOSSample'
    desired_caps['deviceName'] = 'iPhone device'
    desired_caps['udid'] = "auto" # Use #{@@udid} if desired fixed udid
    desired_caps['app'] = "#{@@app_file_ios}"
    server_url = 'http://localhost:4723/wd/hub'
    @driver = Appium::Driver.new({:caps => desired_caps, :appium_lib => {:server_url => server_url}, :global_driver => true})
    @driver.start_driver()
    # Wait max 30 seconds for elements
    @driver.set_implicit_wait(30)

    log("WebDriver response received")
    return @driver
  end

  def quit_driver
    @driver.driver_quit
  end

  def set_screenshot_dir
    log("Will save screenshots at: #{@@screenshot_dir}")
    unless File.directory?(@@screenshot_dir)
      log('Creating directory %s' % @@screenshot_dir)
      FileUtils.mkdir_p(@@screenshot_dir)
    end
  end

end
