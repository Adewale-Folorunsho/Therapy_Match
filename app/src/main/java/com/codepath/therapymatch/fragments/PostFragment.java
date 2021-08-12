package com.codepath.therapymatch.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.therapymatch.MakePostActivity;
import com.codepath.therapymatch.PostsAdapter;
import com.codepath.therapymatch.R;
import com.codepath.therapymatch.models.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;


import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment {
    private String TAG = "PostFragment";
    private int MAX_ITEMS = 20;
    private final int REQUEST_CODE = 20;
    public List<Post> posts = new ArrayList<>();
    protected PostsAdapter postsAdapter;
    private SwipeRefreshLayout swipeContainer;
    FloatingActionButton btnMakePost;
    RecyclerView rvPosts;

    public PostFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        posts = new ArrayList<>();
        postsAdapter = new PostsAdapter(getContext(), posts);
        rvPosts = view.findViewById(R.id.rvPosts);
        btnMakePost = view.findViewById(R.id.btnMakePost);
        swipeContainer = view.findViewById(R.id.swipeContainer);

        rvPosts.setAdapter(postsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(linearLayoutManager);

        btnMakePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Make Post clicked");
                Intent intent = new Intent(getContext(), MakePostActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync();
                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        queryPosts();

    }

    private void fetchTimelineAsync() {
        posts.clear();
        queryPosts();
    }

    private void queryPosts(){
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.include(Post.KEY_TIME);
        query.setLimit(posts.size() - 1);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> postsFromDB, ParseException e) {
                if(e != null) {
                    Log.e(TAG, "Error getting posts " + e);
                    return;
                }
                checkInMatches(postsFromDB);
                postsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void checkInMatches(List<Post> postsFromDB) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ArrayList<ParseUser> matches = (ArrayList<ParseUser>) currentUser.get("matches");
        for (Post post: postsFromDB){
            if(post.getUser().getObjectId().equals(currentUser.getObjectId())) posts.add(post);
            if(matches != null) {
                for (ParseUser match : matches) {
                    if (post.getUser().getObjectId().equals(match.getObjectId())) {
                        posts.add(post);
                    }
                }
            }
        }
    }
}