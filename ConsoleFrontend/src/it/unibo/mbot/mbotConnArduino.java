package it.unibo.mbot;
import it.unibo.iot.sensors.ISensorObserver;
import it.unibo.mbot.serial.JSSCSerialComm;
import it.unibo.mbot.serial.SerialPortConnSupport;
 
public class mbotConnArduino {
private static SerialPortConnSupport conn = null;
private static JSSCSerialComm serialConn;
private static double dataSonar = 0;
private static String curDataFromArduino;
 
	public static void initRasp()   {
		init( "/dev/ttyUSB0" );
	}
	public static void initPc(String port)   {
		init( port );
 	}
 	private static void init(String port)   {
		try {
	 		System.out.println("MbotConnArduino starts");
			serialConn = new JSSCSerialComm(null);
			conn = serialConn.connect(port);	//returns a SerialPortConnSupport
			if( conn == null ) return;
 			curDataFromArduino = conn.receiveALine();
			System.out.println("MbotConnArduino received:" + dataSonar);
 			getDataFromArduino();
		}catch( Exception e) {
			System.out.println("MbotConnArduino ERROR" + e.getMessage());
		}
	}
	
	public static void mbotForward() {
 		try { if( conn != null ) conn.sendCmd("w"); } catch (Exception e) {e.printStackTrace();}
	}
	public static void mbotBackward() {
		try { if( conn != null ) conn.sendCmd("s"); } catch (Exception e) {e.printStackTrace();}
	}
	public static void mbotLeft() {
		try { if( conn != null ) conn.sendCmd("a"); } catch (Exception e) {e.printStackTrace();}
	}
	public static void mbotRight(  ) {
		try { if( conn != null ) conn.sendCmd("d"); } catch (Exception e) {e.printStackTrace();}
	}
	public static void mbotStop() {
		try { if( conn != null ) conn.sendCmd("h"); } catch (Exception e) {e.printStackTrace();}
	}
	public static void mbotLinefollow(  ) {
		try { if( conn != null ) conn.sendCmd("f"); } catch (Exception e) {e.printStackTrace();}
	}
	
  	
	private static  void getDataFromArduino() {
		new Thread() {
			public void run() {
				try {
					System.out.println("MbotConnArduino getDataFromArduino STARTED"  );
					while(true) {
						try {
							curDataFromArduino = conn.receiveALine();
// 	 						System.out.println("MbotConnArduino received:" + curDataFromArduino );
 							double v = Double.parseDouble(curDataFromArduino);
							//handle too fast change
 							double delta =  Math.abs( v - dataSonar);
 							if( delta < 7 && delta > 0.5 ) {
								dataSonar = v;
								System.out.println("MbotConnArduino sonar:" + dataSonar);
 //								QActorUtils.raiseEvent(curActor, curActor.getName(), "realSonar", 
//										"sonar( DISTANCE )".replace("DISTANCE", ""+dataSonar ));
 							}
						} catch (Exception e) {
 							System.out.println("MbotConnArduino ERROR:" + e.getMessage());
						}
					}
				} catch (Exception e) {
  					e.printStackTrace();
				}
			}
		}.start();
	}
}
