package com.codepath.therapymatch;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

public class PostsActivity extends AppCompatActivity {
    public final static String TAG = "PostsActivity";

    Button btnLogout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts_activity);

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "User clicked to logout");
                logoutUser();
            }
        });
    }

    private void logoutUser() {
        Log.i(TAG, "User reached logout method");
        finish();

        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser();
        Toast.makeText(PostsActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(PostsActivity.this, LaunchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
