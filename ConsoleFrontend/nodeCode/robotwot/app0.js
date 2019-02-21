var express      = require('express');
var path         = require('path');
var favicon      = require('serve-favicon');
var logger       = require('morgan');
var cookieParser = require('cookie-parser');  ////npm install --save cookie-parser
var bodyParser   = require('body-parser');

var routes       = require('./appServer/routes/index');	//SET BY DESIGNER
//var users = require('./routes/users');				//REMOVED BY DESIGNER

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'appServer', 'views')); //SET BY DESIGNER
app.set('view engine', 'ejs');

// uncomment after placing your favicon in /public //npm install --save serve-favicon
app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));	
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', routes);			//SET BY DESIGNER
//app.use('/users', users);		//REMOVED (at the moment) BY DESIGNER

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;

//node ./bin/www	//INSERTED BY DESIGNER
