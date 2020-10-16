package com.celeste.celestedaylightapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TenantLogin extends AppCompatActivity {
    private static final String PREFS_NAME = "Celeste_PrefsFile";
    Api api = ApiClient.getInstance(this).create(Api.class);
    TenantLoginModel tenantLoginModel = new TenantLoginModel();
    EditText edit_tenancyName;
    Button btnLogin;
    ProgressBar progressBar;
    String tenantId;
    TextView createAccount, forgotPassword;
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
        createAccount = findViewById(R.id.createAccount);
        forgotPassword = findViewById(R.id.forgotPassword);
    }

    private void login() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                if (isNetworkAvailable()) {
                    isTenantAvailable();
                } else {
                    tenantId = EasyPreference.with(getApplicationContext()).getString(Constants.CREDENTIALS, "");
                    if (tenantId != null) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                }
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
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
                                EasyPreference.with(getApplicationContext()).addObject(Constants.TENANT_ID, response.body().getResult().getTenantId()).save();
                                setPreferences(response.body().getResult().getTenantId(), edit_tenancyName.getText().toString(), response.body().getResult().getState().ordinal());
                                startActivity(new Intent(TenantLogin.this, LoginActivity.class));
                                finish();
                            } catch (GeneralSecurityException | IOException ex) {
                                ex.printStackTrace();
                                TastyToast.makeText(getApplicationContext(), "Tenant ID " + response.body().getResult().getTenantId(), TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show();
                            }
                            progressBar.setVisibility(View.GONE);
                            break;
                        case IN_ACTIVE:
                            TastyToast.makeText(getApplicationContext(), "This user is not active yet, please contact admin", TastyToast.LENGTH_LONG,TastyToast.INFO).show();
                            progressBar.setVisibility(View.GONE);
                            break;
                        case NOT_FOUND:
                            TastyToast.makeText(getApplicationContext(), "User Not Found,Please make sure you are registered", TastyToast.LENGTH_LONG,TastyToast.INFO).show();
                            progressBar.setVisibility(View.GONE);
                            break;
                    }

                }
            }

            @Override
            public void onFailure(Call<TenantResponse> call, Throwable t) {
                //  Toast.makeText(getApplicationContext(), "Tenant Not Found" + t.getMessage(), Toast.LENGTH_LONG).show();
                final InternetDialog dialog = new InternetDialog(TenantLogin.this);
                dialog.showNoInternetDialog();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void setPreferences(int tenantId, String tenantName, int state) throws GeneralSecurityException, IOException {
        Tools.getEncryptedSharedPreferences(getApplicationContext()).edit()
                .putInt(Constants.TENANT_ID, tenantId).putInt(Constants.STATE, state)
                .putString(Constants.TENANT_NAME, tenantName)
                .apply();
    }


}
