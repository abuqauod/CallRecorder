package com.call.recorder.helper;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.call.recorder.ui.models.CallDetails;

import java.util.List;
import java.util.Objects;

/**
 * Created by VS00481543 on 25-10-2017.
 */

public class PhoneStateReceiver extends BroadcastReceiver {

    static final String TAG = "State";
    static final String TAG1 = " Inside State";
    public static String phoneNumber;
    public static String name;
    static Boolean recordStarted;

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        Boolean switchCheckOn = pref.getBoolean(Constants.SWITCH_ON, true);
        if (switchCheckOn) {
            try {
                System.out.println("Receiver Start");

                //            boolean callWait=pref.getBoolean("recordStarted",false);
                Bundle extras = intent.getExtras();
                String state = Objects.requireNonNull(extras).getString(TelephonyManager.EXTRA_STATE);
                Log.d(TAG, " onReceive: " + state);
                Toast.makeText(context, "Call detected(Incoming/Outgoing) " + state, Toast.LENGTH_SHORT).show();

                if (Objects.equals(state, TelephonyManager.EXTRA_STATE_RINGING)) {
                    Log.d(TAG1, " Inside " + state);
                /*int j=pref.getInt("numOfCalls",0);
                pref.edit().putInt("numOfCalls",++j).apply();
                Log.d(TAG, "onReceive: num of calls "+ pref.getInt("numOfCalls",0));*/
                } else if (Objects.equals(state, TelephonyManager.EXTRA_STATE_OFFHOOK)/*&& pref.getInt("numOfCalls",1)==1*/) {

                    int j = pref.getInt("numOfCalls", 0);
                    pref.edit().putInt("numOfCalls", ++j).apply();
                    Log.d(TAG, "onReceive: num of calls " + pref.getInt("numOfCalls", 0));

                    Log.d(TAG1, " recordStarted in offhook: " + recordStarted);
                    Log.d(TAG1, " Inside " + state);

                    phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

                    Log.d(TAG1, " Phone Number in receiver " + phoneNumber);

                    if (pref.getInt("numOfCalls", 1) == 1) {
                        Intent reivToServ = new Intent(context, RecorderService.class);
                        reivToServ.putExtra("number", phoneNumber);
                        context.startService(reivToServ);

                        //name=new CommonMethods().getContactName(phoneNumber,context);

                        int serialNumber = pref.getInt("serialNumData", 1);
                        new DatabaseManager(context).addCallDetails(new CallDetails(serialNumber, phoneNumber, new CommonMethods().getTIme(), new CommonMethods().getDate()));

                        List<CallDetails> list = new DatabaseManager(context).getAllDetails();
                        for (CallDetails cd : list) {
                            String log = "Serial Number : " + cd.getSerial() + " | Phone num : " + cd.getNum() + " | Time : " + cd.getTime1() + " | Date : " + cd.getDate1();
                            Log.d("Database ", log);
                        }


                        //recordStarted=true;
                        pref.edit().putInt("serialNumData", ++serialNumber).apply();
                        pref.edit().putBoolean("recordStarted", true).apply();
                    }

                } else if (Objects.equals(state, TelephonyManager.EXTRA_STATE_IDLE)) {
                    int k = pref.getInt("numOfCalls", 1);
                    pref.edit().putInt("numOfCalls", --k).apply();
                    int l = pref.getInt("numOfCalls", 0);
                    Log.d(TAG1, " Inside " + state);
                    recordStarted = pref.getBoolean("recordStarted", false);
                    Log.d(TAG1, " recordStarted in idle :" + recordStarted);
                    if (recordStarted && l == 0) {
                        Log.d(TAG1, " Inside to stop recorder " + state);

                        context.stopService(new Intent(context, RecorderService.class));

                        pref.edit().putBoolean("recordStarted", false).apply();
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}