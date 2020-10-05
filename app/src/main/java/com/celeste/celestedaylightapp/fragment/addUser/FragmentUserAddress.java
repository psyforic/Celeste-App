package com.celeste.celestedaylightapp.fragment.addUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.celeste.celestedaylightapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentUserAddress#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentUserAddress extends Fragment {


    public FragmentUserAddress() {
        // Required empty public constructor
    }

    public static FragmentUserAddress newInstance() {
        FragmentUserAddress fragment = new FragmentUserAddress();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_address, container, false);
    }
}