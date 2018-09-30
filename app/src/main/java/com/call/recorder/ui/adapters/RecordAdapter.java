package com.call.recorder.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.call.recorder.R;
import com.call.recorder.helper.CommonMethods;
import com.call.recorder.helper.Constants;
import com.call.recorder.ui.activities.ProfileActivity;
import com.call.recorder.ui.dialogs.DialogLongClick;
import com.call.recorder.ui.models.CallDetails;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mohammad on 08-11-1987.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.MyViewHolder> {

    private List<CallDetails> callDetails;
    private Context mContext;
    private SharedPreferences pref;

    public RecordAdapter(List<CallDetails> callDetails, Context context) {
        this.callDetails = callDetails;
        this.mContext = context;
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @NonNull
    @Override
    public RecordAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case 0:
                View v1 = mLayoutInflater.inflate(R.layout.item_list_record, parent, false);
                return new MyViewHolder(v1);
            case 2:
                View v3 = mLayoutInflater.inflate(R.layout.date_layout, parent, false);
                return new MyViewHolder(v3);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.MyViewHolder holder, int position) {

        CallDetails mCallDetails = callDetails.get(position);
        String n = mCallDetails.getNum();
        String name = new CommonMethods().getContactName(n, mContext);
        String name2 = mContext.getString(R.string.unsaved);
        Log.d("Names", "onBindViewHolder: " + name);
        holder.bind(mCallDetails);
        holder.mSeparator.setVisibility(position == callDetails.size() - 1 ? View.GONE : View.VISIBLE);

        switch (getItemViewType(position)) {
            case 0:
                loadView(name, holder, name2, holder.getAdapterPosition(), mCallDetails, n);
                break;
            case 2:
                holder.mDate.setText(callDetails.get(position).getDate().replace("_", "/"));
                loadView(name, holder, name2, holder.getAdapterPosition(), mCallDetails, n);
                break;
        }
    }


    private void loadView(String name, MyViewHolder holder, String name2, int adapterPosition, CallDetails mCallDetails, String number) {
        if (name != null && !name.equals("")) {
            holder.mName.setText(name);
            holder.mName.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            holder.mAvatar.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.man));
         } else {
            holder.mName.setText(name2);
            holder.mName.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.mAvatar.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_grey));
            holder.mAvatar.setText(String.valueOf(name2.charAt(0)));
         }
        holder.mNumber.setText(callDetails.get(adapterPosition).getNum());
        holder.mTime.setText(callDetails.get(adapterPosition).getTime());
        holder.mCallType.setImageResource(getImageType(mCallDetails));
        holder.mName.setText(holder.mName.getText() + " (" + String.format("%02d min, %02d sec", TimeUnit.MILLISECONDS.toMinutes(callDetails.get(adapterPosition).getDuration()), TimeUnit.MILLISECONDS.toSeconds(callDetails.get(adapterPosition).getDuration()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(callDetails.get(adapterPosition).getDuration()))) + ")");
    }

    private int getImageType(CallDetails mCallDetails) {
        switch (mCallDetails.getCallType()) {
            case Constants.CAL_TYPE_MISSED_CALL:
                return R.drawable.ic_phone_missed_black_24dp;
            case Constants.CAL_TYPE_OUT_GOING_CALL_START:
            case Constants.CAL_TYPE_OUT_GOING_CALL_END:
                return R.drawable.ic_call_made_black_24dp;
            case Constants.CAL_TYPE_INCOMING_CALL_START:
            case Constants.CAL_TYPE_INCOMING_CALL_END:
                return R.drawable.ic_call_received_black_24dp;

        }
        return R.drawable.ic_phone_black_24dp;
    }

    public int getItemViewType(int position) {
        CallDetails cd = callDetails.get(position);
        String dt = cd.getDate();
        Log.d("Adapter", "getItemViewType: " + dt);
        Log.d("Adapter", "getItemViewType: " + pref.getString("mDate", ""));

        try {
            if (position != 0 && cd.getDate().equalsIgnoreCase(callDetails.get(position - 1).getDate())) {
                Log.d("Adapter", "getItemViewType: in if condition" + pref.getString("mDate", ""));
                return 0;
            } else {
                Log.d("Adapter", "getItemViewType: in else condition" + pref.getString("mDate", ""));
                return 2;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return callDetails.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mNumber, mTime, mDate, mName, mAvatar;
        ImageView mCallType;
        View mSeparator;

        MyViewHolder(View itemView) {
            super(itemView);
            mSeparator = itemView.findViewById(R.id.item_list_separator);
            mDate = itemView.findViewById(R.id.item_list_date1);
            mName = itemView.findViewById(R.id.item_list_name1);
            mNumber = itemView.findViewById(R.id.item_list_num);
            mTime = itemView.findViewById(R.id.item_list_time1);
            mAvatar = itemView.findViewById(R.id.item_list_avatar);
            mCallType = itemView.findViewById(R.id.item_list_call_type);
        }

        void bind(final CallDetails mCallDetials) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    Bundle b = new Bundle();
                    b.putString(Constants._mobile_number, mCallDetials.getNum()); //Your id
                    intent.putExtras(b); //Put your id to your next Intent
                    mContext.startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new DialogLongClick(mContext, mCallDetials).show();
                    return false;
                }
            });
        }
    }
}
