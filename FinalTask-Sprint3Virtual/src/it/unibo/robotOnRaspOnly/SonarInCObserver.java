package it.unibo.robotOnRaspOnly;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.iot.models.sensorData.Direction;
import it.unibo.iot.models.sensorData.DirectionRelativeValue;
import it.unibo.iot.models.sensorData.Position;
import it.unibo.iot.models.sensorData.PositionValue;
import it.unibo.iot.models.sensorData.distance.Distance;
import it.unibo.iot.models.sensorData.distance.DistanceSensorData;
import it.unibo.qactors.akka.QActor;


public class SonarInCObserver    {
	private QActor qa;
	protected PositionValue pos;
	protected DirectionRelativeValue dir;
	protected DistanceSensorData dsd;     
	protected int distance;
	public SonarInCObserver(QActor qa ) { 
		System.out.println("SonarInCObserver started");
		this.qa = qa;
		readFromSonarInC(qa);
 	}
	
//	@Override
//	public void notify(String data) {
//		System.out.println("SonarInCObserver: " + data );
//		//qa.emit("sonarEvent",  data.getDefStringRep());
//	}
 	
	protected void readFromSonarInC(QActor qa)   {
		new Thread() {
			public void run() {
				try{
					qa.println("SonarInCObserver readFromSonarInC STARTING ... ");
					Process p = Runtime.getRuntime().exec("./SonarAlone");
					BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			 		while(true){
			 			String data = reader.readLine();
			 			if( data != null ) qa.println("SonarInCObserver data= " + data );
			 			//NOTIFY 
			 			if( data != null && data.trim().startsWith("distance") ) {
			 				setDistanceSensorDataArgs(  data ) ;
			 				DistanceSensorData dsd = new DistanceSensorData(
			 						new Distance(distance), new Position(pos), new Direction(dir));
//			 				qa.println("data = " + dsd.getDefStringRep() + " " + dsd.getJsonStringRep() );
			 				qa.emit("sonarEvent",  data );
 			 				if(distance <10) {
 			 					qa.emit("sonarDetect",  "obstacle("+distance+")" );
 			 				}
 			 			}
			 		}
		 		}catch(Exception e){
					qa.println(" ERROR " + e.getMessage() );
				}				
			}
		}.start();
 	}

	protected void setDistanceSensorDataArgs(String data){
 		//data = distance( 24,forward, front)
 		Struct dt = (Struct) Term.createTerm(data);
 		//println("setDistanceSensorDataArgs dt = " + dt);
 		distance = Integer.parseInt(""+dt.getArg(0));
 		String position = ""+dt.getArg(2);
 		//println("setDistanceSensorDataArgs position = " + position);
 		pos =  position.equals("front") ? PositionValue.FRONT : PositionValue.TOP;
 		String direction = ""+dt.getArg(1);
 		//println("setDistanceSensorDataArgs direction = " + direction);
 		dir = direction.equals("forward") ? DirectionRelativeValue.FORWARD  : DirectionRelativeValue.UP ;			
	}

}
