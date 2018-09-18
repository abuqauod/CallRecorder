package com.call.recorder.ui.activities;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.call.recorder.R;
import com.call.recorder.helper.Constants;
import com.call.recorder.helper.DatabaseHandler;
import com.call.recorder.helper.DatabaseManager;
import com.call.recorder.helper.TextView;
import com.call.recorder.ui.adapters.RecordAdapter;
import com.call.recorder.ui.models.CallDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final static String TAGMA = "Main Activity";
    DatabaseHandler db = new DatabaseHandler(this);
    RecordAdapter rAdapter;
    RecyclerView mRecycler;
    LinearLayout mLayout;
    TextView mStartBtn;
    List<CallDetails> callDetailsList;
    boolean checkResume = false;
    SharedPreferences mPreferences;
    SwitchCompat mSwitchCompat;
    View mMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        initControls();
        /*StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());*/

      /*  if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }*/

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.edit().putInt("numOfCalls", 0).apply();

        // pref.edit().putInt("serialNumData", 1).apply();

        //rAdapter.notifyDataSetChanged();
    }

    private void initControls() {
        mRecycler = findViewById(R.id.main_activity_recycler);
        mLayout = findViewById(R.id.main_activity_no_data);
        mStartBtn = findViewById(R.id.main_activity_start_btn);
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording(!mPreferences.getBoolean(Constants.SWITCH_ON, true));
            }
        });

        mMenuView = getLayoutInflater().inflate(R.layout.switch_layout, null, false);
        mSwitchCompat = mMenuView.findViewById(R.id.switchCheck);

    }

    public void startRecording(boolean isTurnItOn) {
        mPreferences.edit().putBoolean(Constants.SWITCH_ON, isTurnItOn).apply();
        Toast.makeText(getApplicationContext(), getString(R.string.call_recording) + " " + (isTurnItOn ? getString(R.string.on) : getString(R.string.off)), Toast.LENGTH_LONG).show();
        mSwitchCompat.setChecked(isTurnItOn);
    }

    protected void onPause() {
        super.onPause();
        SharedPreferences pref3 = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref3.getBoolean("pauseStateVLC", false)) {
            checkResume = true;
            pref3.edit().putBoolean("pauseStateVLC", false).apply();
        } else
            checkResume = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Check", "onResume: ");
        if (checkPermission()) {
            //Toast.makeText(getApplicationContext(), "Permission already granted", Toast.LENGTH_LONG).show();
            if (!checkResume) {
                setUi();
                // this.callDetailsList=new DatabaseManager(this).getAllDetails();
            }
        }
    }

    private boolean checkPermission() {
        int i = 0;
        String[] perm = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS};
        List<String> reqPerm = new ArrayList<>();

        for (String permis : perm) {
            int resultPhone = ContextCompat.checkSelfPermission(MainActivity.this, permis);
            if (resultPhone == PackageManager.PERMISSION_GRANTED)
                i++;
            else {
                reqPerm.add(permis);
            }
        }

        return i == 5 || requestPermission(reqPerm);
    }

    public void setUi() {
        callDetailsList = new DatabaseManager(this).getAllDetails();

        for (CallDetails cd : callDetailsList) {
            String log = "Phone num : " + cd.getNum() + " | Time : " + cd.getTime1() + " | Date : " + cd.getDate1();
            Log.d("Database ", log);
        }

        if (callDetailsList.size() == 0) {
            mRecycler.setVisibility(View.GONE);
            mLayout.setVisibility(View.VISIBLE);
        } else {
            mRecycler.setVisibility(View.VISIBLE);
            mLayout.setVisibility(View.GONE);

            rAdapter.notifyDataSetChanged();
            Collections.reverse(callDetailsList);
            rAdapter = new RecordAdapter(callDetailsList, this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            mRecycler.setLayoutManager(layoutManager);
            mRecycler.setItemAnimator(new DefaultItemAnimator());
            mRecycler.setAdapter(rAdapter);
        }
    }

    private boolean requestPermission(List<String> perm) {
        // String[] permissions={Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        String[] listReq = new String[perm.size()];
        listReq = perm.toArray(listReq);
        for (String permissions : listReq) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissions)) {
                Toast.makeText(getApplicationContext(), getString(R.string.phone_permissions_needed_for) + permissions, Toast.LENGTH_LONG);
            }
        }

        ActivityCompat.requestPermissions(MainActivity.this, listReq, 1);


        return false;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getApplicationContext(), "Permission Granted to access Phone calls", Toast.LENGTH_LONG);
                else
                    Toast.makeText(getApplicationContext(), "You can't access Phone calls", Toast.LENGTH_LONG);
                break;
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        MenuItem item = menu.findItem(R.id.mySwitch);

        mSwitchCompat.setChecked(mPreferences.getBoolean(Constants.SWITCH_ON, true));
        mSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startRecording(true);
                } else {
                    startRecording(false);
                }
            }
        });
        item.setActionView(mMenuView);
        return true;
    }

}