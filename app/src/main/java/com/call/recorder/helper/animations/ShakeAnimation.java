package com.call.recorder.helper.animations;

/*
 * Created by Mohammad on 9/18/2018 3:32 PM.
 */

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.LinearInterpolator;


class ShakeAnimation {

    // How to call
    // ShakeAnimation.create().with(shakeImage).setDuration(2000).setRepeatMode(ShakeAnimation.RESTART).setRepeatCount(ShakeAnimation.INFINITE).start();

    public static final int RESTART = 1;
    public static final int REVERSE = 2;
    public static final int INFINITE = -1;
    private int duration = 2000;
    private int repeatMode = ValueAnimator.RESTART;
    private int repeatCount = -1;
    private View view;

    private ShakeAnimation() {
    }

    public static ShakeAnimation create() {
        return new ShakeAnimation();
    }

    public ShakeAnimation with(@NonNull View view) {
        this.view = view;
        return this;
    }

    public void start() {
        if (this.view == null) {
            throw new NullPointerException("View cant be null!");
        } else {
            ObjectAnimator imageViewObjectAnimator = ObjectAnimator.ofFloat(this.view, "translationX", 0.0F, 25.0F, -25.0F, 25.0F, -25.0F, 15.0F, -15.0F, 6.0F, -6.0F, 0.0F);
            imageViewObjectAnimator.setDuration((long) duration);
            imageViewObjectAnimator.setRepeatMode(repeatMode);
            imageViewObjectAnimator.setRepeatCount(repeatCount);
            imageViewObjectAnimator.setInterpolator(new LinearInterpolator());
            imageViewObjectAnimator.start();
        }
    }

    public ShakeAnimation setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public ShakeAnimation setRepeatMode(int repeatMode) {
        this.repeatMode = repeatMode;
        return this;
    }

    public ShakeAnimation setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
        return this;
    }
}

