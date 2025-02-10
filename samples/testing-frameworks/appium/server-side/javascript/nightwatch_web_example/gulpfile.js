const gulp = require('gulp'),
  nightwatch = require('gulp-nightwatch');

function build_server_side(){
  return gulp.src('./nightwatch.json')
    .pipe(gulp.dest('./build'));
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

gulp.task('zip', async function() {
  const { default: zip } = await import('gulp-zip');
  return gulp.src(['**', '!.credentials.json', '!tests/reports/*', '!build/*', '!dist/*'])
    .pipe(zip('server_side_package.zip'))
    .pipe(gulp.dest('dist'));
});
