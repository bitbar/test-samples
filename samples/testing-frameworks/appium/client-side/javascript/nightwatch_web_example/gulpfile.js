const gulp = require('gulp'),
  nightwatch = require('gulp-nightwatch'),
  replace = require('gulp-replace');

function build_client_side() {
  const credentials = require('./.credentials.json');
  const apiKey = credentials["apiKey"];

  return gulp.src('./nightwatch.json')
    .pipe(replace('<insert_your_apiKey_to_credentials_file>', apiKey))
    .pipe(gulp.dest('./build/'));
}

gulp.task('ios', function () {
  return build_client_side()
    .pipe(nightwatch({
      configFile: 'build/nightwatch.json',
      cliArgs: ['--env bitbar_ios']
    }));
});

gulp.task('android', function () {
  return build_client_side()
    .pipe(nightwatch({
      configFile: 'build/nightwatch.json',
      cliArgs: ['--env bitbar_android']
    }));
});
