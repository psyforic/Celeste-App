package com.celeste.celestedaylightapp.fragment;

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
import com.celeste.celestedaylightapp.model.modes.ModeGetResponse;
import com.celeste.celestedaylightapp.model.modes.ModeResult;
import com.celeste.celestedaylightapp.model.modes.UserModeModel;
import com.celeste.celestedaylightapp.model.user.GetSingleUserResponse;
import com.celeste.celestedaylightapp.model.user.UserModel;
import com.celeste.celestedaylightapp.model.usermode.UserMode;
import com.celeste.celestedaylightapp.retrofit.Api;
import com.celeste.celestedaylightapp.retrofit.ApiClient;
import com.celeste.celestedaylightapp.sqllitedb.MainAdapter;
import com.celeste.celestedaylightapp.sqllitedb.MainData;
import com.celeste.celestedaylightapp.sqllitedb.RoomDB;
import com.celeste.celestedaylightapp.utils.Constants;
import com.iamhabib.easy_preference.EasyPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentUserModes extends Fragment {
    public RecyclerView recyclerView;
    public View view;
    public List<UserMode> modesList;
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
    private int userId;
    private int Id;
    private List<UserMode> userModes;
    private List<Mode> modeList = new ArrayList<>();
    private UserModel userModel;

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
        // EasyPreference.with(getActivity()).getObject(Constants.MODE, ).save();
        database = RoomDB.getInstance(getActivity());
        progressBar = view.findViewById(R.id.progressBar);
        getUserModes();
        return view;
    }

    public void getUserModes() {
        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(), "" + Id, Toast.LENGTH_LONG).show();
        Call<GetSingleUserResponse> call = api.getUser(Id);
        call.enqueue(new Callback<GetSingleUserResponse>() {
            @Override
            public void onResponse(Call<GetSingleUserResponse> call, Response<GetSingleUserResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getResult() != null) {
                        userModel = response.body().getResult();
                        if (userModel.getUserModes().size() != 0) {
                            modes = userModel.getUserModes();
                            modes.forEach(mode -> {
                                getMode(mode.getModeId());
                            });
                        }
                    } else {
                        Toast.makeText(getContext(), "User has no modes", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "" + response.code(), Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GetSingleUserResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
//        Call<UserGetResponse> call = api.getUser(userId);
//        call.enqueue(new Callback<UserGetResponse>() {
//            @Override
//            public void onResponse(Call<UserGetResponse> call, Response<UserGetResponse> response) {
//                if (response.body() != null && response.code() == 200) {
//                    if (response.body().getResult() != null) {
//                        modes = response.body().getResult().getItems();
//                        initRecyclerView(modes.get(0).getUserModes());
//                    } else {
//                        Toast.makeText(getContext(), "User has no modes", Toast.LENGTH_LONG).show();
//                    }
//                } else {
//                    Toast.makeText(getContext(), "" + response.code(), Toast.LENGTH_LONG).show();
//                }
//                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onFailure(Call<UserGetResponse> call, Throwable t) {
//                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
//                progressBar.setVisibility(View.GONE);
//            }
//        });

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

    public void getMode(String modeId) {
        Call<ModeGetResponse> call = api.getMode(modeId);
        call.enqueue(new Callback<ModeGetResponse>() {
            @Override
            public void onResponse(Call<ModeGetResponse> call, Response<ModeGetResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    Mode mode = response.body().getResult();
                    modeList.add(mode);
                    initRecyclerView(modeList);
                    Toast.makeText(getContext(), "Mode" + mode.getName(), Toast.LENGTH_LONG).show();

                    // String sText = editText.getText().toString().trim();
//                    if (mode != null) {
                    //   initRecyclerView(modes);
//                        Toast.makeText(getContext(), "Modes found" + modeId, Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(getContext(), "Modes not found", Toast.LENGTH_LONG).show();
//                    }
                }
            }

            @Override
            public void onFailure(Call<ModeGetResponse> call, Throwable t) {
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //    //When text is not empty
//    //Initialize main data
//    MainData data = new MainData();
//                        data.setText(modes.toString());
//
//    //Insert text in database
//                        database.mainDao().insert(data);
//    //Notify when data is inserted
//
//                        dataList.clear();
//                        dataList.addAll(database.mainDao().getAll());
//    mainAdapter = new MainAdapter(getActivity(), dataList);
//                        mainAdapter.notifyDataSetChanged();
    public void initRecyclerView(List<Mode> modeList) {
        //Initialize Database
        // database = RoomDB.getInstance(getActivity());
        //Store database value in data list
        // modeList = database.mainDao().getAll();
        //Initialize linear layout manager
        //Set layout manager
        //Initialize adapter
        //Set adapter

        recyclerView = view.findViewById(R.id.modesRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        modesAdapter = new UserModesAdapter(getActivity(), modeList);
        recyclerView.setAdapter(modesAdapter);
    }
}