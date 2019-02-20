package it.unibo.utils;

import it.unibo.planning.planUtil;
import it.unibo.qactors.QActorContext;
import it.unibo.qactors.akka.QActor;

public class movePlanUtil {
	
	public static void move(QActor qa, String move, String duration ) {
	  try{	
  		   String temporaryStr = "robotCmd(MOVE,T)".replace("MOVE", move).replace("T", duration) ;
  		   //System.out.println( "movePlanUtil temporaryStr:" + temporaryStr );
 		   qa.sendMsg("robotCmd","player", QActorContext.dispatch, temporaryStr ); //do the robot move
  		   planUtil.doMove( qa ,move );  //update the map
  		   Thread.sleep( Integer.parseInt(duration));
  	  }catch(Exception e ){  
		   System.out.println( "movePlanUtil ERROR:" + e.getMessage() );
 	  }		
	}
	public static void moveToRetriever(QActor qa, String move, String duration ) {
		  try{	
	  		   String temporaryStr = "robotCmd(MOVE,T)".replace("MOVE", move).replace("T", duration) ;
	  		   System.out.println( "movePlanUtil temporaryStr:" + temporaryStr );
	 		   qa.sendMsg("robotCmd","player_retriever", QActorContext.dispatch, temporaryStr ); //do the robot move
	  		   planUtil.doMove( qa ,move );  //update the map
	  		   Thread.sleep( Integer.parseInt(duration));
	  	  }catch(Exception e ){  
			   System.out.println( "movePlanUtil ERROR:" + e.getMessage() );
	 	  }		
		}
	
	public static void moveNoMap(QActor qa, String move, String duration ) {
		  try{	
	  		   String temporaryStr = "robotCmd(MOVE,T)".replace("MOVE", move).replace("T", duration) ;
	  		   System.out.println( "movePlanUtil moveNoMap temporaryStr:" + temporaryStr );
	 		   qa.sendMsg("robotCmd","player", QActorContext.dispatch, temporaryStr ); //do the robot move
 	  		   Thread.sleep( Integer.parseInt(duration));
 	 	  }catch(Exception e ){  
			   System.out.println( "movePlanUtil ERROR:" + e.getMessage() );
	 	  }		
		} 

	/*
	 * ------------------------------------------------
	 * TIMER	
	 * ------------------------------------------------
	 */
	private static long timeStart = 0;
	private static long adjust    = 20;

		public static void startTimer( QActor qa) {
			timeStart = System.currentTimeMillis();
		}
		public static void getDuration( QActor qa) {
			int duration = (int) (System.currentTimeMillis() - timeStart) ;
			//System.out.println( "movePlanUtil getDuration=" + duration);
			long t = duration - adjust;
			if( t > 0 ) qa.replaceRule("moveWDuration(_)", "moveWDuration("+ t + ")");
			else qa.replaceRule("moveWDuration(_)", "moveWDuration("+ duration + ")");		 
		}
}
