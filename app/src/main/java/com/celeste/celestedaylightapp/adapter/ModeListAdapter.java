package com.celeste.celestedaylightapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.domain.Command;
import com.celeste.celestedaylightapp.domain.UartConfiguration;
import com.celeste.celestedaylightapp.model.Mode;

import java.util.ArrayList;

public class ModeListAdapter extends RecyclerView.Adapter<ModeListAdapter.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;
    private UartConfiguration mConfiguration;
    private Context mContext;
    private boolean mEditMode;
    private ArrayList<Mode> modes;

    public ModeListAdapter(final UartConfiguration configuration, Context context, OnItemClickListener onItemClickListener) {
        mConfiguration = configuration;
        mContext = context;
        this.mOnItemClickListener = onItemClickListener;
    }

    public ModeListAdapter(ArrayList<Mode> modes) {
        this.modes = modes;
    }

    public void setConfiguration(final UartConfiguration configuration) {
        mConfiguration = configuration;

        notifyDataSetChanged();
    }

    public Object getItem(final int position) {
        return mConfiguration.getCommands()[position];
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_mode, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        // Update image
      //  Mode myMode = modes.get(position);
        @SuppressLint("Recycle") TypedArray m_icons = mContext.getApplicationContext().getResources().obtainTypedArray(R.array.default_modes_icons);
        String[] names = {"Sunrise", "Mid-Morning", "Mid-Day", "Sun Set", "Therapy", "Off"};
        String[] times = {"04:00-6:45", "6:45-11:00", "12:00-13:00", "17:30-18:30", "", ""};
        int[] myIcons={R.drawable.ic_automatic,R.drawable.ic_sunny,R.drawable.ic_happy,R.drawable.ic_night,R.drawable.ic_hospital,R.drawable.ic_do_not_disturb};
        final Command command = (Command) getItem(position);
        final boolean active = command != null && command.isActive();
        if (active) {
            int icon = command.getIconIndex();
            viewHolder.name.setText(names[position]);
            viewHolder.time.setText(times[position]);
            viewHolder.image.setImageResource(myIcons[position]);

        } else
            viewHolder.image.setImageDrawable(null);
        viewHolder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(command, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mConfiguration != null ? mConfiguration.getCommands().length - 3 : 0;
    }

    public void setEditMode(final boolean editMode) {
        mEditMode = editMode;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Command obj, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public TextView time;
        public View lyt_parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.mode_image);
            name = (TextView) itemView.findViewById(R.id.mode_name);
            time = (TextView) itemView.findViewById(R.id.time);
            lyt_parent = (View) itemView.findViewById(R.id.lyt_parent);
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            lyt_parent.setOnClickListener(onClickListener);
        }
    }
}
