require 'selenium-webdriver'

#
# IMPORTANT: Set the following parameters according to your needs.
# You can use Capabilities creator:
# https://cloud.bitbar.com/#public/capabilities-creator
# Please mind apiKey is required and can be found at
# https://cloud.bitbar.com/#user/my-account (My Integrations > API Access)
#

# user-customizable parameters start here
capabilities = Selenium::WebDriver::Remote::Capabilities.new({
	'platformName' => 'Windows',
	'browserName' => 'chrome',
	'browserVersion' => 'latest',
	'bitbar:options' => {
		'project' => 'Selenium sample project',
		'testrun' => 'Ruby sample test',
		'apiKey' => '<insert your BitBar API key here>',
		'osVersion' => '11',
		'resolution' => '1920x1080'
	}
})
# user-customizable parameters end here

begin
	driver = Selenium::WebDriver.for(:remote,
	  :url => "https://us-west-desktop-hub.bitbar.com/wd/hub",
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
