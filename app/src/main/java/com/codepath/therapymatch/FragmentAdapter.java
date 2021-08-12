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

import java.util.ArrayList;
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
        TextView tvIssues;
        TextView tvIssuesHeader;
        Integer distance;
        String issues = new String();
        tvUsername = cardview.findViewById(R.id.tvNUProfileUsername);
        tvIssues = cardview.findViewById(R.id.tvIssues);
        tvBio = cardview.findViewById(R.id.tvBio);
        tvDistance = cardview.findViewById(R.id.tvDistance);
        ivNUserProfilePicture = cardview.findViewById(R.id.ivNUserProfilePicture);
        tvIssuesHeader = cardview.findViewById(R.id.tvIssuesHeader);


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

        getIssues(user, issues, tvIssuesHeader, tvIssues);

        if(currentUser.get("location") == null || user.get("location") == null){
            tvDistance.setVisibility(View.GONE);
        }else{
            distance = distanceBetweenUsers(user);
            if(distance.equals(1)) tvDistance.setText(distance.toString() + " mile");
            else tvDistance.setText(distance.toString() + " miles");
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

    private void getIssues(ParseUser currentUser, String issues, TextView tvIssuesHeader, TextView tvIssues) {
        issues = "";
        ArrayList issuesList = (ArrayList) currentUser.get("issues");

        if (issuesList == null) return;
        else issues += issuesList.get(0);

        for (int position = 1; position < issuesList.size(); position++) issues += ", " + issuesList.get(position);

        if(!(issues.equals(""))){
            tvIssues.setVisibility(View.VISIBLE);
            tvIssuesHeader.setVisibility(View.VISIBLE);
            tvIssues.setText(issues);
        }
    }
}
