package it.unibo.utils;

import alice.tuprolog.SolveInfo;
import it.unibo.qactors.akka.QActor;

public class allRobots {

	public static void setUp( QActor qa, String robotType, String args ) {
		try {
			qa.println("allRobots " + robotType + " setUp args="+ args);
			switch( robotType ){
//				case "robotRealMbot"      : it.unibo.robotMBot.basicRobotExecutor.setUp(qa,args);break;
				case "robotRealRaspOnly"  : it.unibo.robotOnRaspOnly.basicRobotExecutor.setUp(qa);break;
//				case "robotVirtual"       : it.unibo.robotVirtual.basicRobotExecutor.setUp(qa,args);break;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void doMove( QActor qa, String cmd ) { //Args MUST be String
		try {
			SolveInfo sol = qa.solveGoal("robotType( T, _ )");
			String robotType = sol.getVarValue("T").toString();
			qa.println("allRobots " + robotType + " doMove cmd="+ cmd);
			switch( robotType ){
//				case "robotRealMbot"     : it.unibo.robotMBot.basicRobotExecutor.doMove(qa, cmd);break;
				case "robotRealRaspOnly" : it.unibo.robotOnRaspOnly.basicRobotExecutor.doMove(qa, cmd);break;
//				case "robotVirtual"      : it.unibo.robotVirtual.basicRobotExecutor.doMove(qa,cmd);break;
				default: qa.println("Sorry, robot type unknown");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
