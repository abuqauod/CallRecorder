package com.call.recorder.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by VS00481543 on 07-11-2017.
 */

public class DatabaseSingleton {
    private static SQLiteDatabase database;

    public static SQLiteDatabase getInstance(Context activity) {
        if (database == null)
            database = new DatabaseHandler(activity).getWritableDatabase();
        return database;
    }
}
