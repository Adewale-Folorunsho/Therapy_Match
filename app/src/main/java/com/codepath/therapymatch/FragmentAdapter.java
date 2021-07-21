package com.codepath.therapymatch;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.codepath.therapymatch.fragments.ViewOtherUserSwipeFragment;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class FragmentAdapter extends FragmentStatePagerAdapter {
    List<ParseUser> users;
    Context context;
    public FragmentAdapter(FragmentManager fm, List<ParseUser> users, Context context) {
        super(fm);
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        ViewOtherUserSwipeFragment fragment = new ViewOtherUserSwipeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Message", users.get(position).getUsername());
        ParseFile profilePicture = users.get(position).getParseFile("profileImage");
        String imageUrl = profilePicture.getUrl();
        bundle.putString("Image", imageUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return users.size();
    }
}
