package com.example.smartbox_dup.screen.function.sqlite;

import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.location.GoogleLocationManger;
import com.example.smartbox_dup.screen.function.SerializableClass;
import com.example.smartbox_dup.screen.function.alarm.AlarmCreateActivity;
import com.example.smartbox_dup.sqlite.SQLiteHandler;

import java.util.LinkedHashMap;
import java.util.Map;

public class SQLiteTest extends AppCompatActivity {
    Button btn_1, btn_2, btn_3, btn_4;
    SQLiteHandler sqLiteHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_sqlite);
        init();
        initSqlite();
    }

    private void init() {
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> {
            if(sqLiteHandler == null) return;
            SerializableClass human = new SerializableClass("pr_name", 1, "pr_addr", 1);
            Log.i("this", "insert result: " + sqLiteHandler.insert("Human", human));
        });

        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener((view) -> {
            if(sqLiteHandler == null) return;

            Cursor cursor = sqLiteHandler.select("Human", null, null, null, null, null, null, null);
            while(cursor.moveToNext()) {
                Log.i("this", cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3));
            }
        });

        btn_3 = findViewById(R.id.btn_3);
        btn_3.setOnClickListener((view) -> {
            if(sqLiteHandler == null) return;

            SerializableClass human = new SerializableClass("pr_name", 4, "pr_addr", 4);
            Log.i("this", "update result: " + sqLiteHandler.update("Human", human));
        });

        btn_4 = findViewById(R.id.btn_4);
        btn_4.setOnClickListener((view) -> {
            if(sqLiteHandler == null) return;

            SerializableClass human = new SerializableClass("pr_name", 4, "pr_addr", 4);
            Log.i("this", "delete result: " + sqLiteHandler.delete("Human", human));
        });
    }

    private void initSqlite() {
        SQLiteHandler.Builder sqliteBuilder = new SQLiteHandler.Builder("smartbox_dup2.db", 1);
        Map<String,String> humanInfo = new LinkedHashMap<>();
        humanInfo.put("name","varchar(10) not null");
        humanInfo.put("address","varchar(20) not null");
        humanInfo.put("age","integer not null");
        humanInfo.put("level","integer");
        humanInfo.put("", "primary key(name, address)");

        sqliteBuilder.addTable("Human", humanInfo);
        sqLiteHandler = sqliteBuilder.build();
    }
}
