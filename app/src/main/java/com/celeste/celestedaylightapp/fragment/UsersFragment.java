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
import com.celeste.celestedaylightapp.adapter.UsersAdapter;
import com.celeste.celestedaylightapp.model.User;
import com.celeste.celestedaylightapp.model.user.UserGetResponse;
import com.celeste.celestedaylightapp.model.user.UserModel;
import com.celeste.celestedaylightapp.model.user.UserResult;
import com.celeste.celestedaylightapp.retrofit.Api;
import com.celeste.celestedaylightapp.retrofit.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class UsersFragment extends Fragment {
    Api api = ApiClient.getInstance(getContext()).create(Api.class);
    private View parent_view;
    private UsersAdapter usersAdapter;
    private RecyclerView recyclerView;
    private List<User> items = new ArrayList<>();
    private UserResult userResult;
    private ProgressBar progressBar;

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent_view = inflater.inflate(R.layout.activity_users, container, false);
        progressBar = parent_view.findViewById(R.id.progressBar);
        getUsers();
        return parent_view;
    }

    public void getUsers() {
        progressBar.setVisibility(View.VISIBLE);
        Call<UserGetResponse> call = api.getUsers("", true, 0, 100);
        call.enqueue(new Callback<UserGetResponse>() {

            @Override
            public void onResponse(Call<UserGetResponse> call, Response<UserGetResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    userResult = response.body().getResult();
                    initRecyclerView(userResult.getItems());
                } else {
                    Toast.makeText(getContext(), "Not found", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UserGetResponse> call, Throwable t) {
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void initRecyclerView(List<UserModel> userList) {
        recyclerView = parent_view.findViewById(R.id.usersRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        usersAdapter = new UsersAdapter(getContext(), userList);
        recyclerView.setAdapter(usersAdapter);
    }

}
