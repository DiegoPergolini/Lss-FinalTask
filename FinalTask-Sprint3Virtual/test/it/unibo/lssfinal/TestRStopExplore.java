package it.unibo.lssfinal;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import alice.tuprolog.NoSolutionException;
import alice.tuprolog.SolveInfo;
import it.unibo.qactors.QActorUtils;
import it.unibo.qactors.akka.QActor;

public class TestRStopExplore{
	private QActor mind;
	private QActor worldobserver;
	@Before
	public void setUp() {
		try {
			it.unibo.ctxRobotExplore.MainCtxRobotExplore.initTheContext();
			System.out.println(" ***TEST:*** TestPrototype waits for a while ........ " );
			Thread.sleep( 3000 );
			mind = QActorUtils.getQActor("mind_ctrl");
			worldobserver = QActorUtils.getQActor("worldobserver_ctrl");
			System.out.println(Collections.list(QActorUtils.qActorTable.keys()));
			System.out.println(mind);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	@Test
	public void testRStopExplore() throws InterruptedException {
		try {
			QActorUtils.sendMsg(mind, "mind", "startAppl", "startAppl(X)");
			Thread.sleep(500);
			SolveInfo sol = mind.solveGoal("robotState(X)");
			String robotState = sol.getVarValue("X").toString() ;
			assertTrue(robotState.equals("running"));
			QActorUtils.sendMsg(mind, "mind", "haltAppl", "haltAppl(X)");
			Thread.sleep(500);
			sol = mind.solveGoal("robotState(X)");
			robotState = sol.getVarValue("X").toString() ;
			assertTrue(robotState.equals("stopped"));
		} catch (NoSolutionException e) {
			e.printStackTrace();
		}	
	}
}