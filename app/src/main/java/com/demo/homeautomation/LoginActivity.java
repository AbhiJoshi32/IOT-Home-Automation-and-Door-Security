package com.demo.homeautomation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Login Activity";

    String toastErr;

    FirebaseAuth firebaseAuth;
    @BindView(R.id.emailEdit)
    EditText emailEdit;
    @BindView(R.id.passwordEdit)
    EditText passwordEdit;
    @BindView(R.id.loginBtn)
    Button loginBtn;
    @BindView(R.id.loginProgressBar)
    ProgressBar loginProgressBar;

    @OnClick(R.id.loginBtn)
    public void loginBtnClicked() {
        Log.d(TAG,"login Btn Clicked");
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        if (email.isEmpty()) {
            emailEdit.setError("Please provide a username");
        } else if (password.isEmpty()) {
            passwordEdit.setError("Please proide a password");
        } else {
            login(email, password);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            openMainActivity();
        }
        ButterKnife.bind(this);
    }

    private void openMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void login(String email, String password) {
        showProgressBar();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    openMainActivity();
                } else {
                    hideProgressBar();
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        toastErr = e.getMessage();
                    }
                    showToastError(toastErr);
                }
            }
        });
    }

    private void showToastError(String toastErr) {
        Toast.makeText(this, toastErr, Toast.LENGTH_SHORT).show();
    }

    private void hideProgressBar() {
        emailEdit.setEnabled(true);
        passwordEdit.setEnabled(true);
        loginBtn.setEnabled(true);
        loginProgressBar.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        emailEdit.setEnabled(false);
        passwordEdit.setEnabled(false);
        loginBtn.setEnabled(false);
        loginProgressBar.setVisibility(View.VISIBLE);
    }
}