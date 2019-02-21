/*
 * it.unibo.mbot2018\nodeCode/robot/controllers/robotControl
 */
const robotModel  = require('./../models/robot');
const echannel    =  require("./../utils/channel");

var realRobot = false;
var serialPort ;
var toVirtualRobot;
var myPort;

exports.setRealRobot = function( v ){
	console.log("robotControl setRealRobot=" + (v == true) );
	realRobot =   v ;
	if( realRobot ){    
		serialPort = require('./../utils/serial');
		console.log("serialPort= " + serialPort.path  );
	}
	else{
		toVirtualRobot = require("./../utils/clientRobotVirtual");
	}
}

exports.actuate = function( cmd, req, res ){
	var newState     = "";
	var cmdToVirtual = "";
	console.log("robotControl actuate " + cmd  );
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
	console.log("actuateOnVirtual:" + cmd );
  	toVirtualRobot.send( cmd );  	
  	updateRobotState(newState);
  	setActuateResult(req,cmd);
}

function actuateOnArduino(cmd, newState, req, res ){
	console.log("actuateOnArduino: " + cmd + " " + serialPort.path);
	serialPort.write(cmd);	
  	updateRobotState(newState);
  	setActuateResult(req,cmd);
}

function setActuateResult(req, cmd){
 	req.myresult = "move  "+cmd+ " done";	
}

function updateRobotState(newState){
  	robotModel.robot.state = newState;		
	echannel.emit("robotState", newState);
}


function delegate( hlcmd, newState, req, res ){
 	robotModel.robot.state = newState;
	emitRobotCmd(hlcmd);
//    res.render("access");	
}
var emitRobotCmd = function( cmd ){ //called by delegate;
 	var eventstr = "msg(usercmd,event,js,none,usercmd(robotgui( " +cmd + ")),1)"
  		console.log("emits> "+ eventstr);
// 		mqttUtils.publish( eventstr );	//topic  = "unibo/qasys";
}
