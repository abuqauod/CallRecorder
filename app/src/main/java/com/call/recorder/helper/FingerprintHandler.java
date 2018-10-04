package com.call.recorder.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.call.recorder.R;

/**
 * Created by Mohammad
 * on 5/24/2018 2:02 PM.
 */


@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;

    public FingerprintHandler(Context mContext) {
        context = mContext;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update(context.getString(R.string.fingerprint_auth_error) + "\n" + errString, false);
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update(context.getString(R.string.fingerprint_auth_help) + "\n" + helpString, false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update(context.getString(R.string.fingerprint_auth_succeeded), true);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update(context.getString(R.string.fingerprint_auth_failed), false);
    }

    private void update(String e, Boolean success) {
        TextView textView = ((Activity) context).findViewById(R.id.errorText);
        textView.setText(e);
        if (success) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            //MyHelper.callLoginActivity(context);
        }
    }
}