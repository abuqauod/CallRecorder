package com.call.recorder.helper.animations;

/**
 * Created by Mohammad
 * on 9/18/2018 3:41 PM.
 */

import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class FlipAnimation {

    // How to call:
    // FlipAnimation.create().with(mRecycler).setDuration(3600).setRepeatCount(FlipAnimation.INFINITE).start();

    public static final int RESTART = 1;
    public static final int REVERSE = 2;
    public static final int INFINITE = -1;
    private int duration = 3600;
    private int repeatCount = INFINITE;
    private View view;

    public static FlipAnimation create() {
        return new FlipAnimation();
    }

    public FlipAnimation with(@NonNull View view) {
        this.view = view;
        return this;
    }

    public void start() {

        if (view == null)
            throw new NullPointerException("View cant be null!");

        final ObjectAnimator animation = ObjectAnimator.ofFloat(view, "rotationY", 0.0f, 360f);
        animation.setDuration(duration);
        animation.setRepeatCount(repeatCount);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();

    }

    public FlipAnimation setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public FlipAnimation setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
        return this;
    }
}
