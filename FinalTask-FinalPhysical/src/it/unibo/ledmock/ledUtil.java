package it.unibo.ledmock;

import it.unibo.qactors.akka.QActor;

public class ledUtil {
	private static ILed led =  new LedMock();;

	public static void switchLed(QActor qa ){
		led.switchState();
	}
	public static void turnOffLed(QActor qa ){
		led.turnOff();
	}
}
