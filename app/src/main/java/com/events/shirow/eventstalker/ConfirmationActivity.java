package com.events.shirow.eventstalker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmationActivity extends AppCompatActivity {

    public static final String MyPREFERENCES2 = "MyTickets" ;
    SharedPreferences sharedpref2;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width*.8), (int) (height*.8));

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        final TextView ctitle = (TextView) findViewById(R.id.ctitle);
        final TextView ctype = (TextView) findViewById(R.id.ctype);
        final TextView camount = (TextView) findViewById(R.id.camount);
        final TextView cnum = (TextView) findViewById(R.id.cnum);
        final TextView ctotal = (TextView) findViewById(R.id.ctotal);
        final TextView conf = (TextView) findViewById(R.id.c);
        Button cconfirm = (Button) findViewById(R.id.cconfirm);

        sharedpref2 = getSharedPreferences(MyPREFERENCES2, Context.MODE_PRIVATE);

        final String title = sharedpref2.getString("cTitle","");
        String ticketname = sharedpref2.getString("Ticket_name","");
        String ticketamount = sharedpref2.getString("Ticket_amount","");
        String numberoftickets = sharedpref2.getString("Number_of_tickets","");
        String totalprice = sharedpref2.getString("Total_price","");

        ctitle.setText(title);
        ctype.setText(ticketname);
        camount.setText(ticketamount);
        cnum.setText(numberoftickets);
        ctotal.setText(totalprice);

        cconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String ticket_type = ctype.getText().toString().trim();
                final int amount_per_tickets = Integer.parseInt(camount.getText().toString());
                final int number_of_tickets = Integer.parseInt(cnum.getText().toString());
                final int total_prices = Integer.parseInt(ctotal.getText().toString());
                final String event_title = ctitle.getText().toString().trim();
                final String confirmation = conf.getText().toString().trim();


                pDialog.setMessage("Booking ticket ...");
                showDialog();

                System.out.println("Details:" + ticket_type +amount_per_tickets+number_of_tickets+total_prices+event_title+confirmation);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Intent x = getPackageManager().getLaunchIntentForPackage("com.android.stk");
                                startActivity(x);
                                finish();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmationActivity.this);
                                builder.setMessage("Booking Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                paymentrequest prequest = new paymentrequest(ticket_type,amount_per_tickets, number_of_tickets, total_prices, event_title,confirmation, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ConfirmationActivity.this);
                queue.add(prequest);

                hideDialog();
            }
        });
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
