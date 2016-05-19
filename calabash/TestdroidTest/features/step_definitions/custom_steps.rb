#
# This is the file where you can write custom steps.
#

# Take a screenshot with given filename
Then /^I take a screenshot "([^\"]*)"$/ do |screenshot_name|
  screenshot_embed({:name=>"#{screenshot_name}"})
end

Then /^I set screen to portrait$/ do
    perform_action('set_activity_orientation', 'portrait')
end

Then /^I set screen to landscape$/ do
    perform_action('set_activity_orientation', 'landscape')
end

Then /^I hide keyboard$/ do
    hide_soft_keyboard()
end

Given /^I start the test$/ do
    macro 'I set screen to portrait'
    macro 'I wait upto 3 seconds for the "MM_MainMenu" screen to appear'
end

Then /^I go back to start$/ do
    macro 'I go back'
    macro 'I see "Bitbar"'
    macro 'I wait'
    macro 'I take a screenshot'
end

Then /^I press tiles nbr eight & five ten times$/ do
    10.times do
        macro 'I press image button number 8'
        macro 'I press image button number 5'
    end
end


