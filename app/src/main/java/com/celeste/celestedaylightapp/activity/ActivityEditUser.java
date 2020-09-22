package com.celeste.celestedaylightapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.model.user.UpdateUserResponse;
import com.celeste.celestedaylightapp.model.user.UpdateUserResult;
import com.celeste.celestedaylightapp.model.user.UserGetResponse;
import com.celeste.celestedaylightapp.model.user.UserModel;
import com.celeste.celestedaylightapp.retrofit.Api;
import com.celeste.celestedaylightapp.retrofit.ApiClient;
import com.google.android.material.textfield.TextInputLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        userId = getIntent().getIntExtra("userId", 0);
        Toast.makeText(getApplicationContext(), "userId" + userId, Toast.LENGTH_LONG).show();
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
        //       userModel = new UserModel();
//        userUpdateModel.setEmailAddress(etEmail.getText().toString());
//        userUpdateModel.setName(etName.getText().toString());
//        userUpdateModel.setSurname(etSurname.getText().toString());
//        userUpdateModel.setUserName(etUsername.getText().toString());
//        userUpdateModel.setAddress(etAddress.getText().toString());
//        userUpdateModel.setAddress(etAddress.getText().toString());
//        userUpdateModel.setCellphoneNumber(etCellphone.getText().toString());
        progressBar.setVisibility(View.VISIBLE);
        Call<UserGetResponse> call = api.getUser(userId);
        call.enqueue(new Callback<UserGetResponse>() {
            @Override
            public void onResponse(Call<UserGetResponse> call, Response<UserGetResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getResult() != null) {
                        userModel = response.body().getResult();
                        etUsername.setText(userModel.getUserName());
                        etName.setText(userModel.getName());
                        etSurname.setText(userModel.getSurname());
                        etCellphone.setText(userModel.getCellphoneNumber());
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
            public void onFailure(Call<UserGetResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
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
        userUpdateModel.setCellphoneNumber(etCellphone.getText().toString());
        Call<UpdateUserResponse> call = api.updateUser(userId, userUpdateModel);
        call.enqueue(new Callback<UpdateUserResponse>() {
            @Override
            public void onResponse(Call<UpdateUserResponse> call, Response<UpdateUserResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getResult() != null) {
                        Toast.makeText(getApplicationContext(), "Updated" + response.code(), Toast.LENGTH_LONG).show();
                        onBackPressed();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "User has no modes", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "" + response.message(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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