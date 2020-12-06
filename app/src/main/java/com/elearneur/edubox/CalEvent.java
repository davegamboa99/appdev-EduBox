package com.elearneur.edubox;

import java.io.Serializable;
import java.lang.Comparable;

public class CalEvent implements Comparable<CalEvent>, Serializable {
    private int calendar;
    private int eventId;
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
        if (content_type == null) this.content_type = "";
        else this.content_type = content_type;
        this.duration = duration;
        if (note == null) this.note = "";
        else this.note = note;
    }

    public class JSONPostData {
        private int calendar = CalEvent.this.calendar;
        private String title = CalEvent.this.title;
        private String date = CalEvent.this.date;
        private String time = CalEvent.this.time;
        private String content_type = CalEvent.this.content_type;
        private float duration = CalEvent.this.duration;
        private String note = CalEvent.this.note;

        public void setCalendar(int calendarId){
            this.calendar = calendarId;
        }
    }

    public void setEventId(int eventId){
        this.eventId = eventId;
    }

    public int getEventId(){
        return eventId;
    }

    public void setCalendar(int calendarId){
        this.calendar = calendarId;
    }

    public long getCalendar(){
        return calendar;
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
        return eventId - evt.getEventId();
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