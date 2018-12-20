package com.example.ravikiran.logbook;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class DBclass extends SQLiteOpenHelper{

    private static final String TAG = "SQLiteDB";
    private static final String DB_Name = "myDB";
    private static final String table_01 = "MONEY_HOLDERS";
    private static final String table_02 = "MONEY_PULLERS";
    private static final String table_03 = "LOAN_BALANCE";
    private static final String table_04 = "LOG_LIST";

    DBclass(Context context) {
        super(context, DB_Name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String create_table_02 = "CREATE TABLE " + table_02 +
                " ( ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " NAME TEXT," +
                " BALANCE LONG DEFAULT 0)";
        sqLiteDatabase.execSQL(create_table_02);
        Log.d(TAG, "onCreate: " + table_02 + " created.");

        String create_table_03 = "CREATE TABLE " + table_03 +
                " ( ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " NAME TEXT," +
                " BALANCE LONG DEFAULT 0)";
        sqLiteDatabase.execSQL(create_table_03);
        Log.d(TAG, "onCreate: " + table_03 + " created.");

        String create_table_04 = "CREATE TABLE " + table_04 +
                " ( ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " DATE TEXT," +
                " GIVER TEXT," +
                " TAKER TEXT," +
                " AMOUNT LONG DEFAULT 0)";
        sqLiteDatabase.execSQL(create_table_04);
        Log.d(TAG, "onCreate: " + table_04 + " created.");

        DefaultEntry (sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void DefaultEntry(SQLiteDatabase sqL) {
        AddRow(sqL, table_01, "Bank", 5000);
        AddRow(sqL, table_01, "PayTM", 5000);
        AddRow(sqL, table_01, "inHand", 500);
        Log.d(TAG, "DefaultEntry: " + table_01 + " filled.");

        AddRow(sqL, table_02, "Food", 0);
        AddRow(sqL, table_02, "Travel", 0);
        AddRow(sqL, table_02, "Laundry", 0);
        AddRow(sqL, table_02, "Books", 0);
        AddRow(sqL, table_02, "Education", 0);
        AddRow(sqL, table_02, "others", 0);
        Log.d(TAG, "DefaultEntry: " + table_02 + " filled.");

        AddRow(sqL, table_03,"Simha", 0);
        AddRow(sqL, table_03,"Vamsi bro", 0);
        AddRow(sqL, table_03,"Doraemon", 0);
        AddRow(sqL, table_03,"Alwala", 0);
        Log.d(TAG, "DefaultEntry: " + table_03 + " filled.");
    }

    private void AddRow(SQLiteDatabase sqL, String table, String value_1, long value_2) {
        ContentValues cv = new ContentValues();
        cv.put("NAME", value_1);
        cv.put("BALANCE", value_2);
        sqL.insert(table, null, cv);

        Log.d(TAG, "AddRow: row " + value_1 + " added.");
    }

    void AddRow(String table, String value_1) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("NAME", value_1);
        cv.put("BALANCE", (long) 0);
        sqLiteDatabase.insert(table, null, cv);

        Log.d(TAG, "AddRow: row " + value_1 + " added.");
    }

    ArrayList<ID_Name_Bal_Object> getTable(String table, boolean nonZero) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor c = sqLiteDatabase.query(
                table,
                new String[]{"NAME", "BALANCE"},
                null,
                null,
                null,
                null,
                null);

        ArrayList<ID_Name_Bal_Object> e = new ArrayList<>();
        while(c.moveToNext()) {
            if (c.getLong(1) != 0 || !nonZero)
                e.add(new ID_Name_Bal_Object(
                        c.getString(0),
                        c.getLong(1)
            ));}

        c.close();
        Log.d(TAG, "getTable: " + table + " retrieved.");
        return e;
    }

    ArrayList<String> getColumnNames(String [] table) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> e = new ArrayList<>();

        for (String t : table) {
            Cursor c = sqLiteDatabase.query(
                    t,
                    new String[]{"NAME"},
                    null,
                    null,
                    null,
                    null,
                    null);

            while(c.moveToNext()) {
                e.add(c.getString(0));
            }
            c.close();
        }

        Log.d(TAG, "getColumnNames: " + e + " retrieved.");
        return e;
    }

    void transfer(String from, String to, long amount) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("yyyyMMdd");
        String date = format.format(Calendar.getInstance().getTime());

        ContentValues cv = new ContentValues();
        cv.put("DATE", date);
        cv.put("GIVER", from);
        cv.put("TAKER", to);
        cv.put("AMOUNT", amount);
        sqLiteDatabase.insert(table_04, null, cv);

        long amount_from = getAmount(from);
        long amount_to = getAmount(to);

        amount_from = amount_from - amount;
        amount_to = amount_to + amount;

        UpdateBalance(from, amount_from);
        UpdateBalance(to, amount_to);
    }

    long getAmount(String name) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        long e;

        Cursor c1 = sqLiteDatabase.query(
                table_01,
                new String[]{"BALANCE"},
                "NAME = ?",
                new String[]{name},
                null,
                null,
                null);
        if (c1.moveToPosition(0)) {
            e = c1.getLong(0);
            c1.close();
            return e;
        }

        Cursor c2 = sqLiteDatabase.query(
                table_02,
                new String[]{"BALANCE"},
                "NAME = ?",
                new String[]{name},
                null,
                null,
                null);
        if (c2.moveToPosition(0)) {
            e = c2.getLong(0);
            c2.close();
            return e;
        }

        Cursor c3 = sqLiteDatabase.query(
                table_03,
                new String[]{"BALANCE"},
                "NAME = ?",
                new String[]{name},
                null,
                null,
                null);
        if (c3.moveToPosition(0)) {
            e = c3.getLong(0);
            c3.close();
            return e;
        }

        return 0;
    }

    private void UpdateBalance(String name, long amount) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int i;
        ContentValues cv = new ContentValues();
        cv.put("BALANCE", amount);

        i = sqLiteDatabase.update(
                table_01,
                cv,
                "NAME LIKE ?",
                new String[]{name});
        if(i != 0) return;

        i = sqLiteDatabase.update(
                table_02,
                cv,
                "NAME LIKE ?",
                new String[]{name});
        if(i != 0) return;

        sqLiteDatabase.update(
                table_03,
                cv,
                "NAME LIKE ?",
                new String[]{name});
    }

    ArrayList<ID_Name_Bal_Object> getLog(String[] names) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<ID_Name_Bal_Object> e = new ArrayList<>();

        for (String sn : names) {
            Cursor c = sqLiteDatabase.query(
                    "LOG_LIST",
                    null,
                    "GIVER = ? OR TAKER = ?",
                    new String[]{sn, sn},
                    null,
                    null,
                    "ID DESC");

            while(c.moveToNext()) {
                if(c.getString(2).equals(sn))
                    e.add(new ID_Name_Bal_Object(
                            c.getString(1),
                            c.getString(3),
                            -c.getLong(4)
                    ));
                else if(c.getString(3).equals(sn))
                    e.add(new ID_Name_Bal_Object(
                            c.getString(1),
                            c.getString(2),
                            c.getLong(4)
                    ));
            }
            c.close();
        }

        Log.d(TAG, "getLog: " + Arrays.toString(names) + " retrieved.");
        return e;
    }

    ArrayList<ID_Name_Bal_Object> getFullLog() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> AL_s = getColumnNames(new String[]{"LOAN_BALANCE"});
        ArrayList<ID_Name_Bal_Object> e = new ArrayList<>();

        Cursor c = sqLiteDatabase.query(
                "LOG_LIST",
                null,
                null,
                null,
                null,
                null,
                "ID DESC");

        while(c.moveToNext())   {
            if(AL_s.contains(c.getString(2)) || AL_s.contains(c.getString(3)))
                e.add(new ID_Name_Bal_Object(
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        c.getLong(4)
                ));
            }
            c.close();

        Log.d(TAG, "getFullLog: retrieved.");
        return e;
    }
}
