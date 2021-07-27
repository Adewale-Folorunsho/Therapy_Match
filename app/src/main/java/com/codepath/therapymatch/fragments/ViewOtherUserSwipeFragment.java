package com.codepath.therapymatch.fragments;

import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.therapymatch.R;
import com.codepath.therapymatch.models.Likes;
import com.codepath.therapymatch.models.Matches;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

public class ViewOtherUserSwipeFragment extends Fragment {
    ParseUser currentUser = ParseUser.getCurrentUser();
    TextView tvUsername;
    TextView tvBio;
    ImageView ivNUserProfilePicture;
    TextView tvDistance;

    public ViewOtherUserSwipeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_other_user_swipe, container, false);

        Integer distance;
        tvUsername = view.findViewById(R.id.tvNUProfileUsername);
        tvBio = view.findViewById(R.id.tvBio);
        tvDistance = view.findViewById(R.id.tvDistance);
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

        if(user.get("location") == null){
            tvDistance.setVisibility(View.GONE);
        }else{
            distance = distanceBetweenUsers(user);
            tvDistance.setText(distance.toString() + " miles");
        }

        ParseUser likedUser = getArguments().getParcelable("like");
        if(likedUser != null){
            if(checkOtherUserLikes(likedUser)){
                matchBothUsers(likedUser);
            }else{
                addToLikes(likedUser);
            }
        }
        return view;
    }

    private void addToLikes(ParseUser likedUser) {
        Likes currentUserLikes = (Likes) currentUser.get("likes");
        if (currentUserLikes == null) currentUserLikes = new Likes();
        currentUserLikes.addLikes(likedUser);
        currentUser.put("likes", currentUserLikes);
    }

    private void matchBothUsers(ParseUser likedUser) {
        Matches otherUserMatches = (Matches) likedUser.get("matches");
        Matches currentUserMatches = (Matches) currentUser.get("matches");
        if(otherUserMatches == null) otherUserMatches = new Matches();
        if (currentUserMatches == null) currentUserMatches = new Matches();

        otherUserMatches.addMatches(currentUser);
        likedUser.put("matches", otherUserMatches);

        currentUserMatches.addMatches(likedUser);
        currentUser.put("matches", currentUserMatches);
    }

    private boolean checkOtherUserLikes(ParseUser likedUser) {
        Likes otherUserLikes = (Likes) likedUser.get("likes");
        if(otherUserLikes == null) return false;
        if(otherUserLikes.containsKey(currentUser)) return true;
        return false;
    }

    private Integer distanceBetweenUsers(ParseUser user) {
        ParseGeoPoint currentUserLocation;
        ParseGeoPoint otherUserLocation;
        Number distanceInMiles;
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUserLocation = (ParseGeoPoint) currentUser.get("location");
        otherUserLocation = (ParseGeoPoint) user.get("location");
        distanceInMiles = currentUserLocation.distanceInMilesTo(otherUserLocation);

        return distanceInMiles.intValue();
    }

}