package com.codepath.therapymatch.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.therapymatch.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class EditUserProfileFragment extends Fragment {
    FragmentManager fragmentManager;

    List<Address> addressList;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private File photoFile;
    public String photoFileName = "photo.jpg";
    private String TAG = "EditUserProfileFragment";
    String username;
    String strAddress;
    String bio;
    EditText etUsernameChange;
    EditText etBioChange;
    EditText etAddressChange;
    ImageView ivCUProfilePicture;
    ImageView ivPreview;
    Button btnSave;
    ParseGeoPoint latLong;
    FloatingActionButton fabCUEditProfile;
    View view;
    ParseFile ImageFile;
    Fragment fragment;
    ParseUser currentUser = ParseUser.getCurrentUser();

    public EditUserProfileFragment(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edituser_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        etBioChange = view.findViewById(R.id.etBioChange);
        etUsernameChange = view.findViewById(R.id.etUsernameChange);
        etAddressChange = view.findViewById(R.id.etAddressChange);
        ivCUProfilePicture = view.findViewById(R.id.ivCUProfilePicture);
        btnSave = view.findViewById(R.id.btnSave);
        fabCUEditProfile = view.findViewById(R.id.fabCUEditProfile);

        getUserInitialData();

        etUsernameChange.setText(username);

        fabCUEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchCamera();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsernameChange.getText().toString();
                bio = etBioChange.getText().toString();
                strAddress = etAddressChange.getText().toString();
                geocodeAddress(strAddress);
                updateUser(bio, username,photoFile);
            }
        });
    }

    private void geocodeAddress(String strAddress) {
        Geocoder geocoder = new Geocoder(getContext());
        try {
            addressList = geocoder.getFromLocationName(strAddress, 1);
            Address location = addressList.get(0);

            latLong = new ParseGeoPoint((location.getLatitude()),(location.getLongitude()));
            currentUser.put("location", latLong);
        } catch (IOException e) {
            Log.e(TAG,"Error loading address: "+e);
        }
    }

    private void getUserInitialData() {
        username = currentUser.getUsername();
        ImageFile = currentUser.getParseFile("profileImage");
        Glide.with(getContext()).load(ImageFile.getUrl()).placeholder(R.drawable.ic_baseline_cloud_download_24).into(ivCUProfilePicture);

        if(currentUser.get("bio") != null){
            bio = currentUser.get("bio").toString();
            etBioChange.setText(bio);
        }
    }

    private void updateUser(String bio, String username, File photoFile) {
        currentUser = ParseUser.getCurrentUser();
        if(photoFile != null) {
            currentUser.put("profileImage", new ParseFile(photoFile));
        }
        currentUser.setUsername(username);
        currentUser.put("bio", bio);
        Log.i(TAG, "new bio: " + bio);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Log.i(TAG, "User updated");
                    fragment = new CurrentUserProfileFragment(fragmentManager);
                    fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                }
            }
        });
    }

    public void onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getActivity(), "com.codepath.fileprovider.TherapyMatch", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivPreview = (ImageView) view.findViewById(R.id.ivCUProfilePicture);
                ivPreview.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}