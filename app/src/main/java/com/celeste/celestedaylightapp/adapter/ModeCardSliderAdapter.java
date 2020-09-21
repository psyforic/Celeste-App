package com.celeste.celestedaylightapp.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.balysv.materialripple.MaterialRippleLayout;
import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.utils.Tools;
import com.celeste.celestedaylightapp.domain.Command;
import com.celeste.celestedaylightapp.domain.UartConfiguration;
import com.celeste.celestedaylightapp.model.Mode;


import java.util.List;

public class ModeCardSliderAdapter extends PagerAdapter {
    private Context context;
    private List<Mode> items;
    private UartConfiguration mConfiguration;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Command obj, int position);
    }

    public void setOnclickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ModeCardSliderAdapter(final UartConfiguration configuration, Context context, OnItemClickListener onItemClickListener) {
        mConfiguration = configuration;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }
    public void setConfiguration(final UartConfiguration configuration) {
        mConfiguration = configuration;

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mConfiguration != null ? mConfiguration.getCommands().length - 3 : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (LinearLayout) o;
    }

    public Object getItem(final int position) {
        return mConfiguration.getCommands()[position];
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
       // final Mode mode = items.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_card_slider, container, false);


        ImageView imageView = view.findViewById(R.id.image);
        MaterialRippleLayout lytParent = view.findViewById(R.id.lyt_parent);


        TypedArray m_icons = context.getApplicationContext().getResources().obtainTypedArray(R.array.default_modes_icons);
        final Command command = (Command) getItem(position);
        final boolean active = command != null && command.isActive();
        if (active) {
            int icon = command.getIconIndex();
            Tools.displayImageOriginal(context, imageView, m_icons.getResourceId(icon, 0));
            //viewHolder.image.setImageResource(m_icons.getResourceId(icon, 0));

        }
        //Tools.displayImageOriginal(context, imageView, mode.getDrawableResource());
        lytParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (onItemClickListener != null) {
                    //onItemClickListener.onItemClick(v, mode);
                    onItemClickListener.onItemClick(command, position);
                }
            }
        });


       /* viewHolder.card_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(command, position);
            }
        });*/

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }

}
