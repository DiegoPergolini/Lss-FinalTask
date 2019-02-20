package it.unibo.utils;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import aima.core.agent.Action;
import aima.core.search.framework.SearchAgent;
import aima.core.search.framework.problem.GoalTest;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import alice.tuprolog.SolveInfo;
import it.unibo.exploremap.model.Box;
import it.unibo.exploremap.model.Functions;
import it.unibo.exploremap.model.RobotAction;
import it.unibo.exploremap.model.RobotState;
import it.unibo.exploremap.model.RobotState.Direction;
import it.unibo.exploremap.model.RoomMap;
import it.unibo.qactors.akka.QActor;

public class aiutil {
private static RobotState initialState;
		
/*
 * ------------------------------------------------
 * PLANNING	
 * ------------------------------------------------
 */
	private static BreadthFirstSearch search ;
	
	public static void initAI(QActor qa) throws Exception {
 		initialState = new RobotState(0, 0, RobotState.Direction.DOWN);
		search = new BreadthFirstSearch(new GraphSearch());
	}
	
	public static void cleanQa(QActor qa) throws  Exception {
		System.out.println("aiutil cleanQa" );
		setGoalInit(qa);
 		RoomMap.getRoomMap().setDirty();
		showMap(qa);
	}
	 
	public static void cell0DirtyForHome(QActor qa) throws  Exception {
		RoomMap.getRoomMap().put(0, 0, new Box(false, true, false));
	}
	
	public static GoalTest goalTest;
	
	public static List<Action> doPlan( QActor qa  ) throws Exception {
 		List<Action> actions;
// 		GoalTest goalTest= new Functions();
		SearchAgent searchAgent;
 //		System.out.println("aiutil doPlan newProblem (A) " );
		Problem problem = new Problem(initialState, new Functions(), new Functions(), goalTest, new Functions());
//		System.out.println("aiutil doPlan newProblem (A) search " );
		searchAgent = new SearchAgent(problem, search);
		actions     = searchAgent.getActions();
		if (actions == null || actions.isEmpty()) {
			System.out.println("aiutil doPlan NO MOVES !!!!!!!!!!!! " + actions );
			if (!RoomMap.getRoomMap().isClean()) RoomMap.getRoomMap().setObstacles();
			actions = new ArrayList<Action>();
			qa.addRule("cleanFinished"); //by AN
			return null;
		}else if(actions.get(0).isNoOp() ) {
			System.out.println("aiutil doPlan NoOp" );	
			qa.addRule("endOfWork"); //by AN (optimized target)
			return null;
		} 
		System.out.println("aiutil doPlan actions=" + actions + " iter:" + actions.iterator().hasNext());	
//		if( actions.size() > 2  ) qa.solveGoal("logMove( plan(" + actions + "))");
		Iterator<Action> iter = actions.iterator();
		while( iter.hasNext() ) {
 			String s = iter.next().toString();
  			//System.out.println("aiutil doPlan assertz:" + s);
			qa.solveGoal("assertz( move(" + s + "))");
		}		
		return actions;		
	}
	
	public static void clearCurrentPlan(QActor qa) {
		qa.solveGoal("retractall( move(X) )");
	}

/*
* ------------------------------------------------
* MIND MAP UPDATE	
* ------------------------------------------------
*/
	public static void handleWallDownFound( QActor qa  ) throws Exception {
 		int dimMapx = RoomMap.getRoomMap().getDimX();
		int dimMapy = RoomMap.getRoomMap().getDimY();
		int x   = initialState.getX();
		int y   = initialState.getY();
		System.out.println("aiutil: handleWallDownFound dir=" +  
				initialState.getDirection() + " x=" + x + " y="+y + " dimMapX=" + dimMapx + " dimMapY=" + dimMapy);
 		RoomMap.getRoomMap().put(x, y, new Box(true, false, false));
		System.out.println("aiutil: WallDown found ");
		RoomMap.getRoomMap().put(initialState.getX(), initialState.getY(), new Box(false, false, true));
// 		RoomMap.getRoomMap().put(x, y, new Box(true, false, false));
 		showMap(qa);
	}


	public static void handleWallRightFound( QActor qa   ) throws Exception {
		Direction dir = initialState.getDirection();
		int dimMapx = RoomMap.getRoomMap().getDimX();
		int dimMapy = RoomMap.getRoomMap().getDimY();
		int x   = initialState.getX();
		int y   = initialState.getY();
		System.out.println("aiutil: handleWallRightFound dir=" +  
				dir + " x=" + x + " y="+y + " dimMapX=" + dimMapx + " dimMapY=" + dimMapy  );
		for (int i=0; i<dimMapy; i++) {
			RoomMap.getRoomMap().put(x+1, i,  new Box(true, false, false)); //wall column
		}
		for (int i=0; i<dimMapx; i++) {
 			RoomMap.getRoomMap().put(i, y+1, new Box(true, false, false)); //wall row
		}
		showMap(qa);		
	}

	public static void doMove( QActor qa, String move  )   {
		Direction dir = initialState.getDirection();
  		int dimMapx   = RoomMap.getRoomMap().getDimX();
		int dimMapy   = RoomMap.getRoomMap().getDimY();
		int x         = initialState.getX();
		int y         = initialState.getY();
//		System.out.println("aiutil: doMove move=" +  
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
			System.out.println("aiutil doMove: ERROR:" +  e.getMessage() );
		}
		String newdir  = initialState.getDirection().toString().toLowerCase()+"Dir";
		int x1         = initialState.getX();
		int y1         = initialState.getY();		
		//update the kb
		//System.out.println("aiutil: doMove move=" +  move + " newdir=" + newdir + " x1=" + x1 + " y1="+y1  );
		qa.solveGoal("replaceRule( curPos(_,_,_), curPos("+ x1 +"," + y1 + ","+ newdir + "))");
// 		if( ! move.equals("a") &&  ! move.equals("d") ) showMap(qa);
// 		else System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	}	
	
//	public static void checkIfNextCellCleaned( QActor qa ) {
//	  String dir = initialState.getDirection().toString();
//	  int dimMapx   = RoomMap.getRoomMap().getDimX();
//	  int dimMapy   = RoomMap.getRoomMap().getDimY();
//	  int x         = initialState.getX();
//	  int y         = initialState.getY();
//	  boolean clean = false;
//		  switch( dir ){
//		     case "upDir"     : clean = (y > 0) && ! RoomMap.getRoomMap().isDirty(x,y-1); break;
//		     case "downDir"   : clean = (y < dimMapy-1) && ! RoomMap.getRoomMap().isDirty(x,y+1); break;
//		     case "leftDir"   : clean = (x > 0) && ! RoomMap.getRoomMap().isDirty(x-1,y); break;
//		     case "rigthDir"  : clean = (x < dimMapx-1) && ! RoomMap.getRoomMap().isDirty(x+1,y); break;
//		  }
//		  if( clean) qa.addRule( "nextCellIsClean" );
//	}
	
	public static void findNextCellUncovered( QActor qa ) {
		  int dimMapx   = RoomMap.getRoomMap().getDimX();
		  int dimMapy   = RoomMap.getRoomMap().getDimY();
		  boolean b = false;
		  for (int i=0; i < dimMapx; i++) {			 
			for (int j=0; j < dimMapy; j++ ) {				
 				b = RoomMap.getRoomMap().isDirty(i,j);
				//System.out.println("isDirty " + i +","+ j + "="+b);
				if( b && ! (i == 0 && j==0) ) { 
 					setGoal(qa, i, j );
 					qa.addRule("uncovered("+ i +","+ j+ ")" );
 					break;
				}
			}
			if(b) break;
		  }
	}
	
	public static void setRobotInDownDir( QActor qa ) {  //move also the robot, not only the map
		  Direction dir = initialState.getDirection() ;
//		  System.out.println("aiutil: setRobotInDownDir dir=" + dir   );
		  switch( dir ){
		     case UP     :  movePlanUtil.move(qa,"d","300");movePlanUtil.move(qa,"d","300"); break;
		     case LEFT   :  movePlanUtil.move(qa,"a","300"); break;
		     case RIGHT  :  movePlanUtil.move(qa,"d","300"); break;
		     case DOWN   :  
		  }		
	}
	
	public static void showMap( QActor qa  )   {
		System.out.println( RoomMap.getRoomMap().toString() );
	}
	public static void updateMapWithObstacle( QActor qa  ) throws Exception {
		System.out.println("aiutil: updateMapWithObstacle ...");
		RoomMap.getRoomMap().put(initialState.getX(), initialState.getY(), new Box(true, false));
	}
	
	public static void markCellAsObstacle( QActor qa ) {
		int x         = initialState.getX();
		int y         = initialState.getY(); 
		RoomMap.getRoomMap().put(x, y+1, new Box(true, false, false));
	}
	
	public static void limitRightFound( QActor qa   )   {
		Direction dir = initialState.getDirection();
		int dimMapx = RoomMap.getRoomMap().getDimX();
		int dimMapy = RoomMap.getRoomMap().getDimY();
		int x   = initialState.getX();
		int y   = initialState.getY();
		System.out.println("aiutil: limitRightFound dir=" +  
				dir + " x=" + x + " y="+y + " dimMapX=" + dimMapx + " dimMapY=" + dimMapy  );
		for (int i=0; i<dimMapy; i++) {
			RoomMap.getRoomMap().put(x, i,  new Box(false, false, false)); //limit column
		}
		RoomMap.getRoomMap().put(x, y, new Box(false, false, true));
		showMap(qa);		
		
	}
  
	public static void extendSpaceToexplore(QActor qa )	{
		Direction dir = initialState.getDirection();
		int dimMapx = RoomMap.getRoomMap().getDimX();
		int dimMapy = RoomMap.getRoomMap().getDimY();
		int x   = initialState.getX();
		int y   = initialState.getY();
 		System.out.println("aiutil: extendSpaceToexplore dir=" +  
				dir + " x=" + x + " y="+y + " dimMapX=" + dimMapx + " dimMapY=" + dimMapy  );
		for (int i=0; i<dimMapy; i++) { //row  
			RoomMap.getRoomMap().put(dimMapx, i,  new Box(false, true, false));  
		}
		for (int i=0; i<dimMapx; i++) {
 			RoomMap.getRoomMap().put(i, dimMapy, new Box(false, true, false));  
		}
		  dimMapx = RoomMap.getRoomMap().getDimX();
		  dimMapy = RoomMap.getRoomMap().getDimY();
		System.out.println("aiutil: extendSpaceToexplore  dimMapX=" + dimMapx + " dimMapY=" + dimMapy  );
		RoomMap.getRoomMap().put(dimMapx-1,dimMapy-1, new Box(false, true, false));
		showMap(qa);		
	   
   }
/*
 * ---------------------------------------------------------	
 */
	
	public static void setGoalInit(QActor qa ) {   
		goalTest = new Functions();
	}
	public static void setGoalSonar2(QActor qa, String sx, String sy ) {
		try {
	 		int x = Integer.parseInt(sx);
			int y = Integer.parseInt(sy);
			System.out.println( "setGoalSonar2 " + x + "," + y);
			RoomMap.getRoomMap().put(x, y, new Box(false, true, false));
			showMap(qa);
			goalTest = new GoalTest() {		
				@Override
				public boolean isGoalState(Object state) {
					RobotState robotState = (RobotState) state;
					if (robotState.getX() == x && robotState.getY() == y && 
							robotState.getDirection() == Direction.RIGHT)
						return true;
					else
						return false;
				}
			};
		} catch (Exception e) {
			e.printStackTrace();
		}

 	}
	public static void setGoal(QActor qa, int x, int y ) {
		try {
 			System.out.println( "setGoal " + x + "," + y);
			RoomMap.getRoomMap().put(x, y, new Box(false, true, false));
//			showMap(qa);
			goalTest = new GoalTest() {		
				@Override
				public boolean isGoalState(Object state) {
					RobotState robotState = (RobotState) state;
					if (robotState.getX() == x && robotState.getY() == y ) 
//						&&  robotState.getDirection() == Direction.RIGHT)
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
	
	public static void setGoalHome(QActor qa, String sx, String sy ) {
		try { 
	 		int x = Integer.parseInt(sx);
			int y = Integer.parseInt(sy);
			setGoal(qa,x,y);
//			System.out.println( "setGoalHome " + x + "," + y);
//			RoomMap.getRoomMap().put(x, y, new Box(false, true, false));
//			showMap(qa);
//			goalTest = new GoalTest() {		
//				@Override
//				public boolean isGoalState(Object state) {
//					RobotState robotState = (RobotState) state;
//					if (robotState.getX() == x && robotState.getY() == y ) 
////						&&  robotState.getDirection() == Direction.RIGHT)
//						return true;
//					else
//						return false;
//				}
//			};
//			clearCurrentPlan(qa);
		} catch (Exception e) {
			e.printStackTrace();
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
		System.out.println( "aiutil getDuration=" + duration);
		long t = duration - adjust;
		if( t > 0 ) qa.replaceRule("moveWDuration(_)", "moveWDuration("+ t + ")");
		else qa.replaceRule("moveWDuration(_)", "moveWDuration("+ duration + ")");		 
	}
	public static void getDurationBack( QActor qa ) {
		
	}
	
/*
* ------------------------------------------------
* LOG	
* ------------------------------------------------
*/
	public static void saveLogMove( QActor qa) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("logMoves.pl"));
			SolveInfo sol;
			do {
				sol = qa.solveGoal("retract(movelog(N, X,Y,D,M,T))");
				if( sol.isSuccess() ) {
					    String sn = sol.getVarValue("N").toString();
						String sx = sol.getVarValue("X").toString();
						String sy = sol.getVarValue("Y").toString();
						String sd = sol.getVarValue("D").toString();
						String sm = sol.getVarValue("M").toString();
						String st = sol.getVarValue("T").toString();
						String s = "move(" + sn + "," +  sx + "," +  sy  + "," + sd  + "," + sm  + "," + st + ").\n";
						writer.write(s);					
				} 
			}while( sol.isSuccess() );
			String map =  RoomMap.getRoomMap().toString();
			writer.write("\n%%% Final map\n");
			writer.write("/* \nmap(\n"+map+").\n */");					
			writer.close();
			System.out.println("MOVES SAVED IN logMoves.pl");
		} catch (Exception e) {
			 
			e.printStackTrace();
		}
	}
/*
 * Direction
 */
	public static void rotateDirection( QActor qa ) {
		//System.out.println("before rotateDirection: " + initialState.getDirection() );
		initialState = (RobotState) new Functions().result(initialState, new RobotAction(RobotAction.TURNLEFT));
 		initialState = (RobotState) new Functions().result(initialState, new RobotAction(RobotAction.TURNLEFT));
		//System.out.println("after  rotateDirection: " + initialState.getDirection() );
		//update the kb
		int x         = initialState.getX();
		int y         = initialState.getY();
		String newdir = initialState.getDirection().toString().toLowerCase()+"Dir"; 		
		qa.solveGoal("replaceRule( curPos(_,_,_), curPos("+ x +"," + y + ","+ newdir + "))");
 	}
}
