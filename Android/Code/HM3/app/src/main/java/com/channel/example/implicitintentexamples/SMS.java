package com.channel.example.implicitintentexamples;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SMS
        extends AppCompatActivity
        implements View.OnClickListener {

    private Button Send,Add,Rem;
    private EditText E1,E2,E3,E4,E5,body;
    private int num=1,pass=0;
    private String S;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_back));


        E1=findViewById(R.id.et1);
        E2=findViewById(R.id.et2);
        E3=findViewById(R.id.et3);
        E4=findViewById(R.id.et4);
        E5=findViewById(R.id.et5);

        E2.setVisibility(View.GONE);
        E3.setVisibility(View.GONE);
        E4.setVisibility(View.GONE);
        E5.setVisibility(View.GONE);

        body=findViewById(R.id.BD);

        Add=findViewById(R.id.butt1);
        Rem=findViewById(R.id.butt2);
        Send=findViewById(R.id.send);

        Send.setOnClickListener(this);
        Add.setOnClickListener(this);
        Rem.setOnClickListener(this);
    }

    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {

        if(v==Add)
        {
          if(num==1)
          {
              E2.setVisibility(View.VISIBLE);
              num++;
          }

          else if(num==2)
          {
              E3.setVisibility(View.VISIBLE);
              num++;
          }
          else if(num==3)
          {
              E4.setVisibility(View.VISIBLE);
              num++;
          }
          else if(num==4)
          {
              E5.setVisibility(View.VISIBLE);
              num++;
          }

        }
        else if(v==Rem)
        {

            if(num==5)
            {
                E5.setVisibility(View.GONE);
                E5.setText("");
                E5.setError(null);
                num--;
            }

            else if(num==4)
            {
                E4.setVisibility(View.GONE);
                E4.setText("");
                E4.setError(null);
                num--;
            }
            else if(num==3)
            {
                E3.setVisibility(View.GONE);
                E3.setText("");
                E3.setError(null);
                num--;
            }
            else if(num==2)
            {
                E2.setVisibility(View.GONE);
                E2.setText("");
                E2.setError(null);
                num--;
            }



        }
        else if(v==Send)
        {

        Intent h = new Intent(Intent.ACTION_SENDTO);

        if(!E1.getText().toString().matches(""))
        {
          S=E1.getText().toString().trim();
          pass=1;
        }
        else {
          E1.setError("PLEASE FILL THE GUY YOU WANT TO SENT TO.");
            pass=0;
        }




        if(!E2.getText().toString().matches(""))
        {
          S=S+";"+E2.getText().toString().trim();
            pass=1;
        }
        else if( E2.getVisibility()==View.VISIBLE){
          E2.setError("PLEASE FILL THE GUY YOU WANT TO SENT TO.");
            pass=0;
        }




        if(!E3.getText().toString().matches(""))
        {
          S=S+";"+E3.getText().toString().trim();
            pass=1;
        }
        else if( E3.getVisibility()==View.VISIBLE){
          E3.setError("PLEASE FILL THE GUY YOU WANT TO SENT TO.");
            pass=0;
        }



        if(!E4.getText().toString().matches(""))
        {
          S=S+";"+E4.getText().toString().trim();
            pass=1;
        }
        else if( E4.getVisibility()==View.VISIBLE) {
          E4.setError("PLEASE FILL THE GUY YOU WANT TO SENT TO.");
            pass=0;
        }


        if(!E5.getText().toString().matches(""))
        {
          S=S+";"+E5.getText().toString().trim();
            pass=1;
        }
        else if( E5.getVisibility()==View.VISIBLE) {
          E5.setError("PLEASE FILL THE GUY YOU WANT TO SENT TO.");
            pass=0;
        }


        h.setData(Uri.parse("smsto:"+S));




        if(body.getText().toString().matches(""))
        {
            h.putExtra("sms_body", ("default message: hello and have a nice day" ));
        }
        else if(!body.getText().toString().matches(""))
        {
        h.putExtra("sms_body", (body.getText().toString()));
        }

        if(pass==1)
        startActivity(h);

        }

    }
}
