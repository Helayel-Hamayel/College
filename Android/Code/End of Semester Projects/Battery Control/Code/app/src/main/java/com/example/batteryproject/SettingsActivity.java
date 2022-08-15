package com.example.batteryproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingsActivity
        extends AppCompatActivity
        implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    private AlertDialog.Builder dw1,dw2;
    private ImageButton imgButt1,imgButt2;
    private Intent SETSERV;
    private TextView perc;
    private SeekBar P;

    private ToggleButton toggButt;

    private int percValue,act;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.mipmap.ic_back));

        percValue=85;//default

        perc=findViewById(R.id.tvSB);

        imgButt1=findViewById(R.id.dwSB);
        imgButt2=findViewById(R.id.dwN);

        toggButt=findViewById(R.id.NTB);

        P=findViewById(R.id.SB);

        imgButt1.setOnClickListener(this);
        imgButt2.setOnClickListener(this);

        toggButt.setOnCheckedChangeListener(this);

        P.setOnSeekBarChangeListener(this);

        dw1 = new AlertDialog.Builder(this);
        dw1.setTitle("Information")
           .setIcon(R.mipmap.ic_q3)
           .setMessage("Use the bar to set the battery percentage you want to stop the charger as given above")
           .setPositiveButton("ACKNOWLEDGED", null);

        dw2 = new AlertDialog.Builder(this);
        dw2.setTitle("Information")
                .setIcon(R.mipmap.ic_q3)
                .setMessage("is to allow the smart-phone to remind you by setting off the alarm when it reaches the Limit you specified above, it will keep reminding you til you unplug the smart-phone")
                .setPositiveButton("ACKNOWLEDGED", null);

        LoadPreferences();

    }

    public void SavePreferences()
    {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("toggle", toggButt.isChecked());
        editor.putInt("ProgressInt", P.getProgress());
        editor.putString("ProgressStr", perc.getText().toString());
        editor.commit();
    }
    public void LoadPreferences()
    {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

        boolean T = sharedPreferences.getBoolean("toggle", false);
        boolean RO = sharedPreferences.getBoolean("RadioOne", false);
        boolean RT = sharedPreferences.getBoolean("RadioTwo", false);

        int PI = sharedPreferences.getInt("ProgressInt", 0);
        String PS = sharedPreferences.getString("ProgressStr","85%");

        toggButt.setChecked(T);

        P.setProgress(PI);
        perc.setText(PS);
    }

    @Override
    public void onBackPressed()
    {
        Intent Ret=new Intent();
        Ret.putExtra("Settings_Percentage",perc.getText().toString());
        Ret.putExtra("Settings_Alarm",toggButt.isChecked());
        setResult(Activity.RESULT_OK,Ret);
        SavePreferences();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return super.onSupportNavigateUp ();
    }

    @Override
    protected void onPause()
    {
        SavePreferences();
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        //Toast.makeText(getApplicationContext(),"DESTORY", Toast.LENGTH_LONG).show();
        SavePreferences();

        Intent Ret=new Intent();
        Ret.putExtra("Settings_Percentage",perc.getText().toString());
        Ret.putExtra("Settings_Alarm",toggButt.isChecked());
        setResult(Activity.RESULT_OK,Ret);

        finish();
        super.onDestroy();
    }

    @Override
    public void onClick(View v)
    {
      if(imgButt1==v)
      {
          dw1.show();
      }

      else if(imgButt2==v)
      {
          dw2.show();
      }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(progress)
        {
            case (0):
                perc.setText("85%");
                percValue=85;
                break;
            case (1):
                perc.setText("90%");
                percValue=90;
                break;
            case (2):
                perc.setText("95%");
                percValue=95;
                break;
            case (3):
                perc.setText("100%");
                percValue=100;
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if(isChecked){
            act=1;
            //Toast.makeText(getApplicationContext(),"act = 1", Toast.LENGTH_LONG).show();
        }
        else
        {
            act=0;
           //Toast.makeText(getApplicationContext(),"act = 0", Toast.LENGTH_LONG).show();
        }



    }






}