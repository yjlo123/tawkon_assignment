package com.yjlo.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class JSONParser extends AsyncTaskLoader<JSONArray> {
	
	private String url;
	
	public JSONParser(Context context, String url) {
		super(context);
		this.url = url;
	}

	@Override
	public JSONArray loadInBackground() {
		JSONArray json = getJSONFromUrl(url);
		return json;
	}

	/*
	 * METHOD: Get JSON from URL
	 * 
	 * @param String url
	 * 
	 * @return JSONArray (the json array get from the given url)
	 */
	private static JSONArray getJSONFromUrl(String url) {
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
}
