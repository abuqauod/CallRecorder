package com.call.recorder.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
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
import android.widget.Toast;

import com.call.recorder.R;
import com.call.recorder.helper.CommonMethods;
import com.call.recorder.helper.Constants;
import com.call.recorder.ui.dialogs.DialogLongClick;
import com.call.recorder.ui.models.CallDetails;

import java.io.File;
import java.util.List;

import static android.support.v4.content.FileProvider.getUriForFile;

/**
 * Created by VS00481543 on 03-11-2017.
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
             /*case 1:
                View v2 = mLayoutInflater.inflate(R.layout.record_noname_list, parent, false);
                viewHolder = new MyViewHolder(v2);
                break;*/
            case 2:
                View v3 = mLayoutInflater.inflate(R.layout.date_layout, parent, false);
                return new MyViewHolder(v3);
             /*case 3:
                View v4 = mLayoutInflater.inflate(R.layout.date_noname_layout, parent, false);
                viewHolder = new MyViewHolder(v4);
                break;*/
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.MyViewHolder holder, int position) {

        CallDetails mCallDetials = callDetails.get(position);
        String n = mCallDetials.getNum();
        String name = new CommonMethods().getContactName(n, mContext);
        String name2 = mContext.getString(R.string.unsaved);
        Log.d("Names", "onBindViewHolder: " + name);
        holder.bind(mCallDetials);
        holder.mSeparator.setVisibility(position == callDetails.size() - 1 ? View.GONE : View.VISIBLE);

        switch (getItemViewType(position)) {
            case 0:
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
                holder.mNumber.setText(callDetails.get(position).getNum());
                holder.mTime.setText(callDetails.get(position).getTime());
                holder.mCallType.setImageResource(getImageType(mCallDetials));
                break;
            /*case 1:
                holder.mNumber.setText(callDetails.get(position).getNum());
                holder.mTime.setText(callDetails.get(position).getTime());
                break;*/
            case 2:
                holder.mDate.setText(callDetails.get(position).getDate().replace("_", "/"));
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
                holder.mNumber.setText(callDetails.get(position).getNum());
                holder.mTime.setText(callDetails.get(position).getTime());
                holder.mCallType.setImageResource(getImageType(mCallDetials));
                break;
            /*case 3:
                holder.mDate.setText(callDetails.get(position).getDate());
                holder.mNumber.setText(callDetails.get(position).getNum());
                holder.mTime.setText(callDetails.get(position).getTime());
                break;*/
        }
    }

    public int getItemViewType(int position) {
        CallDetails cd = callDetails.get(position);
        String dt = cd.getDate();
        Log.d("Adapter", "getItemViewType: " + dt);
        Log.d("Adapter", "getItemViewType: " + pref.getString("mDate", ""));
        // String checkDate=pref.getString("mDate","");

        try {
            String checkDate = "";
            if (position != 0 && cd.getDate().equalsIgnoreCase(callDetails.get(position - 1).getDate())) {
                checkDate = dt;
                //pref.edit().putString("mDate",dt).apply();
                Log.d("Adapter", "getItemViewType: in if condition" + pref.getString("mDate", ""));
                return 0;
                /*if(name1!=null && !name1.equals(""))
                    return 0;
                else
                    return 1;*/
            } else {
                checkDate = dt;
                //pref.edit().putString("mDate",dt).apply();
                Log.d("Adapter", "getItemViewType: in else condition" + pref.getString("mDate", ""));
               /* if(name1!=null && !name1.equals(""))
                    return 2;
                else
                    return 3;*/
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

    private int getImageType(CallDetails mCallDetails) {
        switch (mCallDetails.getCallType()) {
            case Constants.CAL_TYPE_MISSED_CALL:
                return R.drawable.ic_phone_missed_black_24dp;
            case Constants.CAL_TYPE_OUT_GOING_CALL:
                return R.drawable.ic_call_made_black_24dp;
            case Constants.CAL_TYPE_INCOMING_CALL:
                return R.drawable.ic_call_received_black_24dp;

        }
        return R.drawable.ic_phone_black_24dp;
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

                    Toast.makeText(mContext, "Clicked on " + mCallDetials.getNum(), Toast.LENGTH_SHORT).show();

                    CallDetails callDetails = new CallDetails();
                    callDetails.setSerial(pref.getInt("serialNumData", 1));
                    callDetails.setNum(mCallDetials.getNum());
                    callDetails.setCallType(mCallDetials.getCallType());
                    callDetails.setTime(CommonMethods.formatTime(new CommonMethods().getTIme()));
                    callDetails.setDate(new CommonMethods().getDate());

                    String path = (Environment.getExternalStorageDirectory() + "") + Constants._file_location + mCallDetials.getDate() + "/" + mCallDetials.getNum() + "_" + mCallDetials.getTime() + Constants._file_format;
                    Log.d("path", "onClick: " + path);
                    //                    Uri uri = Uri.parse(path);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    File file = new File(path);
                    //                    intent.setDataAndType(Uri.fromFile(file), "audio/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(getUriForFile(mContext, Constants.PACKAGE_NAME, file), "audio/*");
                    mContext.startActivity(intent);

                    pref.edit().putBoolean("pauseStateVLC", true).apply();

                    /*FileInputStream fis=null;
                    MediaPlayer mp=new MediaPlayer();
                    try {
                        fis=new FileInputStream(path);
                        mp.setDataSource(fis.getFD());
                        fis.close();
                        mp.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.stop();
                        }
                    });*/
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
