
# install the required gems with bundler by doing:
#   "bundle install"
# execute tests using parallel_test gem:
#  parallel_rspec spec/
require 'json'
require 'selenium-webdriver'
require 'curb'

require File.dirname(__FILE__) + '/helper' 
include Selenium



def log(msg)
    puts "#{Time.now}: #{msg}"
end


describe "BitbarAndroid Sample testing" do

  it "Click on radio button" do
    log ("view1: Clicking button [0] - 'Use Testdroid Cloud'")
    RSpec.configuration.web_driver().find_elements(:name, 'Use Testdroid Cloud')[0].click()
    
  end

end

describe "BitbarAndroid click answer" do

    it "Click on answer button" do
      log ("Clicking button Answer")
    RSpec.configuration.web_driver.find_elements(:name, :Answer)[0].click()

  end
end

