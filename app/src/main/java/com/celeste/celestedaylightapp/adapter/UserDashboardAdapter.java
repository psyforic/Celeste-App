package com.celeste.celestedaylightapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.domain.Command;
import com.celeste.celestedaylightapp.model.modes.Mode;

import java.util.List;

public class UserDashboardAdapter extends RecyclerView.Adapter<UserDashboardAdapter.ViewHolder> {

    private UserDashboardAdapter.OnItemClickListener mOnItemClickListener;
    private List<Mode> mConfiguration;
    private Context mContext;
    Mode userModes;
    //  private List<Mode> modes;

    public UserDashboardAdapter(final List<Mode> configuration, Context context, UserDashboardAdapter.OnItemClickListener onItemClickListener) {
        mConfiguration = configuration;
        mContext = context;
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setConfiguration(final List<Mode> configuration) {
        mConfiguration = configuration;
        notifyDataSetChanged();
    }

    private Object getItem(final int position) {
        return mConfiguration.get(position);
        //  return mConfiguration.getCommands()[position];
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_dashboard_mode, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        userModes = mConfiguration.get(position);
        String[] names = {"Sunrise", "Mid-Morning", "Mid-Day", "Sun Set", "Therapy", "Off"};
        int[] myIcons = {R.drawable.ic_automatic, R.drawable.ic_sunny, R.drawable.ic_happy, R.drawable.ic_night, R.drawable.ic_hospital, R.drawable.ic_do_not_disturb};
        final Command command = new Command();
        command.setCommandName(userModes.getName());
        command.setCommand(userModes.getCommand());
        command.setActive(true);
        final boolean active = command != null && command.isActive();
        if (active) {
            int icon = command.getIconIndex();
            holder.image.setImageResource(myIcons[icon]);
            holder.mode.setText(userModes.getName());
        } else
            holder.image.setImageDrawable(null);
        holder.card_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(userModes, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mConfiguration.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Mode obj, int position);
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
}
