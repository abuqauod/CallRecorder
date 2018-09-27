package com.call.recorder.ui.services;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.call.recorder.helper.CommonMethods;
import com.call.recorder.helper.Constants;
import com.call.recorder.helper.dataBase.DatabaseManager;
import com.call.recorder.ui.models.CallDetails;

import java.util.Date;
import java.util.List;

/**
 * Created by VS00481543 on 25-10-2017.
 */

public class PhoneStateReceiver extends BroadcastReceiver {

    static final String TAG = "State";
    static final String TAG1 = " Inside State";
    public static String phoneNumber;
    public static String name;
    static Boolean recordStarted;
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;  //because the passed incoming is only valid in ringing
    int callType = Constants.CAL_TYPE_MISSED_CALL;

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        Boolean switchCheckOn = pref.getBoolean("switchOn", true);
        if (switchCheckOn) {
            System.out.println("Receiver Start");

            //            boolean callWait=pref.getBoolean("recordStarted",false);
            Bundle extras = intent.getExtras();
            assert extras != null;

            //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
            if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
                savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            } else {
                String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                int state = 0;
                assert stateStr != null;
                if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    state = TelephonyManager.CALL_STATE_IDLE;
                    TelephonyStatRINGING(pref, extras.getString(TelephonyManager.EXTRA_STATE), context);
                } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    state = TelephonyManager.CALL_STATE_OFFHOOK;
                    TelephonyStatOFFHOOK(pref, extras.getString(TelephonyManager.EXTRA_STATE), context, intent);
                } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    state = TelephonyManager.CALL_STATE_RINGING;
                    TelephonyStatIDLE(pref, extras.getString(TelephonyManager.EXTRA_STATE), context);
                }


                onCallStateChanged(context, state, number);
            }
        }

    }

    private void TelephonyStatRINGING(SharedPreferences pref, String state, Context context) {

        Log.d(TAG1, " Inside " + state);
        callType = Constants.CAL_TYPE_INCOMING_CALL;
                /*int j=pref.getInt("numOfCalls",0);
                pref.edit().putInt("numOfCalls",++j).apply();
                Log.d(TAG, "onReceive: num of calls "+ pref.getInt("numOfCalls",0));*/

    }

    private void TelephonyStatOFFHOOK(SharedPreferences pref, String state, Context context, Intent intent) {

        callType = Constants.CAL_TYPE_OUT_GOING_CALL;
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

            int serialNumber = pref.getInt(Constants.SERIAL_NUM_DATA, 1);

            CallDetails callDetails = new CallDetails();
            callDetails.setSerial(serialNumber);
            callDetails.setNum(phoneNumber);
            callDetails.setCallType(callType);
            callDetails.setTime(CommonMethods.formatTime(new CommonMethods().getTIme()));
            callDetails.setDate(new CommonMethods().getDate());

            new DatabaseManager(context).addCallDetails(callDetails);

            List<CallDetails> list = new DatabaseManager(context).getAllDetails();
            for (CallDetails cd : list) {
                String log = "Serial Number : " + cd.getSerial() + " | Phone num : " + cd.getNum() + " | Time : " + cd.getTime() + " | Date : " + cd.getDate() + " | type : " + cd.getCallType();
                Log.d("Database ", log);
            }


            //recordStarted=true;
            pref.edit().putInt(Constants.SERIAL_NUM_DATA, ++serialNumber).apply();
            pref.edit().putBoolean("recordStarted", true).apply();
        }


    }

    private void TelephonyStatIDLE(SharedPreferences pref, String state, Context context) {

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

    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    public void onCallStateChanged(Context context, int state, String number) {
        if (lastState == state) {
            //No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
                onIncomingCallStarted(context, number, callStartTime);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false;
                    callStartTime = new Date();
                    onOutgoingCallStarted(context, savedNumber, callStartTime);
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss
                    onMissedCall(context, savedNumber, callStartTime);
                } else if (isIncoming) {
                    onIncomingCallEnded(context, savedNumber, callStartTime, new Date());
                } else {
                    onOutgoingCallEnded(context, savedNumber, callStartTime, new Date());
                }
                break;
        }
        lastState = state;
    }

    //Derived classes should override these to respond to specific events of interest
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
    }

    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
    }

    protected void onMissedCall(Context ctx, String number, Date start) {
    }

    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    //Deals with actual events

    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
    }
}