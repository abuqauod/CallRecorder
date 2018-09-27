package com.call.recorder.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Mohammad
 * on 9/18/2018 11:15 AM.
 */
public class TextViewCustom extends android.support.v7.widget.AppCompatTextView {

    public TextViewCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())

            init();
    }

    public TextViewCustom(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), Constants.FONT_REGULAR);
        setTypeface(tf);
    }
}
