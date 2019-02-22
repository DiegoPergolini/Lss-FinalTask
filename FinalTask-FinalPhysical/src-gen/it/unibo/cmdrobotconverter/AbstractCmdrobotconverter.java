/* Generated by AN DISI Unibo */ 
package it.unibo.cmdrobotconverter;
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
public abstract class AbstractCmdrobotconverter extends QActor { 
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
		public AbstractCmdrobotconverter(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/cmdrobotconverter/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/cmdrobotconverter/plans.txt";
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
	    	stateTab.put("handleUserCmd",handleUserCmd);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "cmdrobotconverter tout : stops");  
	    		repeatPlanNoTransition(pr,myselfName,"application_"+myselfName,false,false);
	    	}catch(Exception e_handleToutBuiltIn){  
	    		println( getName() + " plan=handleToutBuiltIn WARNING:" + e_handleToutBuiltIn.getMessage() );
	    		QActorContext.terminateQActorSystem(this); 
	    	}
	    };//handleToutBuiltIn
	    
	    StateFun init = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_init",0);
	     pr.incNumIter(); 	
	    	String myselfName = "init";  
	     connectToMqttServer("tcp://192.168.43.137:1883");
	    	//bbb
	     msgTransition( pr,myselfName,"cmdrobotconverter_"+myselfName,false,
	          new StateFun[]{stateTab.get("handleUserCmd") }, 
	          new String[]{"true","E","usercmd" },
	          600000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun handleUserCmd = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleUserCmd",-1);
	    	String myselfName = "handleUserCmd";  
	    	printCurrentEvent(false);
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(robotgui(w(SPEED)))");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="robotCmd(w)";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("usercmd(X)"),  Term.createTerm("usercmd(robotgui(w(SPEED)))"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("robotCmd","player", QActorContext.dispatch, parg ); 
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(robotgui(s(SPEED)))");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="robotCmd(s)";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("usercmd(X)"),  Term.createTerm("usercmd(robotgui(s(SPEED)))"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("robotCmd","player", QActorContext.dispatch, parg ); 
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(robotgui(a(SPEED)))");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="robotCmd(a)";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("usercmd(X)"),  Term.createTerm("usercmd(robotgui(a(SPEED)))"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("robotCmd","player", QActorContext.dispatch, parg ); 
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(robotgui(d(SPEED)))");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="robotCmd(d)";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("usercmd(X)"),  Term.createTerm("usercmd(robotgui(d(SPEED)))"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("robotCmd","player", QActorContext.dispatch, parg ); 
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(robotgui(h(SPEED)))");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="robotCmd(h)";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("usercmd(X)"),  Term.createTerm("usercmd(robotgui(h(SPEED)))"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("robotCmd","player", QActorContext.dispatch, parg ); 
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(explore)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="startAppl(ok)";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("usercmd(X)"),  Term.createTerm("usercmd(explore)"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("startAppl","mind", QActorContext.dispatch, parg ); 
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(halt)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="haltAppl(ok)";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("usercmd(X)"),  Term.createTerm("usercmd(halt)"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("haltAppl","mind", QActorContext.dispatch, parg ); 
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(isbomb)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="backToHomeSinceBomb";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("usercmd(X)"),  Term.createTerm("usercmd(isbomb)"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("backToHomeSinceBomb","mind", QActorContext.dispatch, parg ); 
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(isnotbomb)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="continueExploration";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("usercmd(X)"),  Term.createTerm("usercmd(isnotbomb)"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("continueExploration","mind", QActorContext.dispatch, parg ); 
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(tempOk(T))");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="temperatureState(T)";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("usercmd(X)"),  Term.createTerm("usercmd(tempOk(T))"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("temperatureChangeValue","worldobserver", QActorContext.dispatch, parg ); 
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(tempNotOk)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="temperatureState(28)";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("usercmd(X)"),  Term.createTerm("usercmd(tempNotOk)"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("temperatureChangeValue","worldobserver", QActorContext.dispatch, parg ); 
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(resume)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="resumeExploration(ok)";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("usercmd(X)"),  Term.createTerm("usercmd(resume)"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("resumeExploration","mind", QActorContext.dispatch, parg ); 
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(backtohome)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="backToHome(ok)";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("usercmd(X)"),  Term.createTerm("usercmd(backtohome)"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("backToHome","mind", QActorContext.dispatch, parg ); 
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(startretriever)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="startRetrievingMsg";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("usercmd(X)"),  Term.createTerm("usercmd(startretriever)"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("startRetrievingMsg","map_keeper", QActorContext.dispatch, parg ); 
	    	}
	    	repeatPlanNoTransition(pr,myselfName,"cmdrobotconverter_"+myselfName,false,true);
	    }catch(Exception e_handleUserCmd){  
	    	 println( getName() + " plan=handleUserCmd WARNING:" + e_handleUserCmd.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleUserCmd
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}