package com.navigation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.events.shirow.eventstalker.AppController;
import com.events.shirow.eventstalker.R;
import com.eventstalker.handler.BitmapLruCache;
import com.eventstalker.handler.ImageRecord;

import java.util.List;

public class ImageRecordsAdapter extends ArrayAdapter<ImageRecord> {
    private ImageLoader mImageLoader;

    public ImageRecordsAdapter(Context context) {
        super(context, R.layout.image_list_item);

        mImageLoader = new ImageLoader(AppController.getInstance().getRequestQueue(), new BitmapLruCache());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_list_item, parent, false);
        }

        // NOTE: You would normally use the ViewHolder pattern here
        NetworkImageView imageView = (NetworkImageView) convertView.findViewById(R.id.image1);
        TextView textView = (TextView) convertView.findViewById(R.id.text1);
        TextView event_price = (TextView) convertView.findViewById(R.id.event_price);
        TextView ddt = (TextView) convertView.findViewById(R.id.day_date_time);
        TextView num_dates = (TextView) convertView.findViewById(R.id.num_dates);
        TextView location = (TextView) convertView.findViewById(R.id.location);

        ImageRecord imageRecord = getItem(position);

        imageView.setImageUrl(imageRecord.getUrl(), mImageLoader);
        textView.setText(imageRecord.getTitle());
        event_price.setText(imageRecord.getEvent_price());
        ddt.setText(imageRecord.getDay_date_time());
        num_dates.setText(imageRecord.getNum_dates());
        location.setText(imageRecord.getLocation());

        return convertView;
    }

    public void swapImageRecords(List<ImageRecord> objects) {
        clear();

        for(ImageRecord object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }
}
