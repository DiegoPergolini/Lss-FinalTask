var express      = require('express');
var router       = express.Router();

var robotModel   = require("../models/robotModel"); //See 8) of ReadMe.txt

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Robot' });
});
 

/* see 9) of ReadMe.txt */
//router.get('/model', function(req, res, next) {
//	console.log(req.accepts('json'));
//  	//res.send(robotModel );
// 	req.myresult= robotModel  ;
// 	next();
//});
//
//router.get('/properties', function(req, res, next) {
//	console.log(robotModel);
//	console.log(req.accepts('prolog'));
//  	req.myresult= robotModel.links.properties ;
// 	next();
//});


module.exports = router;
