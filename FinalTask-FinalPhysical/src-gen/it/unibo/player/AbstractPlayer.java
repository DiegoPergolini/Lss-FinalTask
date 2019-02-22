/* Generated by AN DISI Unibo */ 
package it.unibo.player;
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
public abstract class AbstractPlayer extends QActor { 
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
		public AbstractPlayer(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/player/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/player/plans.txt";
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
	    	stateTab.put("cmdIntepreter",cmdIntepreter);
	    	stateTab.put("execMove",execMove);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "player tout : stops");  
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
	    	it.unibo.robotOnRaspOnly.physicalRobotExecutor.setUp( myself  );
	    	//delay  ( no more reactive within a plan)
	    	aar = delayReactive(1000,"" , "");
	    	if( aar.getInterrupted() ) curPlanInExec   = "init";
	    	if( ! aar.getGoon() ) return ;
	    	temporaryStr = "\"player STARTS\"";
	    	println( temporaryStr );  
	    	//switchTo cmdIntepreter
	        switchToPlanAsNextState(pr, myselfName, "player_"+myselfName, 
	              "cmdIntepreter",false, false, null); 
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun cmdIntepreter = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_cmdIntepreter",0);
	     pr.incNumIter(); 	
	    	String myselfName = "cmdIntepreter";  
	    	//bbb
	     msgTransition( pr,myselfName,"player_"+myselfName,false,
	          new StateFun[]{stateTab.get("execMove") }, 
	          new String[]{"true","M","robotCmd" },
	          600000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_cmdIntepreter){  
	    	 println( getName() + " plan=cmdIntepreter WARNING:" + e_cmdIntepreter.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//cmdIntepreter
	    
	    StateFun execMove = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("execMove",-1);
	    	String myselfName = "execMove";  
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("robotCmd(M,T)");
	    	if( currentMessage != null && currentMessage.msgId().equals("robotCmd") && 
	    		pengine.unify(curT, Term.createTerm("robotCmd(M,T)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		{/* JavaLikeMove */ 
	    		String arg1 = "M" ;
	    		arg1 =  updateVars( Term.createTerm("robotCmd(M,T)"), Term.createTerm("robotCmd(M,T)"), 
	    			                Term.createTerm(currentMessage.msgContent()),  arg1 );	                
	    		//end arg1
	    		it.unibo.robotOnRaspOnly.physicalRobotExecutor.doMove(this,arg1 );
	    		}
	    	}
	    	repeatPlanNoTransition(pr,myselfName,"player_"+myselfName,false,true);
	    }catch(Exception e_execMove){  
	    	 println( getName() + " plan=execMove WARNING:" + e_execMove.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//execMove
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
