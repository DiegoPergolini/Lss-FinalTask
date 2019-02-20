package it.unibo.robotOnRaspOnly;
import it.unibo.iot.models.sensorData.ISensorData;
import it.unibo.iot.sensors.ISensorObserver;
import it.unibo.qactors.akka.QActor;


public class SensorObserver<T extends ISensorData>   implements ISensorObserver<T>{
	private QActor qa;
	public SensorObserver(QActor qa ) { 
		System.out.println("SensorObserver started");
		this.qa = qa;
 	}
	@Override
	public void notify(T data) {
		System.out.println("SensorObserver: " + data.getDefStringRep() + " | " + data.getJsonStringRep() );
		qa.emit("sonarEvent",  data.getDefStringRep());
	}

}
