package com.celeste.celestedaylightapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.model.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private List<User> users = new ArrayList<>();
    private Context ctx;

    public UsersAdapter(Context context, List<User> usersList) {
        this.users = usersList;
        ctx = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_people, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user = users.get(position);
        holder.name.setText(user.getFirstName());
        holder.email.setText(user.getUserEmail());
        holder.image.setImageDrawable(user.imageDrw);
//       Glide.with(ctx)
//               .load(imagePath)
//               .placeholder(R.drawable.unknown_avatar)
//               .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CircleImageView image;
        public TextView name;
        public TextView email;
        public LinearLayout lyt_parent;

        public ViewHolder(View v) {
            super(v);
            image = (CircleImageView) v.findViewById(R.id.profile_image);
            name = (TextView) v.findViewById(R.id.tvName);
            email = (TextView) v.findViewById(R.id.tvEmail);
            lyt_parent = (LinearLayout) v.findViewById(R.id.lyt_parent);
        }
    }
}