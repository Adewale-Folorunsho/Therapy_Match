package com.codepath.therapymatch.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.therapymatch.LaunchActivity;
import com.codepath.therapymatch.LoginActivity;
import com.codepath.therapymatch.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class CurrentUserProfileFragment extends Fragment {
    FragmentManager fragmentManager;
    private String TAG = "CurrentUserProfileFragment";

    Fragment fragment;
    TextView tvProfileUsername;
    ImageView ivProfilePicture;
    Button btnLogout;
    FloatingActionButton fabEditProfile;
    ParseUser user = ParseUser.getCurrentUser();

    public CurrentUserProfileFragment(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_user_profile, container, false);
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvProfileUsername = view.findViewById(R.id.tvNUProfileUsername);
        ivProfilePicture = view.findViewById(R.id.ivProfilePicture);

        tvProfileUsername.setText(user.getUsername());

        ParseFile profilePicture = user.getParseFile("profileImage");
        Glide.with(CurrentUserProfileFragment.this)
                .load(profilePicture.getUrl())
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.ic_baseline_cloud_download_24)
                .into(ivProfilePicture);

        btnLogout = view.findViewById(R.id.btnLogout);
        fabEditProfile = view.findViewById(R.id.fabEditProfile);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "User tried to logout");
                logout();
            }
        });

        fabEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new EditUserProfileFragment(fragmentManager);
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            }
        });


    }

    private void logout() {
        getActivity().finish();
        ParseUser.logOut();

        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}