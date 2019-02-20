/*
 * ======================================================================================
 * The file ./hardwareConfiguration.properties  must contain the robot name:
  		configuration=mocksimple
 * The file ./configuration/mocksimple/iotRobot.properties must contain the robot configuration
* ======================================================================================
*/
package it.unibo.robotOnRaspOnly;
import java.io.IOException;

import it.unibo.qactors.akka.QActor;
import it.unibo.robotRaspOnly.BasicRobotUsageNaive;
 
public class physicalRobotExecutor  { 
 
private static SonarInCObserver sonarInCObserver;
private static Process p;
	public static void setUp(QActor qa) {
		if( sonarInCObserver == null ) {
 			sonarInCObserver = new  SonarInCObserver(qa);
		}		
	}
 	
	public static void doMove( QActor qa, String cmd ) throws IOException, InterruptedException { //Args MUST be String
		
		switch(cmd.charAt(0)){
			case 'w': 
				p = Runtime.getRuntime().exec("./up.sh");
				break;
			case 'a':
				p = Runtime.getRuntime().exec("./turnLeft.sh");
				p.waitFor();
				break;
			case 's': 
				p = Runtime.getRuntime().exec("./down.sh");
				p.waitFor();
				break;
			case 'd': 
				p = Runtime.getRuntime().exec("./turnRight.sh");
				p.waitFor();
				break;
			case 'h': 
				p = Runtime.getRuntime().exec("./halt.sh");
				p.waitFor();
				break;	
		}
	}
}
