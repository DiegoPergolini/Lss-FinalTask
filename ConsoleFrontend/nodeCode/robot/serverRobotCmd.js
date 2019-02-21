/*
* =====================================
* serverRobotCmd.js
* =====================================
*/
const http    = require("http"),
	  cors    = require("cors"),
	  path    = require("path"),
      join    = path.join ;

const robotModel     = require('./models/robot');
const robotControl   = require('./controllers/robotControl');
const modelutils     = require('./utils/modelUtils');

/*
* --------------------------------------------------------------
* 0) SET UP THE VIEW ENGINE BY USING EXPRESS
* --------------------------------------------------------------
*/
const express  = require('express'); 	//npm install --save express
const app      = express();

/*
 * Cross-origin resource sharing (CORS) is a mechanism that allows 
 * restricted resources on a web page to be requested from another 
 * domain outside the domain from which the first resource was served
 */
app.use( cors() );

/*
 * SET UP THE RENDERING ENGINE
 */
app.set('views', path.join(__dirname, '.', 'viewRobot'));	 
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
var publicpath = path.resolve(__dirname + '/public');  //cross-platform
app.use(express.static(publicpath));
/*
* --------------------------------------------------------------
* 3a) HANDLE A GET REQUEST
* --------------------------------------------------------------
*/
app.get('/', function(req, res) {
 	//console.log( req.headers.host ); 
//	var state  = robotModel.robot.state;
//	res.render('access', 
//		{'title': 'Robot Control', 'res': "Welcome", 'model': robotModel.robot,
//		'robotstate': state, 'refToEnv': req.headers.host+"/robotenv"} 
//	); 
	res.send("Welcome");
});	

//USED in ajaxAcess.html
app.get('/model', function (req, res) {
	res.send(robotModel);
});
//USED in ajaxAcess.html
app.get('/model/robotdevices', function (req, res) {
	res.send(robotModel.robot.devices.resources);
});
//USED from the page built by access.ejs
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
app.get('/robotstate', function (req, res) {
	var state  = robotModel.robot.properties.resources.state;
	res.render('access', 
			{'title': 'Robot Control', 'res': "answer to /robotstate" , 
		     'model': robotModel.robot, 'robotstate': state, 'refToEnv': req.headers.host+"/robotenv"} 
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
	//console.log("last " + req.myresult );
 	res.render('access', 
		{'title': 'Robot Control', 'res': req.myresult, 'model': robotModel.robot,
		 'robotstate':robotModel.robot.state, 'refToEnv': req.headers.host+"/robotenv"} 
	); 
} );
 
/*
* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
* EXCEPTIONS
* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
*/

//Handle CRTL-C;
process.on('SIGINT', function () {
  console.log('serverRobotCmd Bye, bye!');
  process.exit();
});
process.on('exit', function(code){
	console.log("serverRobotCmd Exiting code= " + code );
});
process.on('uncaughtException', function (err) {
 	console.error('serverRobotCmd uncaught exception:', err.message);
  	process.exit(1);		//MANDATORY!!!;
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

/* 
node serverRobotCmd.js false/true
 
curl -X POST -d "true" http://localhost:8080/robot/actions/commands/w
curl -X GET  http://localhost:8080/robot/state

*/