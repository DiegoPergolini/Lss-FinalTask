package it.unibo.ledmock;

import it.unibo.qactors.akka.QActor;

public class physicalLedUtil {
	private static ILed led =  new PhysicalLed();

	public static void doBlink(QActor qa ){
		led.blink();
	}
	public static void turnOffLed(QActor qa ){
		led.turnOff();
	}
}
