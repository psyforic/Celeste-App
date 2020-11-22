package com.celeste.celestedaylightapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import com.celeste.celestedaylightapp.adapter.UserDashboardAdapter;
import com.celeste.celestedaylightapp.database.DatabaseHelper;
import com.celeste.celestedaylightapp.domain.Command;
import com.celeste.celestedaylightapp.domain.UartConfiguration;
import com.celeste.celestedaylightapp.model.modes.AddModeResponse;
import com.celeste.celestedaylightapp.model.modes.Mode;
import com.celeste.celestedaylightapp.model.modes.UserModeModel;
import com.celeste.celestedaylightapp.model.user.GetSingleUserResponse;
import com.celeste.celestedaylightapp.retrofit.Api;
import com.celeste.celestedaylightapp.retrofit.ApiClient;
import com.celeste.celestedaylightapp.utils.Constants;
import com.celeste.celestedaylightapp.utils.Tools;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iamhabib.easy_preference.EasyPreference;
import com.sdsmdg.tastytoast.TastyToast;
import com.skumar.flexibleciruclarseekbar.CircularSeekBar;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.HyphenStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Frag_Dashboard extends Fragment implements MainActivity.ConfigurationListener {
    public static final String ACTIVEMODE = "ACTIVEMODE";
    private static final float multiplier = 100f;
    private static final String UTILS_CATEGORY = "com.celeste.celestedaylghtapp.UTILS";
    private static final String MDEVICE_NAME = "mDeviceNameView";
    private final static String TAG = "UARTControlFragment";
    private final static String SIS_EDIT_MODE = "sis_edit_mode";
    private final static String PREFS_CONFIGURATION = "configuration_id";
    @BindView(R.id.text_mode_name)
    TextView selectedMode;
    ProgressBar progressBar;
    List<Mode> helperAllModes;
    private final String deviceName = "No Device";
    private final Api api = ApiClient.getInstance(getActivity()).create(Api.class);
    private int Id;
    private String tenantId;
    private final List<Mode> mode = new ArrayList<>();
    private View view;
    private CircularSeekBar mCircularSeekBar;
    private UartConfiguration mConfiguration;
    private TextView mSeekBarValue;
    private DashboardModeAdapter mAdapter;
    private UserDashboardAdapter modeAdapter;
    private FloatingActionButton fabSwitch;
    private IconLabelAdapter iconAdapter;
    private boolean mEditMode;
    private SharedPreferences mPreferences;
    private com.celeste.celestedaylightapp.sqllitedb.DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private List<UserModeModel> arrayList = new ArrayList<>();

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
        Id = EasyPreference.with(getActivity()).getInt(Constants.USERID, 0);
        tenantId = EasyPreference.with(getActivity()).getString(Constants.TENANT_ID, "");
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.OnSharedPreferenceChangeListener listener = (prefs, key) -> Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(prefs.getString(MDEVICE_NAME, "Not Connected"));
            selectedMode.setText(mPreferences.getString(ACTIVEMODE, "No Mode Selected"));
        });
        mPreferences.registerOnSharedPreferenceChangeListener(listener);
        dbHelper = new com.celeste.celestedaylightapp.sqllitedb.DatabaseHelper(getActivity());
        initCircularSeekBar();
        setCircularSeekBarListener();
        return view;
    }

    private void initCircularSeekBar() {
        mCircularSeekBar = view.findViewById(R.id.mCircularSeekBar);
        mSeekBarValue = view.findViewById(R.id.mSeekBarValue);
        mCircularSeekBar.setDrawMarkings(true);
        mCircularSeekBar.setDotMarkers(false);
        mCircularSeekBar.setProgressColor(R.color.colorPrimary);
        mCircularSeekBar.setRoundedEdges(true);
        mCircularSeekBar.setIsGradient(true);
        mCircularSeekBar.setPopup(false);
        mCircularSeekBar.setSweepAngle(270);
        mCircularSeekBar.setArcThickness(8);
        mCircularSeekBar.setArcRotation(225);
        mCircularSeekBar.setMin(0);
        mCircularSeekBar.setMax(70);
        //mCircularSeekBar.setEnabled(false);
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
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), Tools.getGridSpanCount(getActivity()));
        recyclerView.setLayoutManager(mLayoutManager);
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
        mAdapter = new DashboardModeAdapter(mConfiguration, getContext(), (obj, position) -> {
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
            uart.send(text);
        });
        recyclerView.setAdapter(mAdapter);
        fabSwitch = view.findViewById(R.id.fab_switch);
    }

    public void displayModes() {
        List<Mode> modes = dbHelper.getAllModes();
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), Tools.getGridSpanCount(getActivity()));
        recyclerView.setLayoutManager(layoutManager);
        UserDashboardAdapter modesAdapter = new UserDashboardAdapter(modes, getContext(), null);
        recyclerView.setAdapter(modesAdapter);
        // progressBar.setVisibility(View.GONE);
    }

    private void getAllModes() {
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), Tools.getGridSpanCount(getActivity()));
        recyclerView.setLayoutManager(layoutManager);
        Call<GetSingleUserResponse> call = api.getSingleUser(Id);
        call.enqueue(new Callback<GetSingleUserResponse>() {
            @Override
            public void onResponse(Call<GetSingleUserResponse> call, Response<GetSingleUserResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getResult() != null) {
                        arrayList = response.body().getResult().getUserModes();
                        if (arrayList.size() != 0) {
                            for (UserModeModel modes : arrayList) {
                                mode.add(modes.getMode());
                                //insertToDb(mode);
                            }
                            modeAdapter = new UserDashboardAdapter(mode, getContext(), new UserDashboardAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(Mode obj, int position) {
                                    Command comm = new Command();
                                    comm.setActive(true);
                                    comm.setEol(0);
                                    comm.setCommandName(obj.getName());
                                    final Command.Eol eol = comm.getEol();
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
                                    selectedMode.setText(obj.getName());
                                    //selectedMode.setText(names[position]);
                                    switch (position) {
                                        case 0:
                                            mCircularSeekBar.setProgress(27f);
                                            TastyToast.makeText(getActivity(), "Selected mode " + text, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                                            break;
                                        case 1:
                                            mCircularSeekBar.setProgress(30f);
                                            TastyToast.makeText(getActivity(), "Selected mode " + text, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                                            break;
                                        case 2:
                                            mCircularSeekBar.setProgress(45f);
                                            TastyToast.makeText(getActivity(), "Selected mode " + text, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                                            break;
                                        case 3:
                                            mCircularSeekBar.setProgress(65f);
                                            TastyToast.makeText(getActivity(), "Selected mode " + text, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                                            break;
                                    }
                                    mPreferences.edit().putString(ACTIVEMODE, selectedMode.getText().toString()).apply();
                                    uart.send(text);
                                    TastyToast.makeText(getActivity(), "Selected mode " + selectedMode.getText(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                                }
                            });
                            recyclerView.setAdapter(modeAdapter);
                        } else {
                            TastyToast.makeText(getContext(), "It appears you have not been assigned any modes yet", TastyToast.LENGTH_LONG, TastyToast.CONFUSING).show();
                        }
                    }
                } else {
                    TastyToast.makeText(getContext(), "Response code error " + response.code(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                }
                // progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GetSingleUserResponse> call, Throwable t) {
                // progressBar.setVisibility(View.GONE);
                TastyToast.makeText(getContext(), "" + t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
            }
        });
    }
    private void getModes() {
        progressBar.setVisibility(View.VISIBLE);
        selectedMode = view.findViewById(R.id.text_mode_name);
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        try {
            List<Mode> helperAllModes = dbHelper.getAllModes();
            helperAllModes.sort((lhs, rhs) -> lhs.getCommand().compareTo(rhs.getCommand()));
            modeAdapter = new UserDashboardAdapter(helperAllModes, getContext(), (obj, position) -> {
                final Command comm = new Command();
                comm.setEol(0);
                comm.setActive(true);
                if (obj.getName().equals("Therapy")) {
                    comm.setCommand("<<f00");
                } else {
                    comm.setCommand(obj.getCommand());
                }
                addSelectedMode(obj.getId());
                comm.setCommandName(obj.getName());
                String text = comm.getCommand();
                if (text == null)
                    text = "";
                switch (comm.getEol()) {
                    case CR_LF:
                        text = text.replaceAll("\n", "\r\n");
                        break;
                    case CR:
                        text = text.replaceAll("\n", "\r");
                        break;
                }
                final UARTInterface uart = (UARTInterface) getActivity();
                selectedMode.setText(obj.getName());
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
                assert uart != null;
                uart.send(text);
                TastyToast.makeText(getContext(), " " + comm.getCommand(), TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
            });
            recyclerView.setAdapter(modeAdapter);
            recyclerView.getAdapter().notifyDataSetChanged();
            fabSwitch = view.findViewById(R.id.fab_switch);
            progressBar.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addSelectedMode(String modeId) {
        Call<AddModeResponse> call = api.addSelectedMode(Integer.parseInt(tenantId), modeId);
        call.enqueue(new Callback<AddModeResponse>() {
            @Override
            public void onResponse(Call<AddModeResponse> call, Response<AddModeResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    TastyToast.makeText(getContext(), "Success", TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
                }
            }

            @Override
            public void onFailure(Call<AddModeResponse> call, Throwable t) {

            }
        });
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

    @Override
    public void onStart() {
        super.onStart();
        selectedMode = view.findViewById(R.id.text_mode_name);
        progressBar = view.findViewById(R.id.progressBar);
        startAsyncTask();
        getModes();
    }

    private boolean isModeAvailable() {
        progressBar.setVisibility(View.VISIBLE);
        boolean isAvailable;
        helperAllModes = dbHelper.getAllModes();
        if (helperAllModes.size() == 0) {
            isAvailable = false;
            progressBar.setVisibility(View.VISIBLE);
        } else {
            isAvailable = true;
            progressBar.setVisibility(View.GONE);
        }
        return isAvailable;
    }

    public void startAsyncTask() {
        ModesAsyncTask task = new ModesAsyncTask(getContext());
        task.execute(10);
    }

    private class ModesAsyncTask extends AsyncTask<Integer, Integer, String> {

        ModesAsyncTask(Context context) {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            if (!isModeAvailable()) {
                for (int i = 0; i < integers[0]; i++) {
                    publishProgress((i * 100) / integers[0]);
                    try {
                        getModes();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return "Modes loaded successfully";
            }
            return "Modes loaded successfully";
        }

    }
}
