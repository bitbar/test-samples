require 'selenium-webdriver'

#
# IMPORTANT: Set the following parameters according to your needs.
# You can use Capabilities creator:
# https://bitbar.github.io/capabilities-creator/
# Please mind bitbar_apiKey is required and can be found at
# https://cloud.bitbar.com/#user/my-account (My Integrations > API Access)
#

# user-customizable parameters start here
desired_capabilities = Selenium::WebDriver::Remote::Capabilities.new
desired_capabilities['bitbar_apiKey'] = '<insert your Bitbar API key here>'
desired_capabilities['platform'] = 'windows'
desired_capabilities['browserName'] = 'chrome'
desired_capabilities['version'] = '72'
desired_capabilities['resolution'] = '1920x1080'
desired_capabilities['bitbar_project'] = 'Selenium sample project'
desired_capabilities['bitbar_testrun'] = 'Ruby sample test'
desired_capabilities['bitbar_testTimeout'] = '600'
# user-customizable parameters end here

begin
	driver = Selenium::WebDriver.for(:remote,
	  :url => "http://hub.bitbar.com/wd/hub",
	  :desired_capabilities => desired_capabilities)
	  
	# check page title
	test_url = "http://bitbar.github.io/bitbar-samples/"
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
