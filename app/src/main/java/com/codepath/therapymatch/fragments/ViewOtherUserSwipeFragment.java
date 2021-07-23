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
import com.parse.ParseFile;
import com.parse.ParseUser;

public class ViewOtherUserSwipeFragment extends Fragment {

    TextView tvUsername;
    TextView tvBio;
    ImageView ivNUserProfilePicture;

    public ViewOtherUserSwipeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_other_user_swipe, container, false);

        tvUsername = view.findViewById(R.id.tvNUProfileUsername);
        tvBio = view.findViewById(R.id.tvBioHeader);
        ivNUserProfilePicture = view.findViewById(R.id.ivNUserProfilePicture);

        ParseUser user = getArguments().getParcelable("User");
        String username = user.getUsername();
        if(!(user.get("bio") == null)){
            String bio = user.get("bio").toString();
            tvBio.setText(bio);
        }
        ParseFile profilePicture = user.getParseFile("profileImage");
        String profilePictureUrl = profilePicture.getUrl();

        Glide.with(getContext()).load(profilePictureUrl).placeholder(R.drawable.ic_baseline_cloud_download_24).into(ivNUserProfilePicture);
        tvUsername.setText(username);


        return view;
    }
}