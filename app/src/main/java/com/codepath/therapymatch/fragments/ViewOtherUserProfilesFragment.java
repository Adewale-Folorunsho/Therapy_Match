package com.codepath.therapymatch.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.codepath.therapymatch.FragmentAdapter;
import com.codepath.therapymatch.PostsActivity;
import com.codepath.therapymatch.R;
import com.codepath.therapymatch.models.Item;
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
    private int MAX_ITEMS = 20;
    private int MAX_DISTANCE = 100;
    private int count = 0;
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

    private void queryUsers(){
        count = 0;
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseGeoPoint currentUserLocation = (ParseGeoPoint) currentUser.get("location");
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.setLimit(MAX_ITEMS);
        // if(currentUserLocation != null) query.whereWithinMiles("location", currentUserLocation, MAX_DISTANCE);

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> usersFromDB, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error getting users: " + e);
                    return;
                }else{
                    users.clear();
                    for(ParseUser user: usersFromDB) {
                        if (!(inCurrentUserLikes(user))) {
                            if ((!currentUser.getObjectId().equals(user.getObjectId()))) {
                                Log.i(TAG, user.getUsername().toString());
                                users.add(user);
                            }
                        }else{
                            if(inOtherUserLikes(user) && !(inCurrentUserMatches(user))){
                                addToMatches(user);
                            }
                        }
                    }
                }

                    if(currentUser.get("issues") != null) sortUsersByIssues(users);
                    FragmentAdapter fragmentAdapter = new FragmentAdapter(users, getContext());
                    swipeCardsView.setAdapter(fragmentAdapter);
            }
        });
    }

    private void onSwipeLeft() {
        Log.i("Swipe" , "left");
        count += 1;
        if(count >= users.size()){
            MAX_DISTANCE += 10000;
            queryUsers();
        }
    }

    private void onSwipeBottom() {
        Log.i("Swipe" , "bottom");
        count += 1;
        if(count >= users.size()){
            users.clear();
            MAX_DISTANCE += 30;
            queryUsers();
        }
    }

    private void onSwipeTop() {
        Log.i("Swipe" , "up");
        count += 1;
        if(count >= users.size()){
            users.clear();
            MAX_DISTANCE += 30;
            queryUsers();
        }
    }

    private void onSwipeRight() {
        Log.i("Swipe" , "right");
        Log.i("Swipe" , "username: " + users.get(count).getUsername().toString());
        inLikesOrMatches(users.get(count));
        count += 1;
        if(count >= users.size()){
            users.clear();
            MAX_DISTANCE += 30;
            queryUsers();
        }
    }

    // if in other user likes, match. else add other user to likes
    private void inLikesOrMatches(ParseUser likedUser) {
        ArrayList<ParseUser> currentUserLikes = new ArrayList<>();
        if(inOtherUserLikes(likedUser)) {
            addToMatches(likedUser);
        }
        addToLikes(likedUser);
    }

    // check if current user has already been liked the liked user
    private boolean inOtherUserLikes(ParseUser likedUser) {
        ArrayList<ParseUser> userLikes = new ArrayList<ParseUser>();
        ParseUser user = new ParseUser();
        if(likedUser.get("likes") != null){
            userLikes = (ArrayList<ParseUser>) likedUser.get("likes");
        }
        for (int position = 0; position < userLikes.size(); position++){
            user = userLikes.get(position);
            if(user.getObjectId().equals(currentUser.getObjectId())) return true;
        }
        return false;
    }

    //add to current user list of matches
    private void addToMatches(ParseUser matchedUser) {
        currentUser.add("matches", matchedUser);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    Log.e(TAG, "error: " + e);
                    return;
                }
                else{
                    Log.i(TAG, "user matched");
                }
            }
        });
    }

    //add to current user list of likes
    private void addToLikes(ParseUser likedUser) {
        currentUser.add("likes", likedUser);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    Log.e(TAG, "error: " + e);
                    return;
                }
                else{
                    Log.i(TAG, "user liked");
                }
            }
        });
    }

    private boolean inCurrentUserLikes(ParseUser user) {
        ArrayList<ParseUser> currentUserLikes = new ArrayList<>();
        if(currentUser.get("likes") != null) currentUserLikes = (ArrayList<ParseUser>) currentUser.get("likes");
        for (ParseUser likedUser: currentUserLikes){
            if(user.getObjectId().equals(likedUser.getObjectId())) return true;
        }
        return false;
    }

    private boolean inCurrentUserMatches(ParseUser user) {
        ArrayList<ParseUser> currentUserMatches = new ArrayList<>();
        if(currentUser.get("matches") != null) currentUserMatches = (ArrayList<ParseUser>) currentUser.get("matches");
        for (ParseUser matchedUser: currentUserMatches){
            if(user.getObjectId().equals(matchedUser.getObjectId())) return true;
        }
        return false;
    }

    private void sortUsersByIssues(List<ParseUser> users) {
        ArrayList<String> currentUserIssues = (ArrayList<String>) currentUser.get("issues");
        ArrayList<Item> sortedItems = new ArrayList<>();
        ArrayList<Item> listedItems = new ArrayList<>();

        //listUsers -> ListItems
        listedItems = convertToListItems(currentUserIssues,users);

        sortedItems = bubbleSort(listedItems,users);

        //listitems -> listUsers
        users = convertToListUsers(sortedItems);


    }

    private List<ParseUser> convertToListUsers(ArrayList<Item> sortedItems) {
        ArrayList<ParseUser> users = new ArrayList<>();
        for(Item item: sortedItems){
            users.add(0, item.getUser());
        }
        return users;
    }

    private ArrayList<Item> bubbleSort(ArrayList<Item> listedItems, List<ParseUser> users) {
        int n = listedItems.size();
        int i, j;
        Item temp;
        boolean swapped;
        for (i = 0; i < n - 1; i++)
        {
            swapped = false;
            for (j = 0; j < n - i - 1; j++)
            {
                if (listedItems.get(j).getnoOfCommonIssues() > listedItems.get(j + 1).getnoOfCommonIssues())
                {
                    // swap arr[j] and arr[j+1]
                    temp = listedItems.get(j);
                    listedItems.set(j, listedItems.get(j + 1));
                    listedItems.set(j + 1, temp);
                    swapped = true;
                }
            }

            if (swapped == false)
                break;
        }
        return listedItems;
    }

    private ArrayList<Item> convertToListItems(ArrayList<String> currentUserIssues, List<ParseUser> users) {
        ArrayList<Item> currentUserIssuesList = new ArrayList<>();
        ArrayList<String> userIssues = new ArrayList<>();
        HashSet<String> HSCurrentUser = new HashSet<>();
        HashSet<String> HSUser = new HashSet<>();
        Integer commonIssues;
        for(String issue: currentUserIssues){
            HSCurrentUser.add(issue);
        }
        for(ParseUser user: users){
            if (user.get("issues") != null) {
                userIssues = (ArrayList<String>) user.get("issues");

                HSUser = new HashSet<>();
                for (String issue : userIssues) {
                    HSUser.add(issue);
                }
                HSUser.retainAll(HSCurrentUser);
                commonIssues = HSUser.size();
            }else{
                commonIssues = 0;
            }
            Item item = new Item(commonIssues, user);
            currentUserIssuesList.add(item);
        }
        return currentUserIssuesList;
    }
}


