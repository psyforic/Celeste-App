package com.celeste.celestedaylightapp.fragment;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.activity.AutomaticModesActivity;

import java.util.Objects;

public class SettingsFragment extends Fragment {
    private BluetoothAdapter mBluetoothAdapter;
    private View view;
    private LinearLayout lytAutomaticModes, lytSyncData;
    private SwitchCompat switchCompatBluetooth, switchCompatConnect;
    private boolean isTouched = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        setupUI();
        bindClickListeners();
        return view;
    }

    private void setupUI() {
        lytAutomaticModes = view.findViewById(R.id.lyt_automatic_modes);
        lytSyncData = view.findViewById(R.id.lyt_sync);
        switchCompatBluetooth = view.findViewById(R.id.switch_compat_bluetooth);
        switchCompatConnect = view.findViewById(R.id.switch_compat_connect);
        if (mBluetoothAdapter.isEnabled()) {
            switchCompatBluetooth.setChecked(true);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void bindClickListeners() {
        lytAutomaticModes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AutomaticModesActivity.class));
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
            }
        });

        lytSyncData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncData();
            }
        });
        switchCompatBluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    mBluetoothAdapter.disable();
                    switchCompatConnect.setChecked(false);
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.enable();
                    }
                }
                switchBluetooth();
            }
        });
        switchCompatConnect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isTouched = true;
                return false;
            }
        });
        switchCompatConnect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isTouched) {
                    isTouched = false;
                    if (switchCompatBluetooth.isChecked()) {
                        switchCompatConnect.setChecked(true);
                    } else {
                        switchCompatConnect.setChecked(false);
                    }
                }
            }
        });
    }

    private void syncData() {
        // TODO: Implement Method
    }

    private void switchBluetooth() {
    }
}
