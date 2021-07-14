package com.codepath.therapymatch;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //register parse models
        // ParseObject.registerSubclass(Post.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("oE8gqwHTfCR7mSA2OQpUJvsTZqZGO02s0XTysVJl")
                .clientKey("BzXhxFJYTCBxvMJMt4jn6wofgEk82uivoW2YVELc")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}