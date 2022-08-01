package com.example.smartbox_dup.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "session_token")
    public String sessionToken;

    public User(String username, String sessionToken) {
        this.username = username;
        this.sessionToken = sessionToken;
    }

}
