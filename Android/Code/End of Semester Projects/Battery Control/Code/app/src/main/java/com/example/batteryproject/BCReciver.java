package com.example.batteryproject;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.ParcelUuid;
import android.widget.Toast;

import java.io.OutputStream;
import java.lang.reflect.Method;

public final class BCReciver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {

        final Handler H = new Handler();
        int SPLASH_SCREEN_TIME_OUT=1000;
         java.util.Set<BluetoothDevice> bondedDevices;
         Object[] devices;
         BluetoothDevice device;
         BluetoothSocket socket;
         OutputStream outputStream;
         BluetoothAdapter BluetoothStats ;


        switch (intent.getAction())
        {
            case Tracker.SHUTDOWN_ACTION:

                BluetoothStats  = BluetoothAdapter.getDefaultAdapter();

                Intent i = new Intent(context, Tracker.class);
                context.stopService(i);
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


                Toast.makeText(context,"Shutting down...", Toast.LENGTH_LONG).show();
                H.postDelayed(new Runnable() {

                    @Override
                    public void run()
                    {
                      System.exit(0);
                    }
                }, SPLASH_SCREEN_TIME_OUT);



                break;
        }

    }
    private void unpairDevice(BluetoothDevice device)
    {
        try
        {
            Method m = device.getClass().getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        }
        catch (Exception e){}
    }

}
