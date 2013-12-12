package com.yjlo.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
//import android.widget.SimpleAdapter;
import android.widget.TextView;
//import android.widget.Toast;
 
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.app.FragmentActivity;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.yjlo.test.JSONParser;
import com.yjlo.test.ExtendedSimpleAdapter;
 
public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<JSONArray>{
    ListView list;
    TextView full_name;

    List<HashMap<String, Object>> oslist = new ArrayList<HashMap<String, Object>>();
    
    //ImageLoader imageLoader=new ImageLoader(getBaseContext());
 
    //URL to get JSON Array
    private static String url = "http://www.json-generator.com/j/bXtJLtTlQi?indent=4";
 
    //JSON Node Names 
    private static final String FN = "first_name";
    private static final String LN = "last_name";
    private static final String ID = "id";
 
    JSONArray people_list = null;
    private ProgressDialog pDialog;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.activity_main);
        oslist = new ArrayList<HashMap<String, Object>>();
        
        initialize();
        getSupportLoaderManager().initLoader(1, null, this).forceLoad();
    }
    
	private void initialize(){
        full_name = (TextView)findViewById(R.id.names);

	}
    
  	@Override
	public Loader<JSONArray> onCreateLoader(int id, Bundle args) {
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Loading ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
  		Loader<JSONArray> loader = new JSONParse(this);
		return loader;
	}
  	
	public void onLoadFinished(Loader<JSONArray> loader, JSONArray json) {
        pDialog.dismiss();
        try {
       	people_list = json;
           for(int i = 0; i < people_list.length(); i++){
               JSONObject person = people_list.getJSONObject(i);

               String first_name = person.getString(FN);
               String last_name = person.getString(LN);
               String description = person.getString("description");
               String country = person.getString("country");
               String image_url = person.getString("picture");
               String id = person.getString(ID);

               int[] image = new int[] { R.drawable.avatar};

               ImageLoader imageLoader = ImageLoader.getInstance();
               imageLoader.init(ImageLoaderConfiguration.createDefault(getBaseContext()));
               //imageLoader.displayImage("http://lorempixel.com/100/100/people/", imageView);
               
               //DisplayImageOptions options = new DisplayImageOptions.Builder();
               //final Bitmap bmp = imageLoader.loadImageSync("http://lorempixel.com/100/100/people/");
               final Bitmap[] bmp = {null};
               
               DisplayImageOptions options = new DisplayImageOptions.Builder().build();
    ImageSize minImageSize = new ImageSize(120, 80);
    imageLoader.loadImage("http://lorempixel.com/100/100/people/", minImageSize, options, new SimpleImageLoadingListener() {
    	   @Override
    	    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
    	        // Do whatever you want with Bitmap
    		   bmp[0] = loadedImage;
    	    }
    });

               // store values in a HashMap
               HashMap<String, Object> hm = new HashMap<String, Object>();

               hm.put("full_name", first_name+" "+last_name);
               hm.put("first_name", first_name);
               hm.put("last_name", last_name);
               hm.put("description", description);
               hm.put("country", country);
               hm.put("id", id);
               hm.put("image", image[0]); //Integer.toString(image[0])
               hm.put("avatar", Integer.toString(image[0]));
               
               hm.put("pic_url", image_url);

               oslist.add(hm);
               list=(ListView)findViewById(R.id.list);

               ListAdapter adapter = new ExtendedSimpleAdapter(MainActivity.this, oslist,
                       R.layout.list_v,
                       new String[] { "full_name", "image" }, 
                       new int[] {R.id.names, R.id.list_avatar});

               list.setAdapter(adapter);
               list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                   @Override
                   public void onItemClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                   	
                       //Toast.makeText(MainActivity.this, "View details of id: "+oslist.get(+position).get("id"), Toast.LENGTH_SHORT).show();
						try {
							Bundle bdl = new Bundle();
							bdl.putString("id", String.valueOf(id));
							bdl.putString("first_name", (String)oslist.get(+position).get("first_name"));
							bdl.putString("last_name", (String)oslist.get(+position).get("last_name"));
							bdl.putString("description", (String)oslist.get(+position).get("description"));
							bdl.putString("country", (String)oslist.get(+position).get("country"));
							bdl.putString("pic_url", (String)oslist.get(+position).get("pic_url"));
							Class myClass = Class.forName("com.yjlo.test.DetailPersonActivity");
                       	Intent myIntent = new Intent(MainActivity.this, myClass);
                       	myIntent.putExtras(bdl);
                       	startActivity(myIntent);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                   }
               });
           }
       } catch (JSONException e) {
           e.printStackTrace();
       }
	}
	
	@Override
	public void onLoaderReset(Loader<JSONArray> loader) {
		
	}

	private static class JSONParse extends AsyncTaskLoader<JSONArray> {
		public JSONParse(Context context) {
			super(context);
		}
		
		@Override
		public JSONArray loadInBackground() {
            JSONParser jp = new JSONParser();
            JSONArray json = jp.getJSONFromUrl(url);
            return json;
		}
	} 
 
}

