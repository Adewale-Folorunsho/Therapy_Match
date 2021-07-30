package com.codepath.therapymatch.models;

import com.parse.ParseUser;

public class Item {
    int noOfCommonIssues;
    ParseUser user;
    public Item(int noOfCommonIssues, ParseUser user){
        this.noOfCommonIssues = noOfCommonIssues;
        this.user = user;
    }
}
