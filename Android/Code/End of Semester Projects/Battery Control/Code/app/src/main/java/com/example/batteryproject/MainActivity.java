package com.example.batteryproject;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.ParcelUuid;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;


public class MainActivity
        extends AppCompatActivity
        implements View.OnClickListener {

    public static final String SETTINGS_VALUE_MAIN_ACTION = "com.example.batteryproject.SETTINGS_VALUE_MAIN_ACTION";
    public static final String BLUETOOTH_PASS_ACTION = "com.example.batteryproject.BLUETOOTH_PASS_ACTION";

    private static final int REQ_CODE_SETTINGS=37,REQ_CODE_BLUECONNECT=38;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1998;

    private java.util.Set<BluetoothDevice> bondedDevices;
    private Object[] devices;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private BluetoothAdapter BluetoothStats ;

    private String PL; // PL = Percentage Limiter
    private Boolean AL; // AL = Alarm,
    private int PC;//PC = Pass card

    private Button Con,Set,Ab;
    private TextView StBlu,StChrg,StBattper,StAlarm,StBattLimit;

    private Intent K,TRK;
    private IntentFilter BTIntent;

    private BluetoothAdapter BA_Main ;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.mipmap.ic_exit));

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        BA_Main  = BluetoothAdapter.getDefaultAdapter(); //bluetooth

        BluetoothStats  = BluetoothAdapter.getDefaultAdapter();

        //buttons
        Con=findViewById(R.id.blubutt);
        Set=findViewById(R.id.setbutt);
        Ab=findViewById(R.id.abbutt);

        //buttons listeners for explicte activities
        Con.setOnClickListener(this);
        Set.setOnClickListener(this);
        Ab.setOnClickListener(this);

        //Textviews status
        StBlu=findViewById(R.id.blstts);
        StChrg=findViewById(R.id.chrstts);
        StBattper=findViewById(R.id.battperstts);

        StAlarm=findViewById(R.id.alarmstatus);
        StBattLimit=findViewById(R.id.percstatus);

        //Broadcasts for Bluetooth, Battery Charger AND battery level percentage
        BTIntent = new IntentFilter();
        BTIntent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        BTIntent.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(BTR, BTIntent);

        //To check if the Bluetooth is activated FROM outside
        if (BA_Main.isEnabled())
        {
            StBlu.setText(" ONLINE ");
        }
        else if(!BA_Main.isEnabled())
        {
            StBlu.setText(" OFFLINE ");
        }

        LoadPreferences();

        TRK=new Intent ( MainActivity.this,Tracker.class );
        TRK.setAction (SETTINGS_VALUE_MAIN_ACTION);
        TRK.putExtra ( "Percentage",PL );
        TRK.putExtra ( "Alarm",AL );
        startService(TRK);
    }

    private final BroadcastReceiver BTR = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (action.equals(BA_Main.ACTION_STATE_CHANGED))
            {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BA_Main.ERROR);
                switch (state) {
                    case (BluetoothAdapter.STATE_ON):
                        StBlu.setText(" ONLINE ");
                        break;

                    case (BluetoothAdapter.STATE_OFF):
                        StBlu.setText(" OFFLINE");
                        break;
                }
            }

            else if(action.equals(Intent.ACTION_BATTERY_CHANGED))
            {
                int deviceStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

                StBattper.setText(level+"%");

                switch(deviceStatus)
                {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        StChrg.setText(" CHARGING ");
                        break;

                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        StChrg.setText(" DISCHARGING ");
                        break;

                    case BatteryManager.BATTERY_STATUS_FULL:
                        StChrg.setText(" FULL ");
                        break;
                }
            }
        }
    };

    @Override
    public void onBackPressed()
    {
        SavePreferences();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy()
    {
        SavePreferences();
        unregisterReceiver(BTR);
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp()
    {

        try {
            bondedDevices = BluetoothStats.getBondedDevices();

            devices = (Object[]) bondedDevices.toArray();
            device = (BluetoothDevice) devices[0];
            ParcelUuid[] uuids = device.getUuids();

            socket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(device, 1);

            socket.connect();

            String X = "45"; // after unplugging the device, send the value to "reset" the arduino device
            outputStream = socket.getOutputStream();
            outputStream.write(X.getBytes());
            socket.close();

            unpairDevice(device);
        }
        catch (Exception R)
        {}

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit? this will shutdown the entire application")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public final void onClick(DialogInterface dialog, int id)
                    {
                        Toast.makeText(MainActivity.this ,"Shutting down...", Toast.LENGTH_LONG).show();
                        onBackPressed ();

                        if(TRK!=null)
                        stopService(TRK);
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

       // Toast.makeText(MainActivity.this ,"Shutting down...", Toast.LENGTH_SHORT).show();

        return super.onSupportNavigateUp();
    }

    private void unpairDevice(BluetoothDevice device)
    {
        try {
            Method m = device.getClass().getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v)
    {
        if(v==Con)
        {
            K=new Intent(this,BluConnect.class);
            startActivityForResult(K,REQ_CODE_BLUECONNECT);
        }
        else if(v==Set)
        {
            K=new Intent(this, SettingsActivity.class);
            startActivityForResult(K,REQ_CODE_SETTINGS);
        }
        else if(v==Ab)
        {
            K=new Intent(this, ATApp.class);
            startActivity(K);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQ_CODE_SETTINGS)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                StBattLimit.setText(data.getStringExtra("Settings_Percentage"));
                StAlarm.setText((data.getBooleanExtra("Settings_Alarm",false)? "ENABLED":"DISABLED"));
                AL=data.getBooleanExtra("Settings_Alarm",false);
                PL=data.getStringExtra("Settings_Percentage");
                SavePreferences();
                //Toast.makeText(getApplicationContext(),"Batt = "+PL+" // Alarm = "+AL, Toast.LENGTH_LONG).show();

                TRK=new Intent ( MainActivity.this,Tracker.class );
                TRK.setAction (SETTINGS_VALUE_MAIN_ACTION);
                TRK.putExtra ( "Percentage",PL );
                TRK.putExtra ( "Alarm",AL );
                startService(TRK);
            }
        }
        else if (requestCode == REQ_CODE_BLUECONNECT)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                PC=data.getIntExtra("PASSCARD",0);

               // Toast.makeText(getApplicationContext(),"pc = "+PC, Toast.LENGTH_LONG).show();

                TRK=new Intent ( MainActivity.this,Tracker.class );
                TRK.setAction (BLUETOOTH_PASS_ACTION);
                TRK.putExtra ( "PASS_CARD",PC );
                startService(TRK);
            }

        }
    }

    public void SavePreferences()
    {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("Alarm", StAlarm.getText().toString());
        editor.putString("PercentageLimiter", StBattLimit.getText().toString());

        editor.commit();
    }
    public void LoadPreferences()
    {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);


        PL = sharedPreferences.getString("PercentageLimiter", "85%");
        AL = ( (sharedPreferences.getString("Alarm", "DISABLED").matches("")) ? true : false);


        StAlarm.setText(sharedPreferences.getString("Alarm", "DISABLED"));
        StBattLimit.setText(sharedPreferences.getString("PercentageLimiter", "85%"));
    }
}