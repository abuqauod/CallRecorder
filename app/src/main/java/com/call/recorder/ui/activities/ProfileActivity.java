package com.call.recorder.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.call.recorder.R;
import com.call.recorder.helper.CommonMethods;
import com.call.recorder.helper.Constants;
import com.call.recorder.helper.TextViewCustom;
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

    RecyclerView mRecyclerView;
    ImageView mUserImage;
    TextViewCustom mUserName;
    String value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_actvity);
        inti();
    }

    private void inti() {
        mRecyclerView = findViewById(R.id.profile_fragment_list_log);
        mUserImage = findViewById(R.id.activity_pin_profile_image);
        mUserName = findViewById(R.id.activity_pin_profile_name);

        loadData();
    }

    public void loadData() {

        List<CallDetails> callDetailsList = new DatabaseManager(this).getAllDetailsByPhoneNumber(getArgs());
        mUserImage.setImageBitmap(CommonMethods.getContactPhoto(this, value));
        String name = new CommonMethods().getContactName(value, this);

        for (CallDetails cd : callDetailsList) {
            String log = "Phone num : " + cd.getNum() + " | Time : " + cd.getTime() + " | Date : " + cd.getDate() + " | type : " + cd.getCallType();
            Log.d("Database ", log);
        }

        mUserName.setText(name);
        Collections.reverse(callDetailsList);
        ProfileAdapter madAdapter = new ProfileAdapter(callDetailsList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(madAdapter);
    }

    public String getArgs() {
        Bundle b = getIntent().getExtras();
        if (b != null)
            value = b.getString(Constants._mobile_number);

        return value;
    }
}
//holder.mAvatar.setimag();