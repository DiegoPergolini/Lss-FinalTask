/*
 * robotFrontend/systemConfig.js
 */
const virtualRobot = "virtual";
const mbotlRobot   = "mbot";
const uniboRobot   = "uniboRobot";

var robotType      = virtualRobot;
var robotPlugin    = null;
var ledPlugin      = null;

exports.mqttbroker = "mqtt://192.168.43.137";
//exports.mqttbroker = "mqtt://iot.eclipse.org";

exports.robotAddr  = {ip: "192.168.1.41", port: 8999};

exports.setRobotType = function( v ){
	if( v === virtualRobot || v === mbotlRobot || v === uniboRobot){
		robotType = v;
 		setRobotPlugin();
		robotPlugin.start();
	}
}

exports.getRobotType   = function(){ return robotType;      }
exports.getRobotPlugin = function(){ return robotPlugin;	}
	
const  isVirtual    = function(){ return robotType === virtualRobot ; }
const  isMbot       = function(){ return robotType === mbotlRobot ;   }
const  isUnibo      = function(){ return robotType === uniboRobot ;   }

const setRobotPlugin = function(){ 
   	if( isVirtual() ) robotPlugin = require("./appServer/plugins/virtualRobot");
	if( isMbot() )    robotPlugin = require("./appServer/plugins/arduinioRobot");
 	//console.log("\t robotConfig setRobotPlugin " + robotPlugin   );
 }
const setLedPlugin = function(){ 
	ledPlugin = require("./appServer/plugins/ledPlugin");
}