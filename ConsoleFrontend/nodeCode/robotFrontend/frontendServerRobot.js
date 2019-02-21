/*
 * -----------------------------------------------
 * it.unibo.mbot2018/nodeCode/robotFrontend/frontendServerRobot.js
 * -----------------------------------------------
 */
/**
 * Module dependencies.
 */
var app   = require('./appFrontendRobot');	//the new application;
var debug = require('debug')('robotfrontend:server');
var http  = require('http');

require('dotenv').config();  

/**
 * Get port from environment and store in Express.
 */
var port = normalizePort( process.env.PORT || '3000' );
app.set('port', port);

/**
 * Create HTTP server.
 */
var server = http.createServer(app);

server.on('error', onError);
server.on('listening', onListening);

/*
* --------------------------------------------------------------
* EXTENSION 1): CREATE A  EVENT HANDLER and give the iosocket to it
* --------------------------------------------------------------
*/
const io             = require('socket.io').listen(server);  
var echannel         = require("./appServer/utils/channel");
echannel.setIoSocket(io);
const robotControl   = require('./appServer/controllers/robotControl');

/*
* --------------------------------------------------------------
* EXTENSION 2): START THE SERVER
* --------------------------------------------------------------
*/
const systemConfig = require("./systemConfig");

const initMsg=
	"\n"+
	"------------------------------------------------------\n"+
	"serverRobotCmd bound to port: "+ port + "\n" +
	"uses socket.io\n"+
	"USING THE ROBOT: " + systemConfig.getRobotType() + "\n"+
	"------------------------------------------------------\n";
if( process.argv[2] ) systemConfig.setRobotType( process.argv[2] );
else systemConfig.setRobotType( "virtual" );
server.listen(port, function(){console.log(initMsg)});


/**
 * Normalize a port into a number, string, or false.
 */
function normalizePort(val) {
 //console.log( process.env );
  var port = parseInt(val, 10);
  if (isNaN(port)) {
    // named pipe
    return val;
  }
  if (port >= 0) {
    // port number
    return port;
  }
  return false;
}

/**
 * Event listener for HTTP server "error" event.
 */
function onError(error) {
  if (error.syscall !== 'listen') { throw error; }
  var bind = typeof port === 'string'
    ? 'Pipe ' + port
    : 'Port ' + port;
  // handle specific listen errors with friendly messages;
  switch (error.code) {
    case 'EACCES':
      console.error(bind + ' requires elevated privileges');
      process.exit(1);
      break;
    case 'EADDRINUSE':
      console.error(bind + ' is already in use');
      process.exit(1);
      break;
    default: throw error;
  }
}

/**
 * Event listener for HTTP server "listening" event.
 */
function onListening() {
  var addr = server.address();
  var bind = typeof addr === 'string'
    ? 'pipe ' + addr
    : 'port ' + addr.port;
  debug('Listening on ' + bind);
}
/**
 * HANDLE User interruption commands.
 */
//Handle CRTL-C;
process.on('SIGINT', function () {
//  ledsPlugin.stop();
  console.log('serverRobot Bye, bye!');
  process.exit();
});
process.on('exit', function(code){
	console.log("Exiting code= " + code );
});
process.on('uncaughtException', function (err) {
 	console.error('ERROR: serverRobot got uncaught exception:', err.message);
  	process.exit(1);		//MANDATORY!!!;
});


/*
curl -H "Content-Type: application/json" -X POST -d "{\"value\": \"true\" }"
*/