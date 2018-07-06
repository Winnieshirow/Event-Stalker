package com.events.shirow.eventstalker;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shirow on 12/5/16.
 */
public class ticketrequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "https://eventstalker.000webhostapp.com/tickets.php";
    private Map<String, String> params;

    public ticketrequest(String title, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("title", title);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
