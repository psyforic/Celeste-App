package com.celeste.celestedaylightapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.model.user.UserGetResponse;
import com.celeste.celestedaylightapp.model.user.UserModel;
import com.celeste.celestedaylightapp.retrofit.Api;
import com.celeste.celestedaylightapp.retrofit.ApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityProfile extends AppCompatActivity {
    //   @BindView(R.id.tvUsername)
    TextView tvUsername;
    TextView tvTenant;
    TextView tvEmail;
    TextView tvAddress;
    TextView tvNames;
    TextView tvCellphone;
    ProgressBar progressBar;
    FloatingActionButton floatingActionButton;
    int userID;
    Api api = ApiClient.getInstance(this).create(Api.class);
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initToolbar();
        ButterKnife.bind(this);
        initComponent();
        //userID = Integer.parseInt(EasyPreference.with(getApplicationContext()).getString(Constants.USERID, ""));
        initProfile();

        //tvUsername.setText(userID + "");

    }

    private void initProfile() {
        progressBar.setVisibility(View.VISIBLE);
        Call<UserGetResponse> call = api.getUser(25);
        call.enqueue(new Callback<UserGetResponse>() {
            @Override
            public void onResponse(Call<UserGetResponse> call, Response<UserGetResponse> response) {
                if (response.body() != null && response.code() == 200) {
//                    if (response.body().getResult() != null) {
//                        userModel = response.body().getResult();
//                        tvUsername.setText(userModel.getSurname());
//                        tvEmail.setText(userModel.getEmailAddress());
//                        tvCellphone.setText(userModel.getCellphoneNumber());
//                        tvTenant.setText(userModel.getUserName());
//                        tvAddress.setText(userModel.getAddress());
//                    } else {
//                        Toast.makeText(getApplicationContext(), "User has no modes", Toast.LENGTH_LONG).show();
//                    }
                } else {
                    Toast.makeText(getApplicationContext(), "" + response.code(), Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UserGetResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ActivityEditUser.class));
            }
        });
    }

    private void initComponent() {
        tvUsername = findViewById(R.id.tvUsername);
        tvTenant = findViewById(R.id.tvTenant);
        tvAddress = findViewById(R.id.tvAddress);
        tvEmail = findViewById(R.id.tvEmail);
        tvNames = findViewById(R.id.tvName);
        tvCellphone = findViewById(R.id.tvCellphone);
        floatingActionButton = findViewById(R.id.fab);
        progressBar = findViewById(R.id.progressBar);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.str_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}