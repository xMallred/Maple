package com.maple.mallr.mongo;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;


import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.CallbackManager;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.android.gms.tasks.Tasks;
import com.google.gson.Gson;
import com.mongodb.stitch.android.AuthListener;
import com.mongodb.stitch.android.StitchClient;
import com.mongodb.stitch.android.StitchClientFactory;
import com.mongodb.stitch.android.auth.AvailableAuthProviders;
import com.mongodb.stitch.android.auth.UserProfile;
import com.mongodb.stitch.android.auth.anonymous.AnonymousAuthProvider;
import com.mongodb.stitch.android.auth.oauth2.facebook.FacebookAuthProvider;
import com.mongodb.stitch.android.auth.oauth2.facebook.FacebookAuthProviderInfo;
import com.mongodb.stitch.android.auth.oauth2.google.GoogleAuthProvider;
import com.mongodb.stitch.android.auth.oauth2.google.GoogleAuthProviderInfo;
import com.mongodb.stitch.android.services.mongodb.MongoClient;

import static java.lang.System.*;
import org.json.JSONObject;
import com.google.gson.Gson.*;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;


/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private static final long REFRESH_INTERVAL_MILLIS = 1000;
    private static final int RC_SIGN_IN = 421;

    private CallbackManager _callbackManager;
    private StitchClient _client;
    private MongoClient _mongoClient;

    private Handler _handler;
    private Runnable _refresher;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    StitchClient stitchClient;
    MongoClient client;
    MongoClient.Collection coll;
    ArrayList<String> listOfEvents = new ArrayList<>();

    int currentNum;
    int previousNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentNum = 0;
        previousNum = 0;
        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        _handler = new Handler();
        _refresher = new MapRefresher(this);

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);


        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //set up client
        Task<StitchClient> stitchClientTask = StitchClientFactory.create(getApplicationContext(), "eventfinder-wkdhy");
        stitchClient = stitchClientTask.getResult();
        login();
    }

    public void login()
    {
        client = new MongoClient(stitchClient, "mongodb-atlas");
        coll = client.getDatabase("Maple").getCollection("Events");

        stitchClient.logInWithProvider(new AnonymousAuthProvider()).addOnCompleteListener(new OnCompleteListener<String>(){
            @Override
            public void onComplete(@NonNull final Task<String> task) {
                if (task.isSuccessful()) {
                    Log.d("stitch", "logged in anonymously as user " + task.getResult());
                } else {
                    Log.e("stitch", "failed to log in anonymously", task.getException());
                }
                //_refresher.run();
                refreshList();
            }
        });
    }

    private static class MapRefresher implements Runnable {

        private WeakReference<MapsActivity> _main;

        public MapRefresher(final MapsActivity activity) {
            _main = new WeakReference<>(activity);
        }

        @Override
        public void run() {
            final MapsActivity activity = _main.get();

            //activity.refreshList();
            activity._handler.postDelayed(MapRefresher.this, REFRESH_INTERVAL_MILLIS);
        }
    }

    public String parse(String str)
    {
        String ret = str.replaceAll("Document", "");
        String ret1 = ret.replaceAll("\\{", "");
        String[] tokens = ret1.split("_");
        for(int i = 1; i < tokens.length ; i++)
        {
            String temp = tokens[i];
            System.out.println(temp);

            String id = StringUtils.substringBetween(temp, "id=", ",");
            System.out.println("id = " + id);
            String title = StringUtils.substringBetween(temp, "title=", ",");
            System.out.println("title = " + title);
            String venue = StringUtils.substringBetween(temp, "venue=", ",");
            System.out.println("venue = " + venue);
            String address = StringUtils.substringBetween(temp, "Address=", ",");
            System.out.println("address = " + address);
            String latitude = StringUtils.substringBetween(temp, "latitude=", ",");
            System.out.println("latitude = " + latitude);
            String longitude = StringUtils.substringBetween(temp, "longitude=", ",");
            System.out.println("longitude = " + longitude);
            String eventtype = StringUtils.substringBetween(temp, "EventType=", ",");
            System.out.println("EventType = " + eventtype);
            String age = StringUtils.substringBetween(temp, "Age=", ",");
            System.out.println("Age = " + age);
            String date = StringUtils.substringBetween(temp, "Date=", "}");
            System.out.println("Date = " + date);

            previousNum = tokens.length-1;
            if(!listOfEvents.contains(id))
            {
                currentNum++;
                double lat = Double.parseDouble(latitude);
                double log = Double.parseDouble(longitude);
                listOfEvents.add(id);


                if(age.equals("kid")) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, log)).title(title).snippet(eventtype + ", Date: " + date).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                }
                else if(age.equals("teenager"))
                {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, log)).title(title).snippet(eventtype + ", Date: " + date).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                }
                else{
                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, log)).title(title).snippet(eventtype + ", Date: " + date).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                }
            }


        }
        return ret1;

    };


    public void refreshList()
    {
       stitchClient.executeFunction("finddocs").addOnCompleteListener(new OnCompleteListener<Object>() {
            @Override
            public void onComplete(@NonNull Task task){
                if(task.isSuccessful()){
                    //Log.d("Stitch", "Number of collections: " + task.getResult());
                    String t = task.getResult().toString();
                    if(currentNum != previousNum || currentNum ==0) {
                        String obj = parse(t);
                    }

                }
                else {
                    Log.e("stitch", "failed to get number of collections", task.getException());
                }

            }
        });
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    updateLocationUI();
                }
            }
        }

    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

}
