/*
 * ======================================================================================
 * The file ./hardwareConfiguration.properties  must contain the robot name:
  		configuration=mocksimple
 * The file ./configuration/mocksimple/iotRobot.properties must contain the robot configuration
* ======================================================================================
*/
package it.unibo.robotOnRaspOnly;
import it.unibo.qactors.akka.QActor;
import it.unibo.robotRaspOnly.BasicRobotUsageNaive;
 
public class basicRobotExecutor  { 
 
private static BasicRobotUsageNaive robotSupport ;
private static SonarInCObserver sonarInCObserver;

	public static void setUp(QActor qa) {
		if( robotSupport == null ) {
			robotSupport = new BasicRobotUsageNaive();
// 			robotSupport.addObserverToSensors( new SensorObserver(qa) );
 			sonarInCObserver = new  SonarInCObserver(qa);
		}		
	}
 	
	public static void doMove( QActor qa, String cmd ) { //Args MUST be String
		robotSupport.executeTheCommand(cmd.charAt(0));
	}
}
