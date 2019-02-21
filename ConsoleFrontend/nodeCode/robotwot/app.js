var express      = require('express');
var path         = require('path');
var favicon      = require('serve-favicon');
var logger       = require('morgan');
var cookieParser = require('cookie-parser');   
var bodyParser   = require('body-parser');

var mainpageRoute     = require('./appServer/routes/index');		//SET BY DESIGNER after MVC refactoring
//var actuatorsRoutes = require('./appServer/routes/actuators');	//See 10) of ReadMe.txt
//var sensorsRoutes   = require('./appServer/routes/sensors');
 
var robotModel      = require("./appServer/models/robotModel"); //See 8) of ReadMe.txt
var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'appServer', 'views')); //SET BY DESIGNER
app.set('view engine', 'ejs');

// uncomment after placing your favicon in /public  
app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));	
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

//view engine setup
app.set('views', path.join(__dirname, 'appServer', 'views'));
app.set('view engine', 'ejs');

/*
 * 
 */
const dataCreator   = require('./appServer/routes/createDataFields');
dataCreator.createdatafields(robotModel);

//Create Routes  //WORKING
const routesCreator = require('./appServer/routes/routesCreator');
app.use('/', routesCreator.create(robotModel));

//app.use('/', mainpageRoute);	//SET BY DESIGNER after MVC refactoring


//app.use(express.static(__dirname + './../public'));

//INTERFACE
//app.use('/actuators', actuatorsRoutes);		//See 11) of ReadMe.txt
//app.use('/sensors',  sensorsRoutes);

//REPRESENTATION
//app.use( function(req,res){ console.log("last " + req.originalUrl); res.send(req.myresult); } );

//const converter = require('./appServer/utils/converter')
//app.use( converter() );

app.use( function(req,res){ 
	console.log("last " + req.myresult );
	//console.log(  req  );
	res.render('access', {'title': 'Robot Control', 'res': req.myresult} ); 
} );


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



/*
  curl -X POST \
   'http://localhost:8484/actions/ledState?ledId=1&state=true' \
  -H 'Accept: application/json' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: 5b3077f2-6003-051d-e32c-1644252d4119'
 
*/