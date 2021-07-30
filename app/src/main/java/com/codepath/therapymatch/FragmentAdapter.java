package com.codepath.therapymatch;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huxq17.swipecardsview.BaseCardAdapter;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import java.util.List;

public class FragmentAdapter extends BaseCardAdapter {
    List<ParseUser> users;
    Context context;
    public FragmentAdapter(List<ParseUser> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public int getCardLayoutId() {
        return R.layout.fragment_view_other_user_swipe;
    }

    @Override
    public void onBindData(int position, View cardview) {
        if(users == null || users.size() == 0){
            return;
        }
        setDataToCard(position, cardview);
    }

    private void setDataToCard(int position, View cardview) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        TextView tvUsername;
        TextView tvBio;
        ImageView ivNUserProfilePicture;
        TextView tvDistance;
        Integer distance;
        tvUsername = cardview.findViewById(R.id.tvNUProfileUsername);
        tvBio = cardview.findViewById(R.id.tvBio);
        tvDistance = cardview.findViewById(R.id.tvDistance);
        ivNUserProfilePicture = cardview.findViewById(R.id.ivNUserProfilePicture);


        ParseUser user = users.get(position);
        String username = user.getUsername();
        if(!(user.get("bio") == null)){
            String bio = user.get("bio").toString();
            tvBio.setText(bio);
        }
        ParseFile profilePicture = user.getParseFile("profileImage");
        String profilePictureUrl = profilePicture.getUrl();

        Glide.with(context).load(profilePictureUrl).placeholder(R.drawable.ic_baseline_cloud_download_24).into(ivNUserProfilePicture);
        tvUsername.setText(username);

        if(user.get("location") == null){
            tvDistance.setVisibility(View.GONE);
        }else{
            distance = distanceBetweenUsers(user);
            tvDistance.setText(distance.toString() + " miles");
        }
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
