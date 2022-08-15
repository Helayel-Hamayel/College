package com.example.batteryproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;


public class Tracker extends Service
{
    public static final String SHUTDOWN_ACTION = "com.example.batteryproject.SHUTDOWN_ACTION";

    private static final int REQ_CODE=37,Notification_ID=45;
    private static final String Channel_ID="420_BattObv";
    private NotificationCompat.Builder notify ;
    private BluetoothAdapter BluetoothStats ;
    private IntentFilter TY;

    private Set<BluetoothDevice> bondedDevices;
    private Object[] devices;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;

    private String Perc; // Battery LIMITER
    private Boolean Alarm; // to Check if the user enabled the Alarm or not
    private int PValue; //turn the Perc to Integer
    private int PASS=0; // to check that when Bluetooth is on is either CONNECTED TO A DEVICE OR NOT
    private int DONE=0;

    private MediaPlayer AL=null; //AL for ALARM

    private Handler H = new Handler();

    @Override
    public void onCreate()
    {
        super.onCreate ();

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O)
        {
            createNotificationChannel (this);
        }

        BluetoothStats  = BluetoothAdapter.getDefaultAdapter(); //bluetooth

        TY = new IntentFilter();
        TY.addAction(Intent.ACTION_POWER_CONNECTED);
        TY.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(T1, TY);

        Intent Home = new Intent(this, MainActivity.class);
        PendingIntent HPI = PendingIntent.getActivity(this,0, Home, PendingIntent.FLAG_UPDATE_CURRENT);

        notify = new NotificationCompat.Builder(this,Channel_ID )
                .setAutoCancel (false)
                .setOngoing (true)
                .setContentText("Tap for Home menu")
                .setSubText ( "Running" )
                .setSmallIcon (R.mipmap.ic_eye)
                .setColor (getResources().getColor(R.color.BLU))
                .setColorized (true)
                .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                .setWhen(0)
                .setStyle(new NotificationCompat.InboxStyle().addLine("Tap for Home menu"))
                .setContentIntent(HPI);

        Intent SI = new Intent(Tracker.this, BCReciver.class);
        SI.setAction(SHUTDOWN_ACTION);
        PendingIntent SPI=PendingIntent.getBroadcast(this, 0, SI, PendingIntent.FLAG_CANCEL_CURRENT);
        notify.addAction(new NotificationCompat.Action( R.mipmap.ic_shut,"Shutdown",SPI));

        Notification N=notify.build();
        startForeground ( Notification_ID, N );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        //Toast.makeText(getApplicationContext(),"i am in onStartCommand =>"+Perc, Toast.LENGTH_SHORT).show();
        String option=intent.getAction ();

        if(option!=null)
        {
            switch (option)
            {
                case MainActivity.SETTINGS_VALUE_MAIN_ACTION:

                    Perc = intent.getStringExtra("Percentage");
                    Alarm = intent.getBooleanExtra("Alarm", false);

                    if(Perc.length()==4)
                    {PValue = Integer.parseInt(Perc.substring(0, 3));}
                    else if(Perc.length()==3)
                    {PValue = Integer.parseInt(Perc.substring(0, 2));}

                    //Toast.makeText(getApplicationContext(), "(SET_act) BAT = "+PValue+" // ALARM = "+Alarm, Toast.LENGTH_LONG).show();
                    break;

                case MainActivity.BLUETOOTH_PASS_ACTION:

                    PASS = intent.getIntExtra("PASS_CARD",0);
                    //Toast.makeText(getApplicationContext(), "BLUE_act =>"+PASS, Toast.LENGTH_LONG).show();
                    break;

                default:

                    break;
            }
        }
        return START_REDELIVER_INTENT;

    }

    private void unpairDevice(BluetoothDevice device)
    {
        try {
            Method m = device.getClass().getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e)
        {
           //Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy()
    {
        unpairDevice(device);
        unregisterReceiver(T1);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    private void createNotificationChannel(Context context)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(Channel_ID, "General", NotificationManager.IMPORTANCE_DEFAULT );
            NotificationManager Manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Manager.createNotificationChannel(channel);
        }
    }

    private final BroadcastReceiver T1 = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {

            String action = intent.getAction();
            if (action.equals(Intent.ACTION_POWER_CONNECTED))
            {
                if (DONE == 1)
                {
                    DONE = 0;
                    PercentageChecker.run();
                }
            }
            else if (action.equals(Intent.ACTION_POWER_DISCONNECTED))
            {
                BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
                int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

                if (batLevel >= PValue)
                {
                    DONE = 1;
                }

                if (AL!=null)
                {
                     AL.stop ();
                     AL.release ();
                     AL=null;
                }
                if (BluetoothStats.isEnabled() && PASS == 1) // check if Bluetooth is enabled AND is paired to a device
                {
                    try {
                        bondedDevices = BluetoothStats.getBondedDevices();

                        devices = (Object[]) bondedDevices.toArray();
                        device = (BluetoothDevice) devices[0];
                        ParcelUuid[] uuids = device.getUuids();

                        socket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(device, 1);

                        socket.connect();

                        String X = "70"; // after unplugging the device, send the value to "reset" the arduino device
                        outputStream = socket.getOutputStream();
                        outputStream.write(X.getBytes());
                        socket.close();
                        //PASS = 0;
                    }
                    catch (Exception R)
                    {
                       // Toast.makeText(getApplicationContext(), R.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                //Toast.makeText(getApplicationContext(), "Runnable is stopped ", Toast.LENGTH_SHORT).show();
                H.removeCallbacks(PercentageChecker);
            }
        }

    };

    private Runnable PercentageChecker = new Runnable()
    {
        @Override
        public void run()
        {
            BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
            int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

                //Toast.makeText(getApplicationContext(),"DONE_ACT => "+DONE, Toast.LENGTH_SHORT).show();

                if(batLevel>=PValue&&DONE==0)
                {
                    DONE = 1;

                    if( !BluetoothStats.isEnabled() || BluetoothStats.isEnabled() && PASS==0 )
                    {
                       // Toast.makeText(getApplicationContext(),"BLUETOOTH IS SHUT, OR NOT WITH NO CONNECTED =>"+PASS, Toast.LENGTH_SHORT).show();

                        if(Alarm==true)
                        {
                            if (AL==null)
                            {
                                AL=new MediaPlayer ();
                                try
                                {
                                    AL.setDataSource ( getApplicationContext(), Settings.System.DEFAULT_ALARM_ALERT_URI );
                                    AL.prepare();
                                }
                                catch (IOException e)
                                {
                                   // Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (!AL.isPlaying ())
                            {
                                AL.setLooping(true);
                                AL.start();
                            }
                        }
                        H.removeCallbacks(PercentageChecker);
                    }
                    else if(BluetoothStats.isEnabled()&& PASS==1 )
                    {
                       // Toast.makeText(getApplicationContext(),"BLUETOOTH IS ON WITH CONNECTED DEVICE", Toast.LENGTH_SHORT).show();

                        try
                        {
                            bondedDevices = BluetoothStats.getBondedDevices();

                            devices = (Object[]) bondedDevices.toArray();
                            device = (BluetoothDevice) devices[0];
                            ParcelUuid[] uuids = device.getUuids();

                            socket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(device, 1);

                            socket.connect();

                            String X = "90";
                            outputStream = socket.getOutputStream();
                            outputStream.write(X.getBytes());

                            socket.close();
                        }
                        catch (Exception R)
                        {
                            //Toast.makeText(getApplicationContext(),R.toString(),Toast.LENGTH_LONG).show();
                        }

                        if(Alarm==true)
                        {
                            if (AL==null)
                            {
                                AL=new MediaPlayer ();
                                try
                                {
                                    AL.setDataSource ( getApplicationContext(), Settings.System.DEFAULT_ALARM_ALERT_URI );
                                    AL.prepare();
                                }
                                catch (IOException e)
                                {
                                   // Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (!AL.isPlaying ())
                            {
                                AL.setLooping(false);
                                AL.start();
                            }
                        }

                        H.removeCallbacks(PercentageChecker);
                    }
                }
               H.postDelayed(this, 6000);
            }//run function
    }; //Runnable

}
