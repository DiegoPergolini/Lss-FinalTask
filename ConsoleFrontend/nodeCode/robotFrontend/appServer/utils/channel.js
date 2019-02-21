/*
 * nodeCode/robotFrontEnd/utils/channel.js
 */
var fs = require('fs');

const events     = require('events');
const model      = require('./../models/robot.json');
const io         = require('socket.io');
const channel    = new events.EventEmitter();
const mqttUtils  = null;
var robotPosition = {
    x:0,
    y:0,
    s:"R"
};

var obstacleInfo = {
    time: 0,
    x: 0,
    y: 0,
    photo: ""
};

channel.setIoSocket = function( iosock ){
	console.log("\t CHANNEL setIoSocket=" + iosock );
	this.io = iosock;
}
channel.on('sonar', function(data) {	//emitted by clientRobotVirtual or mqtt support; 
	if( data.indexOf('undefined') >= 0 ) return;
	if( data.indexOf('msg') >= 0 ) return;
 	console.log("\t CHANNEL sonar updates the model and the page with:" + data ); 
	model.links.robotenv.envdevices.resources.sonar2.value=data;
	//model.links.robotenv.envdevices.resources.sonar2.data.push(data);	//history (quite long...)
	this.io.sockets.send( data );
});
channel.on('sonarDetect', function(data) {	//emitted by clientRobotVirtual or mqtt support; 
 	console.log("\t CHANNEL sonarDetect updates the model and the page with:" + data ); 
	model.links.robot.resources.robotdevices.resources.sonarRobot.value=data;
	model.links.robot.resources.robotdevices.resources.sonarRobot.data.push(data);	//history
	this.io.sockets.send( data );
});
channel.on('obstaclePhoto', function(data) {	//emitted by clientRobotVirtual or mqtt support;
    console.log("\t CHANNEL obstaclePhoto updates the model and the page with:" + data );
    model.links.robot.resources.robotdevices.resources.sonarRobot.value=data;
    model.links.robot.resources.robotdevices.resources.sonarRobot.data.push(data);	//history

    var curPos = data.split('curPos(')[1].split(")),")[0];
    var x = curPos.split(',')[0];
    var y = curPos.split(',')[1];

    obstacleInfo.x = parseInt(x);
    obstacleInfo.y = parseInt(y);
});
channel.on('robotState', function(data) {  //emitted by robotControl or applRobotControl;
	console.log("\t CHANNEL receives: " + data  );
 	// model.links.robot.resources.robotstate.state=data;  //shown in the page by app renderMainPage
 	// not propagated via io.sockets since a robot state can change only after a user command (??)
 	// CLEAR THE sonar
	model.links.robotenv.envdevices.resources.sonar2.value="";
	this.io.sockets.send( data );
	// model.links.robot.resources.robotdevices.resources.sonarRobot.value="";
});
channel.on('consoleUpdate', function(data) {
    console.log("\t CHANNEL receives: " + data  );
    // data.indexOf("temperature(");
    const worldState = data.split('worldState(')[1].split(")),")[0]+")";
    console.log(worldState);
    const tempString = worldState.split(',')[0];
    const robotStateString = worldState.split(',')[1];
    const tempValue = parseFloat(tempString.split('(')[1].replace(")",""));
    const stateValue = robotStateString.split('(')[1].replace(")","");
    console.log(tempValue,stateValue);
    this.io.sockets.send( data );
    model.links.robotenv.envdevices.resources.temperature.value = tempValue;
    model.links.robot.resources.robotstate.state=stateValue;  //shown in the page by app renderMainPage
});
channel.on('mapUpdate', function(data) {
    console.log("\t CHANNEL receives: " + data);
    console.log("wasd");
    const mapUpdate = data.split('mapUpdate(')[1].split("),")[0];
    const values = mapUpdate.split(',');
    const x = parseInt(values[0]);
    const y = values[1];
    const s = values[2];
    const cell = {
    	x: x,
		y: y,
		s: s
	};
    const index  = model.links.robotenv.envdevices.resources.map.cells.indexOf(cell);
    if(index>=0){
        model.links.robotenv.envdevices.resources.map.cells.splice(index,1);
	}
    model.links.robotenv.envdevices.resources.map.cells.push(cell);
    console.log(model.links.robotenv.envdevices.resources.map.cells);
    this.io.sockets.send( data );
});
channel.on('robotPos', function(data) {
    // const robotPos = data.split('robotPos(')[1].split("),")[0];
    // const values = robotPos.split(',');
    // const x = parseInt(values[0]);
    // const y = values[1];
    // const s = values[2];
    // const cell = {
    //     x: x,
    //     y: y,
    //     s: s
    // };
    // const index  = model.links.robotenv.envdevices.resources.map.cells.indexOf(robotPosition);
    // if(index>=0){
    //     model.links.robotenv.envdevices.resources.map.cells.splice(index,1);
    // }
    // const oldPos = {
    //     x:robotPosition.x,
    //     y:robotPosition.y,
    //     s:"1"
    // }
    // model.links.robotenv.envdevices.resources.map.cells.push(oldPos);
    // model.links.robotenv.envdevices.resources.map.cells.push(cell);
    // robotPosition = cell;
    // console.log(model.links.robotenv.envdevices.resources.map.cells);
    this.io.sockets.send( data );
});
channel.on('publishcmd', function(data) {  //emitted by robotControl;
	console.log("\t CHANNEL publishcmd: " + data + " on topic unibo/qasys" + " mqttUtils=" + mqttUtils);
//	if( mqttUtils == null )  mqttUtils  = require('./../utils/mqttUtils');		//CIRCULAR!!!
    publish( data );	//topic  = "unibo/qasys";
});
channel.on('photoM', function(data) {

    var photo = data.split('photoM(')[1].split("),")[0];
    photo = photo.split(")',")[0];

    obstacleInfo.photo = photo;

    this.io.sockets.send( data );
});
channel.on('bombRetrievedMsg', function(data) {
    var newData = data.replace('bombRetrievedMsg', 'bombRetrievedEvent').replace('dispatch', 'event');
    console.log("publish " + newData);
    publish( newData );
});
channel.on('storeBombInfo', function(data) {
    console.log("storeBombInfo");

    fs.readFile('./appServer/public/bombInfo.json', function (err, data) {
        var json = JSON.parse(data);

        obstacleInfo.time = new Date().toISOString();

        json.bombs.push(obstacleInfo);

        fs.writeFile("./appServer/public/bombInfo.json", JSON.stringify(json), function(err){
            if (err) throw err;
            console.log('The "data to append" was appended to file!');
        });
    });
});

module.exports=channel;

/*
 * ------------------------------------------------
 * MQTT support
 * ------------------------------------------------
 */
const systemConfig = require("./../../systemConfig");
const mqtt    = require ('mqtt');	 
const topic   = "unibo/qasys";
var client    = mqtt.connect(systemConfig.mqttbroker);

client.on('connect', function () {
	  client.subscribe( topic );
	  console.log('\t MQTT client has subscribed successfully ');
});

//The message usually arrives as buffer, so we convert it to string data type;
client.on('message', function (topic, message){
	var msg = message.toString();
	console.log("\t MQTT RECEIVES:"+ msg); //if toString is not given, the message comes as buffer;
	if( msg.indexOf( "sonarDetect" ) > 0 ){
		channel.emit("sonarDetect",  msg  );  //to allow update of the WebPage
	}else if( msg.indexOf( "sonar" ) > 0   ){
		channel.emit("sonar",  msg  );  //to allow update of the WebPage
	}else if( msg.indexOf( "obstaclePhoto" ) > 0   ){
        channel.emit("obstaclePhoto",  msg  );  //to allow update of the WebPage
    }else if( msg.indexOf( "consoleUpdate" ) > 0   ){
        channel.emit("consoleUpdate",  msg  );  //to allow update of the WebPage
    }else if( msg.indexOf( "mapUpdate" ) > 0   ){
        channel.emit("mapUpdate",  msg  );  //to allow update of the WebPage
    }else if( msg.indexOf( "robotPos" ) > 0   ){
        channel.emit("robotPos",  msg  );  //to allow update of the WebPage
    }else if( msg.indexOf( "photoM" ) > 0   ){
        channel.emit("photoM",  msg  );  //to allow update of the WebPage
    }else if( msg.indexOf( "bombRetrievedMsg" ) > 0   ){
        channel.emit("bombRetrievedMsg",  msg  );  //to allow update of the WebPage
    }
});

publish = function( msg ){
	//console.log('\t MQTT  publish ' + client);
	client.publish(topic, msg);
}