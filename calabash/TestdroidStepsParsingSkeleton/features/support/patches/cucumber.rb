module Calabash
  module Cucumber

    # A collection of methods that help you handle Step failures.
    module FailureHelpers

      # Generates a screenshot of the app UI and saves to a file (prefer `screenshot_embed`).
      # Increments a global counter of screenshots and adds the count to the filename (to ensure uniqueness).
      #
      # @see #screenshot_embed
      # @param {Hash} options to control the details of where the screenshot is stored.
      # @option options {String} :prefix (ENV['SCREENSHOT_PATH']) a prefix to prepend to the filename (e.g. 'screenshots/foo-').
      #   Uses ENV['SCREENSHOT_PATH'] if nil or '' if ENV['SCREENSHOT_PATH'] is nil
      # @option options {String} :name ('screenshot') the base name and extension of the file (e.g. 'login.png')
      # @return {String} path to the generated screenshot
      # @todo deprecated the current behavior of SCREENSHOT_PATH; it is confusing
      def screenshot(options={:prefix => nil, :name => nil})
        prefix = options[:prefix]
        name = options[:name]

        @@screenshot_count ||= 0
        res = http({:method => :get, :path => 'screenshot'})
        prefix = prefix || ENV['SCREENSHOT_PATH'] || ''
        if name.nil?
          name = 'screenshot'
        else
          if File.extname(name).downcase == '.png'
            name = name.split('.png')[0]
          end
        end

        path = "#{prefix}#{name}_#{@@screenshot_count}.png"
        puts path
        File.open(path, 'wb') do |f|
          f.write res
        end
        @@screenshot_count += 1
        path
      end
    end
  end
end
