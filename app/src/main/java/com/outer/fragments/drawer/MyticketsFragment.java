package com.outer.fragments.drawer;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.events.shirow.eventstalker.AppController;
import com.events.shirow.eventstalker.R;
import com.eventstalker.handler.TicketRecord;
import com.navigation.adapter.TicketRecordsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyticketsFragment extends Fragment {

    private TicketRecordsAdapter tAdapter;
    private ProgressDialog pDialog;

    public MyticketsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ticket_layout, container, false);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("My Tickets");

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tAdapter = new TicketRecordsAdapter(getActivity());

        ListView listView = (ListView) getView().findViewById(R.id.list2);

        listView.setAdapter(tAdapter);

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
                    "https://eventstalker.000webhostapp.com/mytickets.php",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                List<TicketRecord> ticketRecords = parse(jsonObject);

                                tAdapter.swapTicketRecords(ticketRecords);
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


    private List<TicketRecord> parse(JSONObject json) throws JSONException {
        ArrayList<TicketRecord> records = new ArrayList<TicketRecord>();

        JSONArray jsonImages = json.getJSONArray("payment");

        for(int i =0; i < jsonImages.length(); i++) {
            JSONObject jsonImage = jsonImages.getJSONObject(i);
            String ticket_type = jsonImage.getString("ticket_type");
            String amount_per_tickets = jsonImage.getString("amount_per_tickets");
            String number_of_tickets = jsonImage.getString("number_of_tickets");
            String total_prices = jsonImage.getString("total_prices");
            String event_title = jsonImage.getString("event_title");

            TicketRecord record = new TicketRecord(ticket_type, amount_per_tickets, number_of_tickets, total_prices, event_title);
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
