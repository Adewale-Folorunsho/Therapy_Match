package com.codepath.therapymatch.models;

import com.parse.ParseObject;

import java.util.HashSet;

public class Issues{
    // <"issue", null>
    public HashSet<String> issues = new HashSet<String>();

    public void addIssues(String key){
        issues.add(key);
    }

    public void removeIssues(String key){
        issues.remove(key);
    }

    public boolean containsKey(String key){
        Boolean check = issues.contains(key);
        return check;
    }
}
