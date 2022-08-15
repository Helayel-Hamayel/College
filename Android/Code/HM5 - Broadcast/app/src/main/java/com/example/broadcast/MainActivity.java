package com.example.broadcast;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity
        extends AppCompatActivity
        implements View.OnClickListener {

    private BluetoothAdapter bluetoothAdapter;
    private IntentFilter B,WF,BTIntent;
    private TextView BATT,BATT2,WFR,WFR2,BR;
    private Button WFB,BB;
    private WifiManager I;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BATT=findViewById(R.id.tvBatt);
        BATT2=findViewById(R.id.tvBatt2);

        WFR=findViewById(R.id.tvWifi);
        WFR2=findViewById(R.id.tvWifi2);

        BR=findViewById(R.id.tvB);

        WFB=findViewById(R.id.buttWifi);
        BB=findViewById(R.id.BluetoothButt);

        WFB.setOnClickListener(this);
        BB.setOnClickListener(this);

        I = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter.isEnabled())
        {
            BR.setText("Bluetooth Status : Activated");
        }
        else if(!bluetoothAdapter.isEnabled())
        {
            BR.setText("Bluetooth Status : Deactivated");
        }

        BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(BTR, BTIntent);

        B = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(BatteryReceiver,B);

        WF=new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(WifiReceiver, WF);

    }

    private final BroadcastReceiver BTR = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (action.equals(bluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, bluetoothAdapter.ERROR);

                switch (state) {
                    case (BluetoothAdapter.STATE_ON):

                        BR.setText("Bluetooth Status : Activated");
                        bluetoothAdapter.startDiscovery();

                        break;

                    case (BluetoothAdapter.STATE_OFF):

                        BR.setText("Bluetooth Status : Deactivated");
                        bluetoothAdapter.cancelDiscovery();
                        break;
                }

            }
        }
    };



    private BroadcastReceiver BatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int deviceStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

            BATT2.setText("Battery Percentage : "+level+"%");

            switch(deviceStatus)
            {
              case BatteryManager.BATTERY_STATUS_CHARGING:
                BATT.setText("Battery Status : CHARGING ");
                break;

              case BatteryManager.BATTERY_STATUS_DISCHARGING:
                BATT.setText("Battery Status : DISCHARGING ");
                break;

              case BatteryManager.BATTERY_STATUS_FULL:
                BATT.setText("Battery Status : FULL ");
                break;
           }
        }
    };



    private BroadcastReceiver WifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            int WifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);

            switch (WifiState)
            {
                  case WifiManager.WIFI_STATE_ENABLED:
                    WFR.setText("WiFi Status : ACTIVATED ");
                    WFR2.setText("Connected to : CONNECTING...");

                      new Handler().postDelayed(new Runnable()
                      {
                          @Override
                          public void run()
                          {

                              WifiInfo wifiInfo = I.getConnectionInfo();

                              if (wifiInfo.getBSSID() != null)
                              {
                                  String name = wifiInfo.getSSID();
                                  WFR2.setText("Connected to : "+ name);
                              }
                          }
                      }, 8000 );

                      break;

                  case WifiManager.WIFI_STATE_DISABLED:
                    WFR.setText("WiFi Status : DEACTIVATED");
                    WFR2.setText("Connected to : WiFi is DEACTIVATED");
                    break;
            }

        } };


    @Override
    @Deprecated
    public void onClick(View v)
    {
        if(v==BB)
        {
            if (!bluetoothAdapter.isEnabled())
            {
                bluetoothAdapter.enable();
            }

            else if (bluetoothAdapter.isEnabled())
            {
                bluetoothAdapter.disable();
            }
        }
        else if(v==WFB)
        {
            if(I.isWifiEnabled())
            {
                I.setWifiEnabled(false);
            }
            else if(!I.isWifiEnabled())
            {
                I.setWifiEnabled(true);
            }
        }

    }
}
