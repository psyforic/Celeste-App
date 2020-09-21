package com.celeste.celestedaylightapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.retrofit.Api;
import com.celeste.celestedaylightapp.retrofit.ApiClient;

public class FragmentProfile extends Fragment {

    public View view;
    Api api = ApiClient.getInstance(getContext()).create(Api.class);

    public FragmentProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}
