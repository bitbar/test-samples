#
# This is the file where you can write custom steps.
#

# Take a screenshot with given filename
Then /^I take a screenshot "([^\"]*)"$/ do |screenshot_name|
  screenshot_embed({:name=>"#{screenshot_name}"})
end

