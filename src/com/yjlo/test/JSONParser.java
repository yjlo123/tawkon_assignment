package com.yjlo.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class JSONParser extends AsyncTaskLoader<Vector<Contact>> {
	
	private String url;
	// JSON Node Names
	private static final String FIRSTNAME = "first_name";
	private static final String LASTNAME = "last_name";
	private static final String DESCRIPTION = "description";
	private static final String COUNTRY = "country";
	private static final String PICTURE = "picture";
	private static final String ID = "id";
	
	private Vector<Contact> contactList;
	
	public JSONParser(Context context, String url) {
		super(context);
		this.url = url;
		contactList =  new Vector<Contact>();
	}

	@Override
	public Vector<Contact> loadInBackground() {
		Vector<Contact> contactList = null;
		JSONArray json = getJSONFromUrl(url);
		contactList = getListFromArray(json);
		return contactList;
	}

	/*
	 * METHOD: Get JSON from URL
	 * 
	 * @param String url
	 * 
	 * @return JSONArray (the json array get from the given url)
	 */
	private JSONArray getJSONFromUrl(String url) {
		InputStream is = null;
		JSONArray jArr = null;
		String json = "";
		// HTTP request
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// parse the string to a JSON array
		try {
			jArr = new JSONArray(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		return jArr;
	}
	
	/*
	 * METHOD: Convert JSON Array into Contact list
	 * 
	 * @param JSONArray
	 * 
	 * @return Vector<Contact>
	 */
	private Vector<Contact> getListFromArray (JSONArray json){
		try {
			for (int i = 0; i < json.length(); i++) {
				JSONObject person = json.getJSONObject(i);
				String firstName = person.getString(FIRSTNAME);
				String lastName = person.getString(LASTNAME);
				String description = person.getString(DESCRIPTION);
				String country = person.getString(COUNTRY);
				String imageUrl = person.getString(PICTURE);
				int id = Integer.parseInt(person.getString(ID));
				contactList.add(new Contact(firstName, lastName, country,
						description, imageUrl, id));
			}
		} catch (JSONException e) {
		    e.printStackTrace();
		}
		return contactList;
	}
}
