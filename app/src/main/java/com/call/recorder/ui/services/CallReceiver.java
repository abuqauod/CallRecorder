package com.call.recorder.ui.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.call.recorder.helper.CommonMethods;
import com.call.recorder.helper.Constants;
import com.call.recorder.helper.dataBase.DatabaseManager;
import com.call.recorder.ui.models.CallDetails;

import java.util.Date;
import java.util.List;

/**
 * Created by Mohammad
 * on 9/27/2018 3:01 PM.
 */
public class CallReceiver extends PhoneStateReceiver {

    @Override
    protected void onIncomingCallStarted(Context mContext, String number, Date start) {
        Log.d("aaaaaaa", "onIncomingCallStarted: ");
        //  insertToDB(mContext, number, start, Constants.CAL_TYPE_INCOMING_CALL);
    }

    @Override
    protected void onOutgoingCallStarted(Context mContext, String number, Date start) {
        Log.d("aaaaaaa", "onOutgoingCallStarted: ");
        // insertToDB(mContext, number, start, Constants.CAL_TYPE_OUT_GOING_CALL);
    }

    @Override
    protected void onMissedCall(Context mContext, String number, Date start) {
        Log.d("aaaaaaa", "onMissedCall: ");
        insertToDB(mContext, number, start, Constants.CAL_TYPE_MISSED_CALL);
    }


    @Override
    protected void onIncomingCallEnded(Context mContext, String number, Date start, Date end) {
        Log.d("aaaaaaa", "onIncomingCallEnded: ");
        insertToDB(mContext, number, start, Constants.CAL_TYPE_INCOMING_CALL);
    }

    @Override
    protected void onOutgoingCallEnded(Context mContext, String number, Date start, Date end) {
        Log.d("aaaaaaa", "onOutgoingCallEnded: ");
        insertToDB(mContext, number, start, Constants.CAL_TYPE_OUT_GOING_CALL);
    }

    private void insertToDB(Context mContext, String number, Date start, int calType) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);

        int serialNumber = pref.getInt(Constants.SERIAL_NUM_DATA, 1);
        CallDetails callDetails = new CallDetails();
        callDetails.setSerial(serialNumber);
        callDetails.setNum(number);
        callDetails.setCallType(calType);
        callDetails.setTime(CommonMethods.formatTime(new CommonMethods().getTIme()));
        callDetails.setDate(new CommonMethods().getDate());

        new DatabaseManager(mContext).addCallDetails(callDetails);
        List<CallDetails> list = new DatabaseManager(mContext).getAllDetails();
        for (CallDetails cd : list) {
            String log = "Serial Number : " + cd.getSerial() + " | Phone num : " + cd.getNum() + " | Time : " + cd.getTime() + " | Date : " + cd.getDate() + " | type : " + cd.getCallType();
            Log.d("Database ", log);
        }
    }
}
