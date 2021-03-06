package com.celeste.celestedaylightapp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.fragment.InternetDialog;
import com.celeste.celestedaylightapp.fragment.UsersFragment;
import com.celeste.celestedaylightapp.model.authenticate.AuthenticateModel;
import com.celeste.celestedaylightapp.model.authenticate.AuthenticateResponse;
import com.celeste.celestedaylightapp.model.authenticate.AuthenticateResult;
import com.celeste.celestedaylightapp.model.modes.Mode;
import com.celeste.celestedaylightapp.model.modes.UserModeModel;
import com.celeste.celestedaylightapp.model.user.GetSingleUserResponse;
import com.celeste.celestedaylightapp.model.user.UserModel;
import com.celeste.celestedaylightapp.retrofit.Api;
import com.celeste.celestedaylightapp.retrofit.ApiClient;
import com.celeste.celestedaylightapp.retrofit.NoConnectivityException;
import com.celeste.celestedaylightapp.sqllitedb.SQLLiteOpenHelper;
import com.celeste.celestedaylightapp.utils.Constants;
import com.celeste.celestedaylightapp.utils.InputValidation;
import com.celeste.celestedaylightapp.utils.Tools;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.iamhabib.easy_preference.EasyPreference;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "Celeste_PrefsFile";
    private final AppCompatActivity activity = LoginActivity.this;
    AttemptLoginTask loginTask = null;
    Api apiService = ApiClient.getInstance(this).create(Api.class);
    SharedPreferences.Editor editor;
    String username;
    String password;
    String credentials;
    private TextInputEditText editEmail, editPassword;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private Button btnLogin;
    private TextView btnRegister;
    ProgressBar progressBar;
    private SharedPreferences mSharedPrefs;
    private SQLLiteOpenHelper databaseHelper;
    private InputValidation inputValidation;
    private RelativeLayout login;
    private UserModel userModel;
    private Mode mode = new Mode();
    private com.celeste.celestedaylightapp.sqllitedb.DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logins_layout);
        setupUI();
        initObjects();
        if (isNetworkAvailable()) {
            AuthenticateResponse response = EasyPreference.with(getApplicationContext()).getObject(Constants.CREDENTIALS, AuthenticateResponse.class);
            if (response != null) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        } else {
            credentials = EasyPreference.with(getApplicationContext()).getString(Constants.USERNAME, null);
            if (credentials != null) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }
        btnLogin.setOnClickListener(view -> login());
        btnRegister.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), RegisterActivity.class)));
        Tools.systemBarLolipop(this);
    }

    private void loadUserInfo(int Id) {
        progressBar.setVisibility(View.VISIBLE);
        Call<GetSingleUserResponse> call = apiService.getSingleUser(Id);
        call.enqueue(new Callback<GetSingleUserResponse>() {
            @Override
            public void onResponse(Call<GetSingleUserResponse> call, Response<GetSingleUserResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getResult() != null) {
                        userModel = response.body().getResult();
                        List<UserModeModel> modesList = userModel.getUserModes();
                        for (UserModeModel modes : modesList) {
                            mode = modes.getMode();
                            insertToDb(mode);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "" + response.code(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "" + response.code(), Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GetSingleUserResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void insertToDb(Mode mode) {
        if (mode.getName() != null) {
            if (!dbHelper.recordExists(mode.getName())) {
                boolean inserted = dbHelper.insertUserMode(mode);
                if (inserted) {
                    Toast.makeText(getApplicationContext(), "Saved to db" + mode.getName(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Mode exists" + mode.getName(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            TastyToast.makeText(getApplicationContext(), "It appears you have not been assigned any modes yet", TastyToast.LENGTH_LONG, TastyToast.CONFUSING).show();
        }
    }

    private void getPreferencesData() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (sharedPreferences.contains("pref_username") && sharedPreferences.contains("pref_password")) {
            username = sharedPreferences.getString("pref_username", "not found");
            password = sharedPreferences.getString("pref_password", "invalid");
            editEmail.setText(username);
            editPassword.setText(password);
        }
    }

    private void setupUI() {
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        btnLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        textInputEmail = findViewById(R.id.textInputEmail);
        textInputPassword = findViewById(R.id.textInputPassword);
        btnRegister = findViewById(R.id.createAccount);
        login = findViewById(R.id.login);
        mSharedPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    private void initObjects() {
        dbHelper = new com.celeste.celestedaylightapp.sqllitedb.DatabaseHelper(getApplicationContext());
        databaseHelper = new SQLLiteOpenHelper(activity);
        inputValidation = new InputValidation(activity);
    }

    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(editEmail, textInputEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(editEmail, textInputEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(editPassword, textInputPassword, getString(R.string.error_message_email))) {
            return;
        }
        if (databaseHelper.checkUser(editEmail.getText().toString().trim()
                , editPassword.getText().toString().trim())) {
            Intent accountsIntent = new Intent(activity, UsersFragment.class);
            accountsIntent.putExtra("EMAIL", editEmail.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);
        } else {
            Snackbar.make(login, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText() {
        editEmail.setText(null);
        editPassword.setText(null);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
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
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void login() {
        if (isNetworkAvailable()) {
            if (!validateEmail()) {
                return;
            }
            if (!validatePassword()) {
                return;
            }
            loginTask = new AttemptLoginTask(editEmail.getText().toString().trim(), editPassword.getText().toString().trim());
            loginTask.execute();
        } else {
            getPreferencesData();
            if (editEmail.getText().toString().trim().equals(username) && editPassword.getText().toString().trim().equals(password)) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AttemptLoginTask extends AsyncTask<String, String, String> {

        private final String usernameOrEmailAddress;
        private final String password;

        public AttemptLoginTask(String usernameOrEmailAddress, String password) {
            this.usernameOrEmailAddress = usernameOrEmailAddress;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            hideKeyboard();
            progressBar.setVisibility(View.VISIBLE);
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
                            Log.d("TAG", "index" + response.errorBody());
                            progressBar.setVisibility(View.GONE);
                        } else if (response.body() != null && response.code() == 200) {
                            try {
                                EasyPreference.with(getApplicationContext()).addObject(Constants.CREDENTIALS, response.body().getResult()).save();
                                setPreferences(usernameOrEmailAddress, password, response.body().getResult().getAccessToken());
                                editor = mSharedPrefs.edit();
                                editor.putString("pref_username", editEmail.getText().toString().trim());
                                editor.putString("pref_password", editPassword.getText().toString().trim());
                                editor.putString("pref_token", response.body().getResult().getAccessToken());
                                editor.apply();
                                loadUserInfo(response.body().getResult().getUserId());
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("userId", response.body().getResult().getUserId());
                                EasyPreference.with(getApplicationContext()).addInt(Constants.USERID, response.body().getResult().getUserId()).save();
                                startActivity(intent);
                                finish();
                            } catch (GeneralSecurityException | IOException ex) {
                                ex.printStackTrace();
                                Toast.makeText(getApplicationContext(), "" + ex.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthenticateResult> call, Throwable t) {
                        final InternetDialog dialog = new InternetDialog(LoginActivity.this);
                        if (t instanceof NoConnectivityException) {
                            dialog.showNoInternetDialog();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private void setPreferences(String userName, String password, String accessToken) throws GeneralSecurityException, IOException {
            Tools.getEncryptedSharedPreferences(getApplicationContext()).edit()
                    .putString(Constants.USERNAME, userName)
                    .putString(Constants.PASSWORD, password)
                    .putString(Constants.ACCESS_TOKEN, accessToken)
                    .apply();
        }

        @Override
        protected void onPostExecute(String s) {
            btnLogin.setVisibility(View.VISIBLE);

            hideKeyboard();
            super.onPostExecute(s);
        }
    }
}
