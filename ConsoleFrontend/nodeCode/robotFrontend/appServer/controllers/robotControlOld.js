/*
 * it.unibo.mbot2018/nodeCode/robotFrontend/appServer/controllers/robotControl
 */
const robotModel  = require('./../models/robot');
const echannel    = require("./../utils/channel");

var realRobot = false;
var serialPort ;
var toVirtualRobot;
var myPort;

exports.setRealRobot = function( v ){
	console.log("robotControl setRealRobot=" + (v == true) );
	realRobot =  (v == true) ;
	if( realRobot ){    
		serialPort = require('./../utils/serial');
		console.log("serialPort= " + serialPort.path  );
	}
	else{
		console.log("robot/controllers/robotControl connects to virtual robot"  );
		toVirtualRobot = require("./../utils/clientRobotVirtual");
	}
}

exports.actuate = function( cmd, req, res ){
	var newState     = "";
	var cmdToVirtual = "";
	//console.log("\t robotControl actuate " + cmd  );
	if( cmd === "w" ){ cmdToVirtual=`{ "type": "moveForward",  "arg": -1 }` ; 
		newState="robot moving forward"; }
	else if( cmd === "s" ){ cmdToVirtual=`{ "type": "moveBackward",  "arg": -1 }` ; 
		newState="robot moving backward"; }
	else if( cmd === "h" ){ cmdToVirtual=`{ "type": "alarm",  "arg": 1000 }` ; 
			newState="robot stopped"; }
	else if( cmd === "a" ){ cmdToVirtual=`{ "type": "turnLeft",  "arg": 1000 }` ; 
			newState="robot  moving left"; }
	else if( cmd === "d" ){ cmdToVirtual=`{ "type": "turnRight",  "arg": 1000 }` ; 
		newState="robot  moving right"; }

	if( realRobot){
		actuateOnArduino( cmd, newState, req,res ) 
	}else{
		actuateOnVirtual( cmdToVirtual, newState, req, res )		
	}
}

function actuateOnVirtual(cmd, newState, req, res ){
	console.log("\t robotControl actuateOnVirtual:" + cmd );
  	toVirtualRobot.send( cmd );  	
  	updateRobotState(newState);
  	setActuateResult(req,cmd);
}

function actuateOnArduino(cmd, newState, req, res ){
	console.log("\t robotControl actuateOnArduino: " + cmd + " " + serialPort.path);
	serialPort.write(cmd);	
  	updateRobotState(newState);
  	setActuateResult(req,cmd);
}

function setActuateResult(req, cmd){
 	req.myresult = "move  "+cmd+ " done";	//used by LAST ACTION in appFrontEndRobot
}

function updateRobotState(newState){
	echannel.emit("robotState", newState);  //used to update on the Web page
}