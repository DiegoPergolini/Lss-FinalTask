/*
 * it.unibo.mbot2018/nodeCode/robotFrontEnd/controllers/applRobotControl.js
 */
const echannel    = require("./../utils/channel");

exports.actuate = function( cmd, req, res ){
	console.log("\t applRobotControl actuate " + cmd  );
	if( cmd === "w" ){ delegate("w(low)", "moving forward", req,res); }
	else if( cmd === "s" ){  delegate("s(low)", "moving backward", req,res); }
	else if( cmd === "h" ){  delegate("h(low)", "stopped", req,res); }
	else if( cmd === "a" ){  delegate("a(low)", "moving left", req,res); }
	else if( cmd === "d" ){  delegate("d(low)", "moving right", req,res); }
	//Application
	else if( cmd === "explore" ){   delegate("explore", "robot working at application level", req,res);  }
	else if( cmd === "halt" ){    delegate("halt", "robot halting the application level", req,res);  }
    else if( cmd === "resume" ){   delegate("resume", "robot resume exploration at application level", req,res);  }
    else if( cmd === "backtohome" ){    delegate("backtohome", "robot back to home at the application level", req,res);  }
    else if( cmd === "startretriever" ){    delegate("startretriever", "Operator starts the retriever", req,res);  }
    else if( cmd === "isbomb" ){
        echannel.emit("storeBombInfo", "");
    	delegate("isbomb", "Operator say that it is a bomb", req,res);
    }
    else if( cmd === "isnotbomb" ){   delegate("isnotbomb", "Operator say that it isn't a bomb", req,res);  }
    else if( cmd === "temperature" ){   delegate("tempOk("+req.query.value+")", "Operator say temperature", req,res);  }
}

/*
 * 
 */
var delegate = function ( hlcmd, newState, req, res ){
 	echannel.emit("robotState", newState);
 	var eventstr = "msg(usercmd,event,js,none,usercmd( " + hlcmd + "),1)"
    //console.log("\t robotControl emits: "+ eventstr);
 	echannel.emit("publishcmd", eventstr);
}