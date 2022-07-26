package com.example.smartbox_dup.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


/*
 * AppDatabase는 모든 엔티티 클래스를 entities로 가지고 있어야 함
 * AppDatabase는 RoomDatabase를 상속하는 추상클래스로 정의되어야 함
 * AppDatabase는
 * */
@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance;
    public abstract UserDao userDao();

    public static AppDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context,
                            AppDatabase.class,
                            "task.db"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return instance;
    }
}
