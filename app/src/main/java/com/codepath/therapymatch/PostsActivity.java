package com.codepath.therapymatch;

import android.content.Intent;
import android.graphics.ComposePathEffect;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.codepath.therapymatch.fragments.CurrentUserProfileFragment;
import com.codepath.therapymatch.fragments.PostFragment;
import com.codepath.therapymatch.fragments.ViewOtherUserProfilesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class PostsActivity extends AppCompatActivity {
    final FragmentManager fragmentManager = getSupportFragmentManager();

    public final static String TAG = "PostsActivity";

    private BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts_activity);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.actionOtherUserProfile:
                        Log.i(TAG, "actionOtherUserProfile Clicked");
                        Toast.makeText(PostsActivity.this, "actionOtherUserProfile", Toast.LENGTH_SHORT).show();
                        fragment = new ViewOtherUserProfilesFragment();
                        break;
                    case R.id.actionUserProfile:
                        Log.i(TAG, "actionUserProfile Clicked");
                        Toast.makeText(PostsActivity.this, "User Profile", Toast.LENGTH_SHORT).show();
                        fragment = new CurrentUserProfileFragment();
                        break;
                    case R.id.actionPosts:
                    default:
                        Log.i(TAG, "actionPosts Clicked");
                        Toast.makeText(PostsActivity.this, "actionPosts", Toast.LENGTH_SHORT).show();
                        fragment = new PostFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.actionPosts);
    }


}

