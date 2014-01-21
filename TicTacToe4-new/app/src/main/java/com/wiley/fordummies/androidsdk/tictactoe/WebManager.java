package com.wiley.fordummies.androidsdk.tictactoe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class WebManager {
	public void ReadWebPage (String URL){
		BufferedReader in = null;
		HttpClient client = new DefaultHttpClient();
	    HttpGet request = new HttpGet();
	    try{
	    	request.setURI(new URI("http://code.google.com/android/"));

	    	HttpResponse response = null;
	    	response = client.execute(request);
	    	in = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));

	    	StringBuffer sb = new StringBuffer("");
	    	String line = "";
	    	String NL = System.getProperty("line.separator");
	    	while ((line = in.readLine()) != null) {
				sb.append(line + NL);
	    	}

	    	String page = sb.toString();
	    	System.out.println(page);
	    	if (in != null) in.close();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
