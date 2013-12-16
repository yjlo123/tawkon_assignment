package com.yjlo.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.Toast;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.app.FragmentActivity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MainActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<JSONArray> {
	private ListView list;

	List<HashMap<String, Object>> oslist = new ArrayList<HashMap<String, Object>>();

	ImageLoader imageLoader;

	// URL to get JSON Array
	public final static String URL = "http://www.json-generator.com/j/bXtJLtTlQi?indent=4";

	// JSON Node Names
	private static final String FIRSTNAME = "first_name";
	private static final String LASTNAME = "last_name";
	private static final String DESCRIPTION = "description";
	private static final String COUNTRY = "country";
	private static final String PICTURE = "picture";
	private static final String ID = "id";

	private JSONArray peopleList = null;
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		oslist = new ArrayList<HashMap<String, Object>>();
		
		imageLoader=ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration
				.createDefault(getBaseContext()));

		list = (ListView) findViewById(R.id.list);
		getSupportLoaderManager().initLoader(1, null, this).forceLoad();
	}

	// when the activity is destroyed, dismiss the dialog
	protected void onDestroy() {
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.dismiss();
			pDialog = null;
		}
		super.onDestroy();
	}

	@Override
	public Loader<JSONArray> onCreateLoader(int id, Bundle args) {
		// display loading dialog
		pDialog = new ProgressDialog(MainActivity.this);
		pDialog.setMessage("Loading ...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
		Loader<JSONArray> loader = new JSONParse(this);
		return loader;
	}

	public void onLoadFinished(Loader<JSONArray> loader, JSONArray json) {
		// load finished, dismiss loading dialog
		pDialog.dismiss();

		peopleList = json;
		String[] nameList = new String[peopleList.length()];
		String[] urlList = new String[peopleList.length()];

		try {
			for (int i = 0; i < peopleList.length(); i++) {
				JSONObject person = peopleList.getJSONObject(i);

				String firstName = person.getString(FIRSTNAME);
				String lastName = person.getString(LASTNAME);
				String description = person.getString(DESCRIPTION);
				String country = person.getString(COUNTRY);
				String imageUrl = person.getString(PICTURE);
				String id = person.getString(ID);

				nameList[i] = firstName + " " + lastName;
				urlList[i] = imageUrl;

				// store values in a HashMap
				HashMap<String, Object> hm = new HashMap<String, Object>();

				hm.put("full_name", firstName + " " + lastName);
				hm.put("first_name", firstName);
				hm.put("last_name", lastName);
				hm.put("description", description);
				hm.put("country", country);
				hm.put("id", id);

				hm.put("pic_url", imageUrl);

				oslist.add(hm);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		ListAdapter adapter = new personListAdapter(nameList, urlList);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Bundle bdl = new Bundle();
				bdl.putString("first_name",
						(String) oslist.get(+position).get("first_name"));
				bdl.putString("last_name",
						(String) oslist.get(+position).get("last_name"));
				bdl.putString("description", (String) oslist.get(+position)
						.get("description"));
				bdl.putString("country",
						(String) oslist.get(+position).get("country"));
				bdl.putString("pic_url",
						(String) oslist.get(+position).get("pic_url"));
				Intent myIntent = new Intent(MainActivity.this,
						DetailPersonActivity.class);
				myIntent.putExtras(bdl);
				startActivity(myIntent);
				overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			}
		});
	}

	@Override
	public void onLoaderReset(Loader<JSONArray> arg0) {
		// TODO Auto-generated method stub
	}

	/*
	 * CLASS: Get JSON Array
	 * 
	 * @return JSONArray (the json array get from the given url)
	 */
	private static class JSONParse extends AsyncTaskLoader<JSONArray> {
		public JSONParse(Context context) {
			super(context);
		}

		@Override
		public JSONArray loadInBackground() {
			JSONArray json = getJSONFromUrl(MainActivity.URL);
			return json;
		}
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

	/*
	 * CLASS: View Holder
	 * 
	 * caches the TextView and ImageView
	 */
	static class ViewHolder {
		TextView personName;
		ImageView personAvatar;
	}

	/*
	 * CLASS: personListAdapter
	 * 
	 * The implementation of BaseAdapter
	 * @param String[] nl : nameList String Array
	 * @param int[] il : imageList int Array
	 */
	class personListAdapter extends BaseAdapter {
		private String[] nameList;
		private String[] imgeList;

		personListAdapter() {
			nameList = null;
			imgeList = null;
		}

		public personListAdapter(String[] nl, String[] il) {
			nameList = nl;
			imgeList = il;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return nameList.length;
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;

			if (convertView == null) {
				// inflate the layout
				LayoutInflater inflater = getLayoutInflater();
				convertView = inflater.inflate(R.layout.list_v, parent, false);

				//set up the ViewHolder
				viewHolder = new ViewHolder();
				viewHolder.personName = (TextView) convertView
						.findViewById(R.id.names);
				viewHolder.personAvatar = (ImageView) convertView
						.findViewById(R.id.list_avatar);

				// store the holder with the view.
				convertView.setTag(viewHolder);
			} else {
				// just use the viewHolder
				viewHolder = (ViewHolder) convertView.getTag();
			}
			// get the views from the ViewHolder and then set the values
			viewHolder.personName.setText(nameList[position]);
			int randomNum = (int)(Math.random()*10); 
			//*there are some problems when using the same pic url given by the json file*
			//*so I use a random number to test this, to let the pic for each person is different*
			String url = imgeList[position] + String.valueOf(randomNum);
			imageLoader.displayImage(url, viewHolder.personAvatar);

			return convertView;
		}
	}

}
