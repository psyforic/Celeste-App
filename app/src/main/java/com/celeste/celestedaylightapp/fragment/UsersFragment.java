package com.celeste.celestedaylightapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.activity.AddUser;
import com.celeste.celestedaylightapp.adapter.UsersAdapter;
import com.celeste.celestedaylightapp.data.DataGenerator;
import com.celeste.celestedaylightapp.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class UsersFragment extends Fragment {
    private View parent_view;
    private UsersAdapter mAdapter;
    private RecyclerView recyclerView;
    private List<User> items = new ArrayList<>();
    private FloatingActionButton addUser;

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent_view = inflater.inflate(R.layout.activity_automatic_modes2, container, false);
        initComponent();
        return parent_view;
    }

    private void initComponent() {
        recyclerView = (RecyclerView) parent_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        addUser = parent_view.findViewById(R.id.fab);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddUser.class));
            }
        });
        items = DataGenerator.getPeopleData(Objects.requireNonNull(getContext()));
        items.addAll(DataGenerator.getPeopleData(Objects.requireNonNull(getContext())));
        setAdapter();
        //    showSingleChoiceDialog();
    }

    private void setAdapter() {
        //set data and list adapter
        mAdapter = new UsersAdapter(getActivity(), items);
        recyclerView.setAdapter(mAdapter);
    }

}
