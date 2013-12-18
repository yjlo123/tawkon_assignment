package com.yjlo.test.activity;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yjlo.test.Contact;
import com.yjlo.test.JSONParser;
import com.yjlo.test.R;
import com.yjlo.test.adapter.PersonListAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

public class MainActivity extends Fragment implements
		LoaderManager.LoaderCallbacks<JSONArray> {
	
	private ListView list;
	private Vector<Contact> contactList;

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
	private LinearLayout ll;

	static OnPersonSelectedListener mCallback;

	public interface OnPersonSelectedListener {
		public void onPersonSelected(Bundle bdl);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ll = (LinearLayout) inflater.inflate(R.layout.person_list, container,
				false);
		list = (ListView) ll.findViewById(R.id.list);

		getActivity().getSupportLoaderManager().initLoader(1, null, this)
				.forceLoad();
		return ll;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contactList = new Vector<Contact>();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception.
		try {
			mCallback = (OnPersonSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	// when the activity is destroyed, dismiss the dialog
	public void onDestroy() {
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.dismiss();
			pDialog = null;
		}
		super.onDestroy();
	}

	@Override
	public Loader<JSONArray> onCreateLoader(int id, Bundle args) {
		// display loading dialog
		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Loading ...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
		Loader<JSONArray> loader = new JSONParser(getActivity(), URL);
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
				int id = Integer.parseInt(person.getString(ID));

				nameList[i] = firstName + " " + lastName;
				urlList[i] = imageUrl;

				contactList.add(new Contact(firstName, lastName, country,
						description, imageUrl, id));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		ListAdapter adapter = new PersonListAdapter(getActivity(), nameList,
				urlList);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Bundle bdl = new Bundle();

				bdl.putString("first_name", contactList.get(position)
						.getFirstName());
				bdl.putString("last_name", contactList.get(position)
						.getLastName());
				bdl.putString("description", contactList.get(position)
						.getDescription());
				bdl.putString("country", contactList.get(position).getCountry());
				bdl.putString("pic_url", contactList.get(position).getPicture());

				mCallback.onPersonSelected(bdl);
			}
		});
	}

	@Override
	public void onLoaderReset(Loader<JSONArray> arg0) {
		// TODO Auto-generated method stub
	}
}
