package it.unibo.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.JSONObject;
import it.unibo.qactors.akka.QActor;

public class clientTcpForVirtualRobot   {
private static String hostName = "192.168.1.8";
private static int port        = 8999;
private static String sep      = ";";
protected static Socket clientSocket ;
protected static PrintWriter outToServer;
protected static BufferedReader inFromServer;

public static void initClientConn(QActor qa ) throws Exception {
		initClientConn(qa, hostName, ""+port);
}
	public static void initClientConn(QActor qa, String hostNameStr, String portStr)   {
		try {
		 hostName = hostNameStr;
		 port     = Integer.parseInt(portStr);
		 clientSocket = new Socket(hostName, port);
		 //outToServer  = new DataOutputStream(clientSocket.getOutputStream()); //DOES NOT WORK!!!!;
		 inFromServer = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()) );  
		 outToServer  = new PrintWriter(clientSocket.getOutputStream());
		 startTheReader(  qa );
		}catch( Exception e) {
			qa.println("	$$$clientTcpForVirtualRobot ERROR " + e.getMessage());
			e.printStackTrace();
		}
	}
	public static void sendMsg(QActor qa, String jsonString)  {
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			String msg = sep+jsonObject.toString()+sep;
			outToServer.println(msg);
			outToServer.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
 	protected static void startTheReader(final QActor qa) {		
		new Thread() {
			public void run() {
				while( true ) {				 
					try {
						String inpuStr = inFromServer.readLine();
 						//System.out.println( "reads: " + inpuStr);
						String jsonMsgStr = inpuStr.split(";")[1];
						//System.out.println( "reads: " + jsonMsgStr + " qa=" + qa.getName() );
						JSONObject jsonObject = new JSONObject(jsonMsgStr);
						//System.out.println( "type: " + jsonObject.getString("type"));
						switch (jsonObject.getString("type") ) {
						case "webpage-ready" : System.out.println( "wenv ready "   );break;
						case "sonar-activated" : {
							//wSystem.out.println( "sonar-activated "   );
							JSONObject jsonArg = jsonObject.getJSONObject("arg");
							String sonarName   = jsonArg.getString("sonarName");							
							int distance       = jsonArg.getInt( "distance" );
//							System.out.println( "sonarName=" +  sonarName + " distance=" + distance);
							qa.emit("sonar", 
								"sonar(NAME, player, DISTANCE)".replace("NAME", sonarName.replace("-", ""))
																			.replace("DISTANCE", (""+distance) ));
							break;
						}
						case "collision" : {
							//System.out.println( "collision"   );
							JSONObject jsonArg  = jsonObject.getJSONObject("arg");
							String objectName   = jsonArg.getString("objectName");
							System.out.println( "collision objectName=" +  objectName  );
							qa.emit("sonarDetect",
									"obstacle(TARGET)".replace("TARGET", objectName ));//objectName.replace("-", "")
							break;
						}
						};
 					} catch (IOException e) {
 						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
}
