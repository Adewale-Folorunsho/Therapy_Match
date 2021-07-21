package com.codepath.therapymatch.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.therapymatch.R;

public class ViewOtherUserSwipeFragment extends Fragment {

    TextView tvUsername;
    ImageView profilePicture;

    public ViewOtherUserSwipeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_other_user_swipe, container, false);

        tvUsername = view.findViewById(R.id.tvNUProfileUsername);
        profilePicture = view.findViewById(R.id.ivNUserProfilePicture);

        String message = getArguments().getString("Message");
        String profilePictureUrl = getArguments().getString("Image");

        Glide.with(getContext()).load(profilePictureUrl).placeholder(R.drawable.ic_baseline_cloud_download_24).into(profilePicture);
        tvUsername.setText(message);

        return view;
    }
}