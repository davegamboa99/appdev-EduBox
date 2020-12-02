package com.elearneur.edubox;

import android.content.Context;

import java.util.TreeSet;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

public class PCalendar extends Calendar {
    private TreeSet<GCalendar> groups;
    
    public PCalendar(){
        groups = new TreeSet<>();
        isShareable = false;
    }

    public void addGroup(GCalendar group){
        groups.add(group);
    }

    public GCalendar getGroup(GCalendar gcal){
        GCalendar cal = null;
        for (GCalendar group : groups){
            if (group.equals(gcal)){
                cal = group;
            }
        }
        return cal;
    }

    public void removeGroup(GCalendar group){
        groups.remove(group);
    }

    public TreeSet<GCalendar> getGroups(){
        return groups;
    }

    public String toStringGroups(){
        String s = "Groups[";
        for (GCalendar group : groups){
            s += group.getGroupName() + ",";
        }
        return s + "]";
    }

    public String toString(){
        String s = "PCalendar[";
        for (CalEvent evt : events){
            s += evt + ",";
        }
        s += "]\nGCalendar[";
        for (GCalendar gcal : groups){
            s += gcal.getGroupName() + "[";
            TreeSet<CalEvent> evts = gcal.getEvents();
            for (CalEvent evt : evts){
                s += evt + ",";
            }
            s += "],";
        }

        return s += "]";
    }

    public void saveCalendar(Context context) throws IOException {
//        FileOutputStream file = new FileOutputStream("personal_calendar.ser");
        FileOutputStream file = context.openFileOutput("personal_calendar.ser", Context.MODE_PRIVATE);
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(this);
        out.close();
        file.close();
        System.out.println("Calendar has been serialize.");
    }

    public static PCalendar loadCalendar(Context context) throws IOException, ClassNotFoundException {
//        FileInputStream file = new FileInputStream("personal_calendar.ser");
        FileInputStream file = context.openFileInput("personal_calendar.ser");
        ObjectInputStream in = new ObjectInputStream(file);
        PCalendar cal = (PCalendar) in.readObject();
        in.close();
        file.close();
        System.out.println("Calendar has been deserialize.");
        return cal;
    }
}