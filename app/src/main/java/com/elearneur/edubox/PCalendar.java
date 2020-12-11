package com.elearneur.edubox;

import android.content.Context;

import java.util.LinkedList;
import java.util.Random;
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
    private TreeSet<Integer> ids;
    private Account account;
    
    public PCalendar(){
        groups = new TreeSet<>();
        ids = new TreeSet<>();
        account = new Account();
        isShareable = false;
    }

    public void addGroup(GCalendar group){
        boolean flag = true;
        for (GCalendar gcal : groups){
            if (gcal.equals(group)){
                gcal.setGroupName(group.getGroupName());
                flag = false;
                break;
            }
        }
        if (flag) groups.add(group);
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

    public Account getAccount(){
        return account;
    }

    public int generateId(){
        Random rand = new Random();
        int num = rand.nextInt();
        if (ids.contains(num)) return generateId();
        else return num;
    }

    public static String getColor(int index){
        String[] colors = new String[]{
                "#FF0000", //RED
                "#00FF00" , //GREEN
                "#0000FF" //BLUE
        };
        return colors[index%colors.length];
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
        if (events != null){
            for (CalEvent evt : events){
                s += evt + ",";
            }
        }
        s += "]\nGCalendar[";
        if (groups != null){
            for (GCalendar gcal : groups){
                s += gcal.getGroupName() + "[";
                TreeSet<CalEvent> evts = gcal.getEvents();
                if (evts != null){
                    for (CalEvent evt : evts){
                        s += evt + ",";
                    }
                }
                s += "],";
            }
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
    }

    public static PCalendar loadCalendar(Context context) throws IOException, ClassNotFoundException {
//        FileInputStream file = new FileInputStream("personal_calendar.ser");
        FileInputStream file = context.openFileInput("personal_calendar.ser");
        ObjectInputStream in = new ObjectInputStream(file);
        PCalendar cal = (PCalendar) in.readObject();
        in.close();
        file.close();
        return cal;
    }
}
