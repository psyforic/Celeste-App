package com.celeste.celestedaylightapp.fragment.addUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.celeste.celestedaylightapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAddModes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAddModes extends Fragment {


    public FragmentAddModes() {
        // Required empty public constructor
    }
    public static FragmentAddModes newInstance() {
        FragmentAddModes fragment = new FragmentAddModes();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_modes, container, false);
    }
}