package com.celeste.celestedaylightapp.data;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;


import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.model.Icon;
import com.celeste.celestedaylightapp.model.Mode;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final String CREDENTIALS = "Credentials";
    public static Resources getStrRes(Context context) {
        return context.getResources();
    }

    public static List<Mode> getDefaultModesData(Context context) {
        List<Mode> items = new ArrayList<>();

        String m_names[] = context.getResources().getStringArray(R.array.default_modes_names);
        TypedArray m_icons = context.getResources().obtainTypedArray(R.array.default_modes_icons);
        String m_commands[] = context.getResources().getStringArray(R.array.default_modes_commands);
        TypedArray m_sections = context.getResources().obtainTypedArray(R.array.default_modes_sectioned);


        for (int i = 0; i < m_names.length; i++) {
            Mode mode = new Mode(m_names[i], m_commands[i], m_icons.getResourceId(i, -1), m_sections.getBoolean(i, false));
            items.add(mode);
        }
        return items;

    }

    public static List<Icon> getDeafaultModeIcon(Context context) {
        List<Icon> items = new ArrayList<>();
        TypedArray m_icons = context.getResources().obtainTypedArray(R.array.default_modes_icons);

        for (int i = 0; i < m_icons.length(); i++) {
            Icon mode = new Icon(m_icons.getResourceId(i, 0));
            items.add(mode);
        }
        return items;
    }
}
