package com.call.recorder.ui.models;

import java.io.Serializable;

/**
 * Created by VS00481543 on 01-11-2017.
 */

public class CallDetails implements Serializable {

    private int serial;
    private String num;
    private String time;
    private String date;
    private int callType;////1: received call, 2 outgoing call 0 missed call
    private long duration;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
