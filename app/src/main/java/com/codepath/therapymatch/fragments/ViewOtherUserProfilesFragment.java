package com.codepath.therapymatch.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.therapymatch.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewOtherUserProfilesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewOtherUserProfilesFragment extends Fragment {


    public ViewOtherUserProfilesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_other_user_profiles, container, false);
    }
}