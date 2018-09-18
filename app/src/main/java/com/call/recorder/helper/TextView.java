package com.call.recorder.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Mohammad
 * on 9/18/2018 11:15 AM.
 */
public class TextView extends android.support.v7.widget.AppCompatTextView {

    public TextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())

            init();
    }

    public TextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), Constants.FONT_REGULAR);
        setTypeface(tf);
    }
}
