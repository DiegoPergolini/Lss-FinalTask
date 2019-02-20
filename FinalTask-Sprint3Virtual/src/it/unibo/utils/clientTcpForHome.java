package it.unibo.utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
 
public class clientTcpForHome   {
protected static Socket clientSocket ;
protected static PrintWriter outToServer;
protected static BufferedReader inFromServer;

/*
 * My public address is 82.49.115.40
 * My local PC is 192.168.1.9
 * MAP Personzalizza TCP 8032 ... to 192.168.1.9
 * 
 * Launch ctxWebGuiExecutor on the local host  
 * Launch this main
 */


 	public static void initClientConn( String hostName, int port) throws Exception {
 		 clientSocket = new Socket(hostName, port);
		 inFromServer = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()) );  
		 outToServer  = new PrintWriter(clientSocket.getOutputStream());
	}
	public static void sendMsg( String msg) throws Exception {
 		outToServer.println(msg);
		outToServer.flush();
	}
	
public static void test() throws Exception {
	String msgForward  = "msg(  moveRobot , dispatch, remoteclient, player, usercmd( robotgui(w(low)) ), 1 )";
	String msgBackward = "msg(  moveRobot , dispatch, remoteclient, player, usercmd( robotgui(s(low)) ), 2 )";
	String msgStop     = "msg(  moveRobot , dispatch, remoteclient, player, usercmd( robotgui(h(low)) ), 2 )";
	System.out.println( "clientTcpForHome test START"  );	
	initClientConn(  "82.49.115.40", 8032 );
	System.out.println( "clientTcpForHome test moveForward"  );	
	sendMsg( msgForward );
	Thread.sleep(800);
	System.out.println( "clientTcpForHome test moveBackward"  );	
	sendMsg(  msgBackward );
	Thread.sleep(800);
	sendMsg(  msgStop );
	System.out.println( "clientTcpForHome test END"  );	
}

	public static void main(String[] args) throws Exception {
		clientTcpForHome.test();
	}

}
