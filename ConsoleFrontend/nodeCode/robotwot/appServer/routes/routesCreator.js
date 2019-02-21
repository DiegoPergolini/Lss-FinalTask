var express = require('express'),
  router = express.Router();
//  	 = require('node-uuid'),
  utils = require('./../utils/utils');

exports.create = function (model) {
	  console.log("create the routes "   ); 
 
	  createRootRoute(model);
 	  createModelRoutes(model);
 	  createPropertiesRoutes(model);
	  createActionsRoutes(model);
	  createRobotActionsRoutes(model);
	  
	  return router;
}; //create



function createRootRoute(model) {
	console.log("createRootRoute (GET /)");
	router.route('/').get(function (req, res, next) {
	    req.model = model;
	    req.type = 'root';

	    var fields   = ['id', 'name', 'description', 'tags', 'customFields'];
	    req.myresult = utils.extractFields(fields, model);

	    if (model['@context']) type = model['@context'];
	    else type = 'http://model.webofthings.io/';

	    res.links({
	      model: '/model/',
	      properties: '/properties/',
	      actions: '/actions/',
	      things: '/things/',
	      help: '/help/',
	      ui: '/',
	      type: type
	    });

	    next();
	  });
	};

	function createModelRoutes(model) {
		console.log("createModelRoutes (GET /model)");
		  // GET /model
		  router.route('/model').get(function (req, res, next) {
		    req.myresult = model;
		    console.log(" xxx ................ ");
		    req.model    = model;

		    if (model['@context']) type = model['@context'];
		    else type = 'http://model.webofthings.io/';
		    res.links({
		      type: type
		    });

		    next();
		  });
		};

	function createPropertiesRoutes(model) {
		console.log("createPropertiesRoutes (GET /properties, GET /properties/{id})");
		  var properties = model.links.properties;

		  // GET /properties
		  router.route(properties.link).get(function (req, res, next) {
		    req.model    = model;
		    req.type     = 'properties';
		    req.entityId = 'properties';

		    req.myresult = utils.modelToResources(properties.resources, true);

		    // Generate the Link headers 
		    if (properties['@context']) type = properties['@context'];
		    else type = 'http://model.webofthings.io/#properties-resource';

		    res.links({
		      type: type
		    });

		    next();
		  });

		  // GET /properties/{id}
		  router.route(properties.link + '/:id').get(function (req, res, next) {
		    req.model         = model;
		    req.propertyModel = properties.resources[req.params.id];
		    req.type          = 'property';
		    req.entityId      = req.params.id;

		    req.myresult      = reverseResults(properties.resources[req.params.id].data);

		    // Generate the Link headers 
		    if (properties.resources[req.params.id]['@context']) type = properties.resources[req.params.id]['@context'];
		    else type = 'http://model.webofthings.io/#properties-resource';

		    res.links({
		      type: type
		    });

		    next();
		  });
		};

function reverseResults(array) {
	  return array.slice(0).reverse();
}
const robotControl  = require('../controllers/robotControl');

function createRobotActionsRoutes (model) {
	  console.log("createRobotActionsRoutes ( POST /robot/actions/commands/CMD )"  ); 
	  
	  router.route('/robot/actions/commands/:cmdType').post(function (req, res, next) {
		  var url = req.originalUrl;
 		  console.log( "doing robot command:" + req.params.cmdType ); 
		  robotControl.actuate( req.params.cmdType , req, res);
		  req.myresult ="done " + req.params.cmdType;
		  next();
	  });
}//createRobotActionsRoutes

function createActionsRoutes (model) {
  console.log("createActionsRoutes (GET /actions , POST /actions/{actionType} , GET /actions/{actionType} ");
  var actions = model.links.actions;
 
  // GET /actions
  router.route(actions.link).get(function (req, res, next) {
    req.myresult = utils.modelToResources(actions.resources, true);
    
    req.model = model;
    req.type = 'actions';
    req.entityId = 'actions';

    if (actions['@context']) type = actions['@context'];
    else type = 'http://model.webofthings.io/#actions-resource';

    res.links({
      type: type
    });

    next();
  });

  // POST /actions/{actionType}
  router.route(actions.link + '/:actionType').post(function (req, res, next) {
    console.log( "actions POST " + req.originalUrl );  
    var action       = req.body;
    action.id        = "todo"; 		//uuid.v1();
    action.status    = "pending";
    action.timestamp = utils.isoTimestamp();
    console.log("/actions action=" + action); //AN
    utils.cappedPush(actions.resources[req.params.actionType].data, action);
    res.location(req.originalUrl + '/' + action.id);
    console.log("/actions location=" + (req.originalUrl + '/' + action.id) ); //AN
	//simulate observer plugin reaction AN
	if(req.originalUrl.indexOf( "/actions/ledState" ) >= 0 ){
		console.log("AD HOC" );
		res.send("exec the plugin");
		//res.redirect("/ledChange");
	}
    else next();
  });

  
//GET /actions/{actionType}
  router.route(actions.link + '/:actionType').get(function (req, res, next) {
    req.myresult    = reverseResults(actions.resources[req.params.actionType].data);
	console.log("route actions GET=" + req.myresult  ); //AN
    req.actionModel = actions.resources[req.params.actionType];
    req.model       = model;

    req.type        = 'action';
    req.entityId    = req.params.actionType;
    console.log("route actions GET entityId=" + req.entityId ); //AN
    if (actions.resources[req.params.actionType]['@context']) 
  	  type = actions.resources[req.params.actionType]['@context'];
    else type = 'http://model.webofthings.io/#actions-resource';

    res.links({
      type: type
    });


    next();
  });
 
}//createActionsRoutes


 
