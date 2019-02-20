package it.unibo.mbot.virtual;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.JSONObject;
 

public class clientTcp   {
private static String hostName = "localhost";
private static int port        = 8999;
private static String sep      = ";";
protected static Socket clientSocket ;
protected static PrintWriter outToServer;
protected static BufferedReader inFromServer;

public static void initClientConn( ) throws Exception {
		initClientConn(  hostName, ""+port);
}
	public static void initClientConn( String hostNameStr, String portStr ) throws Exception {
		 hostName = hostNameStr;
		 port     = Integer.parseInt(portStr);
		 clientSocket = new Socket(hostName, port);
		 //outToServer  = new DataOutputStream(clientSocket.getOutputStream()); //DOES NOT WORK!!!!;
		 inFromServer = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()) );  
		 outToServer  = new PrintWriter(clientSocket.getOutputStream());
		 startTheReader(    );
	}
	public static void sendMsg( String jsonString) throws Exception {
		JSONObject jsonObject = new JSONObject(jsonString);
		String msg = sep+jsonObject.toString()+sep;
		outToServer.println(msg);
		outToServer.flush();
	}
 	protected static void startTheReader(  ) {		
		new Thread() {
			public void run() {
				while( true ) {				 
					try {
						String inpuStr = inFromServer.readLine();
						//System.out.println( "reads: " + inpuStr);
						String jsonMsgStr = inpuStr.split(";")[1];
						System.out.println( "reads: " + jsonMsgStr   );
						JSONObject jsonObject = new JSONObject(jsonMsgStr);
						//System.out.println( "type: " + jsonObject.getString("type"));
						switch (jsonObject.getString("type") ) {
						case "webpage-ready" : System.out.println( "webpage-ready "   );break;
						case "sonar-activated" : {
							System.out.println( "sonar-activated "   );
							JSONObject jsonArg = jsonObject.getJSONObject("arg");
							String sonarName   = jsonArg.getString("sonarName");							
							int distance       = jsonArg.getInt( "distance" );
							System.out.println( "sonarName=" +  sonarName + " distance=" + distance);
 							break;
						}
						case "collision" : {
							//System.out.println( "collision"   );
							JSONObject jsonArg  = jsonObject.getJSONObject("arg");
							String objectName   = jsonArg.getString("objectName");
							System.out.println( "collision objectName=" +  objectName  );
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
	
	public void doJob() throws Exception {
		initClientConn();
		String jsonString ="";
//		JSONObject jsonObject;
//		String msg="";
for( int i=1; i<=3; i++ ) {		
		jsonString = "{ 'type': 'moveForward', 'arg': 800 }";
//		jsonObject = new JSONObject(jsonString);
//		msg = sep+jsonObject.toString()+sep;
//		System.out.println("sending msg=" + msg);
		sendMsg( jsonString  );
		Thread.sleep(1000);
		
			jsonString = "{ 'type': 'moveBackward', 'arg': 800 }";
// 			jsonObject = new JSONObject(jsonString);
// 			msg = sep+jsonObject.toString()+sep;
// 			System.out.println("sending msg=" + msg);
			sendMsg( jsonString  );			 
			Thread.sleep(1000); 
			
}			 
//			clientSocket.close();
	}

	
	public static void main(String[] args) throws Exception {
		System.out.println("=============================================="  );
		System.out.println("PLEASE, run node main " + port);
		System.out.println("=============================================="  );
  		new clientTcp().doJob();
	}

}
