package com.example.prjectfreya;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Notification;
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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.common.*;
import com.google.android.gms.common.api.*;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;

import static com.example.prjectfreya.Login.SHARED_PREFS;
import static com.example.prjectfreya.Login.ST_ID;
import static com.example.prjectfreya.Login.ST_PSWRD;

public class MainActivity extends AppCompatActivity
        implements
        View.OnClickListener, //event caller when Button is clicked
        OnMapReadyCallback,//event when Map is ready to use/operate
        GoogleMap.OnMarkerClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        //GoogleMap.OnMarkerDragListener,
        ResultCallback<Status>,
        LocationListener,
        bottomsheet_buses.BottomSheetListener,
        dialog_fragment_daterangepicker.DataPickerListener{


    //the data below used for setting markers for Home and School, which retrived from db.
    private String NAME_HOME="Home",NAME_SCHOOL="School";
    String ID;

    private LatLng LATLONG_HOME;
    private LatLng LATLONG_SCHOOL;//this can change
    private LatLng LATLONG_CITY;
    public LatLng LATLONG_BUS;//this can change depending from updator InformEntryToSchool service
    //when i mean this can change, i mean it is different for each family at least, which you get from db.
    //===

    //below for important info for map
    private ArrayList<LatLng> LATLONG_LIST; //location
    private ArrayList<String> NAME_LIST; // name
    private ArrayList<Bitmap> ICON_LIST; // icon
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
    ImageButton reloc_school, reloc_home, popupMenu, reloc_gps,recall;
    Button SendTest;
    //===

    //[BUG PREVENTION] to prevent multple marker overlapping multple times every time when the app goes pause state to resume state
    protected boolean pass;
    //===

    //for the option to inform the bus driver to ignore your house
    private Boolean isAutoCallOn = false;
    private Boolean isTurnedOn = true;
    //===

    CheckBox Child_1,Child_2,Child_3;

    //for Time in Recall button, to not overwhelm the school reciver with duplicate recalls
    private Snackbar snacCall,Approved,Denied;
    private static final long START_TIME_IN_MILLIS = 15000; //60000 = 60 seconds
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private CountDownTimer mCountDownTimer;
    //===

    // used for Log for information in Logcat, either information,bugs,etc..
    private static final String TAG ="MainActivity";
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

    private DatabaseReference users_abs;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference colRef_CON = db.collection("Pending_confirmation");
    private CollectionReference colRef_ABS = db.collection("Absense");
    private DocumentReference noteRef;

    public static final String SHARED_PREFS_SETTINGS = "SHARED_PREFS_SETTINGS";
    public static final String GTOG = "GeofenceToggler";
    public static final String ACALL = "AutoCaller";

    private String token;

    BitmapDrawable bitmapdraw;
    Bitmap b;
    Bitmap smallMarker;

    //map visualization and stuff
    private PendingIntent geoFencePendingIntent;//used for setting between geofence and GeofenceTranstionService
    private MarkerOptions markerOptions;// used for setting the markers with data you get from db
    private CircleOptions circleOptions;// used for visualize the geofence on the markers
    private CameraUpdate cameraUpdate;// used for the camera view of map, when pressing the relocator buttons, such as home, school, gps location.
    private GoogleApiClient mGoogleApiClient;//important for gps location device
    private LocationRequest mLocationRequest;//setting up the gps location
    private GeofencingClient geofencingClient;// you need this thing
    //===

    private ImageButton test;

    //????
    private MarkerOptions Bus_MO;
    private Marker Bus;
    private PopupMenu ppm;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    //===

    View View_2FA = null;
    AlertDialog AuthDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        LoadPreferences_Settings();

        //this is for approval from the web side on calling on the school for the child
        View snackbarView = findViewById(R.id.Main);

        bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_bus_mock);
        b = bitmapdraw.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(b, 75, 100, false);

        Approved = Snackbar.make(snackbarView,"The child/s is on the way",Snackbar.LENGTH_INDEFINITE)
                .setBackgroundTint(getResources().getColor(R.color.snackbarBLU_Approved))
                .setActionTextColor(getResources().getColor(R.color.actionDismiss));

        Approved.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call your action method here
                Approved.dismiss();
            }
        });

        //add a special layout for it.
        Denied = Snackbar.make(snackbarView,"Denied : call school for support \n[add school number here]",Snackbar.LENGTH_INDEFINITE)
                .setBackgroundTint(getResources().getColor(R.color.snackbarBLU_Denied))
                .setActionTextColor(getResources().getColor(R.color.actionDismiss));

        Denied.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call your action method here
                Denied.dismiss();
            }
        });


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();
                        // Log and toast
                        Log.d(TAG,"YO TOKEN : "+ token);
                    }
                });


        Double Home_cor_Latitude = getIntent().getDoubleExtra("Home_cor_Latitude",0.5);//to avoid dbrl from taking 0.0 to 0 for some reason
        Double Home_cor_Longitude = getIntent().getDoubleExtra("Home_cor_Longitude",0.5);
        ID = getIntent().getStringExtra("ST_ID");

        Log.d(TAG,"IDCUNT : "+ID);

        //Toast.makeText(getBaseContext(),"Home_cor : "+Home_cor_Latitude +" , "+Home_cor_Longitude,Toast.LENGTH_LONG).show();

        //private LatLng LATLONG_HOME = new LatLng(32.21901616,35.221035);//this can change

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

        gps_enabled = false;
        network_enabled = false;

        //Broadcast Reciver for gps location
        BTIntent = new IntentFilter();
        BTIntent.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        registerReceiver(BTR, BTIntent);
        //===

        int height = 100;
        int width = 75;

        BitmapDrawable Home_bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.ic_home_marker);
        BitmapDrawable School_bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.ic_school_marker);

        Bitmap Home_b = Home_bitmapdraw.getBitmap();
        Bitmap School_b = School_bitmapdraw.getBitmap();

        Bitmap Home_Marker = Bitmap.createScaledBitmap(Home_b, width, height, false);
        Bitmap School_Marker = Bitmap.createScaledBitmap(School_b, width, height, false);


        LATLONG_LIST.add(LATLONG_HOME);
        LATLONG_LIST.add(LATLONG_SCHOOL);
        NAME_LIST.add(NAME_HOME);
        NAME_LIST.add(NAME_SCHOOL);
        ICON_LIST.add(Home_Marker);
        ICON_LIST.add(School_Marker);

        //findViewById(~)///
        //buttons
        reloc_home = findViewById(R.id.reloc_home);
        reloc_school = findViewById(R.id.reloc_school);
        reloc_gps = findViewById(R.id.reloc_gps);
        popupMenu = findViewById(R.id.popUpMenu);
        recall = findViewById(R.id.recall_school);
        SendTest = findViewById(R.id.DebugSend);
        //map
        mapView = findViewById(R.id.map_View);
        //===

        //Listeners//
        //SimpleClick
        reloc_home.setOnClickListener(this);
        reloc_school.setOnClickListener(this);
        reloc_gps.setOnClickListener(this);
        popupMenu.setOnClickListener(this);
        recall.setOnClickListener(this);
        //SendTest.setOnClickListener(this);
        //===

        //DEBUG OF JSON COMMUNICATONS, REMOVE THE FIRST TWO LINES AND REMOVE THE COMMENT IN THE THIRD
        SendTest.setVisibility(View.GONE);
        SendTest.setEnabled(false);
        //SendTest.setOnClickListener(this);

        //recall, below is commented for test mode.
        recall.setEnabled(false);
        recall.setVisibility(View.GONE);

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

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {

            ShowIntro("Home", "Press to move the view to your home", R.id.reloc_home, 1);

            prefs = getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstStart", false);
            editor.apply();
        }
        startBusTracker();//the start of a disaster
    }

    private void uploadFromFirestoreCloud() {

        noteRef = colRef_CON.document(ID);//TARGET

        noteRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if (e != null) {
                    Log.e(TAG, "Error while loading! :" + e.toString());
                    return;
                }

                if (documentSnapshot.exists()) {

                    String C = documentSnapshot.getString("Confirmation");
                    final int[] tries_Auth = {5};

                    if (C.matches("PROCESSING")) {

                        final String Auth_2FA = documentSnapshot.getString("Code_2FA");
                        final TextView noOfTries = View_2FA.findViewById(R.id.textView9);
                        final EditText UserInput = View_2FA.findViewById(R.id.EditText_EnterTheCode);

                        Button b = AuthDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if(Auth_2FA.trim().toLowerCase().matches(UserInput.getText().toString().trim().toLowerCase()) && !UserInput.getText().toString().trim().toLowerCase().matches("Dull".toLowerCase()) ){
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("Confirmation", "GRANTED");
                                    colRef_CON.document(ID).set(user, SetOptions.merge());
                                } else if (tries_Auth[0] == 1) {
                                    AuthDialog.dismiss();
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("Confirmation", "DENIED");
                                    colRef_CON.document(ID).set(user, SetOptions.merge());
                                } else {
                                    tries_Auth[0]--;
                                    noOfTries.setText("Number of tries left : " + tries_Auth[0]);
                                }
                            }
                        });
                    } else if (C.matches("GRANTED")) {
                        AuthDialog.dismiss();
                        snacCall.dismiss();
                        Approved.show();
                        recall.setEnabled(false);
                        recall.setVisibility(View.GONE);
                    } else if (C.matches("DENIED")) {
                        AuthDialog.dismiss();
                        snacCall.dismiss();
                        Denied.show();
                        recall.setEnabled(false);
                        recall.setVisibility(View.GONE);
                    }

                    String I = documentSnapshot.getString("UserId");
                    String T = documentSnapshot.getString("Token");

                    Log.d(TAG, "UserId: " + I + "\t" + "Confirmation: " + C + "\t" + "Token: " + T);

                }
                else{
                    Log.d(TAG,"Such document DNE");
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        mapView.onStart();

    }

    public void PickRangeDate(View v){

        dialog_fragment_daterangepicker fragmentdate2 = new dialog_fragment_daterangepicker();
        fragmentdate2.show(getSupportFragmentManager(), "fragmentdate");

    }

    //i have no fukin clue
    public void startBusTracker() {
        Intent SI = new Intent(this, Tracker_bus_driver.class);

        SI.putExtra("ID", ID);//Latlng
        //add assigned bus if needed
        SI.putExtra("Geofence_Radius", GEOFENCE_RADIUS);//int

        startForegroundService(SI);

        //ContextCompat.startForegroundService(this, SI);
    }
    public void stopBusTracker() {
        Intent SI = new Intent(this, Tracker_bus_driver.class);
        stopService(SI);
    }
    //====

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

    @Override
    public void moveTheCamera(final String text) {

        bus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                CameraUpdate cameraUpdateZoom;

                if(dataSnapshot.child(text).child("Location").child("Latitude").exists() && dataSnapshot.child(text).child("Location").child("Longitude").exists())
                {
                    cameraUpdateZoom = CameraUpdateFactory
                            .newLatLngZoom(new LatLng(
                                            (double)dataSnapshot
                                                    .child(text)
                                                    .child("Location")
                                                    .child("Latitude")
                                                    .getValue(),

                                            (double)dataSnapshot
                                                    .child(text)
                                                    .child("Location")
                                                    .child("Longitude")
                                                    .getValue())
                                    , 15);
                     Log.d(TAG,"moveTheCamera : the data uploaded successfully");
                } else {
                    cameraUpdateZoom = CameraUpdateFactory
                            .newLatLngZoom(new LatLng(32.20, 35.23),15);
                    Log.d(TAG,"moveTheCamera : the data does not exist in database, either path is mismatch or path doesn't exist, uploading the default data");
                }
                
                mMap.animateCamera(cameraUpdateZoom);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    private void ShowIntro(String title, String text, int viewId, final int type) {

        new GuideView.Builder(this)
                .setTitle(title)
                .setContentText(text)
                .setTargetView(findViewById(viewId))
                .setContentTextSize(12) //optional
                .setTitleTextSize(14) //optional
                .setDismissType(GuideView.DismissType.outside) //optional - default dismissible by TargetView
                .setGuideListener(new GuideView.GuideListener() {
                    @Override
                    public void onDismiss(View view) {
                        if (type == 1) {
                            ShowIntro("School", "Press to move the view to School", R.id.reloc_school, 2);
                        } else if (type == 2) {
                            ShowIntro("Your location", "Press to move the view to your current location", R.id.reloc_gps, 3);
                        } else if (type == 3) {
                            ShowIntro("Menu", "Press to open for options such as settings and logout", R.id.popUpMenu, 4);
                        } else if (type == 4) {
                            recall.setEnabled(false);
                            recall.setVisibility(View.VISIBLE);
                            ShowIntro("Recall", "Press to recall in case you mispressed the first call when entering the school perimeter", R.id.recall_school, 5);
                        } else if (type == 5) {
                            recall.setVisibility(View.GONE);
                        }
                    }
                })
                .build()
                .show();
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

        mMap.setOnMarkerClickListener(this);

        bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.ic_bus_mock);
        b = bitmapdraw.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(b, 75, 100, false);
/*
        LATLONG_BUS = new LatLng(32.20, 35.23);

        Bus_MO = new MarkerOptions()
                .position(LATLONG_BUS)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .draggable(false)
                .title("Bus_1");//get text from assigned bus

        mMap.addMarker(Bus_MO);
        */
        //mMap.setOnMarkerDragListener(this);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        // Add a marker to the location and move the camera
        mMap.setMinZoomPreference(11f);
        mMap.addMarker(new MarkerOptions().position(LATLONG_CITY).title("Nablus"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LATLONG_CITY));

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        bus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("Bus_1").child("Location").child("Latitude").exists() && dataSnapshot.child("Bus_1").child("Location").child("Longitude").exists())
                {

                    double Lat = new Double(dataSnapshot.child("Bus_1").child("Location").child("Latitude").getValue().toString());
                    double Long = new Double(dataSnapshot.child("Bus_1").child("Location").child("Longitude").getValue().toString());

                    LATLONG_BUS = new LatLng(Lat, Long);

                    if (Bus != null) {
                        Bus.remove();
                    }
                    Bus = mMap.addMarker(new MarkerOptions()
                            .position(LATLONG_BUS)
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                            .draggable(false)
                            .title("Bus_1"));//add blah blah

                    Log.d(TAG,"UploadBusLocation : the data uploaded successfully");
                }
                else{
                    LATLONG_BUS = new LatLng(32.20, 35.23);
                    Log.d(TAG,"UploadBusLocation : the data does not exist in database, either path is mismatch or path doesn't exist, uploading the default data");
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


    }

    public void markerForGeofence(ArrayList<LatLng> LATLONG_X, ArrayList<String> NAME_X, ArrayList<Bitmap> icon) {

        // Define marker options
        if (mMap != null) {
            for(int i=0 ; i< LATLONG_X.size(); i++) {

                markerOptions.position(LATLONG_X.get(i))
                        .icon(BitmapDescriptorFactory.fromBitmap(icon.get(i)))
                        .title(NAME_X.get(i));

                if(NAME_X.get(i).toString().equals("School")){
                    circleOptions.center(LATLONG_X.get(i))
                            .strokeColor(Color.argb(75, 1,140,1))
                            .fillColor( Color.argb(45, 70,70,70) )
                            .radius( GEOFENCE_RADIUS );
                }
                else {//it is a home yes
                    circleOptions.center(LATLONG_X.get(i))
                            .strokeColor(Color.argb(75, 50, 70, 250))
                            .fillColor(Color.argb(45, 70, 70, 70))
                            .radius(GEOFENCE_RADIUS);
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

        //serviceIntent = new Intent( this, GeofenceTransitionService.class);

        serviceIntent = new Intent( this, GeofenceBroadcastReceiver.class);

        serviceIntent.putExtra("autoCaller", isAutoCallOn);

        //getBroadcast

        //return PendingIntent.getForegroundService(this, 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT );

        return PendingIntent.getBroadcast(this, 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT );
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
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        Log.i(TAG, "onLocationChanged");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if(pass) {
        pass = false; // to prevent marker area to overlap.
        // making it connect ONLY once.

        //getting device loaction

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MainActivity.this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onConnected: Permission granted");

            markerForGeofence(LATLONG_LIST, NAME_LIST, ICON_LIST);

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
                                status.startResolutionForResult(MainActivity.this, 342);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                        break;
                    }
                }
            });
        }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 342:
                Log.d(TAG,"startGeofencing: Restarting");
                startGeofencing(NAME_LIST);//restart geofence
                break;
        }
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
    public void onResult(@NonNull Status status) {
        if (status.isSuccess()) {
            Log.d(TAG,"onResult: Data retrieval is success");
        }
        else {
            Log.d(TAG,"onResult: Data retrieval is failure");
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if(!marker.getTitle().matches("Mock bus|Nablus")) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15);
            mMap.animateCamera(cameraUpdate);
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            //FIRETEST
            case R.id.DebugSend :

                sendQueryToSchool();

                break;

            case R.id.reloc_school :

                cameraUpdate = CameraUpdateFactory.newLatLngZoom(LATLONG_SCHOOL, 15);
                mMap.animateCamera(cameraUpdate);

                break;

            case R.id.reloc_home:
                cameraUpdate = CameraUpdateFactory.newLatLngZoom(LATLONG_HOME, 15);
                mMap.animateCamera(cameraUpdate);
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

            case R.id.recall_school:
                Toast.makeText(this, "Recalled", Toast.LENGTH_SHORT).show();

                recall.setEnabled(false);

                sendQueryToSchool();

                break;
        }

    }

    void showPopupWindow(View view) {
        PopupMenu popup = new PopupMenu(MainActivity.this, view);

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

        popup.getMenuInflater().inflate(R.menu.popupmenu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){

                    case R.id.about :

                        AlertDialog.Builder Builder_about = new AlertDialog.Builder(MainActivity.this);
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
                        //AlertDialog.Builder Builder_guide = new AlertDialog.Builder(MainActivity.this);
                        //View View_guide = getLayoutInflater().inflate(R.layout.dialog_guide, null);

                        Toast.makeText(getBaseContext(),"click on guide ",Toast.LENGTH_LONG).show();

                        ShowIntro("SetTheme", "Select Theme and Apply on your video", R.id.reloc_home, 1);

                        break;

                    case R.id.Assigned_Bus:

                        bottomsheet_buses bottomSheet = new bottomsheet_buses();
                        bottomSheet.show(getSupportFragmentManager(), "Bottomsheet_buses");

                        break;

                    case R.id.Settings:
                        AlertDialog.Builder Builder_settings = new AlertDialog.Builder(MainActivity.this);
                        View View_settings = getLayoutInflater().inflate(R.layout.dialog_settings, null);

                        FinDate = View_settings.findViewById(R.id.DateAbsen);
                        FinDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));

                        final CheckBox mCheckBox2 = View_settings.findViewById(R.id.ctSchllAuto);
                        final ToggleButton GeoToggle = View_settings.findViewById(R.id.CtrlGeofence);
                        final CheckBox Child_1 = View_settings.findViewById(R.id.CBC1),
                                       Child_2 = View_settings.findViewById(R.id.CBC2),
                                       Child_3 = View_settings.findViewById(R.id.CBC3);

                        mCheckBox2.setChecked(isAutoCallOn);
                        GeoToggle.setChecked(isTurnedOn);

                        GeoToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                if(isChecked) {
                                    //in order to update the service, we need to stop it and activate it again.

                                    stopService(serviceIntent);
                                    stopGeofencing();
                                    startGeofencing(NAME_LIST);
                                    Log.d(TAG,"Geofence is re/starting");
                                    //Toast.makeText(getApplicationContext(), "Geofence is re/starting", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    stopService(serviceIntent);
                                    stopGeofencing();
                                    Log.d(TAG,"Geofence is killed");
                                    //Toast.makeText(getApplicationContext(), "Geofence is killed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });


                        Builder_settings.setTitle("Settings :")
                                .setView(View_settings)
                                .setCancelable(false)
                                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        isAutoCallOn = mCheckBox2.isChecked() ? true : false;
                                        isTurnedOn = GeoToggle.isChecked() ? true : false;

                                        final StringBuilder absent = new StringBuilder(300);
                                        CheckBox[] CB = {Child_1,Child_2,Child_3};

                                        boolean anyAbsent = false;
                                        int allAbs = 0;

                                        for(int it = 0;it < 3;it++){
                                            if(CB[it].isChecked()){
                                                absent.append(CB[it].getText().toString().trim()+", ");
                                                anyAbsent= true;
                                                allAbs++;
                                            }
                                        }

                                        if(anyAbsent){
                                            if (absent.length() > 0) {
                                                absent.setLength(absent.length() - 2);
                                                absent.append(".");
                                            }
                                          String abs = absent.toString().trim();
                                          Log.d(TAG,"Absent :"+abs);
                                          informAbseneseToSchool(FinDate.getText().toString(),abs);

                                        }

                                        users_abs = database.getReference().child("Users").child(ID).child("Absense");
                                        if(allAbs==3){
                                            users_abs.setValue(true);

                                            Log.d(TAG,"***Uploading the Absense to firebase : TRUE***");
                                        }
                                        else if(allAbs >= 0 && allAbs < 3) {
                                            users_abs.setValue(false);
                                            Log.d(TAG,"***Uploading the Absense to firebase : FALSE***");
                                        }

                                        Toast.makeText(getApplicationContext(), "Options saved", Toast.LENGTH_SHORT).show();
                                        SavePreferences_Settings();
                                        dialogInterface.dismiss();
                                    }

                                })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Toast.makeText(getApplicationContext(), "Options canceled", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();

                            }
                        });

                        AlertDialog D_settings = Builder_settings.create();

                        D_settings.show();

                        Window window = D_settings.getWindow();
                        window.setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

                    break;

                    case R.id.logout_butt:

                        new AlertDialog.Builder(MainActivity.this)
                                .setMessage("Are you sure want to Logout?")
                                .setPositiveButton("yes", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface paramDialogInterface, int paramInt)
                                    {

                                        removeLoginData();
                                        K = new Intent(MainActivity.this, Login.class);
                                        startActivity(K);
                                        unregisterReceiver(BTR); // removeLoginData();  unregisterReceiver(BTR); stopService(serviceIntent); stopGeofencing(); stopBusTracker(); finish();

                                        //to stop geofencing, and GeofenceBroadcastReceiver
                                        stopService(serviceIntent);
                                        stopGeofencing();

                                        //to stop foreground service of Tracker_bus_driver
                                        stopBusTracker();

                                        NotificationManager notifManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                        notifManager.cancelAll();

                                        Animatoo.animateSwipeRight(MainActivity.this);
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

    private void informAbseneseToSchool(String Date,String absentStudent) {
        Map<String, Object> user = new HashMap<>();
        user.put("UserId",ID);
        user.put("Date", Date);
        user.put("Absent", absentStudent);
        user.put("Token", token);

        colRef_ABS.document(ID).set(user);
    }

    public void Shutdown(){

        new AlertDialog.Builder(MainActivity.this)
                .setMessage("Are you sure want to shutdown this app?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt)
                    {

                        unregisterReceiver(BTR);
                        stopService(serviceIntent);
                        stopGeofencing();
                        stopBusTracker();
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
        //LoadPreferences_Settings();
        registerReceiver(BTR, BTIntent);
        mapView.onResume();


        // This registers mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mMessageReceiver,
                        new IntentFilter("my-integer"));
    }

    @Override
    protected void onStop() {
        super.onStop();

        mGoogleApiClient.disconnect();
        mapView.onStop();
    }

    @Override
    protected void onPause() {

        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(mMessageReceiver);
        super.onPause();
        mapView.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

        try{

            unregisterReceiver(BTR);
        }catch (Exception e){
            Log.d(TAG,"The Reciver is Unregestred already");
        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void SavePreferences_Settings() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_SETTINGS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(ACALL, isAutoCallOn);
        editor.putBoolean(GTOG, isTurnedOn);

        Log.d(TAG,"SavePreferences_Settings : isAutoCallOn = "+isAutoCallOn+", isTurnedOn = "+isTurnedOn);

        editor.commit();
    }

    public void LoadPreferences_Settings() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_SETTINGS, MODE_PRIVATE);

        isAutoCallOn = sharedPreferences.getBoolean(ACALL, false);
        isTurnedOn = sharedPreferences.getBoolean(GTOG,true);

        Log.d(TAG,"LoadPreferences_Settings : isAutoCallOn = "+isAutoCallOn+", isTurnedOn = "+isTurnedOn);
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            int Mess_Code = intent.getIntExtra("message",-1);
           // Toast.makeText(getBaseContext(),"School ",Toast.LENGTH_LONG).show();
            switch(Mess_Code) {
                case 7:
                    //Toast.makeText(getBaseContext(),"ENTERING "+Mess_Code,Toast.LENGTH_LONG).show();

                    if(!isAutoCallOn) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setCancelable(false)
                                .setMessage("do you want to inform the school to pick up the child/ren?")
                                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                        sendQueryToSchool();
                                        //send query to school
                                        //if error occures, auto recall
                                    }
                                })
                                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                        recall.setEnabled(true);
                                        recall.setVisibility(View.VISIBLE);
                                    }

                                }).show();
                    } else {
                        sendQueryToSchool();
                        //send query to school
                        //if error occures, auto recall
                    }

                    break;
                case 2:
                    //Toast.makeText(getBaseContext(),"EXITING "+Mess_Code,Toast.LENGTH_LONG).show();
                    Log.d(TAG,"Broadcast Receiver code :"+Mess_Code);
                    //didIAlreadyInformedSchool = false;
                    recall.setEnabled(false);
                    recall.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private void sendQueryToSchool() {

        pendingConfirmation();

        View_2FA = getLayoutInflater().inflate(R.layout.dialog_twofactorauthentication, null);

        AuthDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Two-factor authentication :")
                .setView(View_2FA)
                .setCancelable(false)
                .setPositiveButton("Authenticate", null)
                .show();

        Map<String, Object> user = new HashMap<>();
        user.put("UserId",ID);
        user.put("Confirmation", "PROCESSING");
        user.put("Token", token);
        user.put("Code_2FA", "DULL");

        colRef_CON.document(ID).set(user,SetOptions.merge());

        uploadFromFirestoreCloud();
    }

    private void pendingConfirmation() {
        View view = findViewById(R.id.Main);
        snacCall = Snackbar.make(view,"Pending confirmation...",Snackbar.LENGTH_INDEFINITE);

        snacCall.show();

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            int count = 0;

            @Override
            public void run() {
                count++;

                if (count == 1)
                {

                    snacCall.setText("Pending confirmation.");
                }
                else if (count == 2)
                {
                    snacCall.setText("Pending confirmation..");
                }
                else if (count == 3)
                {
                    snacCall.setText("Pending confirmation...");
                    count = 0;
                }

                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1 * 1000);

    }

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
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    @Override
    public void saveTheDate(String text) {
        FinDate.setText(text);
    }

}