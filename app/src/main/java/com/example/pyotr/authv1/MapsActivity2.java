package com.example.pyotr.authv1;


import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class MapsActivity2 extends FragmentActivity implements  OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<Status> {

    public static final String TAG ="MapsActiivty" ;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1 ;
    private static final int REQ_PERMISSION =999 ;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "working area";
    private static final float GEOFENCE_RADIUS = 500.0f; // in meters
    private static final int DEFAULT_ZOOM = 15;// minimum value=2.0 and maximum value=21.0.
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";
    private static boolean clock=false;
    // Defined in mili seconds.
    // This number in extremely low, and should be used only for debug
    private final int UPDATE_INTERVAL =  1000;
    private final int FASTEST_INTERVAL = 900;
    private final int GEOFENCE_REQ_CODE = 0;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private final String KEY_GEOFENCE_LAT = "GEOFENCE LATITUDE";
    private final String KEY_GEOFENCE_LON = "GEOFENCE LONGITUDE";

    // The entry points to the Places API.
    DatabaseReference empRef;
    private Boolean GeofenceReady=false;
    private GoogleMap mMap;
    private TextView mPlaceDetailsText;
    private TextView mPlaceAttribution;
    private boolean mLocationPermissionGranted;
    private CameraPosition mCameraPosition;
    private GoogleApiClient googleApiClient;
    private Marker geoFenceMarker;
    private Marker locationMarker;
    private DatabaseReference ref;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    private GeoDataClient mGeoDataClient;
    private GoogleApi mGoogleApiClient;






    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private PlaceDetectionClient mPlaceDetectionClient;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private Location lastLocation;//tuttorial
    private LocationRequest locationRequest;
    private PendingIntent geoFencePendingIntent;
    private Button btnSaveGeofenceLatLng;
    private  TextView textLat;
    private  TextView textLong;
    //incercare
    private GeoLocation CURRENT_LOCATION;
    private DatabaseReference database;
    // Draw Geofence circle on GoogleMap
    private Circle geoFenceLimits;

    // Create a Intent send by the notification
    public static Intent makeNotificationIntent(Context context, String msg) {
        Log.i(TAG, "Entering: ne Intoarcem la Login Emp");

        clock=true;
        System.out.println("Clock is true");
        Intent intent = new Intent( context, MapsActivity2.class );
        intent.putExtra( NOTIFICATION_MSG, msg );
        return intent;



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps2);
        empRef = FirebaseDatabase.getInstance().getReference("angajati");


        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);
        // Build a GoogleApiClient with access to the Google Sign-In API and the


        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);





        // create GoogleApiClient
        createGoogleApi();
        //startGeofence();
    }

    // Create GoogleApiClient instance
    private void createGoogleApi() {
        Log.d(TAG, "createGoogleApi()");
        if ( googleApiClient == null ) {
            googleApiClient = new GoogleApiClient.Builder( this )
                    .addConnectionCallbacks( this )
                    .addOnConnectionFailedListener( this )
                    .addApi( LocationServices.API )
                    .build();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Call GoogleApiClient connection when starting the Activity
        googleApiClient.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();

        // Disconnect GoogleApiClient when stopping Activity
        googleApiClient.disconnect();
    }

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }

    // Asks for permission
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                REQ_PERMISSION
        );
    }

    // Verify user's response of the permission requested
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case REQ_PERMISSION: {
                if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // Permission granted
                    getLastKnownLocation();

                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
        }

    }

    // GoogleApiClient.ConnectionCallbacks connected
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected()");
        getLastKnownLocation();
        // recoverGeofenceMarker();
        //startGeofence();
        //getLocation();
    }

    // GoogleApiClient.ConnectionCallbacks suspended
    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "onConnectionSuspended()");
    }

    // GoogleApiClient.OnConnectionFailedListener fail
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "onConnectionFailed()");
    }

    // Get last known location
    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation()");
        if ( checkPermission() ) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if ( lastLocation != null ) {
                Log.i(TAG, "LasKnown location. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());
                writeLastLocation();
               getAngManagerLocation();
                //startGeofence();
                startLocationUpdates();
            } else {
                Log.w(TAG, "No location retrieved yet");
                // startGeofence();
                startLocationUpdates();
              getAngManagerLocation();
            }
        }
        else askPermission();
    }

    @Override
    public void onLocationChanged(final Location location) {
        Log.d(TAG, "onLocationChanged [" + location + "]");
        lastLocation = location;
        writeActualLocation(location);
        checkClock();

    }

    private void startLocationUpdates(){
        Log.i(TAG, "startLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if ( checkPermission() ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);


          /* lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            final double latitude = lastLocation.getLatitude();
            final double longitude = lastLocation.getLongitude();
           geoFire.setLocation("You", new GeoLocation(latitude ,longitude), new GeoFire.CompletionListener() {
                              @Override
                             public void onComplete(String key, DatabaseError error) {
                                  writeActualLocation(lastLocation);
                              }
                           });
                           */
        }
    }

    // Write location coordinates on UI
    private void writeActualLocation(Location location) {
        LatLng loc=new LatLng(location.getLatitude(),location.getLongitude());

        Log.i(TAG, "markerLocation("+loc+")");
        String title = loc.latitude + ", " + loc.longitude;
        MarkerOptions markerOptions = new MarkerOptions()
                .position(loc)
                .title(title);
        if ( mMap!=null ) {
            // Remove the anterior marker
            if ( locationMarker != null )
                locationMarker.remove();
            locationMarker = mMap.addMarker(markerOptions);
            float zoom = 14f;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(loc, zoom);
            mMap.animateCamera(cameraUpdate);
        }
    }

    private void checkClock()
    {
        if(clock)
        {
            Log.i(TAG,"Clock is true");
            //Intent to clock in
            Intent intent = new Intent(getBaseContext(), Login_Emp.class);
            intent.putExtra("keyMap", true);
            finish();
            startActivity(intent);

        }
        else
        {
            Log.i(TAG,"Clock is false");
        }
    }

    private void writeLastLocation() {
        writeActualLocation(lastLocation);

    }

    // App cannot work without the permissions
    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
    }

    // Create a Geofence
    private Geofence createGeofence(LatLng latLng, float radius ) {
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion( latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration( GEO_DURATION )
                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT )
                .build();
    }

    // Create a Geofence Request
    private GeofencingRequest createGeofenceRequest(Geofence geofence ) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger( GeofencingRequest.INITIAL_TRIGGER_ENTER )
                .addGeofence( geofence )
                .build();
    }

    private PendingIntent createGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if ( geoFencePendingIntent != null )
            return geoFencePendingIntent;

        Intent intent = new Intent( this, GeofenceTrasitionService.class);
        return PendingIntent.getService(
                this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT );

    }

    // Add the created GeofenceRequest to the device's monitoring list
    private void addGeofence(GeofencingRequest request) {
        Log.d(TAG, "addGeofence");
        if (checkPermission())
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    request,
                    createGeofencePendingIntent()
            ).setResultCallback((ResultCallback<? super Status>) this);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public void onResult(@NonNull Status status) {
        Log.i(TAG, "onResult: " + status);
        if ( status.isSuccess() ) {
            //saveGeofence();
            //drawGeofence();

            //adaugam locatia pentru geofence in tabelul manageri
        } else {
            // inform about fail
        }
    }

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
        // Add a marker in Sydney, Australia, and move the camera.
       /* LatLng sydney = new LatLng(-34, 151)

        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        Log.d(TAG, "onMapReady()");
        // Add a marker in Sydney and move the camera
        /*geoFire.getLocation("My Location", new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location != null) {
                    System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));

                    LatLng loc=new LatLng(location.latitude,location.longitude);
                    mMap.addMarker(new MarkerOptions().position(loc));
                    // mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            loc, 16));
                    Log.i(TAG, "Error: Status = " + loc.toString());
                } else {
                    System.out.println(String.format("There is no location for key %s in GeoFire", key));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("There was an error getting the GeoFire location: " + databaseError);
            }
        });*/
    }

    // Saving GeoFence marker with prefs mng
    private void saveGeofence() {
        Log.d(TAG, "saveGeofence()");
        SharedPreferences sharedPref = getPreferences( Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putLong( KEY_GEOFENCE_LAT, Double.doubleToRawLongBits( geoFenceMarker.getPosition().latitude ));
        editor.putLong( KEY_GEOFENCE_LON, Double.doubleToRawLongBits( geoFenceMarker.getPosition().longitude ));
        editor.apply();
    }

    // Recovering last Geofence marker
    private void recoverGeofenceMarker() {
        Log.d(TAG, "recoverGeofenceMarker");
        SharedPreferences sharedPref = getPreferences( Context.MODE_PRIVATE );

        if ( sharedPref.contains( KEY_GEOFENCE_LAT ) && sharedPref.contains( KEY_GEOFENCE_LON )) {
            double lat = Double.longBitsToDouble( sharedPref.getLong( KEY_GEOFENCE_LAT, -1 ));
            double lon = Double.longBitsToDouble( sharedPref.getLong( KEY_GEOFENCE_LON, -1 ));
            LatLng latLng = new LatLng( lat, lon );
            //markerForGeofence(latLng);
            drawGeofence();
        }
    }

    private void drawGeofence() {
        Log.d(TAG, "drawGeofence()");

        if ( geoFenceLimits != null )
            geoFenceLimits.remove();

        CircleOptions circleOptions = new CircleOptions()
                .center( geoFenceMarker.getPosition())
                .strokeColor(Color.argb(50, 70,70,70))
                .fillColor( Color.argb(100, 150,150,150) )
                .radius( GEOFENCE_RADIUS );
        geoFenceLimits = mMap.addCircle( circleOptions );
    }


    // Create a Location Marker
    private void markerLocation(LatLng latLng) {
        Log.i(TAG, "markerLocation("+latLng+")");
        String title = latLng.latitude + ", " + latLng.longitude;
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title);
        if ( mMap!=null ) {
            // Remove the anterior marker
            if ( locationMarker != null )
                locationMarker.remove();
            locationMarker = mMap.addMarker(markerOptions);
            float zoom = 14f;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
            mMap.animateCamera(cameraUpdate);
        }
    }

    // Create a marker for the geofence creation
    private void markerForGeofence(LatLng latLng) {
        Log.i(TAG, "markerForGeofence("+latLng+")");
        String title = latLng.latitude + ", " + latLng.longitude;
        // Define marker options
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title(title);
        if ( mMap!=null ) {
            // Remove last geoFenceMarker
            if (geoFenceMarker != null)
                geoFenceMarker.remove();

            geoFenceMarker = mMap.addMarker(markerOptions);
        }
    }

    public void getAngManagerLocation() {
        FirebaseUser userCurrent = FirebaseAuth.getInstance().getCurrentUser();


        empRef.child(userCurrent.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String nume="";
                String manager="";
                System.out.println("Ang:" + dataSnapshot);
                nume=(dataSnapshot.getValue(Employee.class).getNume());
                System.out.println("Ang nume:" + nume);
                manager=( dataSnapshot.getValue(Employee.class).getManager());

                getLocation(manager);

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }


        });
    }

    public void getLocation(String manager)
    {

        ref = FirebaseDatabase.getInstance().getReference("manager").child(manager).child("My Location");
        geoFire = new GeoFire(ref);
        geoFire.getLocation("You", new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location != null) {
                    System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));

                    LatLng loc=new LatLng(location.latitude,location.longitude);
                    mMap.addMarker(new MarkerOptions().position(loc));
                    // mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            loc, 15f));

                    geoFenceMarker=mMap.addMarker(new MarkerOptions().position(loc));
                    Log.i(TAG, "Error: Status = " + loc.toString());

                    startGeofence();
                    drawGeofence();

                } else {
                    System.out.println(String.format("There is no location for key %s in GeoFire", key));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("There was an error getting the GeoFire location: " + databaseError);
            }
        });

    }
    // Start Geofence creation process
    private void startGeofence() {
        Log.i(TAG, "startGeofence()");


        if( geoFenceMarker != null ) {
            Geofence geofence = createGeofence( geoFenceMarker.getPosition(), GEOFENCE_RADIUS );
            GeofencingRequest geofenceRequest = createGeofenceRequest( geofence );
            addGeofence( geofenceRequest );
        } else {
            Log.e(TAG, "Geofence marker is null");
        }
        Log.i(TAG, "startGeofence1()");
       /* if( geoFenceMarker != null ) {
            Geofence geofence = createGeofence( geoFenceMarker.getPosition(), GEOFENCE_RADIUS );
            GeofencingRequest geofenceRequest = createGeofenceRequest( geofence );
            addGeofence( geofenceRequest );
        } else {
            Log.e(TAG, "Geofence marker is null");
        }*/
    }


}
