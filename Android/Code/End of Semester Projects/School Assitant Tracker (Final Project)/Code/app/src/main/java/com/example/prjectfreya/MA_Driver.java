package com.example.prjectfreya;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.common.*;
import com.google.android.gms.common.api.*;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


import static com.example.prjectfreya.Login.SHARED_PREFS;
import static com.example.prjectfreya.Login.ST_ID;
import static com.example.prjectfreya.Login.ST_PSWRD;

public class MA_Driver extends AppCompatActivity
        implements
        View.OnClickListener, //event caller when Button is clicked
        OnMapReadyCallback,//event when Map is ready to use/operate
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
        //GoogleMap.OnMarkerDragListener,
        {

    //Home_cor_Latitude  Home_cor_Longitude

    //the data below used for setting markers for Home and School, which retrived from db.
    private String NAME_HOME="Home",NAME_SCHOOL="School";

    private LatLng LATLONG_HOME;
    private LatLng LATLONG_SCHOOL;//this can change
    private LatLng LATLONG_CITY;
    private LatLng LATLONG_MOCK_BUS;//this can change depending from updator InformEntryToSchool service
    //when i mean this can change, i mean it is different for each family at least, which you get from db.
    //===

    final Handler han = new Handler();
    Runnable runnable;

    //below for important info for map
    private ArrayList<LatLng> LATLONG_LIST; //location
    private ArrayList<String> NAME_LIST; // name
    private ArrayList<Bitmap> ICON_LIST; // icon
    private ArrayList<Boolean> Absense;
    private ArrayList<Marker> gfmarkr; // their markers
    //===

    //Intents
    private IntentFilter BTIntent; // Broadcast reciver for gps location state change listener.
    private Intent K; // for calling Logout activity when the user logs out from their account.
    private Intent serviceIntent; // Important for setting geofence in class GeofenceTransitionService.
    //===

    //to get the gps location state either it is active:(True) or not-active:(False)
    private boolean gps_enabled;
    private boolean network_enabled;
    //===

    //below for the map
    private Circle geoFenceLimits;
    private Marker geoFenceMarker;
    private MapView mapView;
    private GoogleMap mMap;
    //===

    //buttons you see on the map layout.
    ImageButton  popupMenu, reloc_gps;
    Button RefreshGeofenceTest;
    //===

    //[BUG PREVENTION] to prevent multple marker overlapping multple times every time when the app goes pause state to resume state
    protected boolean pass;
    boolean turnOnAfterPause = false;
    //===

    // used for Log for information in Logcat, either information,bugs,etc..
    private static final String TAG ="MA_Driver";
    protected static final String Channel_ID="Tracker_APP";
    //===

    TextView FinDate;

    //below responsible for setting geofence for each marker on the map, for HOME and School
    private Geofence geofence;
    private static final int GEOFENCE_RADIUS = 140; // in meters
    //===

    //both below used for setting gps location on the map and relocating/Update the camera view of the map when pressing the relocate gps button
    private double currentLatitude, currentLongitude;
    //===

    //used as trigger due lack ability to make Bus marker as trigger to geofence, i used the distance between Home location and Bus location.
    private Location HomeLoc,BusLoc;
    //===

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference bus = database.getReference().child("Bus");
    private DatabaseReference userDataLocation = database.getReference().child("Users");
    private DatabaseReference REFRESH_userDataLocation = database.getReference().child("Users");

    public static final String SHARED_PREFS_SETTINGS = "SHARED_PREFS_SETTINGS";
    public static final String GTOG = "GeofenceToggler";
    public static final String ACALL = "AutoCaller";

    //map visualization and stuff
    private PendingIntent geoFencePendingIntent;//used for setting between geofence and GeofenceTranstionService
    private MarkerOptions markerOptions;// used for setting the markers with data you get from db
    private CircleOptions circleOptions;// used for visualize the geofence on the markers
    private GoogleApiClient mGoogleApiClient;//important for gps location device
    private LocationRequest mLocationRequest;//setting up the gps location
    private GeofencingClient geofencingClient;// you need this thing
    //===

    Bitmap Home_Marker;
    Bitmap School_Marker;

    //????
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    //===

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.am_driver);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        Double Home_cor_Latitude = getIntent().getDoubleExtra("Home_cor_Latitude",0);
        Double Home_cor_Longitude = getIntent().getDoubleExtra("Home_cor_Longitude",0);

        LATLONG_HOME = new LatLng(Home_cor_Latitude,Home_cor_Longitude);//this can change

        LATLONG_SCHOOL = new LatLng(32.18994654,35.27648163);//this can change
        LATLONG_CITY = new LatLng(32.22222494, 35.25919533);
        //LATLONG_MOCK_BUS = new LatLng(32.20, 35.23);

        pass = true;

        geofencingClient = LocationServices.getGeofencingClient(this);

        HomeLoc = new Location("");//fixed place
        HomeLoc.setLatitude(LATLONG_HOME.latitude);
        HomeLoc.setLongitude(LATLONG_HOME.longitude);

        markerOptions = new MarkerOptions();
        circleOptions = new CircleOptions();

        gfmarkr = new ArrayList<Marker>();

        LATLONG_LIST = new ArrayList<LatLng>();
        NAME_LIST = new ArrayList<String>();
        ICON_LIST = new ArrayList<Bitmap>();
        Absense = new ArrayList<Boolean>();

        gps_enabled = false;
        network_enabled = false;

        int height = 100;
        int width = 75;

        BitmapDrawable Home_bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.ic_home_marker);
        BitmapDrawable School_bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.ic_school_marker);

        Bitmap Home_b = Home_bitmapdraw.getBitmap();
        Bitmap School_b = School_bitmapdraw.getBitmap();

        Home_Marker = Bitmap.createScaledBitmap(Home_b, width, height, false);
        School_Marker = Bitmap.createScaledBitmap(School_b, width, height, false);
        //add for loop taht reads from DB, lat and long to LATLONG thing and use the add array thingy.

        userDataLocation.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        LATLONG_LIST.add(new LatLng((double)snapshot.child("Home_cor").child("Latitude").getValue(), (double)snapshot.child("Home_cor").child("Longitude").getValue()));
                        Absense.add((Boolean) snapshot.child("Absense").getValue());
                        NAME_LIST.add(NAME_HOME);
                        ICON_LIST.add(Home_Marker);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
        });

        LATLONG_LIST.add(LATLONG_SCHOOL);
        NAME_LIST.add(NAME_SCHOOL);
        ICON_LIST.add(School_Marker);
        Absense.add(false);

        //findViewById(~)///
        //buttons
        RefreshGeofenceTest = findViewById(R.id.RGBUTT);
        reloc_gps = findViewById(R.id.reloc_gps);
        popupMenu = findViewById(R.id.popUpMenu);
        //map
        mapView = findViewById(R.id.map_View);
        //===

        //Broadcast Reciver for gps location
        BTIntent = new IntentFilter();
        BTIntent.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        registerReceiver(BTR, BTIntent);
        //===

        //RefreshGeofenceTest.setOnClickListener(this);
        RefreshGeofenceTest.setEnabled(false);
        RefreshGeofenceTest.setVisibility(View.GONE);

        //Listeners//
        //SimpleClick
        reloc_gps.setOnClickListener(this);
        popupMenu.setOnClickListener(this);

        //===

        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        //to not screw things here
        createGoogleApi();//THIS MUST BE FIRST
        enableLoc();//THEN THIS MUST BE THE SECOND ONE
        //or else the enableLoc will try to use mGoogleClient with Null in it and thus casues to crash

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            createNotificationChannel (this);
        }

        startService();
        startRefresher();
    }

    private void startRefresher() {
        runnable = new Runnable() {
        @Override
            public void run() {
                refreshGeofence();
                Log.d(TAG,"Timer : Hello there SR");
                han.postDelayed(this, TimeUnit.SECONDS.toMillis(40));//40 seconds
            }
        };

        han.postDelayed(runnable, TimeUnit.SECONDS.toMillis(5));
    }

            private void createNotificationChannel(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(
                    Channel_ID,
                    "General",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            channel.enableLights(true);

            NotificationManager Manager = (NotificationManager) getSystemService(NotificationManager.class);
            Manager.createNotificationChannel(channel);
        }
    }

    public void startService() {
        Intent serviceIntent = new Intent(this, Driver_location_updateToFirebase.class);
        serviceIntent.putExtra("ASI_BUS", "Bus_1");
        ContextCompat.startForegroundService(this, serviceIntent);
        Log.d(TAG,"Service has started");
    }

    //stopBusLocUpdateToFB()
    public void stopBusLocUpdateToFB() {
        Intent serviceIntent = new Intent(this, Driver_location_updateToFirebase.class);
        stopService(serviceIntent);
        Log.d(TAG,"Service has stopped");
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        Log.d(TAG,"onMapReady: Map is ready");

        //Toast.makeText(getBaseContext(),"map is rdy",Toast.LENGTH_LONG).show();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"onMapReady: Not granted the permissions");
            return;
        }

        mMap.getUiSettings().setMapToolbarEnabled(false);

        // Add a marker to the location and move the camera
        mMap.setMinZoomPreference(11f);
        mMap.addMarker(new MarkerOptions().position(LATLONG_CITY).title("Nablus"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LATLONG_CITY));

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    public void markerForGeofence(ArrayList<LatLng> LATLONG_X, ArrayList<String> NAME_X, ArrayList<Bitmap> icon,ArrayList<Boolean> Absense_X) {

        // Define marker options
        if (mMap != null) {
            for(int i=0 ; i< LATLONG_X.size(); i++) {

                markerOptions.position(LATLONG_X.get(i))
                        .icon(BitmapDescriptorFactory.fromBitmap(icon.get(i)))
                        .title(NAME_X.get(i));

                Log.d(TAG,"ABS RES SIZE :"+Absense_X.size());

                if(Absense_X.get(i)){//absent
                    circleOptions.center(LATLONG_X.get(i))
                            .strokeColor(Color.argb(75, 140,1,1))
                            .fillColor( Color.argb(65, 70,70,70) )
                            .radius( GEOFENCE_RADIUS );
                }
                else if(NAME_X.get(i).toString().equals("School")){
                    circleOptions.center(LATLONG_X.get(i))
                            .strokeColor(Color.argb(75, 1,140,1))
                            .fillColor( Color.argb(45, 70,70,70) )
                            .radius( GEOFENCE_RADIUS );
                }
                else{//no absense
                    circleOptions.center(LATLONG_X.get(i))
                            .strokeColor(Color.argb(75, 50,70,250))
                            .fillColor( Color.argb(45, 70,70,70) )
                            .radius( GEOFENCE_RADIUS );
                }

                geoFenceLimits = mMap.addCircle( circleOptions );
                geoFenceMarker = mMap.addMarker( markerOptions);

                gfmarkr.add(geoFenceMarker);
            }
            startGeofencing(NAME_X);
        }

    }

    private void startGeofencing(ArrayList<String> NAME_X) {

        if (geoFenceMarker != null) {

            //Toast.makeText(getBaseContext(),"gfmarkr.size() "+gfmarkr.size(),Toast.LENGTH_LONG).show();
            for (int it = 0; it < gfmarkr.size(); it++) {

                if((gfmarkr.size()-1) != it){
                    geofence = createGeofence(gfmarkr.get(it), GEOFENCE_RADIUS,"Home_"+(1+it));
                }
                else if((gfmarkr.size()-1) == it){
                    geofence = createGeofence(gfmarkr.get(it), GEOFENCE_RADIUS,"School");
                }

                // Toast.makeText(getBaseContext(),"adding Geofence "+i,Toast.LENGTH_LONG).show();
                //Toast.makeText(getBaseContext(),"ID "+it,Toast.LENGTH_LONG).show();

                geofencingClient.addGeofences(createGeofenceRequest(geofence), createGeofencePendingIntent())
                        .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG,"startGeofencing: adding Geofence is success");
                            }
                        })
                        .addOnFailureListener(this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG,"startGeofencing: adding Geofence is failure");
                            }
                        });

            }
        }
        else {
            Log.e(TAG,"startGeofencing: Geofence marker is null");
        }
    }

    // Create a Geofence Request. also works as trigger
    private GeofencingRequest createGeofenceRequest( Geofence geofence ) {

        return new GeofencingRequest.Builder()
                .setInitialTrigger( GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_DWELL )
                .addGeofence( geofence )
                .build();
    }

    private PendingIntent createGeofencePendingIntent() {

        if ( geoFencePendingIntent != null )
            return geoFencePendingIntent;

        Log.d(TAG,"createGeofencePendingIntent: The intent is ready");

        serviceIntent = new Intent( this, GeofenceBroadcastReceiver.class);

        return PendingIntent.getBroadcast(
                this, 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT );
    }

    // Create a Geofence
    private Geofence createGeofence(Marker marker, float radius, String id) {

        Log.d(TAG,"GeoID = "+id);

        return new Geofence.Builder()
                .setRequestId(id)
                .setCircularRegion(
                        marker.getPosition().latitude,
                        marker.getPosition().longitude,
                        radius )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(
                        Geofence.GEOFENCE_TRANSITION_ENTER
                                | Geofence.GEOFENCE_TRANSITION_EXIT)
                //| Geofence.GEOFENCE_TRANSITION_DWELL
                .setLoiteringDelay(2)
                .build();
    }

    // Create GoogleApiClient instance
    private void createGoogleApi() {

        try{
            if ( mGoogleApiClient == null ) {
                mGoogleApiClient = new GoogleApiClient.Builder( this )
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi( LocationServices.API )
                        .build();

                mGoogleApiClient.connect();
            }


        }
        catch (IllegalStateException e){
            Log.e(TAG,"CreateGoogleApi: "+ e.toString());
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if(pass) {
            pass = false; // to prevent marker area to overlap.
            // making it connect ONLY once.

            //getting device loaction

            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (location == null) {

                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MA_Driver.this);

            } else {
                //If everything went fine lets get latitude and longitude
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
            }

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onConnected: Permission granted");

                markerForGeofence(LATLONG_LIST, NAME_LIST, ICON_LIST,Absense);

            } else {
                Log.d(TAG, "onConnected: Permission refused");
            }
        }
    }

    private void enableLoc() {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(MA_Driver.this, 342);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                }
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    private void removeLoginData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(ST_ID, "");
        editor.putString(ST_PSWRD, "");

        editor.apply();

        Log.d(TAG,"Login data removed");
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.RGBUTT:
                //Dull for testing ground
                break;

            case R.id.reloc_gps:

                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();

                moveCamera(new LatLng(currentLatitude, currentLongitude), 16);

                break;

            case R.id.popUpMenu:

                showPopupWindow(v);

                break;

        }

    }

    private void refreshGeofence() {

         stopGeofencing();
         mMap.clear();

         LATLONG_LIST.clear();
         NAME_LIST.clear();
         ICON_LIST.clear();
         Absense.clear();

         LATLONG_LIST = new ArrayList<>();
         NAME_LIST = new ArrayList<>();
         ICON_LIST = new ArrayList<>();
         Absense = new ArrayList<>();

         REFRESH_userDataLocation.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                     LATLONG_LIST.add(new LatLng((double)snapshot.child("Home_cor").child("Latitude").getValue(), (double)snapshot.child("Home_cor").child("Longitude").getValue()));
                     Absense.add((Boolean) snapshot.child("Absense").getValue());
                     NAME_LIST.add(NAME_HOME);
                     ICON_LIST.add(Home_Marker);

                     Log.d(TAG,"ABS RES RESULT :"+snapshot.child("Absense").getValue());
                 }

                 LATLONG_LIST.add(LATLONG_SCHOOL);
                 NAME_LIST.add(NAME_SCHOOL);
                 ICON_LIST.add(School_Marker);
                 Absense.add(false);//For school
                 markerForGeofence(LATLONG_LIST, NAME_LIST, ICON_LIST,Absense);
             }
             @Override
             public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            void showPopupWindow(View view) {
        PopupMenu popup = new PopupMenu(MA_Driver.this, view);
        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        popup.getMenuInflater().inflate(R.menu.popupmenu_driver, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){

                    case R.id.about :

                        AlertDialog.Builder Builder_about = new AlertDialog.Builder(MA_Driver.this);
                        View View_about = getLayoutInflater().inflate(R.layout.dialog_about, null);

                        Builder_about.setTitle("About :\n ")
                                .setView(View_about)
                                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                        AlertDialog D_about = Builder_about.create();
                        D_about.show();

                        break;
                    case R.id.Guide:

                        break;

                    case R.id.logout_butt:

                        new AlertDialog.Builder(MA_Driver.this)
                                .setMessage("Are you sure want to Logout?")
                                .setPositiveButton("yes", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface paramDialogInterface, int paramInt)
                                    {

                                        removeLoginData();
                                        K = new Intent(MA_Driver.this, Login.class);
                                        startActivity(K);

                                        stopService(serviceIntent);
                                        stopBusLocUpdateToFB();
                                        stopGeofencing();
                                        NotificationManager notifManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                        notifManager.cancelAll();

                                        Animatoo.animateSwipeRight(MA_Driver.this);
                                        finish();
                                    }
                                })
                                .setNegativeButton("no",null)
                                .show();

                        break;

                    case R.id.Shutdown :
                        Shutdown();
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

            public void Shutdown(){

                new AlertDialog.Builder(MA_Driver.this)
                        .setMessage("Are you sure want to shutdown this app?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt)
                            {

                                unregisterReceiver(BTR);
                                stopService(serviceIntent);
                                stopGeofencing();
                                stopBusLocUpdateToFB();
                                finish();

                                NotificationManager notifManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                notifManager.cancelAll();

                            }
                        })
                        .setNegativeButton("no",null)
                        .show();

            }

            private void stopGeofencing() {

        geofencingClient.removeGeofences(createGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"removeGeofences: removal of Geofence is success");
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"removeGeofences: removal of Geofence is failure");
                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if(turnOnAfterPause) {
            han.postDelayed(runnable, 5000);
            turnOnAfterPause = false;
            Log.d(TAG, "Timer : Hello there OR");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mGoogleApiClient.disconnect();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        han.removeCallbacks(runnable);
        turnOnAfterPause = true;
        Log.d(TAG,"Timer : Bye :c");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        unregisterReceiver(BTR);
        han.removeCallbacks(runnable);
        Log.d(TAG,"Timer : Bye :c");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

            private final BroadcastReceiver BTR = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {

                    String action = intent.getAction();
                    if (action.equals(LocationManager.PROVIDERS_CHANGED_ACTION))
                    {
                        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        //network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                        //Start your Activity if location was enabled:
                        if(!gps_enabled)
                        {
                            gps_enabled=false;
                            // notify user
                            enableLoc();
                        }
                    }
                }
            };

            @Override
            public void onLocationChanged(Location location) {
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
                Log.i(TAG, "onLocationChanged");
            }
        }