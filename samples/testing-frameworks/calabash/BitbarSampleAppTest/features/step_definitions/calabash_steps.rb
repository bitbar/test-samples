require 'calabash-android/calabash_steps'

Then /^I set screen to portrait$/ do
    perform_action('set_activity_orientation', 'portrait')
end

Then /^I set screen to landscape$/ do
    perform_action('set_activity_orientation', 'landscape')
end

Then /^I hide keyboard$/ do
    hide_soft_keyboard()
end

