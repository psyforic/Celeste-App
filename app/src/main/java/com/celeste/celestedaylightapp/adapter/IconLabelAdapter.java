package com.celeste.celestedaylightapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.model.Icon;
import com.squareup.picasso.Picasso;

import java.util.List;

public class IconLabelAdapter extends RecyclerView.Adapter<IconLabelAdapter.ViewHolder> {
    private Context context;
    private List<Icon> list;

    public IconLabelAdapter(Context context, List<Icon> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chart_label, null, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Icon icon = list.get(position);
        Picasso.get().load(icon.getDrawableResource())
                .placeholder(R.drawable.ic_automatic)
                .into(viewHolder.icon);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.mode_icon);
        }
    }
}
