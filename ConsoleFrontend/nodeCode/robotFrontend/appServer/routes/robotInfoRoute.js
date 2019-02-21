/*
 * appServer/routes/robotInfoRoute.js
 */
var express     = require('express'),
  router        = express.Router(),
  resourceModel = require('../models/robot').getResourceModel();

	//console.log(" +++++++++  routes/robotInfoRoute.js 0 "  ); 
  
router.route('/').get(function (req, res, next) {
	req.myresult = "showInfo";
	next();
})

module.exports = router;