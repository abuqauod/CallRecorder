package com.call.recorder.helper;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import com.call.recorder.R;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by VS00481543 on 31-10-2017.
 */

public class CommonMethods {

    private final String TAG = "Inside Service";
    private Calendar cal = Calendar.getInstance();

    public static Bitmap getContactPhoto(Context context, String number) {
        Cursor cursor = null;
        Bitmap photo = BitmapFactory.decodeResource(context.getResources(), R.mipmap.man);
        try {
            ContentResolver contentResolver = context.getContentResolver();
            String contactId = null;
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

            String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};

            cursor = contentResolver.query(uri, projection, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
                }
                cursor.close();
            }


            Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));
            Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
            cursor = context.getContentResolver().query(photoUri, new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
            if (cursor == null) {
                return null;
            }

            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return BitmapFactory.decodeStream(new ByteArrayInputStream(data));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return photo;
    }

    private String findPreference(Service mActivity, String string) {
        SharedPreferences sharedPref = mActivity.getApplicationContext().getSharedPreferences(string, Context.MODE_PRIVATE);
        return "/" + sharedPref.getString(string, "") + "/";
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

    public String getFinalFileName(String phoneNumber) {
        return (new CommonMethods().getPath() + "/." + phoneNumber + "_" + new CommonMethods().getTIme() + Constants._file_format);
    }

    public String getPath() {
        String internalFile = getDate();
        File file = new File(Environment.getExternalStorageDirectory() + Constants._file_location);//"/My Recor ds/");
        File file1 = new File(Environment.getExternalStorageDirectory() + Constants._file_location + internalFile + "/");
        if (!file.exists()) {
            file.mkdir();
        }
        if (!file1.exists())
            file1.mkdir();


        String path = file1.getAbsolutePath();
        Log.d(TAG, "Path " + path);

        return path;
    }

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

        String time = String.valueOf(hr) + ":" + String.valueOf(min) + ":" + String.valueOf(sec) + " " + am_pm;

        Log.d(TAG, "Date " + CommonMethods.formatTime(time));
        return CommonMethods.formatTime(time);
    }

    public String getDate() {
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        String date = String.valueOf(day) + "_" + String.valueOf(month) + "_" + String.valueOf(year);

        Log.d(TAG, "Date " + date);
        return date;
    }

    public static String formatTime(String string) {
        SimpleDateFormat readingFormat = new SimpleDateFormat("hh:mm:ss aa", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm:ss aa", Locale.getDefault());

        try {
            Date date = readingFormat.parse(string);
            return outputFormat.format(date);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return string;
    }
}