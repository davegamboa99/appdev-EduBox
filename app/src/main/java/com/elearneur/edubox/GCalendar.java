package com.elearneur.edubox;

import java.util.TreeSet;
import java.lang.Comparable;

public class GCalendar extends Calendar implements Comparable<GCalendar> {
    private String name;
    private int invitationCode;
    private TreeSet<Member> members;

    public GCalendar(String groupName){
        this.name = groupName;
        invitationCode = 0;
        isShareable = true;
        members = new TreeSet<Member>();
    }

    public class JSONPostData {
        private String name = GCalendar.this.name;
        private boolean isShareable = GCalendar.this.isShareable;
    }

    public void setGroupName(String groupName){
        this.name = groupName;
    }

    public String getGroupName(){
        return name;
    }

    public void setInvitationCode(int invitationCode){
        this.invitationCode = invitationCode;
    }

    public int getInvitationCode(){
        return id;
    }

   public void addMember(Member member){
       members.add(member);
   }

//    public void removeMember(Account acc){
//        members.remove(acc);
//    }

    public int compareTo(GCalendar group){
        return id - group.getId();
    }

    public boolean equals(Object obj){
//        if (obj instanceof GCalendar){
//            return name.equals(((GCalendar) obj).getGroupName());
//        } else {
//            return false;
//        }
        return id == ((GCalendar) obj).getId();
    }

    public String toString(){
        String s = "Group[id=" + id + ",name=" + name + ", isShareable=" + isShareable + ", Members[";
        if (members != null){
            for (Member member : members){
                s += member;
            }
        }
        s += "]]";
        return s;
    }
}
