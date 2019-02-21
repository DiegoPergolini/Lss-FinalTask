/*
 * it.unibo.mbot2018/nodeCode/robotFrontend/appServer/plugins/virtualRobot
 */
var toVirtualRobot = require("./../utils/clientRobotVirtual");

exports.start = function( ){
	console.log("\t plugins/virtualRobot started with toVirtualRobot= " +  toVirtualRobot );
}
exports.actuate = function( cmd  ){
	var cmdToVirtual = "";
	//console.log("\t robotControl actuate " + cmd  );
	if(      cmd === "w" ){ cmdToVirtual=`{ "type": "moveForward",  "arg": -1 }`;  }
	else if( cmd === "s" ){ cmdToVirtual=`{ "type": "moveBackward",  "arg": -1 }` ; }
	else if( cmd === "h" ){ cmdToVirtual=`{ "type": "alarm",  "arg": 1000 }` ; }
	else if( cmd === "a" ){ cmdToVirtual=`{ "type": "turnLeft",  "arg": 1000 }` ;   }
	else if( cmd === "d" ){ cmdToVirtual=`{ "type": "turnRight",  "arg": 1000 }` ; }
	//console.log("\t plugins/virtualRobot actuateOnVirtual:" + cmdToVirtual );
  	toVirtualRobot.send( cmdToVirtual );  	
}
