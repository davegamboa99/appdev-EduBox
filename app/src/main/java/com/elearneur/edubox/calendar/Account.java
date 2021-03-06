package com.elearneur.edubox.calendar;

import java.io.Serializable;

public class Account implements Serializable {
    private int id = 0;
    private String username = "";
    private String email = "";
    private String password = "";

    public class JSONPostPutData {
        private String username = Account.this.username;
        private String email = Account.this.email;
        private String password = Account.this.password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Member toMemberData(){
        return new Member(id, username);
    }

    public String toString(){
        return "Account[id=" + id + ", username=" + username + ", email=" + email + ", password=" + password + "]";
    }
}
