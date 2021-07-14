package com.codepath.therapymatch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    public final static String TAG = "LoginActivity";
    EditText etUsername;
    EditText etPassword;
    Button btnLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        if(ParseUser.getCurrentUser() != null){
          viewOtherUserPosts();
        }
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                Log.i(TAG, "User tried logging in");
                loginUser(username, password);
            }
        });
    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "User tried to login");
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null) {
                    Log.e(TAG, "Issue with Login");
                    return;
                }
                viewOtherUserPosts();
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void  viewOtherUserPosts() {
        Intent intent = new Intent(LoginActivity.this, PostsActivity.class);
        startActivity(intent);
        finish();
    }
}
