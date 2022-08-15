package com.example.prjectfreya;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import static com.example.prjectfreya.MainActivity.Channel_ID;


public class GeofenceTransitionService_DULL extends Service {

    private static final String TAG = "GeofenceTransitionService";
    static final int Notification_ID_BG=66;
    private GeofencingClient geofencingClient;// you need this thing
    private PendingIntent sad;

    public GeofenceTransitionService_DULL() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Intent KILL = new Intent(this, NotificationResponder.class);
            KILL.putExtra("Notification_code", 420);
            //KILL.putExtra("Notification_code", 24534);

            String version = "v0";
            try {
                PackageInfo pInfo = getBaseContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                version = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            PendingIntent KILLIntent = PendingIntent.getBroadcast(this,
                    4, KILL, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new NotificationCompat.Builder(this, Channel_ID)
                    .setSubText("is running on background")
                    .setContentTitle("Tracker "+version)
                    .setContentText("open the notification for further information")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("when you finish press shutdown to shut the app entirely"))
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setWhen(System.currentTimeMillis())
                    .setColor(getResources().getColor(R.color.BLU))
                    .addAction(R.drawable.ic_about, "SHUTDOWN", KILLIntent)
                    .build();

            startForeground(1, notification);
            Log.d(TAG, "GeofenceTransitionService : NotificationChannel is done ");
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Boolean X = intent.getBooleanExtra("autoCaller",false);

        //Log.d(TAG, "The call is " + X);

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.d(TAG, "GeofencingEvent error " + geofencingEvent.getErrorCode());
        }
        else
        {
            int transaction = geofencingEvent.getGeofenceTransition();
            List<Geofence> geofences;
            geofences = geofencingEvent.getTriggeringGeofences();
            Geofence geofence = geofences.get(0);
            //Log.d(TAG, "I am inside Geo func");
            /*
            Log.d("value is " , "|============|  \n");
            for (int i = 0 ; i < geofences.size() ; i++)
                Log.d("value is " , geofences.get(i).toString()+"\n");
            Log.d("value is " , "\n");
            Log.d("value is " , "|============|  \n");
            */


            // the '&& false' thing is to disable the dwelling
            if(geofence.getRequestId().matches("Home|0")) {//HOME
                if (transaction == Geofence.GEOFENCE_TRANSITION_ENTER) {
                    Log.d(TAG, "You entered HOME");
                } else if (transaction == Geofence.GEOFENCE_TRANSITION_DWELL && false) {
                    Log.d(TAG, "You are Dwelling HOME");
                } else if (transaction == Geofence.GEOFENCE_TRANSITION_EXIT) {
                    Log.d(TAG, "You are Exited HOME");
                }
            }
            else if (geofence.getRequestId().matches("School|1")){//SCHOOL
                if (transaction == Geofence.GEOFENCE_TRANSITION_ENTER) {
                    Log.d(TAG, "You entered SCHOOL");
                    //issue :
                    //add condition whne the app is on background, send notification instead of sendmessage thing
                    //how to pass the automatic option ?
                    notifyTheUser();
                    checkAppState(7);

                } else if (transaction == Geofence.GEOFENCE_TRANSITION_DWELL && false) {
                    Log.d(TAG, "You are Dwelling SCHOOL");
                    //Toast.makeText(getApplicationContext(),"You are DWELL",Toast.LENGTH_LONG).show();
                } else if (transaction == Geofence.GEOFENCE_TRANSITION_EXIT) {
                    Log.d(TAG, "You are Exited SCHOOL");
                    //add condition whne the app is on background, send notification instead of sendmessage thing
                    checkAppState(2);
                }
            }
            geofences.clear();
        }
        return START_NOT_STICKY;
    }

    private void checkAppState(int state) {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager
                .getRunningTasks(Integer.MAX_VALUE);
        boolean isActivityFound = false;

        if (services.get(0).topActivity.getPackageName().toString()
                .equalsIgnoreCase(getApplicationContext().getPackageName().toString())) {
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
    }

    private void notifyTheUser() {

        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);

        //==== The actions for notification
        //YES
        Intent yes_broadcastIntent = new Intent(this, NotificationResponder.class);
        yes_broadcastIntent.putExtra("Notification_code", 87);

        PendingIntent yesIntent = PendingIntent.getBroadcast(this,
                0, yes_broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //====
        //NO
        Intent no_broadcastIntent = new Intent(this, NotificationResponder.class);
        no_broadcastIntent.putExtra("Notification_code", 86);

        PendingIntent noIntent = PendingIntent.getBroadcast(this,
                1, no_broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //====
        //DISMISS
        Intent broadcastIntent = new Intent(this, NotificationResponder.class);
        broadcastIntent.putExtra("Notification_code", 24534);

        PendingIntent dismissIntent = PendingIntent.getBroadcast(this,
                3, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //====

        NotificationCompat.Builder notify = new NotificationCompat.Builder(this, Channel_ID)
                .setAutoCancel(false)
                .setOngoing(true)
                .setSubText("Entered a geofence")
                .setContentTitle("You entered the school perimeter")
                .setContentText("open the notification for futher information")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Do you want to call the school to pick your children?"))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_child_notify))
                .setColor(getResources().getColor(R.color.BLU))
                .setColorized(true)
                .setLights(Color.BLUE, 500, 500)//this will do
                .setPriority(Notification.PRIORITY_HIGH)
                .setWhen(System.currentTimeMillis())
                //.addAction(R.drawable.ic_about, "Dismiss", dismissIntent)
                .addAction(R.drawable.ic_about, "Yes", yesIntent)
                .addAction(R.drawable.ic_about, "No", noIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(Notification_ID_BG, notify.build());

        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);

        MediaPlayer mp = MediaPlayer.create(this, R.raw.bus_horn_notif);
        mp.start();
    }

    private void sendMessage(int code) {
        // The string "my-integer" will be used to filer the intent
        Intent intent = new Intent("my-integer");
        // Adding some data
        intent.putExtra("message", code);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}