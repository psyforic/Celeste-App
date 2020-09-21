package com.celeste.celestedaylightapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.UARTInterface;
import com.celeste.celestedaylightapp.activity.MainActivity;
import com.celeste.celestedaylightapp.adapter.DashboardModeAdapter;
import com.celeste.celestedaylightapp.adapter.IconLabelAdapter;
import com.celeste.celestedaylightapp.utils.Tools;
import com.celeste.celestedaylightapp.database.DatabaseHelper;
import com.celeste.celestedaylightapp.domain.Command;
import com.celeste.celestedaylightapp.domain.UartConfiguration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skumar.flexibleciruclarseekbar.CircularSeekBar;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.HyphenStyle;

import java.util.Objects;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Frag_Dashboard extends Fragment implements MainActivity.ConfigurationListener{

    private View view;
    private static final float multiplier = 100f;

    private CircularSeekBar mCircularSeekBar;
    private UartConfiguration mConfiguration;

    private TextView mSeekBarValue;

    //    @BindView(R.id.iconRecycler)
//    RecyclerView iconRecycler;
    private DashboardModeAdapter mAdapter;
    public static final String ACTIVEMODE = "ACTIVEMODE";
    @BindView(R.id.text_mode_name)
   TextView selectedMode;
    private FloatingActionButton fabSwitch;
   // private LineChartView lineChartView;
    private IconLabelAdapter iconAdapter;
    private static final String UTILS_CATEGORY = "com.celeste.celestedaylghtapp.UTILS";
    private static final String MDEVICE_NAME = "mDeviceNameView";
    private final static String TAG = "UARTControlFragment";
    private final static String SIS_EDIT_MODE = "sis_edit_mode";
    private final static String PREFS_CONFIGURATION = "configuration_id";

    private boolean mEditMode;
    private SharedPreferences mPreferences;
    String deviceName = "No Device";

    public Frag_Dashboard() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mEditMode = savedInstanceState.getBoolean(SIS_EDIT_MODE);
            selectedMode.setText(savedInstanceState.getString(ACTIVEMODE));
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_frag__dashboard, container, false);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.OnSharedPreferenceChangeListener listener = (prefs, key) -> Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(prefs.getString(MDEVICE_NAME, "Not Connected"));
            selectedMode.setText(mPreferences.getString(ACTIVEMODE, "No Mode Selected"));
        });
        mPreferences.registerOnSharedPreferenceChangeListener(listener);

        initCircularSeekBar();

        setCircularSeekBarListener();
        setupUI();
        return view;
    }
    @SuppressLint("SetTextI18n")
    private void initCircularSeekBar() {
        mCircularSeekBar = view.findViewById(R.id.mCircularSeekBar);
        mSeekBarValue = view.findViewById(R.id.mSeekBarValue);
        mCircularSeekBar.setDrawMarkings(true);
        mCircularSeekBar.setDotMarkers(false);
        mCircularSeekBar.setShader();
        mCircularSeekBar.getDotMarkers();
        mCircularSeekBar.setProgressColor(R.color.colorPrimary);
        mCircularSeekBar.setRoundedEdges(true);
        mCircularSeekBar.setIsGradient(true);
        mCircularSeekBar.setPopup(false);
        mCircularSeekBar.setSweepAngle(270);
        mCircularSeekBar.setArcThickness(10);
        mCircularSeekBar.setArcRotation(225);
        mCircularSeekBar.setMin(0);
        mCircularSeekBar.setMax(70);
        float progressValue = 10f;
        mCircularSeekBar.setProgress(progressValue);
        mCircularSeekBar.setIncreaseCenterNeedle(20);
        mCircularSeekBar.setValueStep(2);
        mCircularSeekBar.setNeedleFrequency(0.5f);
        mCircularSeekBar.setNeedleDistanceFromCenter(32);
        mCircularSeekBar.setNeedleLengthInDP(12);
        mCircularSeekBar.setIncreaseCenterNeedle(24);
        mCircularSeekBar.setNeedleThickness(1f);
        mCircularSeekBar.setHeightForPopupFromThumb(10);
        mSeekBarValue.setText(String.valueOf(progressValue * multiplier) + "K");

    }
    private void setCircularSeekBarListener() {
        mCircularSeekBar.setOnCircularSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean b) {
                mCircularSeekBar.setMinimumAndMaximumNeedleScale(progress - 2.5f, progress + 2.5f);
                mSeekBarValue.setText(String.valueOf(progress * multiplier) + "K");

                final UARTInterface uart = (UARTInterface) getActivity();

                if (progress == 27f) {
                    selectedMode.setText("Sunrise");
                    uart.send("<<100");
                } else if (progress == 30f) {
                    selectedMode.setText("Mid-Morning");
                    uart.send("<<200");
                } else if (progress == 45f) {
                    selectedMode.setText("Mid-Day");
                    uart.send("<<300");
                } else if (progress == 65f) {
                    uart.send("<<400");
                } else if (progress > 65f) {
                    selectedMode.setText("Therapy");
                    uart.send("<<f00");
                }
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar circularSeekBar) {

            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar circularSeekBar) {

            }
        });
    }
    private void setupUI() {
        selectedMode = view.findViewById(R.id.text_mode_name);
        SharedPreferences.OnSharedPreferenceChangeListener listener = (prefs, key) -> getActivity().runOnUiThread(() -> {
            selectedMode.setText(mPreferences.getString(ACTIVEMODE, "No Mode Selected"));
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), Tools.getGridSpanCount(getActivity()));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        try {
            DatabaseHelper mDatabaseHelper = new DatabaseHelper(getContext());
            long id = mPreferences.getLong(PREFS_CONFIGURATION, 0);
            final String xml = mDatabaseHelper.getConfiguration(id);
            final Format format = new Format(new HyphenStyle());
            final Serializer serializer = new Persister(format);
            mConfiguration = serializer.read(UartConfiguration.class, xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAdapter = new DashboardModeAdapter(mConfiguration, getContext(), new DashboardModeAdapter.OnItemClickListener() {
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
                String[] names = {"Sunrise", "Mid-Morning", "Mid-Day", "Sun Set", "Therapy", "Off"};
                selectedMode.setText(names[position]);
                switch (position) {
                    case 0:
                        mCircularSeekBar.setProgress(27f);
                        break;
                    case 1:
                        mCircularSeekBar.setProgress(30f);
                        break;
                    case 2:
                        mCircularSeekBar.setProgress(45f);
                        break;
                    case 3:
                        mCircularSeekBar.setProgress(65f);
                        break;
                }

                mPreferences.edit().putString(ACTIVEMODE, selectedMode.getText().toString()).apply();
                Objects.requireNonNull(uart).send(text);
            }
        });
        recyclerView.setAdapter(mAdapter);
       fabSwitch = view.findViewById(R.id.fab_switch);

    }
    @Override
    public void onResume() {
        super.onResume();
        selectedMode.setText(mPreferences.getString(ACTIVEMODE, "No Mode Selected"));
    }

    @Override
    public void onPause() {
        super.onPause();
        mPreferences.edit().putString(ACTIVEMODE, selectedMode.getText().toString()).apply();
    }

   @Override
   public void onAttach(Context context) {
       super.onAttach(context);
       try {
           mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
           ((MainActivity) context).setConfigurationListener(this);
       } catch (final ClassCastException e) {
           Log.e(TAG, "The parent activity must implement EditModeListener");
       }
   }
@Override
public void onDestroy() {
    super.onDestroy();
    ((MainActivity) Objects.requireNonNull(getActivity())).setConfigurationListener(null);
    mPreferences.edit().putString(ACTIVEMODE, "No Mode Selected").apply();
}

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(SIS_EDIT_MODE, mEditMode);
        outState.putString(ACTIVEMODE, selectedMode.getText().toString());
    }

    @Override
    public void onConfigurationModified() {

    }

    @Override
    public void onConfigurationChanged(UartConfiguration configuration) {
        mConfiguration = configuration;
        mAdapter.setConfiguration(configuration);
    }
}
