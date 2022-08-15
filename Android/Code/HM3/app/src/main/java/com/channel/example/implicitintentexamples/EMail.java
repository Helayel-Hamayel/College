package com.channel.example.implicitintentexamples;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class EMail
        extends AppCompatActivity
        implements View.OnClickListener {

    private Button SendMail,add,rem;
    private EditText ET1,ET2,ET3,ET4,ET5;
    private CheckBox ET1to,ET1cc,ET1bcc;
    private CheckBox ET2to,ET2cc,ET2bcc;
    private CheckBox ET3to,ET3cc,ET3bcc;
    private CheckBox ET4to,ET4cc,ET4bcc;
    private CheckBox ET5to,ET5cc,ET5bcc;
    private int num=1,pass=0;
    private String S,cc,bcc;
    private EditText Subject,Message;
    private int ccCount=0,bccCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_back));

        SendMail=findViewById(R.id.sendmail);
        add=findViewById(R.id.addbutt);
        rem=findViewById(R.id.rembutt);

        ET1=findViewById(R.id.et1);
        ET2=findViewById(R.id.et2);
        ET3=findViewById(R.id.et3);
        ET4=findViewById(R.id.et4);
        ET5=findViewById(R.id.et5);

        ET1to=findViewById(R.id.et1to);
        ET2to=findViewById(R.id.et2to);
        ET3to=findViewById(R.id.et3to);
        ET4to=findViewById(R.id.et4to);
        ET5to=findViewById(R.id.et5to);

        ET1to.setChecked(true);


        ET1cc=findViewById(R.id.et1cc);
        ET2cc=findViewById(R.id.et2cc);
        ET3cc=findViewById(R.id.et3cc);
        ET4cc=findViewById(R.id.et4cc);
        ET5cc=findViewById(R.id.et5cc);

        ET1bcc=findViewById(R.id.et1bcc);
        ET2bcc=findViewById(R.id.et2bcc);
        ET3bcc=findViewById(R.id.et3bcc);
        ET4bcc=findViewById(R.id.et4bcc);
        ET5bcc=findViewById(R.id.et5bcc);

        ET2.setVisibility(View.GONE);
        ET2to.setVisibility(View.GONE);
        ET2cc.setVisibility(View.GONE);
        ET2bcc.setVisibility(View.GONE);

        ET3.setVisibility(View.GONE);
        ET3to.setVisibility(View.GONE);
        ET3cc.setVisibility(View.GONE);
        ET3bcc.setVisibility(View.GONE);

        ET4.setVisibility(View.GONE);
        ET4to.setVisibility(View.GONE);
        ET4cc.setVisibility(View.GONE);
        ET4bcc.setVisibility(View.GONE);

        ET5.setVisibility(View.GONE);
        ET5to.setVisibility(View.GONE);
        ET5cc.setVisibility(View.GONE);
        ET5bcc.setVisibility(View.GONE);

        Subject=findViewById(R.id.sub);
        Message=findViewById(R.id.mesbody);

        SendMail.setOnClickListener(this);
        add.setOnClickListener(this);
        rem.setOnClickListener(this);

    }

    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {

        if(v==add)
        {
            if(num==1)
            {
                ET2.setVisibility(View.VISIBLE);
                ET2to.setVisibility(View.VISIBLE);
                ET2cc.setVisibility(View.VISIBLE);
                ET2bcc.setVisibility(View.VISIBLE);
                num++;
            }

            else if(num==2)
            {
                ET3.setVisibility(View.VISIBLE);
                ET3to.setVisibility(View.VISIBLE);
                ET3cc.setVisibility(View.VISIBLE);
                ET3bcc.setVisibility(View.VISIBLE);
                num++;
            }
            else if(num==3)
            {
                ET4.setVisibility(View.VISIBLE);
                ET4to.setVisibility(View.VISIBLE);
                ET4cc.setVisibility(View.VISIBLE);
                ET4bcc.setVisibility(View.VISIBLE);
                num++;
            }
            else if(num==4)
            {
                ET5.setVisibility(View.VISIBLE);
                ET5to.setVisibility(View.VISIBLE);
                ET5cc.setVisibility(View.VISIBLE);
                ET5bcc.setVisibility(View.VISIBLE);
                num++;
            }

        }
        else if(v==rem)
        {

            if(num==5)
            {
                ET5.setVisibility(View.GONE);
                ET5to.setVisibility(View.GONE);
                ET5cc.setVisibility(View.GONE);
                ET5bcc.setVisibility(View.GONE);
                ET5.setText("");
                ET5to.setChecked(false);
                ET5cc.setChecked(false);
                ET5bcc.setChecked(false);
                ET5.setError(null);
                num--;
            }

            else if(num==4)
            {
                ET4.setVisibility(View.GONE);
                ET4to.setVisibility(View.GONE);
                ET4cc.setVisibility(View.GONE);
                ET4bcc.setVisibility(View.GONE);
                ET4.setText("");
                ET4to.setChecked(false);
                ET4cc.setChecked(false);
                ET4bcc.setChecked(false);
                ET4.setError(null);
                num--;
            }
            else if(num==3)
            {
                ET3.setVisibility(View.GONE);
                ET3to.setVisibility(View.GONE);
                ET3cc.setVisibility(View.GONE);
                ET3bcc.setVisibility(View.GONE);
                ET3.setText("");
                ET3to.setChecked(false);
                ET3cc.setChecked(false);
                ET3bcc.setChecked(false);
                ET3.setError(null);
                num--;
            }
            else if(num==2)
            {
                ET2.setVisibility(View.GONE);
                ET2to.setVisibility(View.GONE);
                ET2cc.setVisibility(View.GONE);
                ET2bcc.setVisibility(View.GONE);
                ET2.setText("");
                ET2to.setChecked(false);
                ET2cc.setChecked(false);
                ET2bcc.setChecked(false);
                ET2.setError(null);
                num--;
            }
        }

        if(v==SendMail)
        {
            ccCount=0;
            bccCount=0;

            Intent h = new Intent(Intent.ACTION_SENDTO);


            if(!ET1to.isChecked()&&!ET2to.isChecked()&&!ET3to.isChecked()&&!ET4to.isChecked()&&!ET5to.isChecked())
            {
                ET1to.setChecked(true);
                Toast.makeText(getApplicationContext(),"as default, it set the first email to send to",Toast.LENGTH_LONG).show();
            }

            if(!ET1.getText().toString().matches("")&&ET1to.isChecked())
            {
                S=ET1.getText().toString().trim();
                pass=1;
            }
            if(!ET1.getText().toString().matches("")&&ET1cc.isChecked()) {
                cc=ET1.getText().toString();
                pass=1;
            }
            if(!ET1.getText().toString().matches("")&&ET1bcc.isChecked()) {
                bcc=ET1.getText().toString();
                pass=1;
            }
            if(ET1.getText().toString().matches("")&&ET1.getVisibility()==View.VISIBLE)
            {
                ET1.setError("PLEASE FILL THE GUY YOU WANT TO SENT TO.");
                pass=0;
            }

            if(!ET2to.isChecked()&&!ET2cc.isChecked()&&!ET2bcc.isChecked()&&ET2.getVisibility()==View.VISIBLE)
            {ET2to.setChecked(true);}
            if(!ET2.getText().toString().matches("")&&ET2to.isChecked())
            {
                S=S+";"+ET2.getText().toString().trim();
                pass=1;
            }

            if(!ET2.getText().toString().matches("")&&ET2cc.isChecked()) {
                cc=cc+","+ET2.getText().toString();
                pass=1;
            }
            if(!ET2.getText().toString().matches("")&&ET2bcc.isChecked()) {
                bcc=bcc+","+ET2.getText().toString();
                pass=1;
            }
            if(ET2.getText().toString().matches("")&&ET2.getVisibility()==View.VISIBLE)
            {
                ET2.setError("PLEASE FILL THE GUY YOU WANT TO SENT TO.");
                pass=0;
            }






            if(!ET3to.isChecked()&&!ET3cc.isChecked()&&!ET3bcc.isChecked()&&ET3.getVisibility()==View.VISIBLE)
            {ET3to.setChecked(true);}
            if(!ET3.getText().toString().matches("")&&ET3to.isChecked())
            {
                S=S+";"+ET3.getText().toString().trim();
                pass=1;
            }
            if(!ET3.getText().toString().matches("")&&ET3cc.isChecked()) {
                cc=cc+","+ET3.getText().toString();
                pass=1;
            }
            if(!ET3.getText().toString().matches("")&&ET3bcc.isChecked()) {
                bcc=bcc+","+ET3.getText().toString();
                pass=1;
            }
            if(ET3.getText().toString().matches("")&&ET3.getVisibility()==View.VISIBLE)
            {
                ET3.setError("PLEASE FILL THE GUY YOU WANT TO SENT TO.");
                pass=0;
            }



            if(!ET4to.isChecked()&&!ET4cc.isChecked()&&!ET4bcc.isChecked()&&ET4.getVisibility()==View.VISIBLE)
            {ET4to.setChecked(true);}
            if(!ET4.getText().toString().matches("")&&ET4to.isChecked())
            {
                S=S+";"+ET4.getText().toString().trim();
                pass=1;
            }
            if(!ET4.getText().toString().matches("")&&ET4cc.isChecked()) {
                cc=cc+","+ET4.getText().toString();
                pass=1;
            }
            if(!ET4.getText().toString().matches("")&&ET4bcc.isChecked()) {
                bcc=bcc+","+ET4.getText().toString();
                bccCount++;
                pass=1;
            }
            if(ET4.getText().toString().matches("")&&ET4.getVisibility()==View.VISIBLE)
            {
                ET4.setError("PLEASE FILL THE GUY YOU WANT TO SENT TO.");
                pass=0;
            }



            if(!ET5to.isChecked()&&!ET5cc.isChecked()&&!ET5bcc.isChecked()&&ET5.getVisibility()==View.VISIBLE)
            {ET5to.setChecked(true);}
            if(!ET5.getText().toString().matches("")&&ET5to.isChecked())
            {
                S=S+";"+ET5.getText().toString().trim();
                pass=1;
            }
            else if(!ET5.getText().toString().matches("")&&ET5cc.isChecked()) {
                cc=cc+","+ET5.getText().toString();
                ccCount++;
                pass=1;
            }
            if(!ET5.getText().toString().matches("")&&ET5bcc.isChecked()) {
                bcc=bcc+","+ET5.getText().toString();
                bccCount++;
                pass=1;
            }
            if(ET5.getText().toString().matches("")&&ET5.getVisibility()==View.VISIBLE)
            {
                ET5.setError("PLEASE FILL THE GUY YOU WANT TO SENT TO.");
                pass=0;
            }

            h.setData(Uri.parse("mailto:"+S));

            h.putExtra(Intent.EXTRA_CC, new String[]{cc});
            h.putExtra(Intent.EXTRA_BCC, new String[]{bcc});

            if(Subject.getText().toString().matches(""))
            {
                h.putExtra(Intent.EXTRA_SUBJECT, ("untitled subject" ));
            }
            else if(!Subject.getText().toString().matches(""))
            {
                h.putExtra(Intent.EXTRA_SUBJECT, (Subject.getText().toString()));
            }


            if(Message.getText().toString().matches(""))
            {
                h.putExtra(Intent.EXTRA_TEXT, ("default message: hello and have a nice day" ));
            }
            else if(!Message.getText().toString().matches(""))
            {
                h.putExtra(Intent.EXTRA_TEXT, (Message.getText().toString()));
            }

            if(pass==1)
            startActivity(h);
        }


    }
}
