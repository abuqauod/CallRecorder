package com.call.recorder.ui.models;

import java.io.Serializable;

/**
 * Created by VS00481543 on 01-11-2017.
 */

public class CallDetails implements Serializable {

    private int serial;
    private String num;
    private String name;
    private String time;
    private String date;
    private String callType;

    public CallDetails() {
    }

    public CallDetails(int serial, String num, String time, String date, String callType) {
        this.serial = serial;
        this.num = num;
        this.time = time;
        this.date = date;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime1() {
        return time;
    }

    public void setTime1(String time) {
        this.time = time;
    }

    public String getDate1() {
        return date;
    }

    public void setDate1(String date) {
        this.date = date;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }
}
