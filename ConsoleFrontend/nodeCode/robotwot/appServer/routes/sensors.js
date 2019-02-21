/*
 * appServer/routes/sensors.js
 */
var express = require('express'),
  router    = express.Router(),
  model     = require('./../models/robotModel');

router.route('/').get(function (req, res, next) {
    req.type = "defaultView" ;
  	req.myresult = model.links.properties.resources.sensors;  
    next(); 
});

 
router.route('/temperature').get(function (req, res, next) {
  console.log( "................" );
  req.myresult = model.links.properties.resources.sensors.temperature; 
  console.log( req.myresult );
  console.log( "................" );
  next();
});

/*
router.route('/temperatureProlog').get(function (req, res, next) {
  var tval =  model.links.properties.resources.sensors.temperature.value ;
  console.log(tval);
  req.result = "msg( sensor, event, temperatureDev, none, "+ tval+", 0 )";
  //req.result = "msg( sensor, event, temperatureDev, none, 22, 0 )";
  next();
});
*/

module.exports = router;


 