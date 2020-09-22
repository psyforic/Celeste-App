package com.celeste.celestedaylightapp.adapter;

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
import com.celeste.celestedaylightapp.model.modes.Mode;

import java.util.List;

public class UserModesAdapter extends RecyclerView.Adapter<UserModesAdapter.ViewHolder> {
    private List<Mode> modes;
    private Context context;

    public UserModesAdapter(Context ctx, List<Mode> modeList) {
        this.context = ctx;
        this.modes = modeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserModesAdapter.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_mode, parent, false);
        vh = new UserModesAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mode userModes = modes.get(position);
        String[] names = {"Sunrise", "Mid-Morning", "Mid-Day", "Sun Set", "Therapy", "Off"};
        String[] times = {"04:00-6:45", "6:45-11:00", "12:00-13:00", "17:30-18:30", "", ""};
        int[] myIcons = {R.drawable.ic_automatic, R.drawable.ic_sunny, R.drawable.ic_happy, R.drawable.ic_night, R.drawable.ic_hospital, R.drawable.ic_do_not_disturb};
        holder.mode.setText(modes.get(position).getName());
        // holder.time.setText(modes.get(position).getStartTime() + " - " + modes.get(position).getEndTime());
//        holder.time.setText(times[position]);
//        holder.image.setImageResource(myIcons[position]);
    }

    @Override
    public int getItemCount() {
        if (modes != null) {
            return modes.size();
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