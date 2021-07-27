package com.codepath.therapymatch.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.codepath.therapymatch.FragmentAdapter;
import com.codepath.therapymatch.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

public class ViewOtherUserProfilesFragment extends Fragment {

    private final String TAG = "ViewOtherUserProfiles";
    FragmentAdapter fragmentAdapter;
    List<ParseUser> users;
    ViewPager viewPager;
    private int MAX_ITEMS = 20;

    public ViewOtherUserProfilesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_other_user_profiles, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.viewPager);
        users = new ArrayList<>();
        queryUsers();
        fragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager() , users, getContext());

        viewPager.setAdapter(fragmentAdapter);
        viewPager.setPageTransformer(true, new RotateUpPageTransformer());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            float tempPositionOffset = 0;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    if (tempPositionOffset < positionOffset){
                        //Toast.makeText(getContext(), "Scrolling left", Toast.LENGTH_SHORT).show();
                    }
                    if (tempPositionOffset > positionOffset){
                        Toast.makeText(getContext(), "Scrolling right", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
                }
                users.addAll(usersFromDB);
                fragmentAdapter.notifyDataSetChanged();
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
}
