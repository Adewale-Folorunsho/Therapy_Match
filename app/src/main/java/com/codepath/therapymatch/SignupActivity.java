package com.codepath.therapymatch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    public final static String TAG = "SignupActivity";

    EditText etUsername;
    EditText etPassword;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        etUsername = findViewById(R.id.etSignUpUsername);
        etPassword = findViewById(R.id.etSignUpPassword);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                signUpUser(username, password);

            }
        });

    }

    private void signUpUser(String username, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    viewOtherUserPosts();
                    Toast.makeText(SignupActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Signup Success");
                }
                Log.e(TAG, "Issue with signup: "+ e);
                return;
            }
        });
        viewOtherUserPosts();
    }

    private void viewOtherUserPosts() {
        Intent intent = new Intent(SignupActivity.this, PostsActivity.class);
        startActivity(intent);
        finish();
    }
}
