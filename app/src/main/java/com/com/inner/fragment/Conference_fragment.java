package com.com.inner.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.events.shirow.eventstalker.AppController;
import com.events.shirow.eventstalker.R;
import com.events.shirow.eventstalker.ScrollingActivity;
import com.eventstalker.handler.ImageRecord;
import com.navigation.adapter.ImageRecordsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Conference_fragment extends Fragment {


    private ImageRecordsAdapter mAdapter;
    ListView listclick;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private ProgressDialog pDialog;

    SharedPreferences sharedpref;

    public Conference_fragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewmain = inflater.inflate(R.layout.movies_layout, container, false);

        listclick = (ListView) viewmain.findViewById(R.id.list1);

        listclick.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView test = (TextView) view.findViewById(R.id.text1);
                NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.image1);
                TextView event_price = (TextView) view.findViewById(R.id.event_price);
                TextView ddt = (TextView) view.findViewById(R.id.day_date_time);
                TextView num_dates = (TextView) view.findViewById(R.id.num_dates);
                TextView location = (TextView) view.findViewById(R.id.location);

                //code image to string
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                byte[] image=stream.toByteArray();
                //System.out.println("byte array:"+image);

                String img_str = Base64.encodeToString(image, 0);

                sharedpref = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpref.edit();
                editor.putString("Title",test.getText().toString());
                editor.putString("event_price",event_price.getText().toString());
                editor.putString("ddt",ddt.getText().toString());
                editor.putString("num_dates",num_dates.getText().toString());
                editor.putString("location",location.getText().toString());
                editor.putString("Image", img_str);
                editor.commit();

                //System.out.println("image array:"+img_str);

                Toast.makeText(getActivity(), "Book A ticket!: " +test.getText().toString(), Toast.LENGTH_LONG).show();

                // Launch scoll  activity
                Intent i = new Intent(getActivity(), ScrollingActivity.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0,0);
            }
        });


        return viewmain;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new ImageRecordsAdapter(getActivity());

        ListView listView = (ListView) getView().findViewById(R.id.list1);

        listView.setAdapter(mAdapter);

        checkConnection();
    }

    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void checkConnection() {
        if (isOnline()) {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Loading ...");
            showDialog();

            JsonObjectRequest request = new JsonObjectRequest(
                    "https://eventstalker.000webhostapp.com/conference.php",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                List<ImageRecord> imageRecords = parse(jsonObject);

                                mAdapter.swapImageRecords(imageRecords);
                            }
                            catch(JSONException e) {
                                Toast.makeText(getActivity(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getActivity(), "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            AppController.getInstance().getRequestQueue().add(request);
            hideDialog();

        }
        else {
            new AlertDialog.Builder(getActivity())
                    .setTitle("No Internet Connection")
                    .setMessage("It looks like your internet connection is off.Please turn it" +
                            "on and try again")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
        }
    }


    private List<ImageRecord> parse(JSONObject json) throws JSONException {
        ArrayList<ImageRecord> records = new ArrayList<ImageRecord>();

        JSONArray jsonImages = json.getJSONArray("images");

        for(int i =0; i < jsonImages.length(); i++) {
            JSONObject jsonImage = jsonImages.getJSONObject(i);
            String title = jsonImage.getString("title");
            String url = jsonImage.getString("url");
            String event_price = jsonImage.getString("event_price");
            String day_date_time = jsonImage.getString("day_date_time");
            String num_dates = jsonImage.getString("num_dates");
            String location = jsonImage.getString("location");

            ImageRecord record = new ImageRecord(url, title, event_price, day_date_time, num_dates, location);
            records.add(record);
        }

        return records;
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
