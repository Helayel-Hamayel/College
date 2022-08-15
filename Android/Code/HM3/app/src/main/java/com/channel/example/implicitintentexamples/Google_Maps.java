package com.channel.example.implicitintentexamples;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;



public class Google_Maps
        extends AppCompatActivity
        implements View.OnFocusChangeListener ,View.OnClickListener {

    EditText LT,LongT,LatT;
    Button Search;
    Intent T;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google__maps);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_back));


        LT=findViewById(R.id.loctext);
     LongT=findViewById(R.id.longtext);
     LatT=findViewById(R.id.latitext);

     Search=findViewById(R.id.SRCH);

     LT.setOnFocusChangeListener(this);
     LongT.setOnFocusChangeListener(this);
     LatT.setOnFocusChangeListener(this);

     Search.setOnClickListener(this);

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if(v==LT && hasFocus==true)
        {
          LongT.setText("");
          LatT.setText("");
        }
        else if((v==LongT || v==LatT)&&hasFocus==true)
        {
         LT.setText("");
        }

    }

    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    public void onClick(View v) {

        if(v==Search)
        {
          if((!LongT.getText().toString().equals(""))&&(!LatT.getText().toString().equals("")))
          {
              T=new Intent(Intent.ACTION_VIEW);
              T.setData(Uri.parse("geo:"+LatT.getText().toString()+","+LongT.getText().toString()+"?z=10"));
              startActivity(T);
          } //?z=16
          else if(!LT.getText().toString().equals(""))
          {
              T=new Intent(Intent.ACTION_VIEW);
              T.setData(Uri.parse("geo:0,0//?q="+LT.getText().toString()+"?z=10"));
              startActivity(T);
          }


        }

    }
}
