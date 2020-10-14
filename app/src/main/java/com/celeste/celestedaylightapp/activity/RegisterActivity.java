package com.celeste.celestedaylightapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.model.User;
import com.celeste.celestedaylightapp.model.registertenant.RegisterTenantResponse;
import com.celeste.celestedaylightapp.model.registertenant.RegisterTenantResult;
import com.celeste.celestedaylightapp.retrofit.Api;
import com.celeste.celestedaylightapp.retrofit.ApiClient;
import com.celeste.celestedaylightapp.sqllitedb.SQLLiteOpenHelper;
import com.celeste.celestedaylightapp.utils.InputValidation;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private final AppCompatActivity activity = RegisterActivity.this;
    TextView tvLogin;
    View parentView;
    TextInputEditText edit_address,
            edit_tenancyName,
            edit_email,
            edit_firstName,
            edit_lastName,
            edit_password,
            edit_confirmPass;
    Button btn_signUp;
    Api apiService = ApiClient.getInstance(this).create(Api.class);
    private RelativeLayout relativeLayout;
    private TextInputLayout textInputLayoutTenancyName;
    private TextInputLayout textInputLayoutFirstName;
    private TextInputLayout textInputLayoutLastName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;
    private InputValidation inputValidation;
    private SQLLiteOpenHelper databaseHelper;
    private User user;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComponents();
        setOnClickListeners();
    }

    private void initComponents() {
        parentView = findViewById(R.id.content);
        tvLogin = findViewById(R.id.txt_sign_in);
        edit_tenancyName = findViewById(R.id.edit_tenant_name);
        edit_email = findViewById(R.id.edit_email);
        edit_firstName = findViewById(R.id.edit_firstName);
        edit_lastName = findViewById(R.id.edit_lastName);
        edit_password = findViewById(R.id.edit_password);
        edit_confirmPass = findViewById(R.id.edit_confirm_password);
        btn_signUp = findViewById(R.id.btn_signUp);
        relativeLayout = findViewById(R.id.layout_register);
        progressBar = findViewById(R.id.progressBar);
        textInputLayoutTenancyName = findViewById(R.id.textInputLayoutTenancyName);
        textInputLayoutFirstName = findViewById(R.id.textInputLayoutFirstName);
        textInputLayoutLastName = findViewById(R.id.textInputLayoutLastName);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = findViewById(R.id.textInputLayoutConfirmPassword);

        user = new User();
        databaseHelper = new SQLLiteOpenHelper(activity);
        inputValidation = new InputValidation(activity);
    }

    private void setOnClickListeners() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TenantLogin.class));
            }
        });
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //postDataToSQLite();
                registerTenant();
            }
        });
    }

    public void registerTenant() {
        RegisterTenantResult registerTenantResult = new RegisterTenantResult();
        registerTenantResult.setEmail(Objects.requireNonNull(edit_email.getText()).toString().trim());
        registerTenantResult.setFirstName(Objects.requireNonNull(edit_firstName.getText()).toString());
        registerTenantResult.setName(Objects.requireNonNull(edit_firstName.getText()).toString());
        registerTenantResult.setLastName(Objects.requireNonNull(edit_lastName.getText()).toString().trim());
        registerTenantResult.setTenancyName(Objects.requireNonNull(edit_tenancyName.getText()).toString());
        registerTenantResult.setConnectionString("");
        registerTenantResult.setAdminEmailAddress(edit_email.getText().toString());
        registerTenantResult.setPassword(Objects.requireNonNull(edit_password.getText()).toString());
        registerTenantResult.setIsActive(true);
        progressBar.setVisibility(View.VISIBLE);
        Call<RegisterTenantResponse> responseCall = apiService.registerTenant(registerTenantResult);
        responseCall.enqueue(new Callback<RegisterTenantResponse>() {
            @Override
            public void onResponse(Call<RegisterTenantResponse> call, Response<RegisterTenantResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    startActivity(new Intent(getApplicationContext(), TenantLogin.class));
                    emptyInputEditText();
                    finish();
                } else {
                    TastyToast.makeText(getApplicationContext(), "" + response.errorBody(), TastyToast.LENGTH_LONG, TastyToast.CONFUSING).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<RegisterTenantResponse> call, Throwable t) {
                TastyToast.makeText(getApplicationContext(), "" + t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(edit_tenancyName, textInputLayoutTenancyName, getString(R.string.error_message_name))) {
            return;
        }

        if (!inputValidation.isInputEditTextFilled(edit_firstName, textInputLayoutFirstName, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(edit_email, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(edit_lastName, textInputLayoutLastName, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(edit_password, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(edit_password, edit_confirmPass,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }
        if (!databaseHelper.checkUser(Objects.requireNonNull(edit_email.getText()).toString().trim())) {
            user.setTenantName(Objects.requireNonNull(edit_tenancyName.getText()).toString().trim());
            user.setFirstName(Objects.requireNonNull(edit_firstName.getText()).toString().trim());
            user.setLastName(Objects.requireNonNull(edit_lastName.getText()).toString().trim());
            user.setUserEmail(edit_email.getText().toString().trim());
            user.setPassword(Objects.requireNonNull(edit_password.getText()).toString().trim());
            databaseHelper.addUser(user);
            // Snack Bar to show success message that record saved successfully
            Snackbar.make(relativeLayout, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
            emptyInputEditText();
        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(relativeLayout, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        edit_email.setText(null);
        edit_tenancyName.setText(null);
        edit_firstName.setText(null);
        edit_lastName.setText(null);
        edit_password.setText(null);
        edit_confirmPass.setText(null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
