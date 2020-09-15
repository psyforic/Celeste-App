package com.celeste.celestedaylightapp.adapter;

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

public class DashboardModeAdapter extends RecyclerView.Adapter<DashboardModeAdapter.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;
    private UartConfiguration mConfiguration;
    private Context mContext;
    private boolean mEditMode;

    public DashboardModeAdapter(final UartConfiguration configuration, Context context, OnItemClickListener onItemClickListener) {
        mConfiguration = configuration;
        mContext = context;
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setConfiguration(final UartConfiguration configuration) {
        mConfiguration = configuration;

        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Command obj, int position);
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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_dashboard_mode, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        // Update image
        TypedArray m_icons = mContext.getApplicationContext().getResources().obtainTypedArray(R.array.default_modes_icons);
        int[] myIcons={R.drawable.ic_automatic,R.drawable.ic_sunny,R.drawable.ic_happy,R.drawable.ic_night};
        String[] names = {"Sunrise", "Mid-Morning", "Mid-Day", "Sun Set", "Therapy", "Off"};
        final Command command = (Command) getItem(position);
        final boolean active = command != null && command.isActive();
        if (active) {
            int icon = command.getIconIndex();
            viewHolder.image.setImageResource(myIcons[position]);
            viewHolder.mode.setText(names[position]);

        } else
            viewHolder.image.setImageDrawable(null);
        viewHolder.card_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(command, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mConfiguration != null ? mConfiguration.getCommands().length - 5 : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView mode;
        public View card_parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.mode_icon);
            mode = (TextView) itemView.findViewById(R.id.mode_name);
            card_parent = (View) itemView.findViewById(R.id.lyt_inner_parent);
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            card_parent.setOnClickListener(onClickListener);
        }
    }

    public void setEditMode(final boolean editMode) {
        mEditMode = editMode;
        notifyDataSetChanged();
    }
}
