package com.call.recorder.helper.animations;

/**
 * Created by Mohammad
 * on 9/18/2018 3:42 PM.
 */

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.View;

public class PulseAnimation {
    // How to call:
    // PulseAnimation.create().with(pulseImage) .setDuration(310) .setRepeatCount(PulseAnimation.INFINITE) .setRepeatMode(PulseAnimation.REVERSE) .start();

    public static final int RESTART = 1;
    public static final int REVERSE = 2;
    public static final int INFINITE = -1;
    private int duration = 310;
    private View view;
    private int repeatMode = ValueAnimator.RESTART;
    private int repeatCount = INFINITE;

    public static PulseAnimation create() {
        return new PulseAnimation();
    }

    public PulseAnimation with(@NonNull View view) {
        this.view = view;
        return this;
    }

    public void start() {

        if (view == null)
            throw new NullPointerException("View cant be null!");

        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat("scaleX", 1.2f), PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(duration);
        scaleDown.setRepeatMode(repeatMode);
        scaleDown.setRepeatCount(repeatCount);
        scaleDown.start();
    }

    public PulseAnimation setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public PulseAnimation setRepeatMode(int repeatMode) {
        this.repeatMode = repeatMode;
        return this;
    }

    public PulseAnimation setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
        return this;
    }
}
