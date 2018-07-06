package com.events.shirow.eventstalker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BookTicketActivity extends AppCompatActivity {

    private static final String TAG = ScrollingActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private  Context context;
    private TextView eprice;
    private TextView aprice;
    private TextView total;
    private TextView titlemain;
    private TextView gprice;
    Button payment;
    private TextView earlybirdtext;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpref;

    public static final String MyPREFERENCES2 = "MyTickets" ;
    SharedPreferences sharedpref2;
    public BookTicketActivity() {
    }

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ticket);

        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .9));


        final TextView earlybird = (TextView) findViewById(R.id.earlybird);
        final TextView advance = (TextView) findViewById(R.id.advance);
        final TextView gate = (TextView) findViewById(R.id.gate);
        titlemain = (TextView) findViewById(R.id.Title);
        eprice = (TextView) findViewById(R.id.ebprice);
        aprice = (TextView) findViewById(R.id.aprice);
        gprice = (TextView) findViewById(R.id.gprice);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        total = (TextView) findViewById(R.id.total);
        payment = (Button) findViewById(R.id.payment);
        earlybirdtext = (TextView) findViewById(R.id.textVieweb);
        TextView advancetext = (TextView) findViewById(R.id.textViewa);
        TextView gatetext = (TextView) findViewById(R.id.textViewg);

        final Spinner spinnerearly = (Spinner) findViewById(R.id.spinner);
        final Spinner spinneradvance = (Spinner) findViewById(R.id.spinner2);
        final Spinner spinnergate = (Spinner) findViewById(R.id.spinner3);

        sharedpref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String title = sharedpref.getString("Title", "");
        String image = sharedpref.getString("Image", "");
        titlemain.setText(title);

        if (!image.equals("")) {
            //decode string to image
            String base = image;
            byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);
            System.out.println("success");
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        } else {
            System.out.println("image scr is null");
        }


        Intent intent = getIntent();
        String eb = intent.getStringExtra("earlybird_price");
        String a = intent.getStringExtra("advance_price");
        String g = intent.getStringExtra("gate_price");

        earlybird.setText(eb);
        advance.setText(a);
        gate.setText(g);

        String e = (String) earlybird.getText();
        String ad = (String) advance.getText();
        String ga = (String) gate.getText();

        if (!e.equals("") && (!ad.equals("")) && (!ga.equals(""))) {
            total.setText("");
            eprice.setText("");
            aprice.setText("");
            gprice.setText("");
            if (e.equals("Not Available")) {
                spinnerearly.setEnabled(false);
                Toast.makeText(BookTicketActivity.this, "Early Bird tickets Not available", Toast.LENGTH_SHORT).show();
            } else {
                spinnerearly.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String atext = (String) earlybird.getText();
                        String ticket1 = spinnerearly.getSelectedItem().toString();

                        String result = "";
                        try {
                            int value = Integer.parseInt(atext) * Integer.parseInt(ticket1);
                            result = "" + value;

                            eprice.setText(result);

                            eprice.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void afterTextChanged(Editable editable) {
                                    totalcalculation();
                                }
                            });
                        } catch (NumberFormatException ex) {
                            //either a or b is not a number
                            result = "Invalid input";
                            eprice.setText(result);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        total.setText("");
                    }
                });

                spinneradvance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String btext = (String) advance.getText();
                        String ticket2 = spinneradvance.getSelectedItem().toString();

                        String result = "";
                        try {
                            int value = Integer.parseInt(btext) * Integer.parseInt(ticket2);
                            result = "" + value;

                            aprice.setText(result);

                            aprice.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void afterTextChanged(Editable editable) {
                                    totalcalculation();
                                }
                            });
                        } catch (NumberFormatException ex) {
                            //either a or b is not a number
                            result = "Invalid input";
                            aprice.setText(result);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        total.setText("");
                    }
                });

                spinnergate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String ctext = (String) gate.getText();
                        String ticket3 = spinnergate.getSelectedItem().toString();

                        String result = "";
                        try {
                            int value = Integer.parseInt(ctext) * Integer.parseInt(ticket3);
                            result = "" + value;

                            gprice.setText(result);

                            gprice.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void afterTextChanged(Editable editable) {
                                    totalcalculation();
                                }
                            });
                        } catch (NumberFormatException ex) {
                            //either a or b is not a number
                            result = "Invalid input";
                            gprice.setText(result);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        total.setText("");
                    }
                });

            }

        }
        payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(BookTicketActivity.this, "Ticket confirmation", Toast.LENGTH_SHORT).show();
                    sharedpref2 = getSharedPreferences(MyPREFERENCES2, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpref2.edit();
                    editor.putString("cTitle", titlemain.getText().toString());
                    editor.putString("Ticket_name", earlybirdtext.getText().toString());
                    editor.putString("Ticket_amount", earlybird.getText().toString());
                    editor.putString("Number_of_tickets", spinnerearly.getSelectedItem().toString());
                    editor.putString("Total_price", total.getText().toString());
                    editor.commit();


                    Intent intent = new Intent(BookTicketActivity.this, ConfirmationActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }





    public void totalcalculation(){
        String ebfinal = "" + eprice.getText();
        String afinal = "" + aprice.getText();
        String gfinal = "" + gprice.getText();
        String  totalprice = "";
        try{
            int value = Integer.parseInt(ebfinal)+Integer.parseInt(afinal)+Integer.parseInt(gfinal);
            totalprice = ""+value;

            total.setText(totalprice);
        }catch(NumberFormatException ex){

            totalprice = "invalid";
            total.setText(totalprice);
        }
    }
}
