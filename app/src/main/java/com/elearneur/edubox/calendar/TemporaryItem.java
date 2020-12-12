package com.elearneur.edubox.calendar;

import java.util.ArrayList;
import java.util.List;

public class TemporaryItem {
    private String time;
    private List<Event> events;
    private  int eventSize;

    public TemporaryItem(String time, List<Event> events, int eventSize){
        this.time = time;
        this.events = events;
        this.eventSize = eventSize;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public int getEventSize() {
        return eventSize;
    }

    public void setEventSize(int eventSize) {
        this.eventSize = eventSize;
    }
}
