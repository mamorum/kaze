var gulp = require('gulp');
var child = require('child_process')
var gutil = require('gulp-util');

var p;

gulp.task('start', function() {
  p = child.spawn(
    'C:\\gradle-2.10\\bin\\gradle.bat',
    ['-Dtest.single=Serve4Test', 'test'], //['run'],
    ['']
  );
  p.stdout.on('data', function(b) { process.stdout.write(b.toString()); });
  p.stderr.on('data', function(b) { process.stderr.write(b.toString()); });
  gutil.log("PID:" + p.pid);
});

gulp.task('stop', function() {
  child.exec('taskkill /pid ' + p.pid + ' /f /t');
});

gulp.task('default', ['start'], function() {
  gulp.watch('src/**/*.java', ['stop', 'start']);
});