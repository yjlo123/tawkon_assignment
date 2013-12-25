package com.yjlo.test.activity;

import java.util.Vector;

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
		LoaderManager.LoaderCallbacks<Vector<Contact>> {
	
	private ListView list;

	// URL to get JSON Array
	public final static String URL = "http://siwei.herobo.com/t2.json";

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
	public Loader<Vector<Contact>> onCreateLoader(int id, Bundle args) {
		// display loading dialog
		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Loading ...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
		Loader<Vector<Contact>> loader = new JSONParser(getActivity(), URL);
		return loader;
	}

	public void onLoadFinished(Loader<Vector<Contact>> loader, Vector<Contact> cl) {
		// load finished, dismiss loading dialog
		pDialog.dismiss();
		final Vector<Contact> contactList = cl;
		int size = contactList.size();

		String[] nameList = new String[size];
		String[] urlList = new String[size];
		
		for (int i = 0; i < size; i++){
			nameList[i] = contactList.get(i).getFirstName() + " " + contactList.get(i).getLastName();
			urlList[i] = contactList.get(i).getPicture();
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
	public void onLoaderReset(Loader<Vector<Contact>> arg0) {
		// TODO Auto-generated method stub
		
	}
}
