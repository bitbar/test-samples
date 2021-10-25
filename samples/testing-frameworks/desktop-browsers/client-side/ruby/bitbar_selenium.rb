require 'selenium-webdriver'

#
# IMPORTANT: Set the following parameters according to your needs.
# You can use Capabilities creator:
# https://cloud.bitbar.com/#public/capabilities-creator
# Please mind bitbar_apiKey is required and can be found at
# https://cloud.bitbar.com/#user/my-account (My Integrations > API Access)
#

# user-customizable parameters start here
capabilities = Selenium::WebDriver::Remote::Capabilities.new
capabilities['bitbar_apiKey'] = '<insert your Bitbar API key here>'
capabilities['platform'] = 'Windows'
capabilities['browserName'] = 'Chrome'
capabilities['version'] = '94'
capabilities['resolution'] = '1920x1080'
capabilities['bitbar_project'] = 'Selenium sample project'
capabilities['bitbar_testrun'] = 'Ruby sample test'
capabilities['bitbar_testTimeout'] = '600'
# user-customizable parameters end here

if capabilities['platform'].downcase == 'windows'
  hub_url = "https://westeurope-hub.bitbar.com/wd/hub"
elsif capabilities['platform'].downcase == 'linux'
  hub_url = "https://broker-cloud.bitbar.com/wd/hub"
else
  raise "Unsupported platform"
end

begin
	driver = Selenium::WebDriver.for(:remote,
	  :url => hub_url,
	  :capabilities => capabilities)

	# check page title
	test_url = "https://bitbar.github.io/web-testing-target/"
	driver.navigate.to test_url
	expected_title = "Bitbar - Test Page for Samples"
	raise "Wrong page title" unless expected_title == driver.title
	puts driver.title

	# click "Click for answer" button
	button = driver.find_element(:xpath, '//button[contains(., "Click for answer")]')
	button.click()

	# check answer text
	driver.find_element(:xpath, '//p[@id="result_element" and contains(., "Bitbar")]')
	puts driver.find_element(:id, ('result_element')).text

	# verify button changed color
	style = button.attribute('style')
	expected_style = "background-color: rgb(127, 255, 0);"
	raise "Wrong button styling" unless expected_style == style
ensure
	if driver
		driver.quit
	end
end
