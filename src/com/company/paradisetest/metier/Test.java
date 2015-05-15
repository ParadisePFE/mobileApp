package com.company.paradisetest.metier;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HttpClient client=new DefaultHttpClient();
		StringBuilder reponseHTTP = new StringBuilder();
		HttpGet httpGet=new HttpGet("http://192.168.1.39/paradise/symfony/web/app_dev.php/api/products");
		try {
			HttpResponse response=client.execute(httpGet);
			StatusLine statusLine=response.getStatusLine();
			int statusCode=statusLine.getStatusCode();
			if(statusCode==200){
				HttpEntity entity=response.getEntity();
				InputStream content=entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
		        String line;
		        while ((line = reader.readLine()) != null) {
		          reponseHTTP.append(line);
		        }
		        JSONArray jsonArray=new JSONArray(reponseHTTP.toString());
		        System.out.println(jsonArray.length());
		       
			}
		} catch (Exception e) {
			System.out.println("Exception !!!!!!!");
			System.out.println(e.getMessage());
		}
	}

}
