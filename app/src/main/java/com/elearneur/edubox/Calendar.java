package com.elearneur.edubox;

import java.util.Date;
import java.util.TreeSet;
import java.io.Serializable;
import java.util.stream.StreamSupport;

public class Calendar implements Serializable {
    protected long id;
    protected TreeSet<CalEvent> events;
    protected boolean isShareable;

    public Calendar() {
        events = new TreeSet<>();
    }

    public boolean getIsSharable(){
        return isShareable;
    }

    public void addEvent(CalEvent evt) {
        boolean b = events.add(evt);
        if (b)
            System.out.println(evt.getTitle() + " successfully added");
        else
            System.out.println(evt.getTitle() + " added failed");
    }

    public TreeSet<CalEvent> getEvents(){
        return events;
    }

    public void showEvents() {
        for (CalEvent calEvent : events) {
            System.out.println(calEvent);
        }
    }
}
