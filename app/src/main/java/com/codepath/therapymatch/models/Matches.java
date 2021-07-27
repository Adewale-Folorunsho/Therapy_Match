package com.codepath.therapymatch.models;

import com.parse.ParseUser;

import java.util.HashSet;

public class Matches {
    // <"user", null>
    public HashSet<ParseUser> matches = new HashSet<ParseUser>();

    public void addMatches(ParseUser user){
        matches.add(user);
    }

    public void removeMatches(ParseUser user){
        matches.remove(user);
    }

    public Boolean containsKey(ParseUser user){
        Boolean check = matches.contains(user);
        return check;
    }
}
