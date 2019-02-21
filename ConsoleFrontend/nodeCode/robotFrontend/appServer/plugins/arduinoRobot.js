/*
 * it.unibo.mbot2018/nodeCode/robotFrontend/appServer/plugins/arduinoRobot
 */
const serialPort = require('./../utils/serial');

exports.start = function(    ){
	console.log("\t plugins/arduinoRobot started"  );
}
exports.actuateOnArduino = function(cmd  ){
	console.log("\t robotControl actuateOnArduino: " + cmd + " " + serialPort.path);
	serialPort.write(cmd);	
}