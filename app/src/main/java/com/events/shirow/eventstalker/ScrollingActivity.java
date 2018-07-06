package com.events.shirow.eventstalker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ScrollingActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpref;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarmovie);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My title");

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView movieimage = (ImageView)findViewById(R.id.imagedisplay);
        TextView ddt = (TextView) findViewById(R.id.date);
        TextView location  = (TextView) findViewById(R.id.loc);
        TextView price  = (TextView) findViewById(R.id.price);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        sharedpref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String title = sharedpref.getString("Title","");
        String image = sharedpref.getString("Image","");
        String datetime = sharedpref.getString("ddt","");
        String loca = sharedpref.getString("location","");
        String pp = sharedpref.getString("event_price","");
        ddt.setText(datetime);
        price.setText(pp);
        location.setText(loca);

        toolbar.setTitle(title);

        if (!image.equals("")){
            //decode string to image
            String base=image;
            byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);
            System.out.println("success");
            movieimage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length) );
        }
        else {
            System.out.println("image scr is null");
        }

        LinearLayout tickets = (LinearLayout) findViewById(R.id.Ticketing);
        tickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String maintitle = title.toString();
                // Response received from the server

                pDialog.setMessage("Loading...");
                pDialog.show();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                String earlybird = jsonResponse.getString("earlybird_price");
                                String advance = jsonResponse.getString("advance_price");
                                String gate  = jsonResponse.getString("gate_price");

                                System.out.println("DATABASE!!!!!TITLE"+ title);
                                System.out.println("DATABASE!!!!!"+ earlybird+advance+gate);

                                Intent intent = new Intent(ScrollingActivity.this, BookTicketActivity.class);
                                intent.putExtra("earlybird_price", earlybird);
                                intent.putExtra("advance_price", advance);
                                intent.putExtra("gate_price", gate);
                                ScrollingActivity.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ScrollingActivity.this);
                                builder.setMessage("No tickets available")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                ticketrequest trequest = new ticketrequest(title, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ScrollingActivity.this);
                queue.add(trequest);

                Toast.makeText(ScrollingActivity.this, "Booking", Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
