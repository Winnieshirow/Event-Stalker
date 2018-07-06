package com.eventstalker.handler;

/**
 * Created by shirow on 12/5/16.
 */
public class TicketRecord {
    private String ticket_type;
    private String amount_per_tickets;
    private String number_of_tickets;
    private String total_prices;
    private String event_title;



    public TicketRecord(String ticket_type, String amount_per_tickets, String number_of_tickets, String total_prices, String event_title ) {
        this. ticket_type =  ticket_type;
        this.amount_per_tickets= amount_per_tickets;
        this.number_of_tickets = number_of_tickets;
        this.total_prices = total_prices;
        this.event_title = event_title;
    }

    public String getTicket_type() {
        return  ticket_type;
    }
    public String getAmount_per_tickets() {
        return amount_per_tickets;
    }
    public String getNumber_of_tickets() {
        return number_of_tickets;
    }
    public String getTotal_prices() {
        return total_prices;
    }
    public String getEvent_title() {
        return event_title;
    }

}
