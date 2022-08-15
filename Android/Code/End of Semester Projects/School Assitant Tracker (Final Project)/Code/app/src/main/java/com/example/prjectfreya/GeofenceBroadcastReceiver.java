package com.example.prjectfreya;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import static com.example.prjectfreya.MainActivity.Channel_ID;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG ="GeofenceBroadcastReceiver";
    static final int Notification_ID_BG=66;
    private Context context;

    public void onReceive(Context context, Intent intent) {
        this.context = context;

        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;

        Log.d("TopActivityManager",cn.getClassName()); //cn.getClassName()
        //MA_Driver => com.example.prjectfreya.MA_Driver
        //MainActivity => com.example.prjectfreya.MainActivity

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            //String errorMessage = GeofenceStatusCodes.getErrorString(geofencingEvent.getErrorCode());
            Log.e(TAG, "errorMessage");
            return;
        }

        // Get the transition type.

        int transaction = geofencingEvent.getGeofenceTransition();
        List<Geofence> geofences;
        geofences = geofencingEvent.getTriggeringGeofences();
        Geofence geofence = geofences.get(0);


            Log.d(TAG , "|============|  \n");
            for (int i = 0 ; i < geofences.size() ; i++){
                Log.d(TAG ,"value is "+ geofences.get(i).toString()+"\n"); //geofence.getRequestId()
                Log.d(TAG ,"value is "+ geofence.getRequestId()+"\n");}
            Log.d(TAG , "\n");
            Log.d(TAG, "|============|  \n");


        // the '&& false' thing is to disable the dwelling
        if(geofence.getRequestId().contains("Home_")) {//HOME

            if (transaction == Geofence.GEOFENCE_TRANSITION_ENTER) {
                Log.d(TAG, "You entered HOME"); //geofence.getRequestId()
                Log.d(TAG, "Home : "+geofence.getRequestId());
            }
            else if (transaction == Geofence.GEOFENCE_TRANSITION_DWELL && false) {//disabled
                Log.d(TAG, "You are Dwelling HOME");
            }
            else if (transaction == Geofence.GEOFENCE_TRANSITION_EXIT) {
                Log.d(TAG, "You are Exited HOME");
            }

        }
        else if (geofence.getRequestId().matches("School")){//SCHOOL

            if (transaction == Geofence.GEOFENCE_TRANSITION_ENTER) {
                Log.d(TAG, "You entered SCHOOL");
                //issue :
                //add condition whne the app is on background, send notification instead of sendmessage thing
                //how to pass the automatic option ?
                checkAppState(7);

            }
            else if (transaction == Geofence.GEOFENCE_TRANSITION_DWELL && false) {//disabled
                Log.d(TAG, "You are Dwelling SCHOOL");
                //Toast.makeText(getApplicationContext(),"You are DWELL",Toast.LENGTH_LONG).show();
            }
            else if (transaction == Geofence.GEOFENCE_TRANSITION_EXIT) {
                Log.d(TAG, "You are Exited SCHOOL");
                //add condition when the app is on background, send notification instead of sendmessage thing
                sendMessage(2);
            }
        }
        geofences.clear();

    }

    private void checkAppState(int state) {//this ONLY for code 7, which is ENTERING
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
    }

    private void notifyTheUser() {

        //==== The actions for notification
        //YES
        Intent open_broadcastIntent = new Intent(context, NotificationResponder.class);
        open_broadcastIntent.putExtra("Notification_code", 87);

        PendingIntent openIntent = PendingIntent.getBroadcast(context,
                0, open_broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notify = new NotificationCompat.Builder(context, Channel_ID)
                .setAutoCancel(true)
                .setOngoing(true)
                .setSubText("Entered a geofence")
                .setContentTitle("You have entered the school perimeter")
                .setContentText("open this notification for futher information")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("to inform the school press \"Open the application\" for further options"))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_child_notify))
                .setColor(context.getResources().getColor(R.color.BLU))
                .setColorized(true)
                .setLights(Color.BLUE, 500, 500)//this will do
                .setPriority(Notification.PRIORITY_HIGH)
                .setWhen(System.currentTimeMillis())
                .addAction(R.drawable.ic_about, "Open the application", openIntent);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(Notification_ID_BG, notify.build());

        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);

        MediaPlayer mp = MediaPlayer.create(context, R.raw.bus_horn_notif);
        mp.start();
    }

    private void sendMessage(int code) {
        // The string "my-integer" will be used to filer the intent
        Intent intent = new Intent("my-integer");
        // Adding some data
        intent.putExtra("message", code);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }


}
