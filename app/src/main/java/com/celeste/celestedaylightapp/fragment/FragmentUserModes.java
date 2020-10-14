package com.celeste.celestedaylightapp.fragment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.adapter.UserModesAdapter;
import com.celeste.celestedaylightapp.model.modes.Mode;
import com.celeste.celestedaylightapp.model.modes.ModeResult;
import com.celeste.celestedaylightapp.model.modes.UserModeModel;
import com.celeste.celestedaylightapp.model.user.GetSingleUserResponse;
import com.celeste.celestedaylightapp.model.user.UserModel;
import com.celeste.celestedaylightapp.model.usermode.UserMode;
import com.celeste.celestedaylightapp.retrofit.Api;
import com.celeste.celestedaylightapp.retrofit.ApiClient;
import com.celeste.celestedaylightapp.sqllitedb.DatabaseHelper;
import com.celeste.celestedaylightapp.sqllitedb.MainAdapter;
import com.celeste.celestedaylightapp.sqllitedb.MainData;
import com.celeste.celestedaylightapp.sqllitedb.RoomDB;
import com.celeste.celestedaylightapp.utils.Constants;
import com.iamhabib.easy_preference.EasyPreference;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentUserModes extends Fragment {
    public RecyclerView recyclerView;
    public View view;
    public List<Mode> modesList;
    public UserMode userMode;
    ProgressBar progressBar;
    Api api = ApiClient.getInstance(getContext()).create(Api.class);
    ModeResult modeResult;
    List<UserModeModel> modes;
    UserModesAdapter modesAdapter;
    List<MainData> dataList = new ArrayList<>();
    RoomDB database;
    MainAdapter mainAdapter;
    LinearLayoutManager linearLayoutManager;
    boolean doesExist = false;
    private int userId;
    private int Id;
    private List<UserMode> userModes;
    private List<UserModeModel> modeList = new ArrayList<>();
    private UserModel userModel;
    private DatabaseHelper dbHelper;
    private ArrayList<String> _id, mode_id, description, command, start_time, end_time, icon;
    private Mode mode = new Mode();
    private SQLiteDatabase sqLiteDatabase;

    public FragmentUserModes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_modes, container, false);
        userId = Objects.requireNonNull(getActivity()).getIntent().getIntExtra("userId", 0);
        Id = EasyPreference.with(getActivity()).getInt(Constants.USERID, 0);
        dbHelper = new DatabaseHelper(getActivity());
        sqLiteDatabase = dbHelper.getReadableDatabase();
        progressBar = view.findViewById(R.id.progressBar);
        initArrayLists();
        return view;
    }

    private void initArrayLists() {
        _id = new ArrayList<>();
        mode_id = new ArrayList<>();
        description = new ArrayList<>();
        command = new ArrayList<>();
        start_time = new ArrayList<>();
        end_time = new ArrayList<>();
        icon = new ArrayList<>();
    }

    private void displayModes() {
        List<Mode> helperAllModes = dbHelper.getAllModes();
        UserModeModel userModeModel = new UserModeModel();
        if (helperAllModes.size() != 0) {
            for (Mode mode : helperAllModes) {
                userModeModel.setMode(mode);
            }
            modeList.add(userModeModel);
        }
        recyclerView = view.findViewById(R.id.modesRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        modesAdapter = new UserModesAdapter(getContext(), modeList);
        recyclerView.setAdapter(modesAdapter);
        progressBar.setVisibility(View.GONE);
    }

    private void getUserModes() {
        progressBar.setVisibility(View.VISIBLE);
        Call<GetSingleUserResponse> call = api.getUser(Id);
        call.enqueue(new Callback<GetSingleUserResponse>() {
            @Override
            public void onResponse(Call<GetSingleUserResponse> call, Response<GetSingleUserResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getResult() != null) {
                        modeList = response.body().getResult().getUserModes();
                        if (modeList.size() != 0) {
                            initRecyclerView(response.body().getResult().getUserModes());
                            for (UserModeModel modes : modeList) {
                                mode = modes.getMode();
                                insertToDb(mode);
                            }
                        } else {
                            TastyToast.makeText(getContext(), "It appears you have not been assigned any modes yet", TastyToast.LENGTH_LONG, TastyToast.CONFUSING).show();
                        }
                    }
                } else {
                    TastyToast.makeText(getContext(), "Response code error " + response.code(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GetSingleUserResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                TastyToast.makeText(getContext(), "" + t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
            }
        });
    }

    private void getAdminModes() {
        progressBar.setVisibility(View.VISIBLE);
        Call<GetSingleUserResponse> call = api.getSingleUser(Id);
        call.enqueue(new Callback<GetSingleUserResponse>() {
            @Override
            public void onResponse(Call<GetSingleUserResponse> call, Response<GetSingleUserResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getResult() != null) {
                        modeList = response.body().getResult().getUserModes();
                        if (modeList.size() != 0) {
                            initRecyclerView(response.body().getResult().getUserModes());
                            for (UserModeModel modes : modeList) {
                                mode = modes.getMode();
                                insertToDb(mode);
                            }
                        } else {
                            TastyToast.makeText(getContext(), "It appears you have not been assigned any modes yet", TastyToast.LENGTH_LONG, TastyToast.CONFUSING).show();
                        }
                    }
                } else {
                    TastyToast.makeText(getContext(), "Response code error " + response.code(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GetSingleUserResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                TastyToast.makeText(getContext(), "" + t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
            }
        });
    }

    private void insertToDb(Mode mode) {
        Log.d("TAG", "onResponse: " + mode.getName());
        if (mode.getName() != null) {
            if (!dbHelper.recordExists(mode.getName())) {
                boolean inserted = dbHelper.insertUserMode(mode.getStartTime(), mode.getEndTime(), mode.getName(), mode.getCommand(), mode.getIcon(), mode.getId());
                if (inserted) {
                    //  Toast.makeText(getContext(), "Saved to db" + mode.getName(), Toast.LENGTH_LONG).show();
                } else {
                    //   Toast.makeText(getContext(), "Mode exists" + mode.getName(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            TastyToast.makeText(getContext(), "It appears you have not been assigned any modes yet", TastyToast.LENGTH_LONG, TastyToast.CONFUSING).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onStart() {
        if (isNetworkAvailable()) {
            getAdminModes();
        } else {
            displayModes();
        }
        super.onStart();
    }

    private void initRecyclerView(List<UserModeModel> modesList) {
        recyclerView = view.findViewById(R.id.modesRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        modesAdapter = new UserModesAdapter(getActivity(), modesList);
        recyclerView.setAdapter(modesAdapter);
        progressBar.setVisibility(View.GONE);
    }
}