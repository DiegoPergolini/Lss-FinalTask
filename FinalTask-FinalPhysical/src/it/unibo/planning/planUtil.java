/*
 * it.unibo.planning.planUtil in project it.unibo.mbot2018qa
 */
package it.unibo.planning;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List; 
import aima.core.agent.Action;
import aima.core.search.framework.SearchAgent;
import aima.core.search.framework.problem.GoalTest;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import it.unibo.planning.RobotState.Direction;
import it.unibo.qactors.akka.QActor;
 

/*
 * This utility has been defined after the work of the student Federico Stella.
 * It provides operations that can be called from a qa model
 * 
 * GOAL: provide an utility to find a sequence of moves (doPlan) with reference
 * to a map model (RoomMap) in which a cell with content
 * 		0 : means that the cell has not been yet covered by the robot
 * 		1 : means that the cell has been covered
 * 		X : means that the cell contains an obstacle
 * 
 * The sequence of moves provided by doPlan is a list of the form [w,a,...] etc.
 * However, doPlan stores also the sequence as a sequence of Prolog facts in the
 * qa knowledge base in the form:
 * 		move(w).
 * 		move(a).
 * 		...
 */

public class planUtil {
private static RobotState initialState;

/*
 * ------------------------------------------------
 * PLANNING	
 * ------------------------------------------------
 */
	private static BreadthFirstSearch search ;
	public static GoalTest goal;
	public static int goalX; 
	public static int goalY;
	
	public static void initAI(QActor qa) throws Exception {
 		initialState = new RobotState(0, 0, RobotState.Direction.DOWN);
		search = new BreadthFirstSearch(new GraphSearch());
	}

	public static void setGoalInit(QActor qa ) {   
		goal = new Functions();
	}
	
 	public static void setGoal(QActor qa, int x, int y ) {
		try {
 			System.out.println( "setGoal " + x + "," + y);
//			RoomMap.getRoomMap().put(x, y, new Box(false, true, false));
			planUtil.goalX = x;
			planUtil.goalY = y;
 			goal = new GoalTest() {		
				@Override
				public boolean isGoalState(Object state) {
					RobotState robotState = (RobotState) state;
					if (robotState.getX() == x && robotState.getY() == y ) 
 						return true;
					else
						return false;
 				}
			};
			clearCurrentPlan(qa);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public static void setGoal(QActor qa, String sx, String sy ) {
		try { 
	 		int x = Integer.parseInt(sx);
			int y = Integer.parseInt(sy);
 			setGoal(qa,x,y);
		} catch (Exception e) {
			e.printStackTrace();
		}
 	}

	public static void cleanQa(QActor qa) throws  Exception {
		System.out.println("planUtil cleanQa" );
		setGoalInit(qa);
 		RoomMap.getRoomMap().setDirty();
		showMap(qa);
	}
 	public static List<Action> doPlanConservative( QActor qa) throws Exception{
 		List<Action> actions;
		SearchAgent searchAgent;
		clearCurrentPlan(qa);
 		Problem problem = new Problem(initialState, 
 				new FunctionsConservative(), new FunctionsConservative(), goal, new FunctionsConservative());
		searchAgent = new SearchAgent(problem, search);
		actions     = searchAgent.getActions();
		System.out.println("Azioni: "+actions);
		if (actions == null || actions.isEmpty()) {
			System.out.println("planUtil doPlan NO MOVES !!!!!!!!!!!! " + actions );
			if (!RoomMap.getRoomMap().isClean()) RoomMap.getRoomMap().setObstacles();
			actions = new ArrayList<Action>();
			if( qa != null) qa.addRule("cleanFinished"); //by AN
			return null;
		}else if(actions.get(0).isNoOp() ) {
			System.out.println("planUtil doPlan NoOp" );	
			if( qa != null) qa.addRule("endOfWork");  
			return null;
		} 
		System.out.println("planUtil doPlan actions=" + actions );	
 		Iterator<Action> iter = actions.iterator();
		while( iter.hasNext() ) {
 			String s = iter.next().toString();
  			System.out.println("planUtil doPlan assertz:" + s);
 			if( qa != null) qa.solveGoal("assertz( move(" + s + "))");
		}		
		return actions;	
 	}
 	
 	public static List<Action> doPlanEnterprising( QActor qa) throws Exception{
 		List<Action> actions;
		SearchAgent searchAgent;
		clearCurrentPlan(qa);
 		Problem problem = new Problem(initialState, 
 				new Functions(), new Functions(), goal, new Functions());
		searchAgent = new SearchAgent(problem, search);
		actions     = searchAgent.getActions();
		System.out.println("Azioni: "+actions);
		if (actions == null || actions.isEmpty()) {
			System.out.println("planUtil doPlan NO MOVES !!!!!!!!!!!! " + actions );
			if (!RoomMap.getRoomMap().isClean()) RoomMap.getRoomMap().setObstacles();
			actions = new ArrayList<Action>();
			if( qa != null) qa.addRule("cleanFinished"); //by AN
			return null;
		}else if(actions.get(0).isNoOp() ) {
			System.out.println("planUtil doPlan NoOp" );	
			if( qa != null) qa.addRule("endOfWork");  
			return null;
		} 
		System.out.println("planUtil doPlan actions=" + actions );	
 		Iterator<Action> iter = actions.iterator();
		while( iter.hasNext() ) {
 			String s = iter.next().toString();
  			System.out.println("planUtil doPlan assertz:" + s);
 			if( qa != null) qa.solveGoal("assertz( move(" + s + "))");
		}		
		return actions;		
	}
	
	public static void clearCurrentPlan(QActor qa) {
		if( qa != null) {
			qa.solveGoal("retractall( move(X) )");
			System.out.println("All moves removed");
		}else {
			System.out.println("Non hai inserito qa");
		}
	}

/*
* ------------------------------------------------
* MIND MAP UPDATE	
* ------------------------------------------------
*/
 	public static void doMove( QActor qa, String move  )   {
		Direction dir = initialState.getDirection();
  		int dimMapx   = RoomMap.getRoomMap().getDimX();
		int dimMapy   = RoomMap.getRoomMap().getDimY();
		int x         = initialState.getX();
		int y         = initialState.getY();
//		System.out.println("planUtil: doMove move=" +  
//				move + " dir=" + dir +" x=" + x + " y="+y + " dimMapX=" + dimMapx + " dimMapY=" + dimMapy   );
		try { 
			switch( move ){
    	 	case "w" :   
		  		RoomMap.getRoomMap().put(x, y, new Box(false, false, false)); //clean the cell
 			  	initialState = (RobotState) new Functions().result(initialState, new RobotAction(RobotAction.FORWARD));
			  	RoomMap.getRoomMap().put(initialState.getX(), initialState.getY(), new Box(false, false, true));
 	 			break ;
	 		case "s" :   
				initialState = (RobotState) new Functions().result(initialState, new RobotAction(RobotAction.BACKWARD));
				RoomMap.getRoomMap().put(initialState.getX(), initialState.getY(), new Box(false, false, true));
				break ;
			case "a" : 
 	 			initialState = (RobotState) new Functions().result(initialState, new RobotAction(RobotAction.TURNLEFT));
	 			RoomMap.getRoomMap().put(initialState.getX(), initialState.getY(), new Box(false, false, true));
				break ;
			case "d" :  
	 			initialState = (RobotState) new Functions().result(initialState, new RobotAction(RobotAction.TURNRIGHT));
	 			RoomMap.getRoomMap().put(initialState.getX(), initialState.getY(), new Box(false, false, true));
				break ;
			case "c" :  	//forward and  clean 	
				RoomMap.getRoomMap().put(x, y, new Box(false, false, false));
				initialState = (RobotState) new Functions().result(initialState, new RobotAction(RobotAction.FORWARD));
				RoomMap.getRoomMap().put(initialState.getX(), initialState.getY(), new Box(false, false, true));
				break ; 
			case "obstacleOnRight" :   
				RoomMap.getRoomMap().put(x+1, y, new Box(true, false, false));
				break ;		
			case "obstacleOnLeft" :   
				RoomMap.getRoomMap().put(x-1, y, new Box(true, false, false));
				break ;		
			case "obstacleOnUp" :   
				RoomMap.getRoomMap().put(x, y-1, new Box(true, false, false));
				break ;		
			case "obstacleOnDown" :   
				RoomMap.getRoomMap().put(x, y+1, new Box(true, false, false));
				break ;		
			}//switch
		}catch( Exception e) {
			System.out.println("planUtil doMove: "+move+", Dir: "+dir+" in pos "+x+","+y+" ERROR:" +  e.getMessage() );
		}
		String newdir  = initialState.getDirection().toString().toLowerCase()+"Dir";
		int x1         = initialState.getX();
		int y1         = initialState.getY();		
		//update the kb
		System.out.println("planUtil: doMove move=" +  move + " newdir=" + newdir + " x1=" + x1 + " y1="+y1  );
		if( qa != null) qa.solveGoal("replaceRule( curPos(_,_,_), curPos("+ x1 +"," + y1 + ","+ newdir + "))");
 	}	
	
 	
	public static void findNextCellUncovered( QActor qa ) {
		  int dimMapx   = RoomMap.getRoomMap().getDimX();
		  int dimMapy   = RoomMap.getRoomMap().getDimY();
		  boolean b = false;
		  for (int i=0; i < dimMapx; i++) {			 
			for (int j=0; j < dimMapy; j++ ) {				
 				b = RoomMap.getRoomMap().isDirty(i,j);
				//System.out.println("isDirty " + i +","+ j + "="+b);
				if( b && ! (i==0 && j==0) ) {  
					if( qa != null) qa.addRule("uncovered("+ i +","+ j+ ")" );
 					break;
				}
			}
			if(b) break;
		  }
	}
		
	public static void showMap( QActor qa  )   {
		System.out.println( RoomMap.getRoomMap().toString() );
	}
 	
	public static void markCellAsObstacle( QActor qa ) {
		int x         = initialState.getX();
		int y         = initialState.getY(); 
		Direction dir = initialState.getDirection();
		System.out.println("markCellAsObstacle x=" + x + " y=" + y + " dir=" + dir);
		  switch( dir ){
		     case UP     :  RoomMap.getRoomMap().put(x, y-1, new Box(true, false, false)); break;
		     case LEFT   :  RoomMap.getRoomMap().put(x-1, y, new Box(true, false, false)); break;
		     case RIGHT  :  RoomMap.getRoomMap().put(x+1, y, new Box(true, false, false)); break;
		     case DOWN   :  RoomMap.getRoomMap().put(x, y+1, new Box(true, false, false));
		  }		
		
	}
	
 /*
  * Extend the current map with a new row and a new column
  */
	public static void extendSpaceToexplore(QActor qa )	{
		Direction dir = initialState.getDirection();
		int dimMapx = RoomMap.getRoomMap().getDimX();
		int dimMapy = RoomMap.getRoomMap().getDimY();
		int x   = initialState.getX();
		int y   = initialState.getY();
// 		System.out.println("planUtil: extendSpaceToexplore dir=" +  
//				dir + " x=" + x + " y="+y + " dimMapX=" + dimMapx + " dimMapY=" + dimMapy  );
		for (int i=0; i<dimMapy; i++) { //row  
			RoomMap.getRoomMap().put(dimMapx, i,  new Box(false, true, false));  
		}
		for (int i=0; i<dimMapx; i++) {
 			RoomMap.getRoomMap().put(i, dimMapy, new Box(false, true, false));  
		}
		  dimMapx = RoomMap.getRoomMap().getDimX();
		  dimMapy = RoomMap.getRoomMap().getDimY();
	 		System.out.println("planUtil: extendSpaceToexplore dir=" +  
			dir + " x=" + x + " y="+y + " dimMapX=" + dimMapx + " dimMapY=" + dimMapy  );
		RoomMap.getRoomMap().put(dimMapx-1,dimMapy-1, new Box(false, true, false));
		showMap(qa);		
	   
   }
 	
}
