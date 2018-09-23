package com.call.recorder.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.call.recorder.R;
import com.call.recorder.ui.models.CallDetails;

/**
 * Created by Mohammad
 * on 9/23/2018 2:35 PM.
 */
public class DialogLongClick extends Dialog {
    CallDetails mCallDetails;
    private Context mActivity;

    public DialogLongClick(Context activity, CallDetails details) {
        super(activity);
        mActivity = activity;
        mCallDetails = details;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_long_click);

    }
}
