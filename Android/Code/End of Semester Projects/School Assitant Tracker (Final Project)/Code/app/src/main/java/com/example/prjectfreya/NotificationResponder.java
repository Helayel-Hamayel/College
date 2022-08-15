package com.example.prjectfreya;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static com.example.prjectfreya.GeofenceTransitionService_DULL.Notification_ID_BG;
import static com.example.prjectfreya.Tracker_bus_driver.Notification_ID_MAIN;

public class NotificationResponder extends BroadcastReceiver {

    private static final String TAG = "NotificationResponder";

    @Override

    public void onReceive(Context context, Intent intent) {

        if (intent != null) {
            int noti_id = intent.getIntExtra("Notification_code", -1);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            switch (noti_id){

                case  24534 ://Dismiss_Code
                   manager.cancel(Notification_ID_MAIN);
                    break;

                case 87 ://Open_CALLIN
                    Log.d(TAG, "Open_CALLIN");

                    manager.cancel(Notification_ID_BG);

                    Intent activityIntent = new Intent(context, SplashScreen.class);
                    context.startActivity(activityIntent);

                    break;

            }

        }
    }

}