package com.example.prjectfreya;

import static com.example.prjectfreya.MainActivity.Channel_ID;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class Tracker_bus_driver extends Service {

    //private static final String Channel_ID="Tracker_bus_driver_Noti";
    protected static final String TAG="Tracker_bus_driver";
    static final int Notification_ID_MAIN=334;


    String ID;
    String Bus_X = "Bus_1";

    private Location BusLoc, HomeLoc;
    private int GEOFENCE_RADIUS;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mainBase = database.getReference();

    @Override
    public void onCreate() {
        super.onCreate();

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
                        .setContentText("open the notification for further information")
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

        ID = intent.getStringExtra("ID");
        //add get assignedbus in near future
        GEOFENCE_RADIUS = intent.getIntExtra("Geofence_Radius",140);

        HomeLoc = new Location("");//fixed place

        mainBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                HomeLoc.setLatitude((double) snapshot.child("Users").child(ID).child("Home_cor").child("Latitude").getValue());
                HomeLoc.setLongitude((double) snapshot.child("Users").child(ID).child("Home_cor").child("Longitude").getValue());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        BusLoc = new Location("");
        mainBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BusLoc.setLatitude((double) snapshot.child("Bus").child(Bus_X).child("Location").child("Latitude").getValue());
                BusLoc.setLongitude((double) snapshot.child("Bus").child(Bus_X).child("Location").child("Longitude").getValue());

                calculateTheDistance();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

            /*
            runnable = new Runnable() {
            @Override
            public void run() {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager
                .getRunningTasks(Integer.MAX_VALUE);
        boolean isActivityFound = false;

        if (services.get(0).topActivity.getPackageName().toString()
                .equalsIgnoreCase(context.getApplicationContext().getPackageName().toString())) {
            isActivityFound = true;
        }

        //add a timer blockade to prevent duplicate calls bug
        if (isActivityFound) {
            //commence the dialog window
            sendMessage(state);
            Log.d(TAG, "The app is visible/resumed");
        } else {
            //commence notification
            notifyTheUser();
            Log.d(TAG, "The app is on background/paused");
        }

             Log.d(TAG,"***Uploading the bus location to firebase***");
             han.postDelayed(this, TimeUnit.SECONDS.toMillis(45));//45 seconds

            }
        };

        han.postDelayed(runnable, 2000);
*/

            return START_REDELIVER_INTENT;
    }

    private void calculateTheDistance() {
        float D = BusLoc.distanceTo(HomeLoc);
        Log.d(TAG,"I am in");
        Log.d(TAG,"Distance: "+D+" ,Radius: "+GEOFENCE_RADIUS );

        if(D <= GEOFENCE_RADIUS){
            Log.d(TAG,"I am dead");

            Intent activityIntent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this,
                    0, activityIntent, 0);

            Intent broadcastIntent = new Intent(this, NotificationResponder.class);
            broadcastIntent.putExtra("Notification_code", 24534);

            PendingIntent dismissIntent = PendingIntent.getBroadcast(this,
                    23, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notify = new NotificationCompat.Builder(this, Channel_ID)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setSubText("The bus is nearing to your location")
                    .setContentText("open the notification for further information")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("the bus is a few minutes away to your house"))
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_bus_large))
                    .setColor(getResources().getColor(R.color.BLU))
                    .setColorized(true)
                    .setLights(Color.BLUE, 500, 500)//this will do
                    .setUsesChronometer(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setWhen(System.currentTimeMillis())
                    .addAction(R.drawable.ic_about, "Dismiss", dismissIntent);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(Notification_ID_MAIN, notify.build());

            Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(1000);

            MediaPlayer mp = MediaPlayer.create(this, R.raw.bus_horn_notif);
            mp.start();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
