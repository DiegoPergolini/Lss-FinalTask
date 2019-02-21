var express  = require('express'),
  router     = express.Router(),
  robotModel = require('../models/robotModel');

router.route('/').get(function (req, res, next) {
  req.myresult = robotModel.links.properties.resources.actuators;
  next();
});

router.route('/leds').get(function (req, res, next) {
  req.myresult = robotModel.links.properties.resources.actuators.leds;
  next();
});

//The request object is an instance of IncomingMessage
//https://nodejs.org/api/http.html#http_http_incomingmessage

//see pg 189
router.route('/leds/:id')
.get(function (req, res, next) { 
	console.log("get led " + req.params.id);
  req.myresult = robotModel.links.properties.resources.actuators.leds.values[req.params.id];
  next();
})
.put(function(req, res, next) {  
  var selectedLed = robotModel.pi.actuators.leds[req.params.id];
  //UPDATE the value of the selected LED in the model
  console.info('route LED actuators Changing LED %s value to %s', req.params.id, req.body.value);
  //console.info(selectedLed);
  console.info(req.body);
  //for( i in req ){ console.info('req field %s = %s ',   i, req.body.value);  }	
  selectedLed.value = req.body.value;  //#C
  console.info('route LED  Changed LED %s value to %s', req.params.id, selectedLed.value);
  req.myresult = selectedLed;
  next();
});

module.exports = router;

 