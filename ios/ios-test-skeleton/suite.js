#import "ext/jasmine-uiautomation.js"
#import "ext/jasmine/lib/jasmine-1.2.0/jasmine.js"
#import "ext/base64.js"
#import "reporters/jasmine.uiautomation_junit_reporter.js"
#import "ext/jasmine-main-thread.js"

//import your test specifications here
#import "specs/sampleSpec.js"

jasmine.getEnv().addReporter(new jasmine.JUnitXmlReporter());
jasmine.getEnv().execute();
