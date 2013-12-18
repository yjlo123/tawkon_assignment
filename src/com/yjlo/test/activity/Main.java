package com.yjlo.test.activity;

import com.yjlo.test.R;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;

public class Main extends ActionBarActivity implements
		MainActivity.OnPersonSelectedListener {

	private FragmentManager fragmentManager = getSupportFragmentManager();
	private FragmentTransaction fragmentTransaction = fragmentManager
			.beginTransaction();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Configuration config = getResources().getConfiguration();

		setContentView(R.layout.activity_main);

		// Check whether the activity is using the layout version with
		// the fragment_container FrameLayout. 
		// If so, we add the first fragment
		if (findViewById(R.id.fragment_container) != null) {

			// if we're being restored from a previous state,
			// then we don't need to do anything and should return or else
			// we could end up with overlapping fragments.
			if (savedInstanceState != null) {
				return;
			}

			// Create an instance of ExampleFragment
			MainActivity firstFragment = new MainActivity();

			// Add the fragment to the 'fragment_container' FrameLayout
			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, firstFragment).commit();
		}

		// ------------------------
		// check screen orientation
		// ------------------------
		/*
		 * if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) { //
		 * Landscape mode of the device setContentView(R.layout.master_view); }
		 * else{ // Portrait mode of the device
		 * setContentView(R.layout.activity_main); MainActivity pm_fragment =
		 * new MainActivity(); fragmentTransaction.add(R.id.fragment_container,
		 * pm_fragment); }
		 */
		fragmentTransaction.commit();
	}

	@Override
	public void onPersonSelected(Bundle bdl) {

		DetailPersonActivity articleFrag = (DetailPersonActivity) getSupportFragmentManager()
				.findFragmentById(R.id.de);
		if (articleFrag != null) {
			articleFrag.updatePerson(bdl);
		} else {
			// If the frag is not available, we're in the one-pane layout and
			// must swap frags...

			// Create fragment and give it an argument for the selected article
			DetailPersonActivity newFragment = new DetailPersonActivity();

			newFragment.setArguments(bdl);
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();

			// Replace the fragment_container view with this fragment,
			// add the transaction to the back stack so can navigate back
			transaction.replace(R.id.fragment_container, newFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
		}

	}

	// ==========
	// Action Bar
	// ==========
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.person_details_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_share:
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, "firstname" + " "
					+ "lastname" + ": " + "description");
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}