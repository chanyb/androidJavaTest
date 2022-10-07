package com.example.smartbox_dup.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import chanyb.android.java.GlobalApplcation;

public class SQLiteHandler {

    private final String TAG = "this";
    private final String fileName;
    private final ArrayList<String> tables;
    private final ArrayList<Map<String, String>> columns;

    SQLiteOpenHelper mHelper = null;
    SQLiteDatabase mDB = null;
    int version;

    private SQLiteHandler(Builder builder) {
        this.fileName = builder.fileName;
        this.tables = builder.tables;
        this.columns = builder.columns;
        this.version = builder.version;

        ArrayList<String> sqls = getCreateTableSql(tables, columns);

        mHelper = new SQLiteOpenHelper(GlobalApplcation.getContext(), fileName, null, version) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
                for (String sql : sqls) {
                    // 테이블 생성
                    try{
                        sqLiteDatabase.execSQL(sql);
                    } catch (Exception e) {
                        Log.i(TAG, "execSQL error", e);
                    }

                    // 업그레이드인지 확인하고(파일확인), 데이터 있으면 넣기

                }
            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
                for (String table : tables) {
                    // 데이터 저장 필요 (파일저장?..)
                    

                    // 데이터 저장 완료한 테이블 삭제
                    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + table);
                }

                onCreate(sqLiteDatabase);
            }
        };
    }

    private ArrayList<String> getCreateTableSql(ArrayList<String> tables, ArrayList<Map<String,String>> columns) {
        ArrayList<String> sqls = new ArrayList<>();
        for (int idx=0; idx<tables.size(); idx++) {
            // each table
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("CREATE TABLE IF NOT EXISTS ");
            sqlBuilder.append(tables.get(idx));
            sqlBuilder.append("(");

            Map<String, String> column = columns.get(idx);
            for (String key : column.keySet()) {
                sqlBuilder.append(key); // key
                if(key != null && !key.equals("")) sqlBuilder.append(" ");
                sqlBuilder.append(column.get(key)); // key info (ex. INTEGER PRIMARY KEY AUTOINCREMENT)
                sqlBuilder.append(",");
            }
            sqlBuilder.setLength(sqlBuilder.length()-1); // remove last comma
            sqlBuilder.append(");");

            sqls.add(sqlBuilder.toString());
            Log.i("this", "create table sql: " + sqlBuilder.toString());
        }
        return sqls;
    }

    public static class Builder {
        // Requirement paramters
        private final String fileName;
        private final int version;

        // optional paramters
        private final ArrayList<String> tables;
        private final ArrayList<Map<String,String>> columns;

        public Builder(String fileName, int version) {
                this.fileName = fileName;
                this.version = version;
                tables = new ArrayList<>();
                columns = new ArrayList<>();
            }

        public Builder addTable(String table, Map<String,String> column) {
            this.tables.add(table);
            this.columns.add(column);
            return this;
        }

        public SQLiteHandler build() {
            return new SQLiteHandler(this);
        }
    }

    public Cursor select(String tableName, String[] columns, String columnForWhereClause, String[] valueForWhereClause, String groupBy, String having, String orderBy, String limit) {
        mDB = mHelper.getReadableDatabase();
        Cursor c = mDB.query(
                tableName,
                columns,
                columnForWhereClause /* column for where clause*/,
                valueForWhereClause/* value for where clause*/,
                groupBy,
                having,
                orderBy,
                limit);
        return c;
    }

    // 객체로 insert하는 함수
    public long insert(String tableName, Serializable serializable) {
        mDB = mHelper.getWritableDatabase();
        ContentValues contentValue = getContentValuesFromSerializable(tableName, serializable);
        return mDB.insert(tableName, null, contentValue); // 성공 시 0보다 큰 정수, 실패 시 -1 반환
    }

    // ContentValue로 insert하는 함수
    public long insert(String tableName, ContentValues contentValues) {
        mDB = mHelper.getWritableDatabase();
        return mDB.insert(tableName, null, contentValues); // 성공 시 0보다 큰 정수, 실패 시 -1 반환
    }

    // 직접 작성하여 update하는 함수
    public void update(String tableName, ContentValues contentValue, String whereCluase, String[] whereArgs) {
        mDB = mHelper.getWritableDatabase();
        mDB.update(tableName, contentValue, whereCluase, whereArgs);
    }

    // 객체로 update하는 함수
    public long update(String tableName, Serializable serializable) {
        mDB = mHelper.getWritableDatabase();
        ContentValues contentValue = getContentValuesFromSerializable(tableName, serializable);
        Map<String, String[]> whereClauseAndArgs = getWhereClauseFromTable(tableName, serializable);
        String whereClause = whereClauseAndArgs.keySet().iterator().next();
        String[] whereArgs = whereClauseAndArgs.get(whereClause);

        return mDB.update(tableName, contentValue, whereClause, whereArgs); // success: bigger than 0, fail: 0
    }

    // 직접 작성하여 delete 하는 함수
    public long delete(String tableName, String whereClause, String[] whereArgs)
    {
        Log.d(TAG, "delete");
        mDB = mHelper.getWritableDatabase();

        return mDB.delete(tableName, whereClause, whereArgs); // success: bigger than 0, fail: 0
    }

    // 객체로 delete 하는 함수
    public long delete(String tableName, Serializable serializable) {
        mDB = mHelper.getWritableDatabase();
        Map<String, String[]> whereClauseAndArgs = getWhereClauseFromTable(tableName, serializable);
        String whereClause = whereClauseAndArgs.keySet().iterator().next();
        String[] whereArgs = whereClauseAndArgs.get(whereClause);

        return mDB.delete(tableName, whereClause, whereArgs); // success: bigger than 0, fail: 0
    }

    // 해당 테이블의 whereCluase, whereArgs를 생성한다. (prinary key를 통해 구별하는)
    public Map<String, String[]> getWhereClauseFromTable(String tableName, Serializable serializable) {
        ContentValues contentValue = getContentValuesFromSerializable(tableName, serializable);
        Map<String, String> column = getColumns(tableName);
        StringBuilder whereClause = new StringBuilder();
        ArrayList<String> primaryKeys = getPrimaryKeysFromColumn(column);
        String[] whereArgs = new String[primaryKeys.size()];
        for (int idx=0; idx<primaryKeys.size(); idx++) {
            // 각각의 primary key에 대해
            whereClause.append(primaryKeys.get(idx));
            whereClause.append("=?");
            whereClause.append(" and ");
            whereArgs[idx] = (String) contentValue.get(primaryKeys.get(idx));
        }
        whereClause.setLength(whereClause.length()-5); // remove last ' and '
        Map<String, String[]> res = new LinkedHashMap<>();
        res.put(whereClause.toString(), whereArgs);
        return res;
    }

    public ArrayList<String> getPrimaryKeysFromColumn(Map<String, String> column) {
        ArrayList<String> primaryKeyList = new ArrayList<>();
        if(column.size() == 0) throw new RuntimeException("the parameter column size is 0");
        for (String key : column.keySet()) {
            String columnInfo = column.get(key);
            if (columnInfo.toLowerCase().contains("primary key")) {
                // primary key(colName, colName, ...) 인 경우
                if(columnInfo.toLowerCase().contains("primary key(")) {
                    String tmp = columnInfo.toLowerCase();
                    tmp = tmp.replace("primary key(", "");
                    tmp = tmp.replace(")", "");
                    tmp = tmp.replace(", ", ",");
                    tmp = tmp.replace(" ,", ",");
                    String[] tmpArr = tmp.split(",");
                    for(String keyName : tmpArr) {
                        primaryKeyList.add(keyName.trim());
                    }
                    break;
                } else {
                    // primary key라면,
                    primaryKeyList.add(key);
                }
            }
        }
        if(primaryKeyList.size() == 0) throw new RuntimeException("it must have at least one primary key but expected " + primaryKeyList.size() + ".");

        return primaryKeyList;
    }

    public Map<String,String> getColumns(String tableName) {
        for (int idx=0; idx<tables.size(); idx++) {
            if(!tables.get(idx).equals(tableName)) continue;

            // 해당하는 columns 가져와서
            return columns.get(idx);
        }

        throw new RuntimeException("The table name is incorrect.");
    }

    public ContentValues getContentValuesFromSerializable(String tableName, Serializable serializable) {
        ContentValues contentValue = new ContentValues();
        for (Method method : serializable.getClass().getMethods()) {
            if(!method.getDeclaringClass().getName().endsWith("SerializableClass")) continue;

            // Get name of key
            String field = null;
            try{
                field = method.getName().replace("get", "").toLowerCase();
            } catch (NullPointerException e) {
                Log.i(TAG, "insert error", e);
            }

            // Get data of value
            Object value = null;
            try {
                value = method.invoke(serializable);
            } catch (IllegalAccessException | InvocationTargetException e) {
                Log.i(TAG, "error", e);
            }

            if(value == null) throw new NullPointerException("insert error - getter returned null");

            /* field에 해당하는 column의 type찾기 s */
            Map<String, String> column = null;
            for (int idx=0; idx< tables.size(); idx++) {
                // 해당하는 테이블 idx 찾기
                if (!tables.get(idx).equals(tableName)) continue;

                // column찾기
                column = columns.get(idx);

                if (column == null) throw new NullPointerException("insert error - column is null");

                for (String columnName : column.keySet()) {
                    // columnName == field 인 값 찾기
                    if (!columnName.equals(field)) continue;

                    String sColumnInfo = column.get(columnName);
                    if (sColumnInfo == null)
                        throw new NullPointerException("insert error - sColumnInfo is null");

                    // first index is type of column
                    String sType = sColumnInfo.split(" ").length > 1 ? sColumnInfo.split(" ")[0].toLowerCase() : sColumnInfo.toLowerCase();

                    if(sType.contains("text") || sType.contains("varchar")) {
                        contentValue.put(field, (String) value);
                    } else if (sType.contains("integer")) {
                        contentValue.put(field, (int) value);
                    } else if (sType.contains("boolean")) {
                        contentValue.put(field, (Boolean) value);
                    } else {
                        throw new RuntimeException("sType error");
                    }
                    break;
                }
                break;
                /* Key에 해당하는 column의 type찾기 e */
            }
        }

        return contentValue;
    }

    public void close() {
        mHelper.close();
    }
}
