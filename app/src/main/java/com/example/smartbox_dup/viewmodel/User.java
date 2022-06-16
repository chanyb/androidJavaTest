package com.example.smartbox_dup.viewmodel;

public class User {
    private String username;
    private String location;
    private String locatedtime;

    public void setLocatedtime(String locatedtime) {
        this.locatedtime = locatedtime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocatedtime() {
        return locatedtime;
    }

    public String getLocation() {
        return location;
    }

    public String getUsername() {
        return username;
    }
}
