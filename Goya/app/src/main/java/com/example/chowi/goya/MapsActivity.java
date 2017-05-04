package com.example.chowi.goya;

import android.Manifest;
import android.app.ActionBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import java.util.Map;

import static android.R.attr.button;
import static android.R.attr.data;
import static com.example.chowi.goya.R.id.map;
import static com.example.chowi.goya.R.layout.activity_maps;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_CYAN;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        AppCompatCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {
        //let's leave this empty, for now
    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {
        // let's leave this empty, for now
    }

    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private double mLongitude;
    private double mLatitude;

    private DatabaseReference mDatabase;

    private AppCompatDelegate delegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(activity_maps);


        //let's create the delegate, passing the activity at both arguments (Activity, AppCompatCallback)
        delegate = AppCompatDelegate.create(this, this);

        //we need to call the onCreate() of the AppCompatDelegate
        delegate.onCreate(savedInstanceState);

        //we use the delegate to inflate the layout
        delegate.setContentView(R.layout.activity_maps);

        //Finally, let's add the Toolbar
        Toolbar toolbar= (Toolbar) findViewById(R.id.my_toolbar);
        delegate.setSupportActionBar(toolbar);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        FloatingActionButton addFab = (FloatingActionButton)  findViewById(R.id.floatingActionButton);
        addFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("clicking fab", "clicking fab");
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT,
                        50
                );
                LinearLayout addFragment = (LinearLayout) findViewById(R.id.container2);
                addFragment.setLayoutParams(param);
            }
        });
        /*
        final Button btnEvent = (Button) findViewById(R.id.button_marvel);
        btnEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Log.i("hello","hello");
                EditText titleEditText = (EditText) findViewById(R.id.title_text);
                String titleText = titleEditText.getText().toString();

                EditText descEditText = (EditText) findViewById(R.id.desc_text);
                String descText = descEditText.getText().toString();

                // if the number is empty, dont send and toast error message
                if (titleText.isEmpty()) {
                    Toast.makeText(MapsActivity.this, "Enter a title", Toast.LENGTH_SHORT).show();
                    // if the text body is empty, dont send and toast error message
                } else if (descText.isEmpty()) {
                    Toast.makeText(MapsActivity.this, "Enter a description", Toast.LENGTH_SHORT).show();
                    // send text and reset field values
                } else {



                    double currentLatitude = mLatitude;
                    double currentLongitude = mLongitude;
                    LatLng currentLatLng = new LatLng(currentLatitude, currentLongitude);

                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    EventItem newItem = new EventItem(titleText, descText, currentLatitude, currentLongitude, 0, 0);


                    // Generate a reference to a new location and add some data using push()
                    //Create new reference        calling push creates the unique key in firebase database but has no data yet
                    DatabaseReference mypostref = mDatabase.push();
                    //mypostref.setValue(data);
                    String newKey = mypostref.getKey();


                    mDatabase.child("events").child(newKey).setValue(newItem);

                    mMap.addMarker(new MarkerOptions()
                            .position(currentLatLng)
                            .title(titleText)
                            .snippet(descText)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    titleEditText.setText("");
                    descEditText.setText("");
                }

            }
        });
        */
    }
    

    public static final String TAG = MapsActivity.class.getSimpleName();

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        // Read from the database
        FirebaseDatabase.getInstance().getReference().child("events")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            EventItem eventItem = snapshot.getValue(EventItem.class);
                            Log.i("hello in here", eventItem.getTitle());

                            LatLng currentItemLatLng = new LatLng(eventItem.getLatitude(), eventItem.getLongitude());
                            mMap.addMarker(new MarkerOptions()
                                    .position(currentItemLatLng)
                                    .title(eventItem.getTitle())
                                    .snippet(eventItem.getDescription())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


        // Write a message to the database
        Log.i("hello", "firebase should have just worked");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Check the permissions and request permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, 0);
        }


        Log.i(TAG, "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            Log.i("hello", "location is null");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            Log.i("hello", "location is not null");
            handleNewLocation(location);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }


    public void handleNewLocation(Location location) {

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        mLatitude = currentLatitude;
        mLongitude = currentLongitude;
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        Log.i("hello", "handle new location" + location.toString());

        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude) , 14.0f) );
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }


    // If the map needs to be set up, set it up
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(map);
            mapFragment.getMapAsync(this);

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    // Set up the map
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Log.i("settings", "settings click");
                return true;

            case R.id.action_profile:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Log.i("profile", "profile click");
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
