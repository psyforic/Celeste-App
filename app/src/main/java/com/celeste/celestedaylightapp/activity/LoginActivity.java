package com.celeste.celestedaylightapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.data.Tools;
import com.celeste.celestedaylightapp.model.authenticate.AuthenticateModel;
import com.celeste.celestedaylightapp.model.authenticate.AuthenticateResponse;
import com.celeste.celestedaylightapp.model.authenticate.AuthenticateResult;
import com.celeste.celestedaylightapp.retrofit.Api;
import com.celeste.celestedaylightapp.retrofit.ApiClient;
import com.celeste.celestedaylightapp.utils.Constants;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.iamhabib.easy_preference.EasyPreference;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    AttemptLoginTask loginTask = null;
    Api apiService = ApiClient.getInstance().create(Api.class);
    private AppCompatEditText editEmail, editPassword;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private Button btnLogin;
    private TextView btnRegister;
    private ProgressBar progressBar;
    private View parentView;

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  ButterKnife.bind(this);
        setContentView(R.layout.logins_layout);
        setupUI();
        AuthenticateResponse response = EasyPreference.with(getApplicationContext()).getObject(Constants.CREDENTIALS, AuthenticateResponse.class);
        if (response != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateEmail()) {
                    return;
                }
                if (!validatePassword()) {
                    return;
                }
                loginTask = new AttemptLoginTask(editEmail.getText().toString(), editPassword.getText().toString());
                loginTask.execute();
                // submitForm();
                //               startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                hideKeyboard();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
        Tools.systemBarLolipop(this);
    }

    private void setupUI() {
        parentView = findViewById(android.R.id.content);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        btnLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        editEmail.addTextChangedListener(new MyTextWatcher(editEmail));
        textInputEmail = findViewById(R.id.textInputEmail);
        textInputPassword = findViewById(R.id.textInputPassword);
        btnRegister = findViewById(R.id.createAccount);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean validateEmail() {
        String email = Objects.requireNonNull(editEmail.getText()).toString().trim();
        if (email.isEmpty()) {
            textInputEmail.setError(getString(R.string.err_msg_email));
            requestFocus(editEmail);
            return false;
        } else {
            textInputEmail.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {
        if (editPassword.getText().toString().trim().isEmpty()) {
            textInputPassword.setError(getString(R.string.err_msg_password));
            requestFocus(editPassword);
            return false;
        } else {
            textInputPassword.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //      mAuth.addAuthStateListener(mAuthListener);
    }

    //    private void login() {
//        if (!validateEmail()) {
//            return;
//        }
//        if (!validatePassword()) {
//            return;
//        }
//        loginTask = new AttemptLoginTask(editEmail.getText().toString(), editPassword.getText().toString());
//        loginTask.execute();
//    }
    private class AttemptLoginTask extends AsyncTask<String, String, String> {

        private String usernameOrEmailAddress;
        private String password;

        public AttemptLoginTask(String usernameOrEmailAddress, String password) {
            this.usernameOrEmailAddress = usernameOrEmailAddress;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
//            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                AuthenticateModel userLogin = new AuthenticateModel(usernameOrEmailAddress, password);
                userLogin.setRememberClient(true);
                Call<AuthenticateResult> call = apiService.authenticateUser(userLogin);
                call.enqueue(new Callback<AuthenticateResult>() {
                    @Override
                    public void onResponse(Call<AuthenticateResult> call, Response<AuthenticateResult> response) {
                        if (response.code() == 500) {
                            Toast.makeText(getApplicationContext(), "username or password incorrect", Toast.LENGTH_LONG).show();
                            //   progressBar.setVisibility(View.GONE);
                        } else if (response.code() == 200) {
                            if (response.body() != null) {
                                EasyPreference.with(getApplicationContext()).addObject(Constants.CREDENTIALS, response.body().getResult()).save();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                //     progressBar.setVisibility(View.GONE);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthenticateResult> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            btnLogin.setVisibility(View.VISIBLE);
            hideKeyboard();
            super.onPostExecute(s);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {

        }
    }
}
