var gulp = require('gulp'),
    nightwatch = require('gulp-nightwatch'),
    inject = require('gulp-inject-string'),
    zip = require('gulp-zip');

// Client Side Execution

function build_client_side(){
  var credentials = require('./.credentials.json');
  var apiKey = credentials["apiKey"];

  var build_client_side = gulp.src('./nightwatch.json')
  .pipe(inject.replace('<insert_your_apiKey_to_credentials_file>', apiKey))
  .pipe(gulp.dest('./build/'));
  return build_client_side;
}


gulp.task('ios', function() {
  return build_client_side()
    .pipe(nightwatch({
      configFile: 'build/nightwatch.json',
      cliArgs: [ '--env bitbar_ios']
    }));
});

gulp.task('android', function() {
  return build_client_side()
    .pipe(nightwatch({
      configFile: 'build/nightwatch.json',
      cliArgs: [ '--env bitbar_android']
    }));
});


// Server Side Execution

function build_server_side(){
  var build_server_side = gulp.src('./nightwatch.json')
    .pipe(gulp.dest('./build'));
  return build_server_side;
}

gulp.task('androidServerSide', function() {
  return build_server_side()
    .pipe(nightwatch({
      configFile: 'build/nightwatch.json',
      cliArgs: [ '--env bitbar_android_server_side','--verbose']
    }));
});

gulp.task('iosServerSide', function() {
  return build_server_side()
    .pipe(nightwatch({
      configFile: 'build/nightwatch.json',
      cliArgs: [ '--env bitbar_ios_server_side','--verbose']
    }));
});

gulp.task('zip', function() {
    return gulp.src(['**','!.credentials.json','!tests/reports/*','!build/*','!dist/*'])
        .pipe(zip('server_side_package.zip'))
        .pipe(gulp.dest('dist'));
});