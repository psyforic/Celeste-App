package com.celeste.celestedaylightapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.model.User;
import com.celeste.celestedaylightapp.model.user.GetSingleUserResponse;
import com.celeste.celestedaylightapp.model.user.UpdateUserResponse;
import com.celeste.celestedaylightapp.model.user.UpdateUserResult;
import com.celeste.celestedaylightapp.model.user.UserModel;
import com.celeste.celestedaylightapp.retrofit.Api;
import com.celeste.celestedaylightapp.retrofit.ApiClient;
import com.celeste.celestedaylightapp.sqllitedb.SQLLiteOpenHelper;
import com.google.android.material.textfield.TextInputLayout;
import com.sdsmdg.tastytoast.TastyToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityEditUser extends AppCompatActivity {
    EditText etUsername, etName, etSurname, etEmail, etCellphone, etAddress, etPassword;
    TextInputLayout textInputName, textInputSurname, textInputEmail, textInputUsername, textInputPassword, textInputAddress, textInputCellphone;
    Button btnUpdate;
    ProgressBar progressBar;
    UpdateUserResult userUpdateModel;
    int userId;
    Api api = ApiClient.getInstance(this).create(Api.class);
    UserModel userModel;
    private SQLLiteOpenHelper databaseHelper;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        userId = getIntent().getIntExtra("userId", 0);
        //Toast.makeText(getApplicationContext(), "userId" + userId, Toast.LENGTH_LONG).show();
        initComponent();
        initProfile();
        onClick();
    }

    private void onClick() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    private void initProfile() {
        progressBar.setVisibility(View.VISIBLE);
        Call<GetSingleUserResponse> call = api.getSingleUser(userId);
        call.enqueue(new Callback<GetSingleUserResponse>() {
            @Override
            public void onResponse(Call<GetSingleUserResponse> call, Response<GetSingleUserResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getResult() != null) {
                        userModel = response.body().getResult();
                        etUsername.setText(userModel.getUserName());
                        etName.setText(userModel.getName());
                        etSurname.setText(userModel.getSurname());
//                        etCellphone.setText(userModel.getCellphoneNumber());
                        etEmail.setText(userModel.getEmailAddress());
                        etAddress.setText(userModel.getAddress());
                    } else {
                        Toast.makeText(getApplicationContext(), "User has no modes", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "" + response.code(), Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GetSingleUserResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                TastyToast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG, TastyToast.ERROR).show();
            }
        });
    }

    private void updateProfile() {
        progressBar.setVisibility(View.VISIBLE);
        userUpdateModel = new UpdateUserResult();
        userUpdateModel.setEmailAddress(etEmail.getText().toString());
        userUpdateModel.setName(etName.getText().toString());
        userUpdateModel.setSurname(etSurname.getText().toString());
        userUpdateModel.setUserName(etUsername.getText().toString());
        userUpdateModel.setAddress(etAddress.getText().toString());
        userUpdateModel.setIsActive(true);
        Call<UpdateUserResponse> call = api.updateUser(userId, userUpdateModel);
        call.enqueue(new Callback<UpdateUserResponse>() {
            @Override
            public void onResponse(Call<UpdateUserResponse> call, Response<UpdateUserResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getResult() != null) {
                        Toast.makeText(getApplicationContext(), "Profile Updated Successfully", Toast.LENGTH_LONG).show();
                        updateUserProfile();
                        onBackPressed();
                        finish();
                    }
                }
                progressBar.setVisibility(View.GONE);
                Log.d("USER RESPONSE", "onResponse: " + response.code());
            }

            @Override
            public void onFailure(Call<UpdateUserResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void updateUserProfile() {
        SQLLiteOpenHelper dbHelper = new SQLLiteOpenHelper(getApplicationContext());
        User userProfileInfo = new User();
        userProfileInfo.setFirstName(etName.getText().toString());
        userProfileInfo.setLastName(etSurname.getText().toString());
        userProfileInfo.setUserEmail(etEmail.getText().toString());
        dbHelper.updateUser(userProfileInfo);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), ActivityProfile.class));
        finish();
    }

    private void getUserInfo() {

    }

    @Override
    protected void onStart() {
        if (!isNetworkAvailable()) {
            getUserInfo();
        } else {
            initProfile();
        }
        super.onStart();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void initComponent() {
        etUsername = findViewById(R.id.edit_username);
        etName = findViewById(R.id.edit_name);
        etSurname = findViewById(R.id.edit_surname);
        etAddress = findViewById(R.id.edit_address);
        etEmail = findViewById(R.id.edit_email);
        etCellphone = findViewById(R.id.edit_cellphone);
        etPassword = findViewById(R.id.edit_password);
        btnUpdate = findViewById(R.id.btn_update);
        progressBar = findViewById(R.id.progressBar);

        textInputEmail = findViewById(R.id.textInputEmail);
        textInputPassword = findViewById(R.id.textInputPassword);
        textInputSurname = findViewById(R.id.textInputSurname);
        textInputName = findViewById(R.id.textInputName);
        textInputCellphone = findViewById(R.id.textInputCellphone);
        textInputUsername = findViewById(R.id.textInputUsername);
        textInputAddress = findViewById(R.id.textInputAddress);
    }
}