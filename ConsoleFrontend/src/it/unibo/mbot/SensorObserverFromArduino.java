package it.unibo.mbot;
 

public class SensorObserverFromArduino implements ISensorObserverFromArduino{

	public SensorObserverFromArduino( ) { 
 	}
	
	@Override
	public void notify(String data) {
		System.out.println("SensorObserverFromArduino: " + data );
	}

}
