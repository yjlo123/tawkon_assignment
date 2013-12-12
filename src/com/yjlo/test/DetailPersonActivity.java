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
	
    static TextView first_name;
    static TextView last_name;
    static TextView description;
    static TextView country;
    static String person_name;
    static ImageView avatar;
    Button share;
    
    static String got_id;
    static String got_first_name;
    static String got_last_name;
    static String got_description;
    static String got_country;
    static String got_pic_url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		
		initialize();
		Bundle got_bdl = getIntent().getExtras();
		got_id = got_bdl.getString("id");
		got_first_name = got_bdl.getString("first_name");
		got_last_name = got_bdl.getString("last_name");
		got_description = got_bdl.getString("description");
		got_country = got_bdl.getString("country");
		got_pic_url = got_bdl.getString("pic_url");
		
		first_name.setText( got_first_name);
		last_name.setText(got_last_name);
		description.setText(got_description);
		country.setText(got_country);
		
		
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getBaseContext()));
        imageLoader.displayImage(got_pic_url, avatar);
        
        share.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, got_first_name+" "+got_last_name+": "+got_description);
				sendIntent.setType("text/plain");
				startActivity(sendIntent);
			}
		});
	}
	
	private void initialize(){
		first_name = (TextView)findViewById(R.id.tv_first_name);
		last_name = (TextView)findViewById(R.id.tv_last_name);
		description = (TextView)findViewById(R.id.tv_description);
		country = (TextView)findViewById(R.id.tv_country);
		
		avatar = (ImageView)findViewById(R.id.imageView1);
        share = (Button)findViewById(R.id.share_btn);
	}
	
}
