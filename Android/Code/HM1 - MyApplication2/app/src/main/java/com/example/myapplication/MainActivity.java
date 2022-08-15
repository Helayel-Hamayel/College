package com.example.myapplication;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.*;
import android.widget.*;
import java.lang.*;

public class MainActivity extends AppCompatActivity implements OnClickListener , TextWatcher , CompoundButton.OnCheckedChangeListener {

    private Button cal;
    private ToggleButton AC;
    private EditText In;
    private TextView T1,T2,T3,T4,T5,T6;
    private int act=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        In=findViewById(R.id.Input);

        cal=findViewById(R.id.Calculate);

        AC=findViewById(R.id.TACB);

        T1=findViewById(R.id.PalindromOutput);
        T2=findViewById(R.id.PrimeOutput);
        T3=findViewById(R.id.TPrimeOutput);

        T4=findViewById(R.id.RevPrimeOutput);
        T5=findViewById(R.id.TPB2);
        T6=findViewById(R.id.TBA2);


        cal.setOnClickListener(this);

        In.addTextChangedListener(this);
        AC.setOnCheckedChangeListener(this);

        AC.setChecked(false);
    }


    @Override
    public void onClick(View v) {

        if(v==cal)
        {
            //getting the input and store it in the variable
            int val=0;
            int val2=0;
            StringBuffer rev= new StringBuffer (" ");

            //Checks if the input is Empty or not
            if(!In.getText().toString().isEmpty())
            {
                val = Integer.parseInt(In.getText().toString());
                rev = new StringBuffer (""+val+"");
                rev=rev.reverse();
                val2=Integer.parseInt(In.getText().toString());
            }
            else
                {
                    val = 0;
                }


            //checks either the variable is a Palindrome or not
            if(isPalindrome(val)){
                T1.setText(getString(R.string.Yes));
                T1.setBackgroundColor(Color.GREEN);

            }
            else{
                T1.setText(getString(R.string.No));
                T1.setBackgroundColor(Color.RED);
            }


            //checks either the variable is a Prime or not
            if(isPrime(val)){
                T2.setText(getString(R.string.Yes));
                T2.setBackgroundColor(Color.GREEN);

                //checks either the variable is a TWIN-Prime or not
                if(isPrime(val+2))
                {
                    T3.setText(getString(R.string.Yes));
                    T3.setBackgroundColor(Color.GREEN);
                    T6.setText(". . ."+(val+2)+". . .");
                }

                else{
                    T3.setText(getString(R.string.No));
                    T3.setBackgroundColor(Color.RED);
                    T6.setText(". . . X . . .");
                    }


                if(isPrime(val-2))
                {
                    T3.setText(getString(R.string.Yes));
                    T3.setBackgroundColor(Color.GREEN);
                    T5.setText(". . ."+(val-2)+". . .");
                }

                else{
                    T3.setText(getString(R.string.No));
                    T3.setBackgroundColor(Color.RED);
                    T5.setText(". . . X . . .");
                    }
            }
            else{
                T2.setText(getString(R.string.No));
                T2.setBackgroundColor(Color.RED);

                T3.setText(getString(R.string.No));
                T3.setBackgroundColor(Color.RED);

                T5.setText(". . . X . . .");
                T6.setText(". . . X . . .");
               }

            //checks if the value in REVERSE is Prime
            if(isPrime(val2))
            {
                T4.setText(getString(R.string.Yes));
                T4.setBackgroundColor(Color.GREEN);
            }
            else
            {
                T4.setText(getString(R.string.No));
                T4.setBackgroundColor(Color.RED);
            }

        }

    }

    private boolean isPalindrome(int val) {

        int R,sum=0,temp;
        temp=val;

        while(val>0)
        {
            R=val%10;
            sum=(sum*10)+R;
            val=val/10;
        }

        if(temp==sum)
            return true; // Val is Palindrome
        else
            return false; // Val is NOT Palindrome
    }

    private boolean isPrime(int val) {

        int x=0,flag=0;
        x=val/2;

        if(val==0 || val==1){ return false; }
        else
        {
            for( int i=2 ; i <= x ; i++ )
            {
                if(val%i==0) { return false; } // Val is NOT Prime
            }

            return true; // Val is Prime
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
      if(act == 0)
       cal.callOnClick();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(isChecked){
            cal.setEnabled(false);
            act=0;
        }
        else {
            cal.setEnabled(true);
            act=1;
        }

    }
}
