package com.example.smartbox_dup.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDBHandler {

    private final String TAG = "this";

    SQLiteOpenHelper mHelper = null;
    SQLiteDatabase mDB = null;

    public MyDBHandler(Context context, String name) {
        mHelper = new SQLiteOpenHelper(context, name, null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                String sql = "create table if not exists student("
                        + " _id integer PRIMARY KEY autoincrement, "
                        +  " name text, "
                        + " age integer, "
                        + " addressa text)";
                db.execSQL(sql);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int i, int i1) {

            }
        };
    }

    public static MyDBHandler open(Context context, String name) {
        return new MyDBHandler(context, name);
    }

    public Cursor select(String tableName, String name) {
        mDB = mHelper.getReadableDatabase();
        Cursor c = mDB.query(tableName,null,null,null,null,null,null,null);
//        Cursor c = mDB.rawQuery("SELECT * FROM " + tableName + " WHERE name="+name+";", null);

//        mDB.execSQL("SELECT * FROM " + tableName + ";");

        return c;
    }

    public void insert(String name, int age, String address) {

        Log.i(TAG, "insert");

        mDB = mHelper.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put("name", name);
        value.put("age", age);
        value.put("address", address);

        mDB.insert("student", null, value);
//        mDB.rawQuery("INSERT INTO student(name, age, address) values('"+name+"',"+age+",'"+address+"');", null);

    }

    public void delete(String name)
    {
        Log.d(TAG, "delete");
        mDB = mHelper.getWritableDatabase();
        mDB.delete("student", "name=?", new String[]{name});
    }

    public void close() {
        mHelper.close();
    }
}
