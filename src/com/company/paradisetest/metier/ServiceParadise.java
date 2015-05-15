package com.company.paradisetest.metier;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

public class ServiceParadise {
	
	public List<Product> getProducts(){
		List<Product> products = new ArrayList<Product>();
		JSONArray jsonArray = this
				.getDataArrayFromURL("http://192.168.1.14/paradise/symfony/web/api/products");
		if (jsonArray != null)
			for (int i = 0; i < 10; i++) {
				Product p = new Product();
				try {
					p.setId(jsonArray.getJSONObject(i).getLong("id"));
					p.setName(jsonArray.getJSONObject(i).getString("name"));
					p.setDescription(jsonArray.getJSONObject(i)
							.getString("description"));
					p.setPrice(jsonArray.getJSONObject(i).getDouble("price"));
					p.setImg(jsonArray.getJSONObject(i).getString("picture"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				products.add(p);
			}

		return products;
	}

	public List<Seller> getCloseSellers(double lat, double lng) {
		List<Seller> sellers = new ArrayList<Seller>();
		JSONArray jsonArray = this
				.getDataArrayFromURL("http://192.168.1.14/paradise/symfony/web/api/sellers/"
						+ lat + "/" + lng + "/nearest");
		if (jsonArray != null)
			for (int i = 0; i < jsonArray.length(); i++) {
				Seller s = new Seller();
				try {
					s.setId(jsonArray.getJSONObject(i).getLong("id"));
					s.setName(jsonArray.getJSONObject(i).getString("name"));
					s.setAddress(jsonArray.getJSONObject(i)
							.getString("address"));
					s.setLat(jsonArray.getJSONObject(i).getDouble("lat"));
					s.setLng(jsonArray.getJSONObject(i).getDouble("lng"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sellers.add(s);
			}

		return sellers;
	}

	private JSONArray getDataArrayFromURL(String url) {
		HttpClient client = new DefaultHttpClient();
		StringBuilder reponseHTTP = new StringBuilder();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					reponseHTTP.append(line);
				}
				JSONArray jsonArray = new JSONArray(reponseHTTP.toString());
				return jsonArray;
			}
		} catch (Exception e) {
			System.out.println("Exception----------------------");
			System.out.println(e.getMessage());
		}
		return null;
	}

}
