package com.codepath.therapymatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.parse.ParseUser;

public class LaunchActivity extends AppCompatActivity {
    public final static String TAG = "LaunchActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(ParseUser.getCurrentUser() != null){
            Intent intent = new Intent(LaunchActivity.this, PostsActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.launch_activity);

        Button btnLogin;
        Button btnSignup;

        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Login from Launch clicked");
                Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Login from Launch clicked");
                Intent intent = new Intent(LaunchActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}