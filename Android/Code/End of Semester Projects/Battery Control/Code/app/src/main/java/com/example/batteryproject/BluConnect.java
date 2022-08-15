package com.example.batteryproject;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.ParcelUuid;
import android.support.design.widget.Snackbar;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static java.lang.System.out;

public class BluConnect
        extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemClickListener ,View.OnClickListener {

    private AlertDialog.Builder dw;
    private BluetoothAdapter BA;

    private ArrayList<BluetoothDevice> ArrayAdapter = new ArrayList<>(); // addresses
    private ArrayList<String> AL = new ArrayList<>(); //names
    private ArrayAdapter<String> BDF;

    private Object[] devices;
    private BluetoothDevice device;
    private ToggleButton act;
    private Button T;
    private ImageButton unpair,infobutt;
    private TextView st,Constts;
    private ListView lvdev;
    private Set<BluetoothDevice> bondedDevices;

    private static final int BT_REQUEST_CODE = 37;

    private OutputStream outputStream;
    private IntentFilter DD;
    private int FPER;
    private Intent E;
    private TextView Test;
    private BluetoothSocket socket;
    private String X;

    private int PASSCONN=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blu_connect);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.mipmap.ic_back));

        //Toggle Button
        act = findViewById(R.id.tbBlu);
        //TextView
        st = findViewById(R.id.tvStBlu);
        //ListView for discoveries
        lvdev = findViewById(R.id.lvBlu);
        //button for testing the newly connected device
        T=findViewById(R.id.testbutt);
        unpair=findViewById(R.id.unpbutt);
        Test=findViewById(R.id.testnum);
        //text view for showing which device i connected to
        Constts=findViewById(R.id.cdstts);

        infobutt=findViewById(R.id.ibutt);

        T.setVisibility(View.GONE);
        T.setEnabled(false);

        unpair.setVisibility(View.GONE);
        unpair.setEnabled(false);

        Test.setVisibility(View.GONE);

        FPER = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        BA = BluetoothAdapter.getDefaultAdapter(); //bluetooth

        if (BA.isEnabled() && !act.isChecked())
        {
            act.setChecked(true);
            T.setEnabled(true);
            unpair.setEnabled(true);

            T.setVisibility(View.VISIBLE);
            Test.setVisibility(View.VISIBLE);
            unpair.setVisibility(View.VISIBLE);

            Constts.setText("Fetching the name...");

            E = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(E, BT_REQUEST_CODE);

            BA.startDiscovery();
            BDF=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,AL);

            lvdev.setAdapter(BDF);
            BDF.notifyDataSetChanged();
        }

        DD = new IntentFilter();
        DD.addAction(BluetoothDevice.ACTION_FOUND); // found a device to connect
        DD.addAction(BluetoothAdapter.ACTION_STATE_CHANGED); // state of Bluetooth
        DD.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED); // Pairing with devices
        DD.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        DD.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(F, DD);//to Find the devices

        dw = new AlertDialog.Builder(this);
        dw.setTitle("Information")
                .setIcon(R.mipmap.ic_q3)
                .setMessage("Test the device by inserting the level percentage\n         (OR)\nUnpair the Device")
                .setPositiveButton("ACKNOWLEDGED", null);


        lvdev.setOnItemClickListener(this);
        T.setOnClickListener(this);
        act.setOnCheckedChangeListener(this);
        unpair.setOnClickListener(this);
        infobutt.setOnClickListener(this);

        BDF=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,AL);
        lvdev.setAdapter(BDF);
    }

    private void unpairDevice(BluetoothDevice device)
    {
        try
        {
            Method m = device.getClass().getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
            PASSCONN=0;
        } catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    private final BroadcastReceiver F = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {

            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (action.equals(BluetoothDevice.ACTION_FOUND))
            {
                ArrayAdapter.add(device);

               //Toast.makeText(getApplicationContext(),"FOUND THE DEVICE",Toast.LENGTH_LONG).show();

                if(device.getName()==null) // have no name, but with Address
                {
                    AL.add(device.getAddress());
                }

                else if (device.getName()!=null)// have a Name, then add it to ListView
                {
                    AL.add(device.getName());
                }

                //to remove the Duplication Devices
                HashSet<String> set = new HashSet<String>();
                set.addAll(AL);
                AL.clear();
                AL.addAll(set);
                BDF.notifyDataSetChanged();
            }

            else if (action.equals(BA.ACTION_STATE_CHANGED))
            {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BA.ERROR);

                switch (state)
                {
                    case (BluetoothAdapter.STATE_ON):
                        act.setChecked(true);
                        st.setText("Bluetooth is Activated");

                        BA.startDiscovery();
                        //Toast.makeText(getApplicationContext(),"STATE_ON",Toast.LENGTH_LONG).show();
                        break;

                    case (BluetoothAdapter.STATE_OFF):
                        act.setChecked(false);
                        st.setText("Bluetooth is Deactivated");
                        BA.cancelDiscovery();
                        //Toast.makeText(getApplicationContext(),"state off",Toast.LENGTH_LONG).show();
                        break;
                }
            }

            else if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED))
            {
                //Bond is success
                if (device.getBondState() == BluetoothDevice.BOND_BONDED)
                {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Bond is success : "+device.getName(), Snackbar.LENGTH_LONG).show();
                    Constts.setText(device.getName());
                    T.setVisibility(View.VISIBLE);
                    T.setEnabled(true);
                    unpair.setVisibility(View.VISIBLE);
                    unpair.setEnabled(true);
                    Test.setVisibility(View.VISIBLE);
                }
                //Bonding in process
                if (device.getBondState() == BluetoothDevice.BOND_BONDING)
                {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Bonding...", Snackbar.LENGTH_INDEFINITE).show();
                }
                //Bonding is Disconnected
                if (device.getBondState() == BluetoothDevice.BOND_NONE)
                {
                    Snackbar.make(getWindow().getDecorView().getRootView(),"Bond is Disconnected",Snackbar.LENGTH_LONG).show();
                }
            }
        }
    };

    @Override
    public void onBackPressed()
    {
        Intent Ret2=new Intent();
        Ret2.putExtra("PASSCARD",PASSCONN);
        setResult(Activity.RESULT_OK,Ret2);

        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return super.onSupportNavigateUp ();
    }

    @Override
    protected void onDestroy()
    {
        unregisterReceiver(F);
        super.onDestroy();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if(buttonView==act)
        {
            if (BA == null)
            {
                st.setText("This Device does not support bluetooth");
                act.setChecked(false);
            }

            else if (!BA.isEnabled() && act.isChecked())
            {
                E = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(E, BT_REQUEST_CODE);

                T.setEnabled(true);
                T.setVisibility(View.VISIBLE);
                Test.setVisibility(View.VISIBLE);
                unpair.setVisibility(View.VISIBLE);
                unpair.setEnabled(true);

                BDF=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,AL);
                lvdev.setAdapter(BDF);
                BDF.notifyDataSetChanged();
            }

            else if (BA.isEnabled() && !act.isChecked())
            {
                BA.disable();
                lvdev.setAdapter(null);
                BA.cancelDiscovery();
                BDF.notifyDataSetChanged();
                ArrayAdapter.clear();
                unpair.setVisibility(View.GONE);
                unpair.setEnabled(false);
                AL.clear();
                Constts.setText("Bluetooth is Deactivated");
                st.setText("Bluetooth is Deactivated");
                T.setVisibility(View.GONE);
                T.setEnabled(false);
                Test.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == BT_REQUEST_CODE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                st.setText("Bluetooth is Activated");
            }

            else if(resultCode == RESULT_CANCELED)
            {
                act.setChecked(false);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        BA.cancelDiscovery();

        String deviceName = AL.get(position);

       // Toast.makeText(getApplicationContext(),"Test"+ArrayAdapter.get(1).getName()+" "+deviceName.matches(ArrayAdapter.get(1).getName()),Toast.LENGTH_LONG).show();

        //Create the bond.
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2)
        {
                for (int i = 0; i < ArrayAdapter.size(); i++)
                {
                    if (ArrayAdapter.get(i).getName() != null)
                    {
                        if (deviceName.matches(ArrayAdapter.get(i).getName()))
                        {
                            ArrayAdapter.get(i).createBond();

                            /*
                            X="90";
                            T.callOnClick();
                            X="70";
                            T.callOnClick();
                            */

                            break;
                        }
                    } else if (ArrayAdapter.get(i).getAddress() != null)
                    {
                        if (deviceName.matches(ArrayAdapter.get(i).getAddress()))
                        {
                            ArrayAdapter.get(i).createBond();

                            /*
                            X="90";
                            T.callOnClick();
                            X="70";
                            T.callOnClick();
                            */
                            break;
                        }
                    }
                }
        }
    }

    @Override
    public void onClick(View v)
    {
        if(v==T)
        {
            try
            {
                Test.setError(null);
                X="90";
                if(!Test.getText().toString().equals(""))
                {
                    X = Test.getText().toString();
                    X=""+X+"";
                }

                bondedDevices = BA.getBondedDevices();

                devices = (Object []) bondedDevices.toArray();
                device = (BluetoothDevice) devices[0];
                ParcelUuid[] uuids = device.getUuids();

                socket =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);

                //Toast.makeText(getApplicationContext(),"Before Socket",Toast.LENGTH_LONG).show();
                socket.connect();


                outputStream = socket.getOutputStream();

                if(Integer.valueOf(X)>=90){X="90";}
                else if(Integer.valueOf(X)<90){X="75";}

                outputStream.write(X.getBytes());

                //Toast.makeText(getApplicationContext(),"C O N N E C T E D",Toast.LENGTH_LONG).show();

                PASSCONN=1;

                socket.close();
            }
            catch (Exception R)
            {
                Test.setError("Please Connect the device to test");
                PASSCONN=0;
                //Toast.makeText(getApplicationContext(),R.toString(), Toast.LENGTH_LONG).show();
            }
        }
        else if(unpair==v)
        {
            if(device!=null)
            {
                unpairDevice(device);
                Snackbar.make(getWindow().getDecorView().getRootView(), "Unpairing the device : " + device.getName() + "...", Snackbar.LENGTH_LONG).show();
            }
            else
                {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "There is no device to unpair ", Snackbar.LENGTH_LONG).show();
                }
            }
        else if(infobutt==v)
        {
            dw.show();
        }
    }
}
