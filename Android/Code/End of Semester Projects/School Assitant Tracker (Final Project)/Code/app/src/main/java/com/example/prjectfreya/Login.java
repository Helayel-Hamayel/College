package com.example.prjectfreya;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import android.util.Log;
import android.view.*;
import android.widget.*;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import static com.example.prjectfreya.MainActivity.Channel_ID;
import static com.example.prjectfreya.SplashScreen.DB_Message;
import static com.example.prjectfreya.SplashScreen.PERMISSIONS;
import static com.example.prjectfreya.SplashScreen.ReqCode;


public class Login extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference MainBase = database.getReference();

    private static final String TAG ="Login";

    private Button L;

    private Intent K,Recv_message;
    private EditText student_id,student_pswrd;

    private TextView M;
    private ImageButton X;

    private String in1,in2;
    private String MSS_ERR="none";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String ST_ID = "ID";
    public static final String ST_PSWRD = "PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //add condition of permission is refuesd
        ActivityCompat.requestPermissions(Login.this,PERMISSIONS, ReqCode);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Recv_message=getIntent();
        MSS_ERR = Recv_message.getStringExtra(DB_Message);

        M = findViewById(R.id.ERR_REPORT);
        X = findViewById(R.id.Rmv_mess);
        X.setOnClickListener(this);

        M.setText(MSS_ERR);

        X.setVisibility(View.GONE);
        M.setVisibility(View.GONE);


        if(MSS_ERR!=null) {
            if (!MSS_ERR.matches("")) {
                X.setVisibility(View.VISIBLE);
                M.setVisibility(View.VISIBLE);
            }
        }
        student_id =  findViewById(R.id.input_id);
        student_pswrd = findViewById(R.id.input_pswrd);

        loadLoginData();

        L=findViewById(R.id.Login_button);

        L.setOnClickListener(this);
        L.setEnabled(false);
    }


    @Override
    public void onClick(View v)
    {
        if(v==L)
        {
            if(!student_id.getText().toString().trim().isEmpty() && !student_pswrd.getText().toString().trim().isEmpty() )
            {
                in1=student_id.getText().toString().trim();
                in2=student_pswrd.getText().toString().trim();

                /*
                 * call the database to check if the value in1 and in2 exists, if so proceed to main,
                 * with values you got from database to set up values, such as geo area location
                 * also in success login, it saves the data and send it to splashscreen.
                 */
                Authinticate(in1,in2);

            }
            else {
                if(student_id.getText().toString().trim().length() == 0)
                {
                    student_id.setError("Field cannot be left blank.");
                }
                if(student_pswrd.getText().toString().trim().length() == 0)
                {
                    student_pswrd.setError("Field cannot be left blank.");
                }
            }
        }
        else if(v==X){
           M.setVisibility(View.GONE);
           X.setVisibility(View.GONE);
        }
    }

    private void Authinticate(final String in1,final String in2) {


        MainBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            if(dataSnapshot.child("Users").child(in1).exists()) {
                if (dataSnapshot.child("Users").child(in1).child("Password").exists()
                        && dataSnapshot.child("Users").child(in1).child("Home_cor").child("Latitude").exists() && dataSnapshot.child("Users").child(in1).child("Home_cor").child("Longitude").exists()) {

                    if (dataSnapshot.child("Users").child(in1).child("Password").getValue().toString().equals(in2)) {

                        //Home_cor
                        //Toast.makeText(getBaseContext(),"Home_cor : " + (double)dataSnapshot.child(in1).child("Home_cor").child("Latitude").getValue() +" , " + (double)dataSnapshot.child(in1).child("Home_cor").child("Longitude").getValue() ,Toast.LENGTH_SHORT).show();

                        //ONLY CALL THIS FUNCTION IF LOGIN TRANSACTION IS SUCCESS
                        saveLoginData(in1, in2);

                        K = new Intent(getBaseContext(), MainActivity.class);
                        //K.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                        K.putExtra("Home_cor_Latitude", (double) dataSnapshot.child("Users").child(in1).child("Home_cor").child("Latitude").getValue());
                        K.putExtra("Home_cor_Longitude", (double) dataSnapshot.child("Users").child(in1).child("Home_cor").child("Longitude").getValue());
                        K.putExtra("ST_ID",in1);

                        startActivity(K);
                        Animatoo.animateSwipeLeft(Login.this);
                        finish();
                    } else {
                        idOrPasswordIsIncorrect();
                    }
                } else {
                    idOrPasswordIsIncorrect();
                }
            }
            else if(dataSnapshot.child("Drivers").child(in1).exists()){

                if (dataSnapshot.child("Drivers").child(in1).child("Password").getValue().toString().equals(in2)) {

                    saveLoginData(in1, in2);

                    K = new Intent(getBaseContext(), MA_Driver.class);
                    //K.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    Log.e(TAG,":D");
                    startActivity(K);
                    Animatoo.animateSwipeLeft(Login.this);
                    finish();
                }
            }
            else{
                idOrPasswordIsIncorrect();
            }

            }
            private void idOrPasswordIsIncorrect() {
                M.setVisibility(View.VISIBLE);
                M.setText("Either the id or the password is incorrect");
                X.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

    }

    //SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_SETTINGS, MODE_PRIVATE);
    private void loadLoginData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        student_id.setText(sharedPreferences.getString(ST_ID,"").toString());
    }


    private void saveLoginData(String v1,String v2) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(ST_ID, v1);
        editor.putString(ST_PSWRD, v2);

        editor.apply();

        //Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ReqCode)
        {

                if(ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0])== PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[1])==PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[2])==PackageManager.PERMISSION_GRANTED)
            {
                //Toast.makeText(this, "Thanks for enabling the permission", Toast.LENGTH_SHORT).show();

                L.setEnabled(true);

            } else {

                Toast.makeText(this, "Please allow the Permission", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(this)
                        .setTitle("Permission Needed")
                        .setMessage("Permission is needed to access necessary features from your device to optimaly run the application...")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(Login.this,PERMISSIONS, ReqCode);
                            }
                        })
                        .create().show();

            }
        }

    }


}
