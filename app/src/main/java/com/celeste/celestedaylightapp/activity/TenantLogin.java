package com.celeste.celestedaylightapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.fragment.InternetDialog;
import com.celeste.celestedaylightapp.model.authenticate.AuthenticateResponse;
import com.celeste.celestedaylightapp.model.tenant.TenantLoginModel;
import com.celeste.celestedaylightapp.model.tenant.TenantResponse;
import com.celeste.celestedaylightapp.retrofit.Api;
import com.celeste.celestedaylightapp.retrofit.ApiClient;
import com.celeste.celestedaylightapp.utils.Constants;
import com.celeste.celestedaylightapp.utils.Tools;
import com.iamhabib.easy_preference.EasyPreference;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TenantLogin extends AppCompatActivity {

    Api api = ApiClient.getInstance(this).create(Api.class);
    TenantLoginModel tenantLoginModel = new TenantLoginModel();
    EditText edit_tenancyName;
    Button btnLogin;
    ProgressBar progressBar;
    //   SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.CREDENTIALS, 0); // 0 - for private mode
//    SharedPreferences.Editor editor = pref.edit();
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenant_layout);
        initComponents();
        login();
        AuthenticateResponse response = EasyPreference.with(getApplicationContext()).getObject(Constants.CREDENTIALS, AuthenticateResponse.class);
        if (response != null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }

    public void initComponents() {
        edit_tenancyName = findViewById(R.id.edit_tenancyName);
        btnLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
    }

    private void login() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                isTenantAvailable();
            }
        });
    }
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private void isTenantAvailable() {
        tenantLoginModel.setTenancyName(edit_tenancyName.getText().toString().trim());
        Call<TenantResponse> call = api.tenantLogin(tenantLoginModel);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<TenantResponse>() {
            @Override
            public void onResponse(Call<TenantResponse> call, Response<TenantResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    switch (response.body().getResult().getState()) {
                        case AVAILABLE:
                            try {
                                EasyPreference.with(getApplicationContext()).addInt(Constants.TENANT_ID, response.body().getResult().getTenantId()).save();
                                setPreferences(response.body().getResult().getTenantId(), response.body().getResult().getState().ordinal());
                                startActivity(new Intent(TenantLogin.this, LoginActivity.class));
                                finish();
                            } catch (GeneralSecurityException | IOException ex) {
                                ex.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Tenant ID " + response.body().getResult().getTenantId(), Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
                            break;
                        case IN_ACTIVE:
                            Toast.makeText(getApplicationContext(), "This user is not active yet, please contact admin", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            break;
                        case NOT_FOUND:
                            Toast.makeText(getApplicationContext(), "Tenant Not Found", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            break;
                    }

                }
            }

            @Override
            public void onFailure(Call<TenantResponse> call, Throwable t) {
              //  Toast.makeText(getApplicationContext(), "Tenant Not Found" + t.getMessage(), Toast.LENGTH_LONG).show();
                final InternetDialog dialog=new InternetDialog(TenantLogin.this);
                dialog.showNoInternetDialog();
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void setPreferences(int tenantId, int state) throws GeneralSecurityException, IOException {
        Tools.getEncryptedSharedPreferences(getApplicationContext()).edit()
                .putInt(Constants.TENANT_ID, tenantId).putInt(Constants.STATE, state)
                .apply();
    }

}
