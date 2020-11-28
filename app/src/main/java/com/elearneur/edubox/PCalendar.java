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
    private TreeSet<GroupCalendar> groups;
    
    public PCalendar(){
        groups = new TreeSet<>();
        isShareable = false;
    }

    public void addGroup(GroupCalendar group){
        groups.add(group);
    }

    public void removeGroup(GroupCalendar group){
        groups.remove(group);
    }

    public String toString(){
        String s = "Calendar[";
        for (CalEvent evt : events){
            s += evt + ",";
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
