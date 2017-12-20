require 'testdroid-api-client'

require 'optparse'


options = {}
options[:show_all] = false
options[:abort_all] = false
options[:host] = "https://cloud.bitbar.com"

optparse = OptionParser.new do |opts|
  opts.banner = "Usage: run_testrun.rb [options] PROJECT_NAME"

  opts.on('-u', '--username USERNAME', 'user name') do |p|
    options[:username] = p
  end

  opts.on('-p', '--password PASSWORD', 'password') do |p|
    options[:password] = p
  end
  opts.on('-e', '--endpoint API_ENDPOINT', 'cloud API endpoint') do |p|
    options[:host] = p
  end
  opts.on('-a', '--app-file-path APP_FILE_PATH', 'Application file path(.IPA)') do |p|
    options[:app_file_path] = p
  end
  opts.on('-t', '--zip-file-path ZIP_FILE_PATH', 'Zip file path') do |d|
        options[:zip_file_path] = d
  end

  opts.on('-g', '--device-group DEVICE_GROUP', 'Device group name') do |d|
        options[:device_group] = d
  end

  opts.on('-c', '--calabash-args CALABASH_CMD_ARGS', 'Calabash command line arguments') do |pp|
        options[:calabash_args] = pp
  end
   opts.on('-w', '--wait_until_completed', 'Wait until project is completed') do |w|
        options[:wait_until_completed] = w
  end

  opts.on('-h', '--help', 'Display this screen') do
    puts opts
    exit
  end

end


begin
  optparse.parse!
  mandatory = [:username, :password, :app_file_path, :zip_file_path, :device_group  ]                                         # Enforce the presence of
  missing = mandatory.select{ |param| options[param].nil? }
  unless missing.empty?                                            #
    puts "Missing options: #{missing.join(', ')}"                  #
    puts optparse                                                  #
    exit                                                           #
  end                                                              #
rescue OptionParser::InvalidOption, OptionParser::MissingArgument    #
  puts $!.to_s                                                           # Friendly output when parsing fails
  puts optparse                                                          #
  exit
end

project_name = ARGV.pop
raise "Need to specify a project name to process" unless project_name

app_file = options[:app_file_path]
test_file = options[:zip_file_path]
device_group_name = options[:device_group]

raise "The file #{app_file} doesn't exist" unless File.exist?(app_file)
raise "The file #{test_file} doesn't exist" unless File.exist?(test_file)

logger = Logger.new('logfile.log')

client = TestdroidAPI::Client.new(options[:username], options[:password], options[:host], logger)

user = client.authorize

# Search for project

testdroid_project = user.projects.list_all.detect{|p| p.name.casecmp(project_name) == 0 }

raise "Can't find project: #{project_name}" unless testdroid_project
puts "Found project: #{testdroid_project.name} with id: #{testdroid_project.id}"
device_group = user.device_groups.list_all.detect{|p| p.display_name.casecmp(device_group_name) == 0 }

raise "Can't find device group: #{device_group_name}" unless device_group
puts "Found device group: #{device_group.display_name} with id: #{device_group.id}"

#Application
testdroid_project.files.uploadApplication(app_file)

puts "Uploading of #{app_file} file completed"

#test package
testdroid_project.files.uploadTest(test_file)

puts "Uploading of #{test_file} file completed"


#Set device group
testdroid_project.config.update({:params => {:usedDeviceGroupId => device_group.id}})

testdroid_project.config.parameters.list_all.each{|p| p.delete}
if (options[:calabash_args])
	testdroid_project.config.parameters.create({:params =>  {:key => 'CALABASH_CMD_ARGS', :value => options[:calabash_args] }})

	testdroid_project.config.parameters.list_all.each{ |cp| puts " #{cp.key} #{cp.value}"}
end

#Run project using the parameters
testdroid_run = testdroid_project.run()


if(options[:wait_until_completed])
	#wait until the whole test run is completed
	puts "Project launched..."
	sleep 20 until testdroid_run.refresh.state == "FINISHED"

	puts "Project finished with success ratio: #{testdroid_run.success_ratio*100}% [executed:#{testdroid_run.execution_ratio*100}%]"
	#download junit xml from the all device runs
	#testdroid_run.device_runs.list({:params => {:limit => 0}}).each { |device_run| device_run.download_junit("#{device_run.id}_junit.xml") }
else
	puts "Project launched"
end
