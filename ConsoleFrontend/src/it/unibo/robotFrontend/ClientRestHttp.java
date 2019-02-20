package it.unibo.robotFrontend;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

public class ClientRestHttp {
private CloseableHttpClient httpclient;

	public ClientRestHttp(   ) {
   		httpclient  = HttpClients.createDefault();
	}
	
	public String sendGet( String url ) {
	  try {
//		System.out.println("\t ClientRestHttp sendGet url=" + url);
		HttpGet httpGet = new HttpGet(url);	
		httpGet.addHeader("accept", "application/json");
		return sendRequest(httpGet);
  	  } catch ( Exception e) {
  		  e.printStackTrace();
  		  return "sendGet error";
	 }	
	}//sendGet
	
	public String sendPost( String data, String url ) {
		HttpPost request     = new HttpPost(url);
		if( data.length() > 0 ) {
			StringEntity params = new StringEntity(data,"UTF-8");
			params.setContentType("application/json");
			request.setEntity(params);
		}
		request.addHeader("Content-type", "application/json");
		request.addHeader("Accept", "*/*");
		request.addHeader("Accept-Encoding", "gzip,deflate,sdch");
		request.addHeader("Accept-Language", "en-US,en;q=0.8");
		return sendRequest( request );
	}//sendPost

	public String sendPut( String data, String url ) {
		HttpPut request     = new HttpPut(url);
		if( data.length() > 0 ) {
			StringEntity params = new StringEntity(data,"UTF-8");
			params.setContentType("application/json");
			request.setEntity(params);
		}
		request.addHeader("Content-type", "application/json");
		request.addHeader("Accept", "*/*");
		request.addHeader("Accept-Encoding", "gzip,deflate,sdch");
		request.addHeader("Accept-Language", "en-US,en;q=0.8");
		return sendRequest( request );
 	}//sendPut

	protected String sendRequest( HttpRequestBase request ) {
 		try {
			CloseableHttpResponse response = httpclient.execute(request);
 	        return handleResponse(response);
		} catch (ClientProtocolException e) { e.printStackTrace();
		} catch (IOException e) { e.printStackTrace(); }  
 		return "sendRequest errro";
 	}//sendRequest
	
	protected String handleResponse(CloseableHttpResponse response) {
	    int responseCode = response.getStatusLine().getStatusCode();
		String info = "handleResponse error responseCode=" + responseCode;
	 	try {
	//      System.out.println("\t ClientRestHttp responseCode " + responseCode);
	        if (responseCode == 200 || responseCode == 204) {
	            BufferedReader br = new BufferedReader(
	                    new InputStreamReader((response.getEntity().getContent())));
	            String data;
	            info = "";
	    		while ((data = br.readLine()) != null) {
	    			info = info + data;
	    			//System.out.println(data);
	    		}
	//    		System.out.println(info);
	        }
	     }catch (Exception ex) {
	 //	    	httpclient.close();
	     }
	 	 return info;
   }//handleResponse
	

	public static void main (String args[]) throws Exception{
		System.out.println("============================================================");
		System.out.println("1) Activate a MQTT server on  hostAddr:1883");
		System.out.println("2) Run node frontendServerRobot.js ");
		System.out.println("============================================================");
		String serverurl = "http://localhost:8080";
		ClientRestHttp client = new ClientRestHttp( );
		String response;
		
		System.out.println("----------- SECTION 1 ---------------");
		System.out.println("GET THE META DATA");
		response = client.sendGet(serverurl+"/root");	
		System.out.println(response);
		System.out.println("GET THE SET OF ROBOT MOVES");
		response = client.sendGet(serverurl+"/commands");	
		System.out.println(response);
		JSONObject reader = new JSONObject(response);

		System.out.println("----------- SECTION 2 ---------------");
		System.out.println("DISCOVER THE ROBOT MOVES");
		JSONObject moves  = reader.getJSONObject("moves");
		System.out.println( moves.names() );
		//Consumer<String> consumer= v -> System.out.println(" Move:"+ v);	
		moves.names().forEach( v -> System.out.println( v + ":" + moves.getJSONObject(""+v) ) );
		
		System.out.println("----------- SECTION 3 ---------------");		
		System.out.println("MOVE THE ROBOT");
		response = client.sendPost("",serverurl+"/commands/w");	
		System.out.println(response);
		Thread.sleep(1500);
		response = client.sendPost("",serverurl+"/commands/s");	
		System.out.println(response);
		Thread.sleep(1500);
		response = client.sendPost("",serverurl+"/commands/h");	
		System.out.println(response);
	}
}
