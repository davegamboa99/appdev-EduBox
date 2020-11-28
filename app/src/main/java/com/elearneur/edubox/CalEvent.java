package com.elearneur.edubox;

import java.io.Serializable;
import java.lang.Comparable;

public class CalEvent implements Comparable<CalEvent>, Serializable {
    private long eventId;
    private long calendarId;
    private String title;
    private String date;
    private String time;
    private String content_type;
    private float duration; // in hours
    private String note;
    private boolean isRead;

    public CalEvent(String title, String date, String time, String content_type, float duration, String note){
        this.title = title;
        this.date = date;
        this.time = time;
        this.content_type = content_type;
        this.duration = duration;
        this.note = note;
    }

    public void setEventId(long eventId){
        this.eventId = eventId;
    }

    public long getEventId(){
        return eventId;
    }

    public void setCalendarId(long calendarId){
        this.calendarId = calendarId;
    }

    public long getCalendarId(){
        return calendarId;
    }

    public String getTitle(){
        return title;
    }

    public String getDate(){
        return date;
    }

    public String getTime(){
        return time;
    }

    public String getContentType(){
        return content_type;
    }

    public float getDuration(){
        return duration;
    }

    public String getNote(){
        return note;
    }

    public int compareTo(CalEvent evt){
        int comp = date.compareTo(evt.getDate());
        if (comp >= 0) return 1;
        else return -1;
    }

    public void setIsRead(boolean isRead){
        this.isRead = isRead;
    }

    public boolean getIsRead(){
        return isRead;
    }

    public String toString(){
        String s = "Event[id = " + eventId;
        s += ", title = " + title;
        s += ", date = " + date;
        s += ", type = " + content_type;
        s += ", duration = " + duration;
        s += ", note = " + note;
        s += "]";
        return s;
    }
}