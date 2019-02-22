/* Generated by AN DISI Unibo */ 
package it.unibo.robot_retriever;
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
public abstract class AbstractRobot_retriever extends QActor { 
	protected AsynchActionResult aar = null;
	protected boolean actionResult = true;
	protected alice.tuprolog.SolveInfo sol;
	protected String planFilePath    = null;
	protected String terminationEvId = "default";
	protected String parg="";
	protected boolean bres=false;
	protected IActorAction action;
	//protected String mqttServer = "tcp://192.168.43.137:1883";
	
		protected static IOutputEnvView setTheEnv(IOutputEnvView outEnvView ){
			return outEnvView;
		}
		public AbstractRobot_retriever(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/robot_retriever/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/robot_retriever/plans.txt";
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
	    	stateTab.put("startRetrieving",startRetrieving);
	    	stateTab.put("doActions",doActions);
	    	stateTab.put("backToHome",backToHome);
	    	stateTab.put("nextMove",nextMove);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "robot_retriever tout : stops");  
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
	    	temporaryStr = "\"Robot Retriever Ready\"";
	    	println( temporaryStr );  
	    	it.unibo.planning.planUtil.initAI( myself  );
	    	//bbb
	     msgTransition( pr,myselfName,"robot_retriever_"+myselfName,false,
	          new StateFun[]{stateTab.get("startRetrieving") }, 
	          new String[]{"true","M","bombPositionMsg" },
	          6000000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun startRetrieving = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("startRetrieving",-1);
	    	String myselfName = "startRetrieving";  
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("bombPositionMsg(X,Y)");
	    	if( currentMessage != null && currentMessage.msgId().equals("bombPositionMsg") && 
	    		pengine.unify(curT, Term.createTerm("bombPositionMsg(X,Y)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg="bombPosition(X,Y)";
	    		/* AddRule */
	    		parg = updateVars(Term.createTerm("bombPositionMsg(X,Y)"),  Term.createTerm("bombPositionMsg(X,Y)"), 
	    			    		  			Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) addRule(parg);	    		  					
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?bombPosition(X,Y)" )) != null ){
	    	it.unibo.planning.planUtil.setGoal( myself ,guardVars.get("X"), guardVars.get("Y")  );
	    	}
	    	it.unibo.planning.planUtil.doPlanConservative( myself  );
	    	//bbb
	     msgTransition( pr,myselfName,"robot_retriever_"+myselfName,false,
	          new StateFun[]{stateTab.get("doActions") }, 
	          new String[]{"true","M","startRetrievingCmd" },
	          6000000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_startRetrieving){  
	    	 println( getName() + " plan=startRetrieving WARNING:" + e_startRetrieving.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//startRetrieving
	    
	    StateFun doActions = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("doActions",-1);
	    	String myselfName = "doActions";  
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?curPos(X,Y,D)" )) != null ){
	    	temporaryStr = "currPos(X,Y,D)";
	    	temporaryStr = QActorUtils.substituteVars(guardVars,temporaryStr);
	    	println( temporaryStr );  
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?move(M)" )) != null ){
	    	temporaryStr = "doActions_doingTheMove(M)";
	    	temporaryStr = QActorUtils.substituteVars(guardVars,temporaryStr);
	    	println( temporaryStr );  
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " not !?move(M)" )) != null )
	    	{
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"endAction","endAction", guardVars ).toString();
	    	sendMsg("endAction",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?moveDone(M)" )) != null ){
	    	temporaryStr = "moveDone(M)";
	    	temporaryStr = QActorUtils.substituteVars(guardVars,temporaryStr);
	    	println( temporaryStr );  
	    	}
	    	temporaryStr = "moveDone(_)";
	    	removeRule( temporaryStr );  
	    	{
	    	String tStr1 = "moveDuration(_)";
	    	String tStr2 = "moveDuration(moveWDuration(0))";
	    	 replaceRule( tStr1, tStr2 );  
	    	 }
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?doTheMove(a)" )) != null ){
	    	{//actionseq
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"moveAction(D,REPLAN)","moveAction(a,false)", guardVars ).toString();
	    	sendMsg("moveAction",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    	};//actionseq
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?doTheMove(d)" )) != null ){
	    	{//actionseq
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"moveAction(D,REPLAN)","moveAction(d,false)", guardVars ).toString();
	    	sendMsg("moveAction",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    	};//actionseq
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?doTheMove(w)" )) != null ){
	    	{//actionseq
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"moveAction(D,REPLAN)","moveAction(w,false)", guardVars ).toString();
	    	sendMsg("moveAction",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    	};//actionseq
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"robot_retriever_"+myselfName,false,
	          new StateFun[]{stateTab.get("nextMove"), stateTab.get("backToHome") }, 
	          new String[]{"true","M","moveAction", "true","M","endAction" },
	          60000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_doActions){  
	    	 println( getName() + " plan=doActions WARNING:" + e_doActions.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//doActions
	    
	    StateFun backToHome = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("backToHome",-1);
	    	String myselfName = "backToHome";  
	    	temporaryStr = "\"Back to home state\"";
	    	println( temporaryStr );  
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?curPos(0,0,D)" )) != null ){
	    	{//actionseq
	    	temporaryStr = "\"AT HOME \"";
	    	println( temporaryStr );  
	    	it.unibo.planning.planUtil.showMap( myself  );
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " ??bombPosition(X,Y)" )) != null ){
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"bombRetrieved(X,Y)","bombRetrieved(X,Y)", guardVars ).toString();
	    	sendMsg("bombRetrieved","map_keeper", QActorContext.dispatch, temporaryStr ); 
	    	}
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"backToInit","backToInit", guardVars ).toString();
	    	sendMsg("backToInit",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    	};//actionseq
	    	}
	    	else{ {//actionseq
	    	it.unibo.planning.planUtil.setGoal( myself ,"0", "0"  );
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?curPos(X,Y,D)" )) != null ){
	    	temporaryStr = "backToHome(X,Y,D)";
	    	temporaryStr = QActorUtils.substituteVars(guardVars,temporaryStr);
	    	println( temporaryStr );  
	    	}
	    	it.unibo.planning.planUtil.doPlanConservative( myself  );
	    	};//actionseq
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"robot_retriever_"+myselfName,false,
	          new StateFun[]{stateTab.get("init") }, 
	          new String[]{"true","M","backToInit" },
	          100, "doActions" );//msgTransition
	    }catch(Exception e_backToHome){  
	    	 println( getName() + " plan=backToHome WARNING:" + e_backToHome.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//backToHome
	    
	    StateFun nextMove = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("nextMove",-1);
	    	String myselfName = "nextMove";  
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("moveAction(w,_)");
	    	if( currentMessage != null && currentMessage.msgId().equals("moveAction") && 
	    		pengine.unify(curT, Term.createTerm("moveAction(D,REPLAN)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		{//actionseq
	    		if( (guardVars = QActorUtils.evalTheGuard(this, " ??moveDone(T)" )) != null ){
	    		it.unibo.utils.movePlanUtil.moveToRetriever( myself ,"w", guardVars.get("T")  );
	    		}
	    		};//actionseq
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("moveAction(a,_)");
	    	if( currentMessage != null && currentMessage.msgId().equals("moveAction") && 
	    		pengine.unify(curT, Term.createTerm("moveAction(D,REPLAN)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		{//actionseq
	    		if( (guardVars = QActorUtils.evalTheGuard(this, " !?timeTurn(T)" )) != null ){
	    		it.unibo.utils.movePlanUtil.moveToRetriever( myself ,"a", guardVars.get("T")  );
	    		}
	    		};//actionseq
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("moveAction(d,_)");
	    	if( currentMessage != null && currentMessage.msgId().equals("moveAction") && 
	    		pengine.unify(curT, Term.createTerm("moveAction(D,REPLAN)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		{//actionseq
	    		if( (guardVars = QActorUtils.evalTheGuard(this, " !?timeTurn(T)" )) != null ){
	    		it.unibo.utils.movePlanUtil.moveToRetriever( myself ,"d", guardVars.get("T")  );
	    		}
	    		};//actionseq
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?curPos(X,Y,D)" )) != null ){
	    	temporaryStr = "curPos(X,Y,D)";
	    	temporaryStr = QActorUtils.substituteVars(guardVars,temporaryStr);
	    	println( temporaryStr );  
	    	}
	    	//switchTo doActions
	        switchToPlanAsNextState(pr, myselfName, "robot_retriever_"+myselfName, 
	              "doActions",false, false, null); 
	    }catch(Exception e_nextMove){  
	    	 println( getName() + " plan=nextMove WARNING:" + e_nextMove.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//nextMove
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
