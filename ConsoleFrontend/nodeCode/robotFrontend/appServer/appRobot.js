var createError  = require('http-errors');
var express      = require('express');
var path         = require('path');
var cookieParser = require('cookie-parser');
var logger       = require('morgan');
//const cors     = require("cors");
const http    	 = require("http");


//IMPORTANT POINT
const robotRoutes    = require('./routes/robot');
const robotModel     = require('./models/robot');
const robotControl   = require('./controllers/robotControl');
const modelutils     = require('./utils/modelUtils');
//---------------------------------------------------------------------
 
const app            = express();

/*
 * Cross-origin resource sharing (CORS) is a mechanism that allows 
 * restricted resources on a web page to be requested from another 
 * domain outside the domain from which the first resource was served
 */
//app.use( cors() );

/*
 * SET UP THE RENDERING ENGINE
 */
app.set('views', path.join(__dirname, '.', 'views'));	 
app.set("view engine", "ejs");			//npm install --save ejs

/*
* --------------------------------------------------------------
* 1) DEFINE THE SERVER (using express) that enables socket.io
* --------------------------------------------------------------
*/
const server  = http.createServer( app );   
const io      = require('socket.io').listen(server); //npm install --save socket.io

/*
* --------------------------------------------------------------
* CREATE A  EVENT HANDLER and give the iosocket to it
* --------------------------------------------------------------
*/
var echannel  =  require("./utils/channel");
echannel.setIoSocket(io);

/*
* --------------------------------------------------------------
* Static files with middleware
* --------------------------------------------------------------
*/
app.use(express.static(path.join(__dirname, 'public')));

//IMPORTANT POINT
app.use('/testroute',   robotRoutes);
//---------------------------------------------------------------------

/*
* --------------------------------------------------------------
* 3a) HANDLE A GET REQUEST
* --------------------------------------------------------------
*/
app.get('/', function(req, res) {
 	var state  = robotModel.robot.properties.resources.state;
	console.log( req.headers.host + " state=" + state ); 
	//res.send(" xxxx "+ state);
	 
    var state  = robotModel.robot.properties.resources.state;
	res.render('access', 
		{'title': 'Robot Control', 'res': "Welcome", 'model': robotModel.robot,
		'robotstate': state, 'refToEnv': req.headers.host+"/robotenv"} 
	); 
});	

app.get('/robotenv', function (req, res) {
	//console.log( req.headers.host ); 
	var state     =  robotModel.robot.properties.resources.state;
	var withValue = false;
	var envToShow = JSON.stringify( 
			modelutils.modelToResources(robotModel.robotenv.devices.resources, withValue)
	);
 	res.render('robotenv', 
 		{'title': 'Robot Environment', 'res': envToShow, 
 		'model': robotModel.robotenv, 'host': req.headers.host, 'refToEnv': req.headers.host+"/robotenv" } 
 	); 
});

 
/*
* --------------------------------------------------------------
* 3b) HANDLE A POST REQUEST
* --------------------------------------------------------------
*/
app.post("/robot/actions/commands/w", function(req, res, next) { 
	robotControl.actuate("w", req, res ); next();});
app.post("/robot/actions/commands/s", function(req, res, next) { 
	robotControl.actuate("s", req, res ); next();});
app.post("/robot/actions/commands/h", function(req, res, next) { 
	robotControl.actuate("h", req, res ); next(); });
app.post("/robot/actions/commands/d", function(req, res, next) { 
	robotControl.actuate("d", req, res ); next(); });
app.post("/robot/actions/commands/a", function(req, res, next) { 
	robotControl.actuate("a", req, res ); next(); });
		
 
app.use( function(req,res){ 
 console.log("last use - req.myresult=" + req.myresult );
 var state  = robotModel.robot.properties.resources.state;
	res.render('access', 
		{'title': 'Robot Control', 'res': "Welcome", 'model': robotModel.robot,
		'robotstate': state, 'refToEnv': req.headers.host+"/robotenv"} 
	); 
} );

//---------------------------------------------------------------------

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  var state  = robotModel.robot.properties.resources.state;
	res.render('access', 
		{'title': 'Robot Control', 'res': "Welcome", 'model': robotModel.robot,
		'robotstate': state, 'refToEnv': req.headers.host+"/robotenv"} 
	); 
});

/*
* --------------------------------------------------------------
* 2) START THE SERVER
* --------------------------------------------------------------
*/
const initMsg=
	"\n"+
	"------------------------------------------------------\n"+
	"serverRobotCmd bound to port 8080\n"+
	"uses socket.io\n"+
	"------------------------------------------------------\n";
 
process.argv.forEach(function (val, index, array) {
	  console.log("input args[" + index + ']: ' + (val=='true') + " " + array.length);
	  //robotControl.setRealRobot( false );
	  if( index == 2 ) //the user has specified if we must work with a real robot or not
		  	 robotControl.setRealRobot( val=='true' );
	  if( index == (array.length-1) ) server.listen(8080, function(){console.log(initMsg)}); 
});

