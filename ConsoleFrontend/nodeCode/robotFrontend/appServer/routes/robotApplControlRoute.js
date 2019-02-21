/*
 * appServer/routes/robotApplControlRoute.js
 */
var express     = require('express'),
  router        = express.Router() ;

const robotControl   = require('../controllers/applRobotControl');

router.post("/explore", function(req, res, next) { 
	robotControl.actuate("explore", req, res ); 
	next();
});
router.post("/halt", function(req, res, next) { 
	robotControl.actuate("halt", req, res ); 
	next(); 
});
router.post("/resume", function(req, res, next) {
    robotControl.actuate("resume", req, res );
    next();
});
router.post("/backtohome", function(req, res, next) {
    robotControl.actuate("backtohome", req, res );
    next();
});
router.post("/startretriever", function(req, res, next) {
    robotControl.actuate("startretriever", req, res );
    next();
});
router.post("/isbomb", function(req, res, next) {
    robotControl.actuate("isbomb", req, res );
    next();
});
router.post("/isnotbomb", function(req, res, next) {
    robotControl.actuate("isnotbomb", req, res );
    next();
});
router.get("/temperature", function(req, res, next) {
    console.log("weee");
    console.log("temp is " + req.query.value);
    robotControl.actuate("temperature", req, res );
    next();
});
module.exports = router;