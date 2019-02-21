/*
 * nodeCode/robot/utils/channel.js
 */
const events = require('events');
const model  = require('./../models/robot.json');
const io      = require('socket.io');
const channel = new events.EventEmitter();

channel.setIoSocket = function( iosock ){
	console.log("channel setIoSocket=" + iosock );
	this.io = iosock;
}

channel.on('sonarEvent', function(data) {	//emitted by serial
//	console.log("channel sonarEvent=" + data + 
//		" updates " + model.robot.devices.resources.sonarRobot.value );
	model.robot.devices.resources.sonarRobot.value=data;
	this.io.sockets.send( data );
});

channel.on('robotState', function(data) { //emitted by 
	console.log("channel robotState=" + data + 
		" updates " + model.robot.properties.resources.state );
	model.robot.properties.resources.state=data;
});

//channel.emit('sonarEvent', 32.5);

module.exports=channel;