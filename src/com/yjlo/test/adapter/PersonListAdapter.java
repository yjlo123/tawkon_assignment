package com.yjlo.test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yjlo.test.R;

public class PersonListAdapter extends BaseAdapter {
	private String[] nameList;
	private String[] imgeList;
	private Context context;
	private ImageLoader imageLoader;

	public PersonListAdapter() {
		nameList = null;
		imgeList = null;
	}

	public PersonListAdapter(Context context, String[] nl, String[] il) {
		nameList = nl;
		imgeList = il;
		this.context = context;
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this.context));
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
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_v, parent, false);

			// set up the ViewHolder
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
		imageLoader.displayImage(imgeList[position], viewHolder.personAvatar);

		return convertView;
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
}
