package com.elearneur.edubox;

import java.util.TreeSet;
import java.lang.Comparable;

public class GCalendar extends Calendar implements Comparable<GCalendar> {
    private String name;
    private long invitationCode;
//    private TreeSet<Account> members;

    public GCalendar(String groupName){
        this.name = groupName;
        invitationCode = id;
//        members = new TreeSet<>();
        isShareable = true;
    }

    public void setGroupName(String groupName){
        this.name = groupName;
    }

    public String getGroupName(){
        return name;
    }

    public void setInvitationCode(long invitationCode){
        this.invitationCode = invitationCode;
    }

    public long getInvitationCode(){
        return invitationCode;
    }

//    public void addMember(Account acc){
//        members.add(acc);
//    }

//    public void removeMember(Account acc){
//        members.remove(acc);
//    }

    public int compareTo(GCalendar group){
        int comp = name.compareTo(group.getGroupName());
//        if (comp >= 0) return 1;
//        else return -1;
        return comp;
    }

    public boolean equals(Object obj){
        if (obj instanceof GCalendar){
            return name.equals(((GCalendar) obj).getGroupName());
        } else {
            return false;
        }
    }

    public String toString(){
        return "Group[id=" + id + ",name=" + name + ", isShareable=" + isShareable + "]";
    }
}
