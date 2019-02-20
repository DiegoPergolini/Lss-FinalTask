/*
 * it.unibo.mbot2018/src/it/unibo/robotFrontend/restClientHttp.java
 */ 
package it.unibo.robotFrontend;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;

public class restClientHttp {
private static String hostAddr  = "localhost";
private static String serverurl = "http://localhost:8080";

	public static int sendPut(String data, String url) {
	    int responseCode = -1;
	    CloseableHttpClient httpclient = HttpClients.createDefault();
	    try {
	        HttpPut request = new HttpPut(url);
	        StringEntity params =new StringEntity(data,"UTF-8");
	        params.setContentType("application/json");
	        request.addHeader("content-type", "application/json");
	        request.addHeader("Accept", "*/*");
	        request.addHeader("Accept-Encoding", "gzip,deflate,sdch");
	        request.addHeader("Accept-Language", "en-US,en;q=0.8");
	        request.setEntity(params);
	        CloseableHttpResponse response = httpclient.execute(request);
	        responseCode = response.getStatusLine().getStatusCode();
	        if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 204) {
	            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
	            String output;
 	    		String info = "";
	    		while ((output = br.readLine()) != null) {
	    			info = info + output;
	    		}
	    		System.out.println(info);
	        }
	        else{  throw new RuntimeException("Failed : HTTP error code : "
	                    + response.getStatusLine().getStatusCode());
	        }

	    }catch (Exception ex) {
 	    } finally {// 	    	httpclient.close();
	    }
	    return responseCode;
	}
	
	public static void connectPost(){
 		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(serverurl);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("username", "vip"));
		nvps.add(new BasicNameValuePair("password", "secret"));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			CloseableHttpResponse response2 = httpclient.execute(httpPost);
		    HttpEntity entity2 = response2.getEntity();
		    // do something useful with the response body and ensure it is fully consumed
		    EntityUtils.consume(entity2);
	 	  } catch ( Exception e) {
	 			e.printStackTrace();	 			
	 	}
 	}
	
	//This works
	public static void connectGet(String url){
	 try {
 		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);		
		httpGet.addHeader("accept", "application/json");
		CloseableHttpResponse response = httpclient.execute(httpGet);
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
			   + response.getStatusLine().getStatusCode());
		}
		BufferedReader br = new BufferedReader(
                         new InputStreamReader((response.getEntity().getContent())));
		String output;
		String info = "";
		while ((output = br.readLine()) != null) {
			info = info + output;
		}
 		System.out.println(info);
 	  } catch ( Exception e) { e.printStackTrace(); }
	}
	
	
	public static void work() throws InterruptedException {
//		for( int i=1; i<=3; i++) {
	  		connectGet(serverurl+"/root");	
//			sendPut("{\"value\": \"true\" }", "http://"+hostAddr+":3000/pi/actuators/leds/1");
//			Thread.sleep(700);
// 			sendPut("{\"value\": \"false\" }", "http://"+hostAddr+":3000/pi/actuators/leds/1");
//			//curl -H "Content-Type: application/json" -X PUT -d "{\"value\": \"false\" }" http://localhost:3000/pi/actuators/leds/1;
//	  		connectGet("http://"+hostAddr+":3000/pi/actuators/leds/1");		
//			Thread.sleep(700);
//		}
	}
	public static void main (String args[]) throws Exception{
		System.out.println("=====================================================================================");
		System.out.println("1) Activate a MQTT server on  hostAddr:1883");
		System.out.println("2) Run node frontendServerRobot.js ");
		System.out.println("=====================================================================================");
		work();
	}
}