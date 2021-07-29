package com.codepath.therapymatch.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.codepath.therapymatch.FragmentAdapter;
import com.codepath.therapymatch.PostsActivity;
import com.codepath.therapymatch.R;
import com.huxq17.swipecardsview.SwipeCardsView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ViewOtherUserProfilesFragment extends Fragment{

    private final String TAG = "ViewOtherUserProfiles";
    private SwipeCardsView swipeCardsView;
    List<ParseUser> users = new ArrayList<>();
    private int count = 0;
    private int MAX_ITEMS = 20;
    ParseUser currentUser = ParseUser.getCurrentUser();

    public ViewOtherUserProfilesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_other_user_profiles, container, false);

        swipeCardsView =  view.findViewById(R.id.swipeCardsView);
        swipeCardsView.retainLastCard(false);
        swipeCardsView.enableSwipe(true);
        queryUsers();

        GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
            private static final float SWIPE_THRESHOLD = 100;
            private static final float SWIPE_VELOCITY_THRESHOLD = 100;
            @Override
            public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
                boolean result = false;
                float diffX = moveEvent.getX() - downEvent.getX();
                float diffY = moveEvent.getY() - downEvent.getY();

                if (Math.abs(diffX) > Math.abs(diffY)) {
                    //right or left swipe
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                } else {
                    //up or down swipe
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                        result = true;
                    }
                }
                return result;

            }
        };

        GestureDetector gestureDetector = new GestureDetector(simpleOnGestureListener);

        PostsActivity.MyOnTouchListener myOnTouchListener = new PostsActivity.MyOnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent ev) {
                gestureDetector.onTouchEvent(ev);
                return false;
            }
        };
        ((PostsActivity) getActivity()).registerMyOnTouchListener(myOnTouchListener);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void checkLikes(ParseUser user){
        ArrayList<HashSet<ParseUser>> userLikes = (ArrayList<HashSet<ParseUser>>) user.get("likes");
        if(userLikes == null || userLikes.isEmpty()) {
            addLikes(user);
            return;
        }
        HashSet<ParseUser> userLikesHS = userLikes.get(0);
        if(userLikesHS.contains(currentUser)){
            addMatches(user);
        }else {
            addLikes(user);
        }
    }

    private void addMatches(ParseUser user) {
        // get other user matches
        ArrayList<HashSet<ParseUser>> userMatches = (ArrayList<HashSet<ParseUser>>) user.get("matches");
        if(userMatches == null || userMatches.isEmpty()){
            userMatches = new ArrayList<>();
        }
        HashSet<ParseUser> userMatchesHS = userMatches.get(0);
        userMatchesHS.add(user);
        userMatches.add(userMatchesHS);
        user.put("matches", userMatches);

        // get current user matches
        ArrayList<HashSet<ParseUser>> currentUserMatches = (ArrayList<HashSet<ParseUser>>) currentUser.get("Matches");
        if(currentUserMatches == null || currentUserMatches.isEmpty()){
            currentUserMatches = new ArrayList<>();
        }
        HashSet<ParseUser> currentUserMatchesHS = currentUserMatches.get(0);
        currentUserMatchesHS.add(user);
        currentUserMatches.add(userMatchesHS);
        currentUser.put("matches", currentUserMatches);

        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) Log.e(TAG, "error: " + e);
                Toast.makeText(getContext(), "User liked", Toast.LENGTH_SHORT).show();
            }
        });

        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) Log.e(TAG, "error: " + e);
            }
        });

    }

    private void addLikes(ParseUser user) {
        // get other user matches
        ArrayList<HashSet<ParseUser>> userLikes = (ArrayList<HashSet<ParseUser>>) user.get("likes");
        if(userLikes == null || userLikes.isEmpty()){
            userLikes = new ArrayList<>();
        }
        HashSet<ParseUser> userLikesHS = userLikes.get(0);
        userLikesHS.add(user);
        userLikes.add(userLikesHS);
        user.put("matches", userLikes);

        // get current user matches
        ArrayList<HashSet<ParseUser>> currentUserLikes = (ArrayList<HashSet<ParseUser>>) currentUser.get("likes");
        if(currentUserLikes == null || currentUserLikes.isEmpty()){
            currentUserLikes = new ArrayList<>();
        }
        HashSet<ParseUser> currentUserLikesHS = currentUserLikes.get(0);
        currentUserLikesHS.add(user);
        currentUserLikes.add(userLikesHS);
        currentUser.put("matches", currentUserLikes);

        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) Log.e(TAG, "error: " + e);
                Toast.makeText(getContext(), "User liked", Toast.LENGTH_SHORT).show();
            }
        });

        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) Log.e(TAG, "error: " + e);
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
                        //if(user1 != null && user1.getUsername() != null) Log.i(TAG, "User: " + user1.getUsername().toString());
                    }
                }
            }
        });
    }

    private void onSwipeLeft() {
        Log.i("Swipe" , "left");
        count += 1;
    }

    private void onSwipeBottom() {
        Log.i("Swipe" , "bottom");
        count += 1;
    }

    private void onSwipeTop() {
        Log.i("Swipe" , "up");
        count += 1;
    }

    private void onSwipeRight() {
        Log.i("Swipe" , "right");
        Log.i("Swipe" , "username: " + users.get(count).getUsername().toString());

        if(count < users.size()){
            count += 1;
            checkLikes(users.get(count));
        }
    }



//    public static class OnSwipeTouchListener implements View.OnTouchListener {
//        private final GestureDetector gestureDetector;
//        Context context;
//
//        OnSwipeTouchListener(Context ctx, View mainView) {
//            gestureDetector = new GestureDetector(ctx, new GestureListener());
//            mainView.setOnTouchListener(this);
//            context = ctx;
//        }
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            return gestureDetector.onTouchEvent(event);
//        }
//
//        public static class GestureListener extends GestureDetector.SimpleOnGestureListener {
//            private static final int SWIPE_THRESHOLD = 100;
//            private static final int SWIPE_VELOCITY_THRESHOLD = 100;
//
//            @Override
//            public boolean onDown(MotionEvent e) {
//                return true;
//            }
//
//            @Override
//            public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
//                boolean result = false;
//                try {
//                    float diffY = moveEvent.getY() - downEvent.getY();
//                    float diffX = moveEvent.getX() - downEvent.getX();
//                    if (Math.abs(diffX) > Math.abs(diffY)) {
//                        //right or left swipe
//                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
//                            if (diffX > 0) {
//                                onSwipeRight();
//                            } else {
//                                onSwipeLeft();
//                            }
//                            result = true;
//                        }
//                    } else {
//                        //up or down swipe
//                        if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
//                            if (diffY > 0) {
//                                onSwipeBottom();
//                            } else {
//                                onSwipeTop();
//                            }
//                            result = true;
//                        }
//                    }
//                } catch (Exception e) {
//                    Log.i("Some", "error: " + e);
//                }
//                return result;
//            }
//
//            private void onSwipeTop() {
//                Log.i("Some", "Swipe Up");
//            }
//
//            private void onSwipeBottom() {
//                Log.i("Some", "Swipe down");
//            }
//
//            private void onSwipeLeft() {
//                Log.i("Some", "Swipe left");
//            }
//
//            private void onSwipeRight() {
//                Log.i("Some", "Swipe right");
//            }
//
//            interface onSwipeListener {
//                void swipeRight();
//
//                void swipeTop();
//
//                void swipeBottom();
//
//                void swipeLeft();
//            }
//
//            onSwipeListener onSwipe;
//        }
//    }
}


