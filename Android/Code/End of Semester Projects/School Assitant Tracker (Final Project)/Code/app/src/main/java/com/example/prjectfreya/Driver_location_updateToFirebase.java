package com.example.prjectfreya;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static com.example.prjectfreya.MainActivity.Channel_ID;

public class Driver_location_updateToFirebase extends Service
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    final Handler han = new Handler();
    Runnable runnable;
    private final String TAG = "Driver_location_updateToFirebase";
    private String Assigned_bus;
    private Context context;
    private boolean stopService = false;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef ;

    /* For Google Fused API */
    protected GoogleApiClient mGoogleApiClient;
    protected LocationSettingsRequest mLocationSettingsRequest;
    private Double latitude = 0.01, longitude = 0.01;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    /* For Google Fused API */

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        if (Build.VERSION.SDK_INT >= 26) {

            String version = "v0";
            try {
                PackageInfo pInfo = getBaseContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                version = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            Intent open_broadcastIntent = new Intent(this, NotificationResponder.class);
            open_broadcastIntent.putExtra("Notification_code", 87);

            PendingIntent openIntent = PendingIntent.getBroadcast(this,
                    0, open_broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new NotificationCompat.Builder(this, Channel_ID)
                    .setSubText("is running on background")
                    .setContentTitle("Tracker " + version)
                    .setContentText("open the notification for futher information")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("to access to the app, press \"Open the application\""))
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setWhen(System.currentTimeMillis())
                    .setColor(getResources().getColor(R.color.BLU))
                    .addAction(R.drawable.ic_about, "Open the application", openIntent)
                    .build();

            startForeground(1, notification);
            Log.d(TAG, "Creation of notification channel is a success ");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Assigned_bus = intent.getStringExtra("ASI_BUS");
        Log.d(TAG, "The Assigned bus is : " + Assigned_bus);

        myRef = database.getReference("Bus").child(Assigned_bus).child("Location");

        runnable = new Runnable() {
            @Override
            public void run() {
                    if (!stopService) {

                        myRef.child("Latitude").setValue(latitude);
                        myRef.child("Longitude").setValue(longitude);
                        Log.d(TAG,"***Uploading the bus location to firebase***");
                        han.postDelayed(this, TimeUnit.SECONDS.toMillis(45));//45 seconds
                    }
            }
        };

        han.postDelayed(runnable, 2000);

        buildGoogleApiClient();
        Log.d(TAG,"onStartCommand is called");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Service Stopped");
        stopService = true;
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            Log.d(TAG, "Location Update Callback Removed");
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location Changed Latitude : " + location.getLatitude() + "\tLongitude : " + location.getLongitude());

        latitude = (location.getLatitude());
        longitude = (location.getLongitude());

        if (latitude == 0.01 && longitude == 0.01) {
            requestLocationUpdate();
        } else {
            Log.d(TAG, "Latitude : " + location.getLatitude() + "\tLongitude : " + location.getLongitude());
        }
        Log.d(TAG,"onLocationChanged is called");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(35 * 1000);//35 seconds
        mLocationRequest.setFastestInterval(30 * 1000);//30 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();

        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.d(TAG, "LocationSettingsResponse(aka : GPS) is a success");
                        requestLocationUpdate();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.d(TAG, "Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
                }
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Log.d(TAG, "checkLocationSettings -> onCanceled");
            }
        });
        Log.d(TAG,"onConnected is called");
    }

    @Override
    public void onConnectionSuspended(int i) {
        connectGoogleClient();
        Log.d(TAG,"onConnectionSuspended is called");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        buildGoogleApiClient();
        Log.d(TAG,"onConnectionFailed is called");
    }

    protected synchronized void buildGoogleApiClient() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mSettingsClient = LocationServices.getSettingsClient(context);

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        connectGoogleClient();

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.d(TAG, "===================================");
                Log.d(TAG, "Location Received");
                mCurrentLocation = locationResult.getLastLocation();
                onLocationChanged(mCurrentLocation);
            }
        };

        Log.d(TAG,"buildGoogleApiClient is called");
    }

    private void connectGoogleClient() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(context);
        if (resultCode == ConnectionResult.SUCCESS) {
            mGoogleApiClient.connect();
        }
        Log.d(TAG,"connectGoogleClient is called");
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdate() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        Log.d(TAG,"requestLocationUpdate is called");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG,"onStatusChanged is called");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG,"onProviderEnabled is called");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG,"onProviderDisabled is called");
    }


}