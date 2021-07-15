package com.codepath.therapymatch.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codepath.therapymatch.LaunchActivity;
import com.codepath.therapymatch.MakePostActivity;
import com.codepath.therapymatch.PostsActivity;
import com.codepath.therapymatch.R;
import com.parse.ParseUser;

public class PostFragment extends Fragment {
    private String TAG = "PostFragment";

    Button btnMakePost;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnMakePost = view.findViewById(R.id.btnMakePost);
        btnMakePost.setOnClickListener(new View.OnClickListener() {
            Fragment fragment;
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Make Post clicked");
                Intent intent = new Intent(getActivity(), MakePostActivity.class);
                startActivity(intent);
            }
        });

    }
}