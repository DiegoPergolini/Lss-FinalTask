/*
 * -----------------------------------------------
 * it.unibo.mbot2018/nodeCode/robotFrontend/appServer/models/robot.js
 * -----------------------------------------------
*/
var resources  = require('./robot.json');

exports.actionsLinkUrlStr = resources.links.robot.resources.commands.link;		 //	"/commands";
exports.stateLinkUrlStr   = resources.links.robot.resources.robotstate.link; 		 // "/robotState";
exports.robotMoves   = resources.links.robot.resources.commands.resources; 		  
 
exports.getResourceModel = function(){ return resources; }

exports.getRobotState = function(){ return resources.links.robot.resources.robotstate.state; }

exports.extractFields = function(fields, object, target) {
	  if(!target) var target = {};
	  var arrayLength = fields.length;
	  for (var i = 0; i < arrayLength; i++) {
	    var field = fields[i];
	    target[field] = object[field];
	  }
	  return target;
}

exports.envmodelToResources = function( subModel ) {
	  var resources = [];
	  Object.keys(subModel).forEach(function(key) {
		//console.log("utils modelToResources key=" +key );
	    var val       = subModel[key];
	    var resource  = {};
	    resource.id   = key;
	    resource.name = val['name'];
	    resource.value= val['value'];
	    //console.log( val.data );
	    if( val.data != undefined) {
	    	resource.values = val.data;
	    }
	    resources.push(resource);
	  });
	  return resources;
};

//module.exports = resources;

