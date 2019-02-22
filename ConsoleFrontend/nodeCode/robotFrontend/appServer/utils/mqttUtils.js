/*
* =====================================
*  nodeCode/robotFrontEnd/utils/mqttUtils.js
* =====================================
*/
const echannel  = require("./channel");
const mqtt      = require ('mqtt');	 
const topic     = "unibo/qasys";

//const events     = require('events');
//const channel    = new events.EventEmitter();

console.log( echannel   );

//var client   = mqtt.connect('mqtt://iot.eclipse.org');
//var client   = mqtt.connect('mqtt://192.168.1.100');
 var client    = mqtt.connect('mqtt://localhost');

client.on('connect', function () {
	  client.subscribe( topic );
	  console.log('\t MQTT client has subscribed successfully ');
});

//The message usually arrives as buffer, so we convert it to string data type;
client.on('message', function (topic, message){
	var msg = message.toString();
	console.log("\t MQTT UTILS RECEIVES:"+ msg); //if toString is not given, the message comes as buffer;
	if( msg.indexOf( "sonarEvent" ) > 0 || msg.indexOf( "sonarDetect" ) > 0 ){
		echannel.emit("sonarEvent",  "xxx"  );  //to allow update of the WebPage
	}
});

exports.publish = function( msg ){
	//console.log('\t MQTT  publish ' + client);
	client.publish(topic, msg);
}



