package it.unibo.planning;

import it.unibo.qactors.akka.QActor;

public class mapUtil {
	public final static Strategy enterprising = Strategy.ENTERPRISING;
	public final static Strategy conservative = Strategy.CONSERVATIVE;
	
	public static Strategy getEnterprising() {
		return enterprising;
	}
	public static Strategy getConservative() {
		return conservative;
	}
	public static void setObstacle(QActor qa,String x,String y) {
		RoomMap.getRoomMap().put(
					Integer.parseInt(x),
					Integer.parseInt(y),
					new Box(true,false,false)
				);
	}
	public static void setFreeCell(QActor qa,String x,String y) {
		RoomMap.getRoomMap().put(
					Integer.parseInt(x),
					Integer.parseInt(y),
					new Box(false,false,false)
				);
	}
	public static void removeEmptyCells(QActor qa) {
		RoomMap.getRoomMap().removeEmptyCells();
	}
	public static void setEmptyCellsToNull(QActor qa) {
		RoomMap.getRoomMap().setEmptyCellsToNull();
	}
}
