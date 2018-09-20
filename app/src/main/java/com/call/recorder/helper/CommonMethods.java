package com.call.recorder.helper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.File;
import java.util.Calendar;

/**
 * Created by VS00481543 on 31-10-2017.
 */

public class CommonMethods {

    private final String TAGCM = "Inside Service";
    private Calendar cal = Calendar.getInstance();

    public String getTIme() {
        String am_pm = "";
        int sec = cal.get(Calendar.SECOND);
        int min = cal.get(Calendar.MINUTE);
        int hr = cal.get(Calendar.HOUR);
        int amPm = cal.get(Calendar.AM_PM);
        if (amPm == 1)
            am_pm = "PM";
        else if (amPm == 0)
            am_pm = "AM";

        String time = String.valueOf(hr) + ":" + String.valueOf(min) + ":" + String.valueOf(sec) + "__" + am_pm;

        Log.d(TAGCM, "Date " + time);
        return time;
    }

    public String getPath() {
        String internalFile = getDate();
        File file = new File((Environment.getExternalStorageDirectory() + "") + Constants._file_location);
        File file1 = new File((Environment.getExternalStorageDirectory() + "") + Constants._file_location + internalFile + "/");
        if (!file.exists()) {
            file.mkdir();
        }
        if (!file1.exists())
            file1.mkdir();


        String path = file1.getAbsolutePath();
        Log.d(TAGCM, "Path " + path);

        return path;
    }

    public String getDate() {
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        String date = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);

        Log.d(TAGCM, "Date " + date);
        return date;
    }

    public String getContactName(final String number, Context context) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
        String contactName = "";
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(0);
            }
            cursor.close();
        }

        if (contactName != null && !contactName.equals(""))
            return contactName;
        else
            return "";
    }

}
