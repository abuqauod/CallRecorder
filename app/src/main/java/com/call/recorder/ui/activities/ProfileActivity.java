package com.call.recorder.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.call.recorder.R;
import com.call.recorder.helper.Constants;
import com.call.recorder.helper.dataBase.DatabaseManager;
import com.call.recorder.ui.adapters.ProfileAdapter;
import com.call.recorder.ui.models.CallDetails;

import java.util.Collections;
import java.util.List;

/**
 * Created by Mohammad
 * on 9/23/2018 3:41 PM.
 */
public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_actvity);
        inti();
    }

    private void inti() {
        RecyclerView mRecyclerView = findViewById(R.id.profile_fragment_list_log);

        List<CallDetails> callDetailsList = new DatabaseManager(this).getAllDetailsByPhoneNumber(getArgs());

        for (CallDetails cd : callDetailsList) {
            String log = "Phone num : " + cd.getNum() + " | Time : " + cd.getTime() + " | Date : " + cd.getDate() + " | type : " + cd.getCallType();
            Log.d("Database ", log);
        }

        Collections.reverse(callDetailsList);
        ProfileAdapter madAdapter = new ProfileAdapter(callDetailsList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(madAdapter);
    }

    public String getArgs() {
        Bundle b = getIntent().getExtras();
        String value = "";
        if (b != null)
            value = b.getString(Constants._mobile_number);

        return value;
    }
}
