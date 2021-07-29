package com.codepath.therapymatch;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.codepath.therapymatch.fragments.CurrentUserProfileFragment;
import com.codepath.therapymatch.fragments.PostFragment;
import com.codepath.therapymatch.fragments.ViewOtherUserProfilesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class PostsActivity extends AppCompatActivity {
    final FragmentManager fragmentManager = getSupportFragmentManager();

    public final static String TAG = "PostsActivity";
    private GestureDetectorCompat mDetector;
    private BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts_activity);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.actionOtherUserProfile:
                        fragment = new ViewOtherUserProfilesFragment();
                        break;
                    case R.id.actionUserProfile:
                        fragment = new CurrentUserProfileFragment(fragmentManager);
                        break;
                    case R.id.actionPosts:
                    default:
                        fragment = new PostFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.actionPosts);
    }

    public interface MyOnTouchListener {
        public boolean onTouch(MotionEvent ev);
    }

    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(10);

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            if(listener != null) {
                listener.onTouch(ev);
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }
    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener) ;
    }
//    public class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
//        private static final float SWIPE_THRESHOLD = 100;
//        private static final float SWIPE_VELOCITY_THRESHOLD = 100;
//
//        @Override
//        public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
//            boolean result = false;
//            float diffX = moveEvent.getX() - downEvent.getX();
//            float diffY = moveEvent.getY() - downEvent.getY();
//
//            if (Math.abs(diffX) > Math.abs(diffY)) {
//                //right or left swipe
//                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
//                    if (diffX > 0) {
//                        onSwipeRight();
//                    } else {
//                        onSwipeLeft();
//                    }
//                    result = true;
//                }
//            } else {
//                //up or down swipe
//                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
//                    if (diffY > 0) {
//                        onSwipeBottom();
//                    } else {
//                        onSwipeTop();
//                    }
//                    result = true;
//                }
//            }
//            return result;
//
//        }
//    }
//
//    private void onSwipeLeft() {
//        Log.i("Swipe" , "left");
//    }
//
//    private void onSwipeBottom() {
//        Log.i("Swipe" , "bottom");
//    }
//
//    private void onSwipeTop() {
//        Log.i("Swipe" , "up");
//    }
//
//    private void onSwipeRight() {
//        Log.i("Swipe" , "right");
//    }

}

