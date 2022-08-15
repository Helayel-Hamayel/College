package com.example.prjectfreya;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.prjectfreya.Login.SHARED_PREFS;
import static com.example.prjectfreya.Login.ST_ID;
import static com.example.prjectfreya.Login.ST_PSWRD;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT=3200;
    public final static String DB_Message="Error_in_Database_transaction";

    final Handler H = new Handler();

    private TextView loki;

    private Intent K, I;
    private String l1,l2;
    private String Message_Error;
    public final static int ReqCode = 4;
    private Boolean greenLight = false;
    private int noOfTries = 5;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference MainBase = database.getReference();

    private static final String TAG ="SplashScreen";

    public final static String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.VIBRATE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        loki=findViewById(R.id.dataDB);

        ActivityCompat.requestPermissions(SplashScreen.this,PERMISSIONS, ReqCode);

    }


    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        l1 = sharedPreferences.getString(ST_ID, "");
        l2 = sharedPreferences.getString(ST_PSWRD, "");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ReqCode)
        {

            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[1]) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[2]) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[3]) == PackageManager.PERMISSION_GRANTED)
            {
                //Toast.makeText(this, "Thanks for enabling the permission", Toast.LENGTH_SHORT).show();
                //if in1 and in2 are not empty, take me to main activity
                //else take me to login screen,
                // else if error occured during checking the data of two to main, take me to login screen with note of error.

                loadData();

                //data is empty
                if( l1.isEmpty() && l2.isEmpty() )
                {/*take me to login activity*/
                    //Toast.makeText(getBaseContext(),"data is empty",Toast.LENGTH_SHORT).show();
                    H.postDelayed(new Runnable() {
                        @Override
                        public void run()
                        {
                            I =new Intent(SplashScreen.this, Login.class);
                            startActivity(I);
                            Animatoo.animateFade(SplashScreen.this);
                            finish(); //the current activity will get finished.
                        }
                    }, SPLASH_SCREEN_TIME_OUT);

                }
                else {

                    //checks the connection to database either by Mobile data or Wi-Fi//
                    checkConnection();
                    }
            } else {

                Toast.makeText(this, "Please allow the Permission", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(this)
                        .setTitle("Permission Needed")
                        .setMessage("Permission is needed to access necessary features from your device to optimaly run the application...")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(SplashScreen.this,PERMISSIONS, ReqCode);
                            }
                        })
                        .create().show();

            }
        }

    }

    private void checkConnection() {

        //Toast.makeText(getBaseContext(),"data is NOT empty",Toast.LENGTH_SHORT).show();

        //to prevent screen freeze if theres no internet connection of any sort
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        //Toast.makeText(getBaseContext(),"Connection : "+isConnected,Toast.LENGTH_LONG).show();
        //===

        //do DB transaction check here
        if(isConnected){
            //Toast.makeText(getBaseContext(),"hello 1",Toast.LENGTH_LONG).show();
            Authinticate(l1,l2);
            greenLight = true;
        } else if(noOfTries == 0){
            Toast.makeText(getBaseContext(),"You ran out tries, taking you to Login screen ",Toast.LENGTH_SHORT).show();
            I =new Intent(SplashScreen.this, Login.class);
            startActivity(I);
            Animatoo.animateFade(SplashScreen.this);
            finish();
        }
        else {
            new AlertDialog.Builder(SplashScreen.this)
                    .setTitle("No internet connection :")
                    .setMessage("Please enable internet connection first - either Wi-Fi or Mobile data - then press retry connection")
                    .setPositiveButton("Retry Connection", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt)
                        {
                            checkConnection();

                            if(noOfTries!=0 && !greenLight) {
                                Toast.makeText(getBaseContext(), "number of tries left : " + noOfTries, Toast.LENGTH_SHORT).show();
                                noOfTries--;
                            }
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    private void Authinticate(final String in1,final String in2) {

        loki.setText("Retriving the data from Database...");

        //This timeout failsafe, if it took TOO LONG to retrive data from database it takes you to login screen
        H.postDelayed(retriveDataTimer, 7500);

        MainBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /*if DB transaction is success then take me to Main activity with data from DB*/
                if (dataSnapshot.child("Users").child(in1).exists()) {
                 if (dataSnapshot.child("Users").child(in1).child("Password").exists()
                        && dataSnapshot.child("Users").child(in1).child("Home_cor").child("Latitude").exists() && dataSnapshot.child("Users").child(in1).child("Home_cor").child("Longitude").exists()) {
                    if (dataSnapshot.child("Users").child(in1).child("Password").getValue().toString().equals(in2)) {

                        loki.setText("Success : now retriving the data...");

                        //Home_cor
                        //Toast.makeText(getBaseContext(),"Home_cor : " + (double)dataSnapshot.child(in1).child("Home_cor").child("Latitude").getValue() +" , " + (double)dataSnapshot.child(in1).child("Home_cor").child("Longitude").getValue() ,Toast.LENGTH_SHORT).show();

                        K = new Intent(getBaseContext(), MainActivity.class);
                        //K.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);//FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS

                        K.putExtra("Home_cor_Latitude", (double) dataSnapshot.child("Users").child(in1).child("Home_cor").child("Latitude").getValue());
                        K.putExtra("Home_cor_Longitude", (double) dataSnapshot.child("Users").child(in1).child("Home_cor").child("Longitude").getValue());
                        K.putExtra("ST_ID",in1);

                        //Toast.makeText(getBaseContext(),"DB transaction is success ",Toast.LENGTH_SHORT).show();
                        H.removeCallbacks(retriveDataTimer);

                        startActivity(K);
                        Animatoo.animateSwipeLeft(SplashScreen.this);
                        finish();
                    } else {
                        IncorrectDataORdatabaseFailure();
                    }
                 } else {
                    IncorrectDataORdatabaseFailure();
                }
            }
                else if(dataSnapshot.child("Drivers").child(in1).exists()){

                    if (dataSnapshot.child("Drivers").child(in1).child("Password").getValue().toString().equals(in2)) {

                        K = new Intent(getBaseContext(), MA_Driver.class);
                        //K.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                        H.removeCallbacks(retriveDataTimer);

                        Log.e(TAG,"hello johny");
                        startActivity(K);
                        Animatoo.animateSwipeLeft(SplashScreen.this);
                        finish();
                    }

                }
            }

            private void IncorrectDataORdatabaseFailure() {
                    /*take me to login activity, with login message error, to tell if such login has changed password. or error in DB*/
                    //get the error message and store it to Message_Error
                    Message_Error="Either ID or the password has changed OR it failed to connect to database";
                    //Toast.makeText(getBaseContext(),"DB transaction is failure ",Toast.LENGTH_SHORT).show();
                    H.removeCallbacks(retriveDataTimer);
                    I = new Intent(SplashScreen.this, Login.class);
                    I.putExtra(DB_Message,Message_Error);
                    startActivity(I);
                    Animatoo.animateFade(SplashScreen.this);
                    finish(); //the current activity will get finished.
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getBaseContext(),"cancel",Toast.LENGTH_SHORT).show();
            }

        });


    }

    private Runnable retriveDataTimer = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getBaseContext(),"Slow internet connection timeout : taking you to Login screen",Toast.LENGTH_LONG).show();

            new AlertDialog.Builder(SplashScreen.this)
                    .setTitle("Slow internet connection timeout :")
                    .setMessage("return later when you have strong internet connection OR wait til you get stronger connection")
                    .setPositiveButton("Ok", null)
                    .create().show();

            loki.setText("Waiting for proper connection...");
        }
    };


}
