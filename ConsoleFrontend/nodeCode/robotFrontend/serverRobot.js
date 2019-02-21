/*
 * -----------------------------------------------
 * it.unibo.mbot2018/nodeCode/robotFrontend/serverRobot.js
 * -----------------------------------------------
 */
/**
 * Module dependencies.
 */
var app   = require('./app');
var debug = require('debug')('robotfrontend:server');
var http  = require('http');

/**
 * Get port from environment and store in Express.
 */
var port = normalizePort( process.env.PORT || '3000' );
app.set('port', port);

/**
 * Create HTTP server.
 */
var server = http.createServer(app);

/**
 * Listen on provided port, on all network interfaces.
 */
server.listen(port, function(){ console.log("serverRobot starts on port="+port); });
server.on('error', onError);
server.on('listening', onListening);

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
  if (error.syscall !== 'listen') {
    throw error;
  }

  var bind = typeof port === 'string'
    ? 'Pipe ' + port
    : 'Port ' + port;
  // handle specific listen errors with friendly messages
  switch (error.code) {
    case 'EACCES':
      console.error(bind + ' requires elevated privileges');
      process.exit(1);
      break;
    case 'EADDRINUSE':
      console.error(bind + ' is already in use');
      process.exit(1);
      break;
    default:
      throw error;
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
 	console.error('serverRobot got uncaught exception:', err.message);
  	process.exit(1);		//MANDATORY!!!;
});