package com.yjlo.test;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
public class DetailPersonActivity extends Activity {
	
    private TextView firstName;
    private TextView lastName;
    private TextView description;
    private TextView country;
    private ImageView avatar;
    private Button share;
    
    private String gotFirstName;
    private String gotLastName;
    private String gotDescription;
    private String gotCountry;
    private String gotPicUrl;
	
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
		
		firstName.setText( gotFirstName);
		lastName.setText(gotLastName);
		description.setText(gotDescription);
		country.setText(gotCountry);
		
		
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getBaseContext()));
        imageLoader.displayImage(gotPicUrl, avatar);
        
        share.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, gotFirstName+" "+gotLastName+": "+gotDescription);
				sendIntent.setType("text/plain");
				startActivity(sendIntent);
			}
		});
	}
	
	private void initialize(){
		firstName = (TextView)findViewById(R.id.tv_first_name);
		lastName = (TextView)findViewById(R.id.tv_last_name);
		description = (TextView)findViewById(R.id.tv_description);
		country = (TextView)findViewById(R.id.tv_country);
		
		avatar = (ImageView)findViewById(R.id.imageView1);
        share = (Button)findViewById(R.id.share_btn);
	}
	
}
