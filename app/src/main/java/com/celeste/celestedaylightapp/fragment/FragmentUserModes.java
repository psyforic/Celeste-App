package com.celeste.celestedaylightapp.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

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
    private Mode mode;
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
        Toast.makeText(getContext(), "User ID" + Id, Toast.LENGTH_LONG).show();
        progressBar = view.findViewById(R.id.progressBar);
        //getUserModes();
        initArrayLists();
        storeDataInArrays();
        //displayModes();
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

    private void storeDataInArrays() {
        progressBar.setVisibility(View.VISIBLE);
        Cursor cursor = dbHelper.readAllData();
        if (!(cursor.moveToFirst()) || cursor.getCount() == 0) {
            TastyToast.makeText(getActivity(), "No data, please sync database", TastyToast.LENGTH_LONG,
                    TastyToast.INFO);
        } else {
            _id.add(cursor.getString(0));
            start_time.add(cursor.getString(1));
            end_time.add(cursor.getString(2));
            description.add(cursor.getString(3));
            command.add(cursor.getString(4));
            icon.add(cursor.getString(5));
            mode_id.add(cursor.getString(6));
        }
    }

    public void displayModes() {
        List<Mode> helperAllModes = dbHelper.getAllModes();
        UserModeModel userModeModel = new UserModeModel();
        for (Mode mode : helperAllModes) {
            userModeModel.setMode(mode);
        }
        modeList.add(userModeModel);
        recyclerView = view.findViewById(R.id.modesRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        modesAdapter = new UserModesAdapter(getContext(), modeList);
        recyclerView.setAdapter(modesAdapter);
        progressBar.setVisibility(View.GONE);
    }

    public void getUserModes() {
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
                            }
                            if (!dbHelper.isFieldExists(sqLiteDatabase, "USER_MODES", mode.getName())) {
                                boolean inserted = dbHelper.insertUserMode(mode.getStartTime(), mode.getEndTime(), mode.getName(), mode.getCommand(), mode.getIcon(), mode.getId());
                                if (inserted) {
                                    Toast.makeText(getContext(), "Saved to db" + mode.getName(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(), "Mode exists" + mode.getName(), Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            Toast.makeText(getContext(), "User has no modes", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Response code error " + response.code(), Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GetSingleUserResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
//        String id = "25";
//        Call<UserGetResponse> call = api.getUser(id);
//        call.enqueue(new Callback<UserGetResponse>() {
//            @Override
//            public void onResponse(Call<UserGetResponse> call, Response<UserGetResponse> response) {
//                if (response.body() != null && response.code() == 200) {
//                    String modes = response.body().getResult().getUserModes() + ""+response.body().getResult().getSurname();
//                    Toast.makeText(getContext(), modes, Toast.LENGTH_LONG).show();
//                    Call<UserModeGetResponse> call1 = api.getUserMode(modes);
//                    call1.enqueue(new Callback<UserModeGetResponse>() {
//                        @Override
//                        public void onResponse(Call<UserModeGetResponse> call, Response<UserModeGetResponse> response) {
//                            if (response.body() != null && response.code() == 200) {
//                             //   modesList = response.body().getResult().getUserModes();
//                               // Toast.makeText(getContext(), "ID FIRST" + modesList.get(0).getUserName(), Toast.LENGTH_LONG).show();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<UserModeGetResponse> call, Throwable t) {
//                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    });
//                    //   modesList = response.body().getResult().getUserModes();
//                    //   Toast.makeText(getContext(), "ID FIRST" + modesList.get(0).getUserModes(), Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getContext(), "" + response.code(), Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserGetResponse> call, Throwable t) {
//                Toast.makeText(getContext(), "Error responsible onFAILURE" + t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onStart() {
        //    displayModes();
        if (isNetworkAvailable()) {
            getUserModes();
        } else {
            displayModes();
        }
        super.onStart();
    }
//
//    public void getMode(Mode modes) {
//        Call<ModeGetResponse> call = api.getMode(modes.getId());
//        call.enqueue(new Callback<ModeGetResponse>() {
//            @Override
//            public void onResponse(Call<ModeGetResponse> call, Response<ModeGetResponse> response) {
//                if (response.body() != null && response.code() == 200) {
//                    mode = response.body().getResult();
//                    modeList.add(mode);
//                    initRecyclerView(modeList);
//                    if (mode != null) {
//                        if (!dbHelper.isFieldExists(sqLiteDatabase, "USER_MODES", mode.getName())) {
//                            boolean inserted = dbHelper.insertUserMode(mode.getStartTime(), mode.getEndTime(), mode.getName(), mode.getCommand(), mode.getIcon(), mode.getId());
//                            if (inserted) {
//                                Toast.makeText(getContext(), "Saved to db" + mode.getName(), Toast.LENGTH_LONG).show();
//                            } else {
//                                Toast.makeText(getContext(), "Mode exists" + mode.getName(), Toast.LENGTH_LONG).show();
//                            }
//                        } else {
//                            Toast.makeText(getContext(), "Mode exists" + mode.getName(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//                    // String sText = editText.getText().toString().trim();
//                    if (mode != null) {//   initRecyclerView(modes);
//                        Toast.makeText(getContext(), "Modes found" + mode, Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(getContext(), "Modes not found", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ModeGetResponse> call, Throwable t) {
//                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }

    public void initRecyclerView(List<UserModeModel> modesList) {
        //List<Mode> mode = dbHelper.getAllModes();
        Toast.makeText(getContext(), "Modes size " + modesList.size(), Toast.LENGTH_LONG).show();
        recyclerView = view.findViewById(R.id.modesRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        modesAdapter = new UserModesAdapter(getActivity(), modesList);
        recyclerView.setAdapter(modesAdapter);
        progressBar.setVisibility(View.GONE);
    }
}