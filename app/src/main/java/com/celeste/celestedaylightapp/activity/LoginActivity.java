package com.celeste.celestedaylightapp.activity;

import android.content.Context;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.data.Tools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private AppCompatEditText editEmail, editPassword;
    //    @BindView(R.id.btn_login)
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
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                // if (user != null) {
                //  startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                } else
//
//                {
//                    // do something
//                }
            }
        };
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                submitForm();
//                hideKeyboard();
//            }
//        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // submitForm();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                hideKeyboard();
            }
        });
      btnRegister.setOnClickListener(new View.OnClickListener()
      {
          @Override
          public void onClick(View view) {
             startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
          }
      });
        Tools.systemBarLolipop(this);
    }

    private void login() {
        String userEmailString = editEmail.getText().toString().trim();
        String userPasswordString = editPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(userEmailString) && !TextUtils.isEmpty(userPasswordString)) {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(userEmailString, userPasswordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // SharedPreferences.Editor editor = getSharedPreferences(Constants.USER_ID, MODE_PRIVATE).edit();
                        // editor.putString(Constants.USER_KEY, mAuth.getCurrentUser().getUid());
                        // editor.apply();

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Snackbar.make(parentView, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    private void setupUI() {
        parentView = findViewById(android.R.id.content);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        btnLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        editEmail.addTextChangedListener(new MyTextWatcher(editEmail));
       // btnRegister = findViewById(R.id.btnRegister);
        btnRegister = findViewById(R.id.createAccount);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Validating form
     */
    private void submitForm() {

        if (!validateEmail()) {
            return;
        }
        login();
    }

    private boolean validateEmail() {
        String email = editEmail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            editEmail.setError(getString(R.string.err_msg_email));
            requestFocus(editEmail);
            return false;
        } else {

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
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
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
//            switch (view.getId()) {
//                case R.id.edit_email:
//                    validateEmail();
//                    break;
//            }
        }
    }
}
