package com.eventstalker.handler;

public class ImageRecord {
    private String url;
    private String title;
    private String event_price;
    private String day_date_time;
    private String num_dates;
    private String location;

    public ImageRecord(String url, String title, String event_price, String day_date_time,String num_dates, String location) {
        this.url = url;
        this.title = title;
        this.event_price = event_price;
        this.day_date_time = day_date_time;
        this.num_dates = num_dates;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
    public String getEvent_price() {
        return event_price;
    }
    public String getDay_date_time() {
        return day_date_time;
    }
    public String getNum_dates() {
        return num_dates;
    }
    public String getLocation() {
        return location;
    }
}
