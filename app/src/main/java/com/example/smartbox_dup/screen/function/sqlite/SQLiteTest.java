package com.example.smartbox_dup.screen.function.sqlite;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.screen.function.SerializableClass;
import com.example.smartbox_dup.sqlite.SQLiteHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
            if (sqLiteHandler == null) return;
            SerializableClass human = new SerializableClass("pr_name2", 2, "pr_addr2", 2);
            Log.i("this", "insert result: " + sqLiteHandler.insert("Human", human));
        });

        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener((view) -> {
            if (sqLiteHandler == null) return;

            try {
                ArrayList<Object> queryResult = sqLiteHandler.select(SerializableClass.class, "Human", null, null, null, null, null, null, null);
                Log.i("this", "====>" + (queryResult.size()));
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        });

        btn_3 = findViewById(R.id.btn_3);
        btn_3.setOnClickListener((view) -> {
            if (sqLiteHandler == null) return;

            SerializableClass human = new SerializableClass("pr_name", 4, "pr_addr", 4);
            Log.i("this", "update result: " + sqLiteHandler.update("Human", human));
        });

        btn_4 = findViewById(R.id.btn_4);
        btn_4.setOnClickListener((view) -> {
            if (sqLiteHandler == null) return;

            SerializableClass human = new SerializableClass("pr_name", 4, "pr_addr", 4);
            Log.i("this", "delete result: " + sqLiteHandler.delete("Human", human));
        });
    }

    private void initSqlite() {
        SQLiteHandler.Builder sqliteBuilder = new SQLiteHandler.Builder("smartbox_dup2.db", 1);
        Map<String, String> humanInfo = new LinkedHashMap<>();
        humanInfo.put("name", "varchar(10) not null");
        humanInfo.put("address", "varchar(20) not null");
        humanInfo.put("age", "integer not null");
        humanInfo.put("level", "integer");
        humanInfo.put("", "primary key(name, address)");

        sqliteBuilder.addTable("Human", humanInfo);
        sqLiteHandler = sqliteBuilder.build();
    }


}
