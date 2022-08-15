package com.example.converter;

import android.os.BatteryManager;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.*;
import android.widget.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.text.Editable;
import android.text.TextWatcher;
import java.lang.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener , TextWatcher
 {

    private EditText input;
    private BottomNavigationView BNVIT;
    private TextView Re1,Re2,Re3;

    private int ch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_exit));

        ch=0;
        input=findViewById(R.id.In);
        input.setKeyListener(DigitsKeyListener.getInstance("01"));

        BNVIT=findViewById(R.id.BNV);

        Re1=findViewById(R.id.RE1);
        Re2=findViewById(R.id.RE2);
        Re3=findViewById(R.id.RE3);

        input.addTextChangedListener(this);
        BNVIT.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {

        if(menuItem.getItemId()==R.id.Bi) {
            Toast.makeText(getApplicationContext(), "Binary Input", Toast.LENGTH_SHORT).show();
            ch = 0;
            input.setKeyListener(DigitsKeyListener.getInstance("01"));
            input.setText("");
        }

        else if(menuItem.getItemId()==R.id.Octo) {
            Toast.makeText(getApplicationContext(), "Octal Input", Toast.LENGTH_SHORT).show();
            ch = 1;
            input.setKeyListener(DigitsKeyListener.getInstance("01234567"));
            input.setText("");
        }
        else if(menuItem.getItemId()==R.id.Deca) {
            Toast.makeText(getApplicationContext(), "Decimal Input", Toast.LENGTH_SHORT).show();
            ch = 2;
            input.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
            input.setText("");
        }
        else if(menuItem.getItemId()==R.id.Hexa) {
            Toast.makeText(getApplicationContext(), "Hexa-decimal Input", Toast.LENGTH_SHORT).show();
            ch = 3;
            input.setKeyListener(DigitsKeyListener.getInstance("0123456789ABCDEF"));
            input.setText("");
        }

        return true;
    }
    
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        String value = input.getText().toString();
        if(value.isEmpty())
        {
            value="0";
        }

        if(ch==0)
        {
         Re1.setText("(8) " + Long.toOctalString(Long.parseLong(value, 2)));
         Re2.setText("(10) " + Long.parseLong(value, 2));
         Re3.setText("(16) " + Long.toHexString(Long.parseLong(value, 2)).toUpperCase());
        }
        else if(ch==1)
        {
         Re1.setText("(2) " + Long.toBinaryString(Long.parseLong(value, 8)));
         Re2.setText("(10) " + Long.parseLong(value, 8));
         Re3.setText("(16) " + Long.toHexString(Long.parseLong(value, 8)).toUpperCase());
        }
        else if(ch==2)
        {

         Re1.setText("(2) " + Long.toBinaryString(Long.parseLong(value, 10)));
         Re2.setText("(8) " + Long.toOctalString(Long.parseLong(value, 10)));
         Re3.setText("(16) " + Long.toHexString(Long.parseLong(value, 10)).toUpperCase());
        }
        else if(ch==3)
        {
         Re1.setText("(2) " + Long.toBinaryString(Long.parseLong(value, 16)));
         Re2.setText("(8) " + Long.toOctalString(Long.parseLong(value, 16)));
         Re3.setText("(10) " + Long.parseLong(value, 16));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void afterTextChanged(Editable s) {}
}
