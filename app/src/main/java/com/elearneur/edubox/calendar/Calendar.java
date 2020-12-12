package com.elearneur.edubox.calendar;

import java.util.TreeSet;
import java.io.Serializable;

public class Calendar implements Serializable {
    protected int id;
    protected TreeSet<CalEvent> events;
    protected boolean isShareable;

    public Calendar() {
        events = new TreeSet<>();
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public boolean getIsSharable(){
        return isShareable;
    }

    public void addEvent(CalEvent evt) {
        if (events == null){
            events = new TreeSet<>();
        }
        boolean flag = true;
        for (CalEvent event: events){
            if (event.equals(evt)){
                event.copy(evt); //update
                flag = false;
                break;
            }
        }
        if (flag) {
            evt.setCalendar(id);
            events.add(evt);
        }
    }

    public TreeSet<CalEvent> getEvents(){
        return events;
    }

    public CalEvent getEvent(CalEvent evt){
        CalEvent e = null;
        for (CalEvent event : events){
            if (event.getTitle().equals(evt.getTitle())){
                e = event;
                break;
            }
        }
        return e;
    }

    public void showEvents() {
        for (CalEvent calEvent : events) {
            System.out.println(calEvent);
        }
    }
}
