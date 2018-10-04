package com.call.recorder.ui.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.call.recorder.helper.CommonMethods;

import java.io.IOException;

/**
 * Created by VS00481543 on 30-10-2017.
 */

public class RecorderService extends Service {

    static final String TAGS = " Inside Service";
    MediaRecorder recorder;

    public int onStartCommand(Intent intent, int flags, int startId) {
        recorder = new MediaRecorder();
        recorder.reset();

        String phoneNumber = intent.getStringExtra("number");
        Log.d(TAGS, "Phone number in service: " + phoneNumber);

        String rec = new CommonMethods().getFinalFileName(phoneNumber);

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        recorder.setOutputFile(rec);

        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();

        Log.d(TAGS, "onStartCommand: " + "Recording started");

        return START_NOT_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();

        recorder.stop();
        recorder.reset();
        recorder.release();
        recorder = null;

        Log.d(TAGS, "onDestroy: " + "Recording stopped");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}