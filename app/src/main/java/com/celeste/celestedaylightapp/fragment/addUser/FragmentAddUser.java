package com.celeste.celestedaylightapp.fragment.addUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.celeste.celestedaylightapp.R;

public class FragmentAddUser extends Fragment {


    public FragmentAddUser() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentAddUser newInstance() {
        FragmentAddUser fragment = new FragmentAddUser();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_user, container, false);
    }
}