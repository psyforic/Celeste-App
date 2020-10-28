package com.celeste.celestedaylightapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.model.modes.UserModeModel;

import java.util.ArrayList;
import java.util.List;

public class UserModesAdapter extends RecyclerView.Adapter<UserModesAdapter.ViewHolder> {
    private List<UserModeModel> modes;
    private Context context;
    private ArrayList<String> mode_id, description, command, icon, start_time, end_time;
    private UserModeModel userModes;

    public UserModesAdapter(Context ctx, List<UserModeModel> modeList) {
        this.context = ctx;
        this.modes = modeList;
    }

    public UserModesAdapter(Context ctx, ArrayList start_time, ArrayList end_time, ArrayList description, ArrayList command, ArrayList icon, ArrayList mode_id) {
        this.context = ctx;
        this.mode_id = mode_id;
        this.description = description;
        this.command = command;
        this.icon = icon;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserModesAdapter.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_mode, parent, false);
        vh = new UserModesAdapter.ViewHolder(v);
        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        userModes = modes.get(position);
//        String[] names = {"Sunrise", "Mid-Morning", "Mid-Day", "Sun Set", "Therapy", "Off"};
//        String[] times = {"04:00-6:45", "6:45-11:00", "12:00-13:00", "17:30-18:30", "", ""};
        int[] myIcons = {R.drawable.ic_automatic, R.drawable.ic_sunny, R.drawable.ic_happy, R.drawable.ic_night, R.drawable.ic_hospital};

        //  holder.mode.setText(String.valueOf(description.get(position)));
        //  holder.time.setText(start_time.get(position) + "-" + end_time.get(position));
        holder.mode.setText(userModes.getMode().getName());
        holder.time.setText(userModes.getMode().getStartTime() + " - " + userModes.getMode().getEndTime());
        holder.image.setImageResource(myIcons[position]);
        //   holder.time.setText(modes.get(position).getStartTime() + " - " + modes.get(position).getEndTime());
        /*
         Set images using a switch,check mode names and assign images appropriately
        */

    }

    @Override
    public int getItemCount() {
        if (modes != null) {
            return modes.size();
        } else if (mode_id != null) {
            return mode_id.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView mode;
        public TextView time;
        public MaterialRippleLayout lyt_parent;

        public ViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.mode_image);
            mode = v.findViewById(R.id.mode_name);
            time = v.findViewById(R.id.time);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }
    }
}
