package com.elearneur.edubox;

import java.io.Serializable;
import java.lang.Comparable;

public class Member implements Comparable, Serializable {
    private int id;
    private String username;

    public Member(int id, String username){
        this.id = id;
        this.username = username;
    }

    public class JSONPostData{
        private int calendar;
        private int account = Member.this.id;

        public void setCalendar(int calendarId){
            this.calendar = calendarId;
        }
    }

    public int getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }

    public int compareTo(Object member){
        return id - ((Member) member).getId();
    }

    public String toString(){
        return "Member[id=" + id + ",username=" + username + "]";
    }
}
