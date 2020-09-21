package com.celeste.celestedaylightapp.adapter;

import android.content.Context;
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
import com.celeste.celestedaylightapp.model.Mode;

import java.util.List;

public class CustomModeAdapter  extends PagerAdapter {
    private Context context;
    private List<Mode> items;
    private OnItemClickListener onItemClickListener;

    private interface OnItemClickListener {
        void onItemClick(View view, Mode obj);
    }

    public void setOnclickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CustomModeAdapter(Context context, List<Mode> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (LinearLayout) o;
    }

    public Mode getItem(int pos) {
        return items.get(pos);

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final Mode mode = items.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_card_slider, container, false);

        ImageView imageView = view.findViewById(R.id.image);
        MaterialRippleLayout lytParent = view.findViewById(R.id.lyt_parent);
        Tools.displayImageOriginal(context, imageView, mode.getDrawableResource());
        lytParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, mode);
                }
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }

}
