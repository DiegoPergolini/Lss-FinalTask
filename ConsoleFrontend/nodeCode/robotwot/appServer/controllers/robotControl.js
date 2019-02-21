var serialPort ;
var toVirtualRobot;
var myPort;


const realRobot = false;

if( realRobot ){    
	serialPort = require('./serial');
	console.log("serialPort= " + serialPort.path  );
}
else   toVirtualRobot = require("./clientRobotVirtual");

exports.actuate = function( cmd, req, res ){
	var newState     = "";
	var cmdToVirtual = "";
	console.log("actuate " + cmd  );
	if( cmd === "w" ){ cmdToVirtual=`{ "type": "moveForward",  "arg": -1 }` ; 
		newState="server moving forward"; }
	else if( cmd === "s" ){ cmdToVirtual=`{ "type": "moveBackward",  "arg": -1 }` ; 
		newState="server moving backward"; }
	else if( cmd === "h" ){ cmdToVirtual=`{ "type": "alarm",  "arg": 1000 }` ; 
			newState="server stopped"; }
	else if( cmd === "a" ){ cmdToVirtual=`{ "type": "turnLeft",  "arg": 1000 }` ; 
			newState="server  moving left"; }
	else if( cmd === "d" ){ cmdToVirtual=`{ "type": "turnRight",  "arg": 1000 }` ; 
		newState="server  moving right"; }
	
	if( realRobot){
		actuateOnArduino( cmd, newState, req,res ) 
	}else{
		actuateOnVirtual( cmdToVirtual, newState, req,res )		
	}
	
}
function actuateOnVirtual(cmd, newState, req, res ){

	console.log("actuateOnVirtual:" + cmd );
  	toVirtualRobot.send( cmd );  	
// 	robotModel.robot.state = newState;
 	
//		setTimeout( function(){
//			//var now = new Date().toUTCString();
//			io.sockets.send( newState ); //+ " time=" + now
//		}, 200 ) ;

//  	res.render("access");
}

function actuateOnArduino(cmd, newState, req, res ){
	console.log("actuateOnArduino: " + cmd + " " + serialPort.path);
	serialPort.write(cmd);	
}

function delegate( hlcmd, newState, req, res ){
 	robotModel.robot.state = newState;
	emitRobotCmd(hlcmd);
    res.render("access");	
}
var emitRobotCmd = function( cmd ){ //called by delegate;
 	var eventstr = "msg(usercmd,event,js,none,usercmd(robotgui( " +cmd + ")),1)"
  		console.log("emits> "+ eventstr);
// 		mqttUtils.publish( eventstr );	//topic  = "unibo/qasys";
}
