package com.yjlo.test.activity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yjlo.test.R;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailPersonActivity extends Fragment {

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
	private LinearLayout ll;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ll = (LinearLayout) inflater.inflate(R.layout.details, container, false);
		firstName = (TextView)ll.findViewById(R.id.tv_first_name);
		lastName = (TextView)ll.findViewById(R.id.tv_last_name);
		description = (TextView)ll.findViewById(R.id.tv_description);
		country = (TextView)ll.findViewById(R.id.tv_country);
		avatar = (ImageView)ll.findViewById(R.id.imageView1);
		return ll;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updatePerson(args);
        }
    }
    
    public void updatePerson(Bundle got_bdl) {
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
				.createDefault(getActivity()));
		imageLoader.displayImage(gotPicUrl, avatar);
    }
	/*
	@Override
	public void onBackPressed() {
	    finish();//go back to the previous Activity
	    overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);   
	}
	*/
}
