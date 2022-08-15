package com.channel.example.implicitintentexamples;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity
        extends AppCompatActivity
        implements  View.OnClickListener {

    private Button sms,EMail,GMaps,Camera;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_exit));

        sms=findViewById(R.id.smsb);
        EMail=findViewById(R.id.emb);
        GMaps=findViewById(R.id.mapb);
        Camera=findViewById(R.id.camb);

        sms.setOnClickListener(this);
        EMail.setOnClickListener(this);
        GMaps.setOnClickListener(this);
        Camera.setOnClickListener(this);
    }


    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {

        Intent T;

        if(v==GMaps) {
            T=new Intent(this,Google_Maps.class);
            startActivity(T);
        }
        else if(v==Camera)
        {
            T=new Intent(this,Camera.class);
            startActivity(T);
        }
        else if(v==EMail)
        {
            T=new Intent(this,EMail.class);
            startActivity(T);
        }
        else if(v==sms)
        {
            T=new Intent(this,SMS.class);
            startActivity(T);
        }

    }
}

