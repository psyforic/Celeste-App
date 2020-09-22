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
import com.celeste.celestedaylightapp.model.usermode.UserMode;
import com.celeste.celestedaylightapp.retrofit.Api;
import com.celeste.celestedaylightapp.retrofit.ApiClient;
import java.util.List;
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
    List<Mode> modes;
    UserModesAdapter modesAdapter;
    private int userId;

    public FragmentUserModes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_modes, container, false);
    //    userID = EasyPreference.with(Objects.requireNonNull(getActivity())).getInt(Constants.TENANT_ID, 0);
        progressBar = view.findViewById(R.id.progressBar);
        getUserModes();
        return view;
    }

    public void getUserModes() {
        progressBar.setVisibility(View.VISIBLE);
        //Toast.makeText(getContext(), "" + userID, Toast.LENGTH_LONG).show();
        Call<ModeGetResponse> call = api.getModes(1000,0);
        call.enqueue(new Callback<ModeGetResponse>() {
            @Override
            public void onResponse(Call<ModeGetResponse> call, Response<ModeGetResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getResult() != null) {
                        modeResult = response.body().getResult();
                        initRecyclerView(modeResult.getItems());
                    } else {
                        Toast.makeText(getContext(), "User has no modes", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "" + response.code(), Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<ModeGetResponse> call, Throwable t) {
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
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

    public void initRecyclerView(List<Mode> modeList) {
        recyclerView = view.findViewById(R.id.modesRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        modesAdapter = new UserModesAdapter(getActivity(), modeList);
        recyclerView.setAdapter(modesAdapter);
    }
}