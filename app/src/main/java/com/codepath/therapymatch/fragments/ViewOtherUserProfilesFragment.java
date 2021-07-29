package com.codepath.therapymatch.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.codepath.therapymatch.FragmentAdapter;
import com.codepath.therapymatch.R;
import com.huxq17.swipecardsview.SwipeCardsView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ViewOtherUserProfilesFragment extends Fragment{

    private final String TAG = "ViewOtherUserProfiles";
    private SwipeCardsView swipeCardsView;
    FragmentAdapter fragmentAdapter;
    List<ParseUser> users = new ArrayList<>();
    private int MAX_ITEMS = 20;
    private GestureDetector gestureDetector;

    public ViewOtherUserProfilesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_other_user_profiles, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeCardsView = view.findViewById(R.id.swipeCardsView);
        swipeCardsView.retainLastCard(false);
        swipeCardsView.enableSwipe(true);

        queryUsers();
    }



    private void addLikes(ParseUser user) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ArrayList<ParseUser> userLikes = (ArrayList<ParseUser>) currentUser.get("likes");
        if (userLikes == null) userLikes = new ArrayList<>();
        userLikes.add(user);
        //for (ParseUser user1: userLikes) Log.i(TAG, "liked user: "+ user1.getUsername().toString() );
        currentUser.put("likes", userLikes);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Toast.makeText(getContext(), "Likes updated", Toast.LENGTH_SHORT).show();
                }else{
                    Log.e(TAG, "Error: " + e);
                }
            }
        });

    }


    private void queryUsers(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseGeoPoint currentUserLocation = (ParseGeoPoint) currentUser.get("location");
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.whereWithinMiles("location", currentUserLocation, 10);
        query.setLimit(MAX_ITEMS);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> usersFromDB, ParseException e) {
                if(e != null){
                    e.printStackTrace();
                    Log.e(TAG, "Error getting posts");
                    return;
                }else{
                    users.addAll(usersFromDB);
                    FragmentAdapter fragmentAdapter = new FragmentAdapter(users, getContext());
                    swipeCardsView.setAdapter(fragmentAdapter);
                    for(ParseUser user1: users){
                        if(user1 != null && user1.getUsername() != null) Log.i(TAG, "User: " + user1.getUsername().toString());
                    }
                }
            }
        });
    }



    public class RotateUpPageTransformer implements ViewPager.PageTransformer{
        private static final float ROTATION = -15f;

        @Override
        public void transformPage( View page, float pos ) {
            final float width = page.getWidth();
            final float height = page.getHeight();
            final float rotation = ROTATION * pos * -1.25f;

            page.setPivotX( width * 0.5f );
            page.setPivotY( height );
            page.setRotation( rotation );

        }
    }



//    private class SwipeListener implements View.OnTouchListener{
//        GestureDetector gestureDetector;
//
//        SwipeListener(View view){
//            int threshold = 100;
//            int velocity_threshold = 100;
//
//            GestureDetector.SimpleOnGestureListener listener =
//                    new GestureDetector.SimpleOnGestureListener(){
//                        @Override
//                        public boolean onDown(MotionEvent e){
//                            return true;
//                        }
//
//                        @Override
//                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                            float xDiff = e2.getX() - e1.getX();
//                            float yDiff = e2.getY() - e1.getY();
//                            try {
//                                if(Math.abs(xDiff) > Math.abs(yDiff)){
//                                    if(Math.abs(xDiff) > threshold && Math.abs(velocityX) > velocity_threshold){
//                                        if(xDiff > 0){
//                                            Log.i(TAG, "Swiped right");
//                                        }else{
//                                            Log.i(TAG, "Swiped left");
//                                        }
//                                        return true;
//                                    }else {
//                                        if(Math.abs(yDiff) > threshold && Math.abs(velocityY) > velocity_threshold){
//                                            if(yDiff > 0){
//                                                Log.i(TAG, "Swiped down");
//                                            }else {
//                                                Log.i(TAG, "Swiped up");
//                                            }
//                                            return true;
//                                        }
//                                    }
//                                }
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }
//                            return false;
//                        }
//                    };
//            gestureDetector = new GestureDetector(listener);
//            view.setOnTouchListener(this);
//        }
//    }
}


