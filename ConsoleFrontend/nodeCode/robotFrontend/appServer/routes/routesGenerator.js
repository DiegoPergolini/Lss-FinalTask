/*
 * -----------------------------------------------
 * it.unibo.mbot2018/nodeCode/robotFrontend/appServer/routes/routesGenerator.js
 * -----------------------------------------------
*/
var express = require('express'),
  router    = express.Router();
const modelutils = require('./../utils/modelUtils');

exports.create = function (model) {
//Extend the model with data
createDefaultData(model.links.robot.resources.robotdevices.resources);
createDefaultData(model.links.robotenv.envdevices.resources);

  // Let's create the routes
  createRootRoute(model);
  createModelRoutes(model);
//  createActionsRoutes(model);
  return router;
};

function createDefaultData(resources) {
	Object.keys(resources).forEach(function (resKey) {
	    var resource  = resources[resKey];
	    resource.data = [];
	});
}

function createRootRoute(model) {
	  router.route('/root').get(function (req, res, next) {
	    req.model = model;
	    req.type = 'robotInfo';

	    var fields = ['id', 'name', 'description', 'customFields', 'help'];
	    req.myresult = modelutils.extractFields(fields, model);

 	    //CREATE the Link header containing links to the other resources (HATEOAS);see the header
	    res.links({
	        robot: model.links.robot.link,
 	        commands: model.links.robot.resources.commands.link
	    });
//	    next();
	    if (req.accepts('html')) {
         	res.render(req.type,  
    				{'title': 'Robot Metadata', 'res': "", 'model': model,'host': req.headers.host}); 	
	    }	else if (req.accepts('application/json')) res.send( req.myresult );
	  });
};

function createModelRoutes(model) {
	  // GET /showmodel
	  router.route('/showmodel').get(function (req, res, next) {
	    req.myresult  = model;
	    req.model     = model;
	    req.modelStr  = "\""+JSON.stringify(model, null, 2)+"\"";
	    req.type   = 'showmodel';
	    type = 'http://model.webofthings.io/';
	    res.links({
	      type: type
	    });

	    next();
	  });
};