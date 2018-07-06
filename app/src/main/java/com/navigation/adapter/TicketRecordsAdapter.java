package com.navigation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.events.shirow.eventstalker.R;
import com.eventstalker.handler.TicketRecord;

import java.util.List;

/**
 * Created by shirow on 12/8/16.
 */
public class TicketRecordsAdapter extends ArrayAdapter<TicketRecord> implements Filterable {


    public TicketRecordsAdapter(Context context) {
        super(context, R.layout.fragment_mytickets);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_mytickets, parent, false);
        }

        // NOTE: You would normally use the ViewHolder pattern here

        TextView ticket_type = (TextView) convertView.findViewById(R.id.ticket_type);
        TextView apt = (TextView) convertView.findViewById(R.id.apt);
        TextView not = (TextView) convertView.findViewById(R.id.not);
        TextView totalprice = (TextView) convertView.findViewById(R.id.totalprice);
        TextView eventtitle = (TextView) convertView.findViewById(R.id.EventTitle);

        TicketRecord ticketRecord = getItem(position);


        eventtitle.setText(ticketRecord.getEvent_title());
        ticket_type.setText(ticketRecord.getTicket_type());
        apt.setText(ticketRecord.getAmount_per_tickets());
        not.setText(ticketRecord.getNumber_of_tickets());
        totalprice.setText(ticketRecord.getTotal_prices());

        return convertView;
    }

    public void swapTicketRecords(List<TicketRecord> objects) {
        clear();

        for(TicketRecord object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }
}
