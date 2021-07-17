package com.codepath.therapymatch.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.codepath.therapymatch.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class CurrentUserProfileFragment extends Fragment {
    private String TAG = "CurrentUserProfileFragment";

    TextView tvProfileUsername;
    ImageView ivProfilePicture;
    Button btnLogout;

    ParseUser user = ParseUser.getCurrentUser();

    public CurrentUserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_user_profile, container, false);
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvProfileUsername = view.findViewById(R.id.tvProfileUsername);
        ivProfilePicture = view.findViewById(R.id.ivProfilePicture);

        tvProfileUsername.setText(user.getUsername());

        ParseFile profilePicture = user.getParseFile("profileImage");
        Glide.with(CurrentUserProfileFragment.this)
                .load(profilePicture.getUrl())
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.ic_baseline_cloud_download_24)
                .into(ivProfilePicture);

        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "User tried to logout");
                logout();
            }
        });
    }

    private void logout() {
        getActivity().finish();
        ParseUser.logOut();

        Intent intent = new Intent(getContext(), LaunchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}