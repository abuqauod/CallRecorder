package com.call.recorder.helper.dataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.call.recorder.helper.CommonMethods;
import com.call.recorder.ui.models.CallDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VS00481543 on 07-11-2017.
 */
// Will be performing all actions on database.
public class DatabaseManager {

    private SQLiteDatabase sqLiteDatabase;

    public DatabaseManager(Context activity) {
        sqLiteDatabase = DatabaseSingleton.getInstance(activity);
    }

    public void addCallDetails(CallDetails callDetails) {


        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.SERIAL_NUMBER, callDetails.getSerial());
        values.put(DatabaseHandler.PHONE_NUMBER, callDetails.getNum());
        // values.put(DatabaseHandler.CONTACT_NAME,callDetails.getName());
        values.put(DatabaseHandler.CALL_TYPE, callDetails.getCallType());

        values.put(DatabaseHandler.TIME, callDetails.getTime());
        values.put(DatabaseHandler.DATE, callDetails.getDate());


        sqLiteDatabase.insert(DatabaseHandler.TABLE_RECORD, null, values);
    }


    public List<CallDetails> getAllDetails() {
        List<CallDetails> recordList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DatabaseHandler.TABLE_RECORD;

        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CallDetails callDetails = new CallDetails();
                callDetails.setSerial(cursor.getInt(0));
                callDetails.setNum(cursor.getString(1));
                callDetails.setCallType(Integer.parseInt(cursor.getString(2)));

                // callDetails.setName(cursor.getString(2));
                callDetails.setTime(CommonMethods.formatTime(cursor.getString(3)));
                callDetails.setDate(cursor.getString(4));


                recordList.add(callDetails);
            } while (cursor.moveToNext());
        }

        return recordList;
    }

    public List<CallDetails> getAllDetailsByPhoneNumber(String phoneNumber) {
        List<CallDetails> recordList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DatabaseHandler.TABLE_RECORD + " WHERE phoneNumber='" + phoneNumber+"'";

        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CallDetails callDetails = new CallDetails();
                callDetails.setSerial(cursor.getInt(0));
                callDetails.setNum(cursor.getString(1));
                callDetails.setCallType(Integer.parseInt(cursor.getString(2)));
                callDetails.setTime(CommonMethods.formatTime(cursor.getString(3)));
                callDetails.setDate(cursor.getString(4));
                recordList.add(callDetails);
            } while (cursor.moveToNext());
        }

        return recordList;
    }
}
