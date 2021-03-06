/* Generated by AN DISI Unibo */ 
package it.unibo.worldobserver;
import it.unibo.qactors.PlanRepeat;
import it.unibo.qactors.QActorContext;
import it.unibo.qactors.StateExecMessage;
import it.unibo.qactors.QActorUtils;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.qactors.action.AsynchActionResult;
import it.unibo.qactors.action.IActorAction;
import it.unibo.qactors.action.IActorAction.ActionExecMode;
import it.unibo.qactors.action.IMsgQueue;
import it.unibo.qactors.akka.QActor;
import it.unibo.qactors.StateFun;
import java.util.Stack;
import java.util.Hashtable;
import java.util.concurrent.Callable;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.qactors.action.ActorTimedAction;
public abstract class AbstractWorldobserver extends QActor { 
	protected AsynchActionResult aar = null;
	protected boolean actionResult = true;
	protected alice.tuprolog.SolveInfo sol;
	protected String planFilePath    = null;
	protected String terminationEvId = "default";
	protected String parg="";
	protected boolean bres=false;
	protected IActorAction action;
	//protected String mqttServer = "tcp://localhost:1883";
	
		protected static IOutputEnvView setTheEnv(IOutputEnvView outEnvView ){
			return outEnvView;
		}
		public AbstractWorldobserver(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/worldobserver/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/worldobserver/plans.txt";
	  	}
		@Override
		protected void doJob() throws Exception {
			String name  = getName().replace("_ctrl", "");
			mysupport = (IMsgQueue) QActorUtils.getQActor( name ); 
			initStateTable(); 
	 		initSensorSystem();
	 		history.push(stateTab.get( "init" ));
	  	 	autoSendStateExecMsg();
	  		//QActorContext.terminateQActorSystem(this);//todo
		} 	
		/* 
		* ------------------------------------------------------------
		* PLANS
		* ------------------------------------------------------------
		*/    
	    //genAkkaMshHandleStructure
	    protected void initStateTable(){  	
	    	stateTab.put("handleToutBuiltIn",handleToutBuiltIn);
	    	stateTab.put("init",init);
	    	stateTab.put("second",second);
	    	stateTab.put("handleBombPosition",handleBombPosition);
	    	stateTab.put("handleBombRetrieved",handleBombRetrieved);
	    	stateTab.put("handlePhoto",handlePhoto);
	    	stateTab.put("sendMapToRetriever",sendMapToRetriever);
	    	stateTab.put("handleCellValueChange",handleCellValueChange);
	    	stateTab.put("handleRobotStateChange",handleRobotStateChange);
	    	stateTab.put("handleChange",handleChange);
	    	stateTab.put("consoleUpdate",consoleUpdate);
	    	stateTab.put("checkCond",checkCond);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "worldobserver tout : stops");  
	    		repeatPlanNoTransition(pr,myselfName,"application_"+myselfName,false,false);
	    	}catch(Exception e_handleToutBuiltIn){  
	    		println( getName() + " plan=handleToutBuiltIn WARNING:" + e_handleToutBuiltIn.getMessage() );
	    		QActorContext.terminateQActorSystem(this); 
	    	}
	    };//handleToutBuiltIn
	    
	    StateFun init = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("init",-1);
	    	String myselfName = "init";  
	     connectToMqttServer("tcp://localhost:1883");
	    	//switchTo second
	        switchToPlanAsNextState(pr, myselfName, "worldobserver_"+myselfName, 
	              "second",false, false, null); 
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun second = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_second",0);
	     pr.incNumIter(); 	
	    	String myselfName = "second";  
	    	//bbb
	     msgTransition( pr,myselfName,"worldobserver_"+myselfName,true,
	          new StateFun[]{stateTab.get("handleBombRetrieved"), stateTab.get("handleChange"), stateTab.get("handleRobotStateChange"), stateTab.get("handleCellValueChange"), stateTab.get("handleBombPosition"), stateTab.get("sendMapToRetriever") }, 
	          new String[]{"true","M","bombRetrievedMsg", "true","M","temperatureChangeValue", "true","M","robotChangeState", "true","M","cellValueMsg", "true","M","bombPosition", "true","M","discoveryAtHome" },
	          6000000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_second){  
	    	 println( getName() + " plan=second WARNING:" + e_second.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//second
	    
	    StateFun handleBombPosition = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleBombPosition",-1);
	    	String myselfName = "handleBombPosition";  
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("bombPosition(X,Y)");
	    	if( currentMessage != null && currentMessage.msgId().equals("bombPosition") && 
	    		pengine.unify(curT, Term.createTerm("bombPosition(X,Y)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		/* replaceRule */
	    		String parg1="cellState(X,Y,_)"; 
	    		String parg2="cellState(X,Y,1)"; 
	    		parg1 = updateVars( Term.createTerm("bombPosition(X,Y)"),  Term.createTerm("bombPosition(X,Y)"), 
	    			    		  			Term.createTerm(currentMessage.msgContent()), parg1);
	    		parg2 = updateVars( Term.createTerm("bombPosition(X,Y)"),  Term.createTerm("bombPosition(X,Y)"), 
	    			    		  			Term.createTerm(currentMessage.msgContent()), parg2);
	    		replaceRule(parg1,parg2);
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("bombPosition(X,Y)");
	    	if( currentMessage != null && currentMessage.msgId().equals("bombPosition") && 
	    		pengine.unify(curT, Term.createTerm("bombPosition(X,Y)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg="bombPosition(X,Y)";
	    		/* AddRule */
	    		parg = updateVars(Term.createTerm("bombPosition(X,Y)"),  Term.createTerm("bombPosition(X,Y)"), 
	    			    		  			Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) addRule(parg);	    		  					
	    	}
	    	//switchTo second
	        switchToPlanAsNextState(pr, myselfName, "worldobserver_"+myselfName, 
	              "second",false, false, null); 
	    }catch(Exception e_handleBombPosition){  
	    	 println( getName() + " plan=handleBombPosition WARNING:" + e_handleBombPosition.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleBombPosition
	    
	    StateFun handleBombRetrieved = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleBombRetrieved",-1);
	    	String myselfName = "handleBombRetrieved";  
	    	temporaryStr = "\"Bomb retrieved msg\"";
	    	println( temporaryStr );  
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("bombRetrievedMsg(X,Y)");
	    	if( currentMessage != null && currentMessage.msgId().equals("bombRetrievedMsg") && 
	    		pengine.unify(curT, Term.createTerm("bombRetrievedMsg(X,Y)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg = "mapUpdate(X,Y,1)";
	    		/* PublishEventMove */
	    		parg =  updateVars( Term.createTerm("bombRetrievedMsg(X,Y)"), Term.createTerm("bombRetrievedMsg(X,Y)"), 
	    			    		  Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) sendMsgMqtt(  "unibo/qasys", "mapUpdate", "none", parg );
	    	}
	    	//switchTo second
	        switchToPlanAsNextState(pr, myselfName, "worldobserver_"+myselfName, 
	              "second",false, false, null); 
	    }catch(Exception e_handleBombRetrieved){  
	    	 println( getName() + " plan=handleBombRetrieved WARNING:" + e_handleBombRetrieved.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleBombRetrieved
	    
	    StateFun handlePhoto = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handlePhoto",-1);
	    	String myselfName = "handlePhoto";  
	    	temporaryStr = "\"Handling photo\"";
	    	println( temporaryStr );  
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("photoMsg(P)");
	    	if( currentMessage != null && currentMessage.msgId().equals("photoMsg") && 
	    		pengine.unify(curT, Term.createTerm("photoMsg(PHOTO)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg = "photoM(P)";
	    		/* PublishEventMove */
	    		parg =  updateVars( Term.createTerm("photoMsg(PHOTO)"), Term.createTerm("photoMsg(P)"), 
	    			    		  Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) sendMsgMqtt(  "unibo/qasys", "photoM", "none", parg );
	    	}
	    	//switchTo second
	        switchToPlanAsNextState(pr, myselfName, "worldobserver_"+myselfName, 
	              "second",false, false, null); 
	    }catch(Exception e_handlePhoto){  
	    	 println( getName() + " plan=handlePhoto WARNING:" + e_handlePhoto.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handlePhoto
	    
	    StateFun sendMapToRetriever = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("sendMapToRetriever",-1);
	    	String myselfName = "sendMapToRetriever";  
	    	parg = "allCell(D)";
	    	//QActorUtils.solveGoal(myself,parg,pengine );  //sets currentActionResult		
	    	solveGoal( parg ); //sept2017
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " ??goalResult(allCell(D))" )) != null ){
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"allMap(X)","allMap(D)", guardVars ).toString();
	    	sendMsg("allMap","map_keeper", QActorContext.dispatch, temporaryStr ); 
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " ??bombPosition(X,Y)" )) != null ){
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"bombPosition(X,Y)","bombPosition(X,Y)", guardVars ).toString();
	    	sendMsg("bombPosition","map_keeper", QActorContext.dispatch, temporaryStr ); 
	    	}
	    	//switchTo second
	        switchToPlanAsNextState(pr, myselfName, "worldobserver_"+myselfName, 
	              "second",false, false, null); 
	    }catch(Exception e_sendMapToRetriever){  
	    	 println( getName() + " plan=sendMapToRetriever WARNING:" + e_sendMapToRetriever.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//sendMapToRetriever
	    
	    StateFun handleCellValueChange = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleCellValueChange",-1);
	    	String myselfName = "handleCellValueChange";  
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("cellValue(X,Y,S)");
	    	if( currentMessage != null && currentMessage.msgId().equals("cellValueMsg") && 
	    		pengine.unify(curT, Term.createTerm("cellValue(X,Y,S)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		/* replaceRule */
	    		String parg1="cellState(X,Y,_)"; 
	    		String parg2="cellState(X,Y,S)"; 
	    		parg1 = updateVars( Term.createTerm("cellValue(X,Y,S)"),  Term.createTerm("cellValue(X,Y,S)"), 
	    			    		  			Term.createTerm(currentMessage.msgContent()), parg1);
	    		parg2 = updateVars( Term.createTerm("cellValue(X,Y,S)"),  Term.createTerm("cellValue(X,Y,S)"), 
	    			    		  			Term.createTerm(currentMessage.msgContent()), parg2);
	    		replaceRule(parg1,parg2);
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("cellValue(X,Y,S)");
	    	if( currentMessage != null && currentMessage.msgId().equals("cellValueMsg") && 
	    		pengine.unify(curT, Term.createTerm("cellValue(X,Y,S)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg = "mapUpdate(X,Y,S)";
	    		/* PublishEventMove */
	    		parg =  updateVars( Term.createTerm("cellValue(X,Y,S)"), Term.createTerm("cellValue(X,Y,S)"), 
	    			    		  Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) sendMsgMqtt(  "unibo/qasys", "mapUpdate", "none", parg );
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("cellValue(X,Y,1)");
	    	if( currentMessage != null && currentMessage.msgId().equals("cellValueMsg") && 
	    		pengine.unify(curT, Term.createTerm("cellValue(X,Y,S)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg = "robotPos(X,Y,r)";
	    		/* PublishEventMove */
	    		parg =  updateVars( Term.createTerm("cellValue(X,Y,S)"), Term.createTerm("cellValue(X,Y,1)"), 
	    			    		  Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) sendMsgMqtt(  "unibo/qasys", "robotPos", "none", parg );
	    	}
	    	//switchTo second
	        switchToPlanAsNextState(pr, myselfName, "worldobserver_"+myselfName, 
	              "second",false, false, null); 
	    }catch(Exception e_handleCellValueChange){  
	    	 println( getName() + " plan=handleCellValueChange WARNING:" + e_handleCellValueChange.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleCellValueChange
	    
	    StateFun handleRobotStateChange = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleRobotStateChange",-1);
	    	String myselfName = "handleRobotStateChange";  
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("robotState(V)");
	    	if( currentMessage != null && currentMessage.msgId().equals("robotChangeState") && 
	    		pengine.unify(curT, Term.createTerm("robotState(V)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg="changeRobotState(V)";
	    		/* PHead */
	    		parg =  updateVars( Term.createTerm("robotState(V)"), 
	    		                    Term.createTerm("robotState(V)"), 
	    			    		  	Term.createTerm(currentMessage.msgContent()), parg);
	    			if( parg != null ) {
	    			    aar = QActorUtils.solveGoal(this,myCtx,pengine,parg,"",outEnvView,86400000);
	    				//println(getName() + " plan " + curPlanInExec  +  " interrupted=" + aar.getInterrupted() + " action goon="+aar.getGoon());
	    				if( aar.getInterrupted() ){
	    					curPlanInExec   = "handleRobotStateChange";
	    					if( aar.getTimeRemained() <= 0 ) addRule("tout(demo,"+getName()+")");
	    					if( ! aar.getGoon() ) return ;
	    				} 			
	    				if( aar.getResult().equals("failure")){
	    					if( ! aar.getGoon() ) return ;
	    				}else if( ! aar.getGoon() ) return ;
	    			}
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?ledState(blinking)" )) != null ){
	    	{//actionseq
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"ledBlink","ledBlink", guardVars ).toString();
	    	sendMsg("ledBlink","ledcontrol", QActorContext.dispatch, temporaryStr ); 
	    	};//actionseq
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?ledState(off)" )) != null ){
	    	{//actionseq
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"ledOff","ledOff", guardVars ).toString();
	    	sendMsg("ledOff","ledcontrol", QActorContext.dispatch, temporaryStr ); 
	    	};//actionseq
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"worldobserver_"+myselfName,false,
	          new StateFun[]{}, 
	          new String[]{},
	          200, "consoleUpdate" );//msgTransition
	    }catch(Exception e_handleRobotStateChange){  
	    	 println( getName() + " plan=handleRobotStateChange WARNING:" + e_handleRobotStateChange.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleRobotStateChange
	    
	    StateFun handleChange = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleChange",-1);
	    	String myselfName = "handleChange";  
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("temperatureState(T)");
	    	if( currentMessage != null && currentMessage.msgId().equals("temperatureChangeValue") && 
	    		pengine.unify(curT, Term.createTerm("temperatureState(V)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg="changeTemperatureState(T)";
	    		/* PHead */
	    		parg =  updateVars( Term.createTerm("temperatureState(V)"), 
	    		                    Term.createTerm("temperatureState(T)"), 
	    			    		  	Term.createTerm(currentMessage.msgContent()), parg);
	    			if( parg != null ) {
	    			    aar = QActorUtils.solveGoal(this,myCtx,pengine,parg,"",outEnvView,86400000);
	    				//println(getName() + " plan " + curPlanInExec  +  " interrupted=" + aar.getInterrupted() + " action goon="+aar.getGoon());
	    				if( aar.getInterrupted() ){
	    					curPlanInExec   = "handleChange";
	    					if( aar.getTimeRemained() <= 0 ) addRule("tout(demo,"+getName()+")");
	    					if( ! aar.getGoon() ) return ;
	    				} 			
	    				if( aar.getResult().equals("failure")){
	    					if( ! aar.getGoon() ) return ;
	    				}else if( ! aar.getGoon() ) return ;
	    			}
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?invalidCond(T)" )) != null ){
	    	{//actionseq
	    	{
	    	String tStr1 = "robotState(_)";
	    	String tStr2 = "robotState(atHomeWithInvalidCondition)";
	    	 replaceRule( tStr1, tStr2 );  
	    	 }
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine, "invalidCondition(V)","invalidCondition(temperature(t))", guardVars ).toString();
	    	emit( "invalidCondition", temporaryStr );
	    	};//actionseq
	    	}
	    	else{ {//actionseq
	    	{
	    	String tStr1 = "robotState(_)";
	    	String tStr2 = "robotState(readyToStart)";
	    	 replaceRule( tStr1, tStr2 );  
	    	 }
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine, "validCondition(V)","validCondition(temperature(T))", guardVars ).toString();
	    	emit( "validCondition", temporaryStr );
	    	};//actionseq
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"worldobserver_"+myselfName,false,
	          new StateFun[]{}, 
	          new String[]{},
	          200, "consoleUpdate" );//msgTransition
	    }catch(Exception e_handleChange){  
	    	 println( getName() + " plan=handleChange WARNING:" + e_handleChange.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleChange
	    
	    StateFun consoleUpdate = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("consoleUpdate",-1);
	    	String myselfName = "consoleUpdate";  
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?worldState(T,S)" )) != null ){
	    	//PublisEventhMove simple
	    	parg = "worldState(temperature(T),robotState(S))";
	    	parg = QActorUtils.substituteVars(guardVars,parg);
	    	sendMsgMqtt(  "unibo/qasys", "consoleUpdate", "none", parg );
	    	}
	    	//switchTo second
	        switchToPlanAsNextState(pr, myselfName, "worldobserver_"+myselfName, 
	              "second",false, false, null); 
	    }catch(Exception e_consoleUpdate){  
	    	 println( getName() + " plan=consoleUpdate WARNING:" + e_consoleUpdate.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//consoleUpdate
	    
	    StateFun checkCond = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("checkCond",-1);
	    	String myselfName = "checkCond";  
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?invalidCond(T)" )) != null ){
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine, "invalidCondition(V)","invalidCondition(temperature(T))", guardVars ).toString();
	    	emit( "invalidCondition", temporaryStr );
	    	}
	    	else{ temporaryStr = QActorUtils.unifyMsgContent(pengine, "validCondition(V)","validCondition(temperature(T))", guardVars ).toString();
	    	emit( "validCondition", temporaryStr );
	    	}
	    	//switchTo init
	        switchToPlanAsNextState(pr, myselfName, "worldobserver_"+myselfName, 
	              "init",false, false, null); 
	    }catch(Exception e_checkCond){  
	    	 println( getName() + " plan=checkCond WARNING:" + e_checkCond.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//checkCond
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
