package it.unibo.mbot;
import it.unibo.mbot.serial.JSSCSerialComm;
import it.unibo.mbot.serial.SerialPortConnSupport;
 
public class MbotConnArduinoObj {
private  SerialPortConnSupport conn = null;
private  JSSCSerialComm serialConn;
private  double dataSonar = 0;
private  String curDataFromArduino;
private  ISensorObserverFromArduino observer;
 
	public   void initRasp( String port )   { //"/dev/ttyUSB0"
		init( port );
	}
	public   void initPc(String port)   { //"COM6"
		init( port );
 	}
 	private   void init(String port)   {
		try {
	 		System.out.println("mbotConnArduinoObj starts");
			serialConn = new JSSCSerialComm(null);
			conn = serialConn.connect(port);	//returns a SerialPortConnSupport
			if( conn == null ) return;
 			curDataFromArduino = conn.receiveALine();
			System.out.println("mbotConnArduinoObj received:" + dataSonar);
 			getDataFromArduino();
		}catch( Exception e) {
			System.out.println("mbotConnArduinoObj ERROR" + e.getMessage());
		}
	}
	
 	public void executeTheCommand( char cmd) {
 		System.out.println("mbotConnArduinoObj executeTheCommand " + cmd + " conn=" + conn);
 		switch(cmd) {
 			case 'h' : mbotStop(); break;
 			case 'w' : mbotForward(); break;
 			case 's' : mbotBackward(); break;
 			case 'a' : mbotLeft(); break; 
 			case 'd' : mbotRight(); break;
 			case 'f' : mbotLinefollow(); break;
 		}
 	}
	public   void mbotForward() {
 		try { if( conn != null ) conn.sendCmd("w"); } catch (Exception e) {e.printStackTrace();}
	}
	public   void mbotBackward() {
		try { if( conn != null ) conn.sendCmd("s"); } catch (Exception e) {e.printStackTrace();}
	}
	public   void mbotLeft() {
		try { if( conn != null ) conn.sendCmd("a"); } catch (Exception e) {e.printStackTrace();}
	}
	public   void mbotRight(  ) {
		try { if( conn != null ) conn.sendCmd("d"); } catch (Exception e) {e.printStackTrace();}
	}
	public   void mbotStop() {
		try { if( conn != null ) conn.sendCmd("h"); } catch (Exception e) {e.printStackTrace();}
	}
	public   void mbotLinefollow(  ) {
		try { if( conn != null ) conn.sendCmd("f"); } catch (Exception e) {e.printStackTrace();}
	}

	public void addObserverToSensors( ISensorObserverFromArduino observer ){
		this.observer = observer;
	}

	private void getDataFromArduino() {
		new Thread() {
			public void run() {
				try {
					System.out.println("mbotConnArduinoObj getDataFromArduino STARTED"  );
					while(true) {
						try {
							curDataFromArduino = conn.receiveALine();
// 	 						System.out.println("mbotConnArduinoObj received:" + curDataFromArduino );
 							double v = Double.parseDouble(curDataFromArduino);
							//handle too fast change
 							double delta =  Math.abs( v - dataSonar);
 							if( delta < 7 && delta > 0.5 ) {
								dataSonar = v;
								System.out.println("mbotConnArduinoObj sonar:" + dataSonar);
								observer.notify(""+dataSonar);
//								QActorUtils.raiseEvent(curActor, curActor.getName(), "realSonar", 
//										"sonar( DISTANCE )".replace("DISTANCE", ""+dataSonar ));
 							}
						} catch (Exception e) {
 							System.out.println("mbotConnArduinoObj ERROR:" + e.getMessage());
						}
					}
				} catch (Exception e) {
  					e.printStackTrace();
				}
			}
		}.start();
	}
	
	
}
