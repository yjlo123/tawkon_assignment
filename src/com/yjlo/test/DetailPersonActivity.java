package com.yjlo.test;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import android.support.v7.app.ActionBarActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailPersonActivity extends ActionBarActivity {

	private TextView firstName;
	private TextView lastName;
	private TextView description;
	private TextView country;
	private ImageView avatar;

	private String gotFirstName;
	private String gotLastName;
	private String gotDescription;
	private String gotCountry;
	private String gotPicUrl;
	
	private ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);

		initialize();
		Bundle got_bdl = getIntent().getExtras();
		gotFirstName = got_bdl.getString("first_name");
		gotLastName = got_bdl.getString("last_name");
		gotDescription = got_bdl.getString("description");
		gotCountry = got_bdl.getString("country");
		gotPicUrl = got_bdl.getString("pic_url");

		firstName.setText(gotFirstName);
		lastName.setText(gotLastName);
		description.setText(gotDescription);
		country.setText(gotCountry);

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration
				.createDefault(getBaseContext()));
		imageLoader.displayImage(gotPicUrl, avatar);

	}

	private void initialize() {
		firstName = (TextView) findViewById(R.id.tv_first_name);
		lastName = (TextView) findViewById(R.id.tv_last_name);
		description = (TextView) findViewById(R.id.tv_description);
		country = (TextView) findViewById(R.id.tv_country);
		avatar = (ImageView) findViewById(R.id.imageView1);
	}
	
	@Override
	public void onBackPressed() {
	    finish();//go back to the previous Activity
	    overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);   
	}
	
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
			sendIntent.putExtra(Intent.EXTRA_TEXT, gotFirstName + " "
					+ gotLastName + ": " + gotDescription);
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
}
