package com.elearneur.edubox;

import java.util.Calendar;

public class Dates2 {
    private Calendar current;

    public Dates2(){
        current = Calendar.getInstance();
    }

    public void moveNextMonth(){
        current.add(Calendar.MONTH, 1);
    }

    public void movePrevMonth(){
        current.add(Calendar.MONTH, -1);
    }

    public void setCurrentDate(int dayOfMonth){
        current.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    public String getCurrentDayOfWeek(){
        String s = "";
        switch(current.get(Calendar.DAY_OF_WEEK)){
            case 1:
                s = "Sunday";
                break;
            case 2:
                s = "Monday";
                break;
            case 3:
                s = "Tuesday";
                break;
            case 4:
                s = "Wednesday";
                break;
            case 5:
                s = "Thursday";
                break;
            case 6:
                s = "Friday";
                break;
            case 7:
                s = "Saturday";
                break;
        }
        return s;
    }

    public String getCurrentMonthYear(){
        String s = "";
        switch (current.get(Calendar.MONTH)){
            case 0:
                s += "January";
                break;
            case 1:
                s += "February";
                break;
            case 2:
                s += "March";
                break;
            case 3:
                s += "April";
                break;
            case 4:
                s += "May";
                break;
            case 5:
                s += "June";
                break;
            case 6:
                s += "July";
                break;
            case 7:
                s += "August";
                break;
            case 8:
                s += "September";
                break;
            case 9:
                s += "October";
                break;
            case 10:
                s += "November";
                break;
            case 11:
                s += "December";
                break;
        }
        s += ", " + current.get(Calendar.YEAR);
        return s;
    }

    public int getMinDay(){
        return current.getActualMinimum(Calendar.DAY_OF_MONTH);
    }

    public int getMaxDay(){
        return current.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public int getCurrentDay(){
        return current.get(Calendar.DAY_OF_MONTH);
    }

    public String toString(){
        return "Dates[date=" + current.getTime() + "]";
    }
}
