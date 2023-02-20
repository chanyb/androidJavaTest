package com.example.smartbox_dup.sqlite;

import java.util.LinkedHashMap;
import java.util.Map;

public class Database {
    private SQLiteHandler locationTableHandler, huntInfoTableHandler;

    private static Database instance;

    private Database() {
        huntInfoTableInit();
    }

    public static SQLiteHandler getHuntInfoTable() {
        if (instance == null) instance = new Database();
        return instance.huntInfoTableHandler;
    }

    private void huntInfoTableInit() {
        SQLiteHandler.Builder sqliteBuilder = new SQLiteHandler.Builder("hunt_info.db", 1);
        Map<String, String> huntInfoTableInfo = new LinkedHashMap<>();
        huntInfoTableInfo.put("datetime", "text primary key");
        huntInfoTableInfo.put("image", "blob not null");

        sqliteBuilder.addTable("hunt_info", huntInfoTableInfo);
        huntInfoTableHandler = sqliteBuilder.build();
    }
}
