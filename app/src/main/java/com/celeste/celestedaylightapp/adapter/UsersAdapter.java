package com.celeste.celestedaylightapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.model.user.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private List<UserModel> users = new ArrayList<>();
    private Context ctx;


    public UsersAdapter(Context context, List<UserModel> usersList) {
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

        UserModel user = users.get(position);
        holder.name.setText(users.get(position).getName() + " " + users.get(position).getSurname());
        holder.email.setText(users.get(position).getEmailAddress());
        holder.image.setImageResource(R.drawable.ic_person);
//        Picasso.get().load(R.drawable.ic_profile).resize(50, 50)
//                .transform(new CircleTransform())
//                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        if (users != null) {

            return users.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView image;
        public TextView name;
        public TextView email;
        public LinearLayout lyt_parent;

        public ViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.profile_image);
            name = (TextView) v.findViewById(R.id.tvName);
            email = (TextView) v.findViewById(R.id.tvEmail);
            lyt_parent = (LinearLayout) v.findViewById(R.id.lyt_parent);
        }
    }
}
