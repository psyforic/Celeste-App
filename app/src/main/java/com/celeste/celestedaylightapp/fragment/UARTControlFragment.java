package com.celeste.celestedaylightapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.UARTInterface;
import com.celeste.celestedaylightapp.activity.MainActivity;
import com.celeste.celestedaylightapp.adapter.ModeListAdapter;
import com.celeste.celestedaylightapp.utils.Tools;
import com.celeste.celestedaylightapp.database.DatabaseHelper;
import com.celeste.celestedaylightapp.domain.Command;
import com.celeste.celestedaylightapp.domain.UartConfiguration;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.HyphenStyle;

public class UARTControlFragment extends Fragment implements MainActivity.ConfigurationListener {
    private final static String TAG = "UARTControlFragment";
    private final static String SIS_EDIT_MODE = "sis_edit_mode";
    private final static String PREFS_CONFIGURATION = "configuration_id";
    private UartConfiguration mConfiguration;
    private ModeListAdapter mAdapter;
    private boolean mEditMode;
    private SharedPreferences mPreferences;

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        try {
            mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            ((MainActivity) context).setConfigurationListener(this);
        } catch (final ClassCastException e) {
            Log.e(TAG, "The parent com.celeste.celestedaylightapp.activity must implement EditModeListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mEditMode = savedInstanceState.getBoolean(SIS_EDIT_MODE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) getActivity()).setConfigurationListener(null);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(SIS_EDIT_MODE, mEditMode);
    }

    @Override
    public void onConfigurationModified() {

    }

    @Override
    public void onConfigurationChanged(final UartConfiguration configuration) {
        mConfiguration = configuration;
        mAdapter.setConfiguration(configuration);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_uart_control, container, false);
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), Tools.getGridSpanCount(getActivity()));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        //mAdapter.setOnItemClickListener(this);
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(getContext());
        long id = mPreferences.getLong(PREFS_CONFIGURATION, 0);
        final String xml = mDatabaseHelper.getConfiguration(id);
        final Format format = new Format(new HyphenStyle());
        final Serializer serializer = new Persister(format);
        try {
            mConfiguration = serializer.read(UartConfiguration.class, xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAdapter = new ModeListAdapter(mConfiguration, getContext(), new ModeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Command obj, int position) {
                final Command.Eol eol = obj.getEol();
                String text = obj.getCommand();
                if (text == null)
                    text = "";
                switch (eol) {
                    case CR_LF:
                        text = text.replaceAll("\n", "\r\n");
                        break;
                    case CR:
                        text = text.replaceAll("\n", "\r");
                        break;
                }
                final UARTInterface uart = (UARTInterface) getActivity();
                Toast.makeText(getContext(), "Clicked " + obj.getCommandName(), Toast.LENGTH_LONG).show();
                uart.send(text);
            }
        });
        recyclerView.setAdapter(mAdapter);

        return view;
    }
/*
    @Override
    public void setEditMode(final boolean editMode) {
        mEditMode = editMode;
        mAdapter.setEditMode(mEditMode);
    }*/

}
