package com.codepath.therapymatch;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.codepath.therapymatch.fragments.CurrentUserProfileFragment;
import com.codepath.therapymatch.fragments.PostFragment;
import com.codepath.therapymatch.fragments.ViewOtherUserProfilesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
                        fragment = new ViewOtherUserProfilesFragment();
                        break;
                    case R.id.actionUserProfile:
                        fragment = new CurrentUserProfileFragment(fragmentManager);
                        break;
                    case R.id.actionPosts:
                    default:
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

