package com.celeste.celestedaylightapp.fragment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.adapter.UserModesAdapter;
import com.celeste.celestedaylightapp.model.modes.Mode;
import com.celeste.celestedaylightapp.model.modes.UserModeModel;
import com.celeste.celestedaylightapp.model.user.GetSingleUserResponse;
import com.celeste.celestedaylightapp.retrofit.Api;
import com.celeste.celestedaylightapp.retrofit.ApiClient;
import com.celeste.celestedaylightapp.retrofit.NoConnectivityException;
import com.celeste.celestedaylightapp.sqllitedb.DatabaseHelper;
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
    ProgressBar progressBar;
    Api api = ApiClient.getInstance(getContext()).create(Api.class);
    UserModesAdapter modesAdapter;
    private int Id;
    private List<UserModeModel> modeList = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private LinearLayout lyt_not_found;
    private SwipeRefreshLayout pullToRefresh;

    public FragmentUserModes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_modes, container, false);
        Id = EasyPreference.with(Objects.requireNonNull(getActivity())).getInt(Constants.USERID, 0);
        initComponents();
        return view;
    }

    private void initComponents() {
        dbHelper = new DatabaseHelper(getActivity());
        progressBar = view.findViewById(R.id.progressBar);
        lyt_not_found = view.findViewById(R.id.lyt_not_found);
        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(this::getUserModes);
    }

    private void displayModes() {
        pullToRefresh.setRefreshing(true);
        swipeRefreshListener();
        List<Mode> helperAllModes = dbHelper.getAllModes();
        UserModeModel userModeModel = new UserModeModel();
        if (helperAllModes.size() != 0) {
            for (Mode mode : helperAllModes) {
                userModeModel.setMode(mode);
                modeList.add(userModeModel);
            }
        } else {
            lyt_not_found.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        pullToRefresh.setRefreshing(false);
        swipeRefreshListener();
        recyclerView = view.findViewById(R.id.modesRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        modesAdapter = new UserModesAdapter(getContext(), modeList);
        recyclerView.setAdapter(modesAdapter);
        progressBar.setVisibility(View.GONE);
        pullToRefresh.setRefreshing(false);
        swipeRefreshListener();
    }

    private void getUserModes() {
        pullToRefresh.setRefreshing(true);
        swipeRefreshListener();
        Call<GetSingleUserResponse> call = api.getSingleUser(Id);
        call.enqueue(new Callback<GetSingleUserResponse>() {
            @Override
            public void onResponse(Call<GetSingleUserResponse> call, Response<GetSingleUserResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getResult() != null) {
                        modeList = response.body().getResult().getUserModes();
                        if (modeList.size() != 0) {
                            initRecyclerView(response.body().getResult().getUserModes());
                            pullToRefresh.setRefreshing(false);
                            swipeRefreshListener();
                        } else {
                            TastyToast.makeText(getContext(), "It appears you have not been assigned any modes yet", TastyToast.LENGTH_LONG, TastyToast.CONFUSING).show();
                        }
                        pullToRefresh.setRefreshing(false);
                        swipeRefreshListener();
                    }
                } else {
                    TastyToast.makeText(getContext(), "Response code error " + response.code(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                    lyt_not_found.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GetSingleUserResponse> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    TastyToast.makeText(getContext(), "" + t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                    lyt_not_found.setVisibility(View.VISIBLE);
                } else {
                    TastyToast.makeText(getContext(), "" + t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                    pullToRefresh.setRefreshing(false);
                }
                swipeRefreshListener();
            }
        });
    }

    private void swipeRefreshListener() {
        if (pullToRefresh.isRefreshing()) {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
            getUserModes();
        } else {
            displayModes();
        }
        super.onStart();
    }

    private void initRecyclerView(List<UserModeModel> modesList) {
        recyclerView = view.findViewById(R.id.modesRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        modesList.sort((lhs, rhs) -> lhs.getMode().getCommand().compareTo(rhs.getMode().getCommand()));
        modesAdapter = new UserModesAdapter(getActivity(), modesList);
        recyclerView.setAdapter(modesAdapter);
        recyclerView.getAdapter().notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }
}