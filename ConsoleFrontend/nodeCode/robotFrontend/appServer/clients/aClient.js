 /*
* =====================================
* it.unibo.mbot2018/nodeCode/robotFrontend/appserver/clients/aClient.js
* =====================================
*/
'use strict';
var request = require('request'); //npm install --save request;

var doGet = function(path){
	const optionsGetRoot = {
			method: 'GET',
			url: 'http://192.168.1.41:8080/'+path, //;
			headers: {
				'Content-Type': 'application/json',
				 'accept': 'application/json'
			}
		};
		request(optionsGetRoot, function(error, response, body){
			if (!error && response.statusCode == 200) {
				console.log(body);
			}
		} );	
}

var doMove = function(move){
	var optionsMove = {
		method: 'POST',
		url: 'http://192.168.1.41:8080/commands/'+move, //;
	    headers: { "Content-Type": "application/json" }
	};
 	request(optionsMove, function(error, response, body){
		if (!error && response.statusCode == 200) {
 			//console.log("done: " + move);
		}
	} );
}
//Get the metadata;
setTimeout( function(){ doGet("root") }, 100);
//Do some move;
setTimeout( function(){ doMove("w"); } , 400);
setTimeout( function(){ doMove("s"); } , 1000);
setTimeout( function(){ doMove("h"); } , 2000);
//Get the robotdevices;
setTimeout( function(){ doGet("robotdevices") }, 2300);
//Get the full model;
//setTimeout( function(){ doGet("showmodel") }, 3000);
