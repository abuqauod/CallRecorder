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
import com.call.recorder.ui.models.CallDetails;

import java.io.File;
import java.util.List;

import static android.support.v4.content.FileProvider.getUriForFile;

/**
 * Created by VS00481543 on 03-11-2017.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.MyViewHolder> {

    private List<CallDetails> callDetails;
    private Context context;
    private SharedPreferences pref;

    public RecordAdapter(List<CallDetails> callDetails, Context context) {
        this.callDetails = callDetails;
        this.context = context;
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

        CallDetails cd1 = callDetails.get(position);
        String n = cd1.getNum();
        String name = new CommonMethods().getContactName(n, context);
        String name2 = context.getString(R.string.unsaved);
        Log.d("Names", "onBindViewHolder: " + name);
        holder.bind(cd1.getDate1(), cd1.getNum(), cd1.getTime1());
        switch (getItemViewType(position)) {
            case 0:
                if (name != null && !name.equals("")) {
                    holder.mName.setText(name);
                    holder.mName.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                    holder.mAvatar.setBackground(ContextCompat.getDrawable(context, R.mipmap.man));
                } else {
                    holder.mName.setText(name2);
                    holder.mName.setTextColor(context.getResources().getColor(R.color.red));
                    holder.mAvatar.setBackground(ContextCompat.getDrawable(context, R.drawable.circle_grey));
                    holder.mAvatar.setText(String.valueOf(name2.charAt(0)));
                }
                holder.mNumber.setText(callDetails.get(position).getNum());
                holder.mTime.setText(callDetails.get(position).getTime1());
                break;
            /*case 1:
                holder.mNumber.setText(callDetails.get(position).getNum());
                holder.mTime.setText(callDetails.get(position).getTime1());
                break;*/
            case 2:
                holder.mDate.setText(callDetails.get(position).getDate1().replace("_", "/"));
                if (name != null && !name.equals("")) {
                    holder.mName.setText(name);
                    holder.mName.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                    holder.mAvatar.setBackground(ContextCompat.getDrawable(context, R.mipmap.man));
                } else {
                    holder.mName.setText(name2);
                    holder.mName.setTextColor(context.getResources().getColor(R.color.red));
                    holder.mAvatar.setBackground(ContextCompat.getDrawable(context, R.drawable.circle_grey));
                    holder.mAvatar.setText(String.valueOf(name2.charAt(0)));
                }
                holder.mNumber.setText(callDetails.get(position).getNum());
                holder.mTime.setText(callDetails.get(position).getTime1());
                break;
            /*case 3:
                holder.mDate.setText(callDetails.get(position).getDate1());
                holder.mNumber.setText(callDetails.get(position).getNum());
                holder.mTime.setText(callDetails.get(position).getTime1());
                break;*/
        }
    }

    public int getItemViewType(int position) {
        CallDetails cd = callDetails.get(position);
        String dt = cd.getDate1();
        Log.d("Adapter", "getItemViewType: " + dt);
        Log.d("Adapter", "getItemViewType: " + pref.getString("mDate", ""));
        // String checkDate=pref.getString("mDate","");

        try {
            String checkDate = "";
            if (position != 0 && cd.getDate1().equalsIgnoreCase(callDetails.get(position - 1).getDate1())) {
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

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mNumber, mTime, mDate, mName, mAvatar;
        ImageView mCallType;

        MyViewHolder(View itemView) {
            super(itemView);
            mDate = itemView.findViewById(R.id.item_list_date1);
            mName = itemView.findViewById(R.id.item_list_name1);
            mNumber = itemView.findViewById(R.id.item_list_num);
            mTime = itemView.findViewById(R.id.item_list_time1);
            mAvatar = itemView.findViewById(R.id.item_list_avatar);
            mCallType = itemView.findViewById(R.id.item_list_call_type);
        }

        void bind(final String dates, final String number, final String times) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context, "Clicked on " + number, Toast.LENGTH_SHORT).show();

                    String path = (Environment.getExternalStorageDirectory() + "") + Constants._file_location + dates + "/" + number + "_" + times + Constants._file_format;
                    Log.d("path", "onClick: " + path);
                    //                    Uri uri = Uri.parse(path);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    File file = new File(path);
                    //                    intent.setDataAndType(Uri.fromFile(file), "audio/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(getUriForFile(context, Constants.PACKAGE_NAME, file), "audio/*");
                    context.startActivity(intent);

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
        }
    }
}
