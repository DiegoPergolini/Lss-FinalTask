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

channel.on('obstacle', function(data) {	//emitted by clientRobotVirtual or mqtt support;
    const result = data.unifyWithContent("obstacle(curPos(X,Y,D))","X","Y","D");
    const content = {
        msgId:data.msgId,
        x: result.X,
        y: result.Y,
        d: result.D
    };
    console.log("\t CHANNEL obstaclePhoto updates the model and the page with:" + data );

    obstacleInfo.x = parseInt(result.X);
    obstacleInfo.y = parseInt(result.Y);
});

channel.on('robotState', function(data) {  //emitted by robotControl or applRobotControl;
	console.log("\t CHANNEL receives: " + data  );

	model.links.robotenv.envdevices.resources.sonar2.value="";
	this.io.sockets.send( data );
});
channel.on('consoleUpdate', function(data) {
    console.log("\t CHANNEL receives: " + data  );

    console.log(data);
    const result = data.unifyWithContent("worldState(temperature(T), robotState(S))","T","S");
    const content = {
        msgId:data.msgId,
        temperature: result.T,
        robotState: result.S
    };
    this.io.sockets.send( content );

    console.log(result);
    model.links.robotenv.envdevices.resources.temperature.value = result.T;
    model.links.robot.resources.robotstate.state = result.S;  //shown in the page by app renderMainPage
});
channel.on('mapUpdate', function(data) {
    const result = data.unifyWithContent("mapUpdate(X,Y,S)","X","Y","S");
    const content = {
        msgId:data.msgId,
        x: result.X,
        y: result.Y,
        s: result.S.toString()
    };

    console.log("\t CHANNEL receives: " + data);

    const cell = {
        x: result.X,
        y: result.Y,
        s: result.S.toString()
	};
    const index  = model.links.robotenv.envdevices.resources.map.cells.indexOf(cell);
    if(index>=0){
        model.links.robotenv.envdevices.resources.map.cells.splice(index,1);
	}
    model.links.robotenv.envdevices.resources.map.cells.push(cell);
    console.log(model.links.robotenv.envdevices.resources.map.cells);
    this.io.sockets.send( content );
});
channel.on('robotPos', function(data) {
    const result = data.unifyWithContent("robotPos(X,Y,r)","X","Y");
    const content = {
        msgId:data.msgId,
        x: result.X,
        y: result.Y,
        s: 'r'
    };

    console.log("\t CHANNEL receives: " + data);

    const cell = {
        x: result.X,
        y: result.Y,
        s: 'r'
    };
    this.io.sockets.send( content );
});
channel.on('publishcmd', function(data) {  //emitted by robotControl;
	console.log("\t CHANNEL publishcmd: " + data + " on topic unibo/qasys" + " mqttUtils=" + mqttUtils);
//	if( mqttUtils == null )  mqttUtils  = require('./../utils/mqttUtils');		//CIRCULAR!!!
    publish( data );	//topic  = "unibo/qasys";
});
channel.on('photoM', function(data) {

    var photo = data.split('photoM(')[1].split("),")[0];
    photo = photo.split(")',")[0];

    this.io.sockets.send( data );
});

channel.on('bombRetrievedMsg', function(data) {
    data.msgId = "bombRetrievedEvent";
    data.msgType = "event";
    console.log("publish " + data);
    publish( data.toCompactForm());
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

channel.on('testMsg',function (msg) {
    console.log(msg.testUnification("testMsg(temp(10,20))"));
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
var QAmsg = require("./QAmsg.js");
client.on('connect', function () {
	  client.subscribe( topic );
	  console.log('\t MQTT client has subscribed successfully ');
});
//The message usually arrives as buffer, so we convert it to string data type;
client.on('message', function (topic, message){
	var msg = message.toString();
	var tempMsg;
    if( msg.indexOf( "photoM" ) > 0   ) {
        channel.emit("photoM", msg)
    }else if(msg.startsWith("msg(")){
	    console.log(msg);
        var qAMsg = new QAmsg(msg);
        channel.emit(qAMsg.msgId,qAMsg);
    }else if( msg.indexOf( "sonarDetect" ) > 0 ){
    	channel.emit("sonarDetect",  msg  );  //to allow update of the WebPage
    }else if( msg.indexOf( "sonar" ) > 0   ){
    	channel.emit("sonar",  msg  );  //to allow update of the WebPage
    }
	console.log("\t MQTT RECEIVES:"+ msg); //if toString is not given, the message comes as buffer;
});

publish = function( msg ){
	console.log('\t MQTT  publish ' + client);
	client.publish(topic, msg);
}