package com.codepath.therapymatch.models;

import com.parse.ParseUser;

import java.util.HashSet;

public class Likes {
    // <"user", null>
    public HashSet<ParseUser> likes = new HashSet<ParseUser>();

    public void addLikes(ParseUser user){
        likes.add(user);
    }

    public void removeLikes(ParseUser user){
        likes.remove(user);
    }

    public Boolean containsKey(ParseUser user){
        Boolean check = likes.contains(user);
        return check;
    }
}
