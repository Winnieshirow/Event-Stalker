package com.events.shirow.eventstalker;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shirow on 12/7/16.
 */
public class paymentrequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://eventstalker.000webhostapp.com/ticketpayment.php?";
    private Map<String, String> params;

    public paymentrequest(String ticket_type, int amount_per_tickets, int number_of_tickets, int total_prices, String event_title, String confirmation, Response.Listener<String> listener) {
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("ticket_type", ticket_type);
        params.put("amount_per_tickets", amount_per_tickets + "");
        params.put("number_of_tickets", number_of_tickets + "");
        params.put("total_prices", total_prices +"");
        params.put("event_title", event_title);
        params.put("confirmation", confirmation);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
