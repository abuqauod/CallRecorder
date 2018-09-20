package com.call.recorder.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by VS00481543 on 01-11-2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "callRecords";
    static final String TABLE_RECORD = "callRecord";
    static final String SERIAL_NUMBER = "serialNumber";
    static final String PHONE_NUMBER = "phoneNumber";
    // public static final String CONTACT_NAME="contactName";
    static final String TIME = "time";
    static final String DATE = "date";
     private static final int DATABASE_VERSION = 4;


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_LOG_TABLE = "CREATE TABLE " + TABLE_RECORD + "(" + SERIAL_NUMBER + " INTEGER PRIMARY KEY,"
                + PHONE_NUMBER + " TEXT,"
                + TIME + " TEXT,"
                 + DATE + " TEXT" + ")";

        db.execSQL(CREATE_LOG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORD);
        onCreate(db);
    }
}
