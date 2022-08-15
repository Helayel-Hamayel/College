package com.channel.example.databasehm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Modify
        extends AppCompatActivity
        implements View.OnClickListener {

    private Button Data,Save;
    private EditText T,C,Len,Lang,Id;
    private TextView R1,R2,R3;
    private SQLiteDatabase con;
    private Cursor rs;
    private String OriTitle,OriLang;
    private long OriLen,OriCost;
    private SQLiteStatement state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_back));

        con=this.openOrCreateDatabase ("dodo.db",MODE_PRIVATE,null);

        R1=findViewById(R.id.Mrep1);
        R2=findViewById(R.id.Mrep2);
        R3=findViewById(R.id.ModRep);

        Data=findViewById(R.id.gdButt);
        Save=findViewById(R.id.save);

        Id=findViewById(R.id.MID);

        T=findViewById(R.id.MT);
        C=findViewById(R.id.MC);
        Len=findViewById(R.id.ML);
        Lang=findViewById(R.id.MLang);

        T.setEnabled(false);
        C.setEnabled(false);
        Len.setEnabled(false);
        Lang.setEnabled(false);
        Save.setEnabled(false);

        Data.setOnClickListener(this);
        Save.setOnClickListener(this);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed ();
        return super.onSupportNavigateUp ();
    }
    @Override
    public void onClick(View v)
    {

        if(v==Data)
        {
            String res=Id.getText().toString();
            rs=con.rawQuery ( "select * from Movies WHERE ID = ?" ,new String[] {res});
            rs.moveToPosition ( -1 );

            if(rs.moveToNext())
            {
                T.setEnabled(true);
                C.setEnabled(true);
                Len.setEnabled(true);
                Lang.setEnabled(true);
                Save.setEnabled(true);

                OriTitle=rs.getString(1).toLowerCase().trim();
                OriCost=rs.getLong(2);
                OriLen=rs.getLong(3);
                OriLang=rs.getString(4).toLowerCase().trim();

                T.setText(OriTitle);
                C.setText(String.valueOf(OriCost));
                Len.setText(String.valueOf(OriLen));
                Lang.setText(OriLang);

                R1.setText("{ The Data has been successfully fetched from database }");
                R1.setTextColor(Color.parseColor("#2196F3"));

                R3.setText("                    ***THE FETCHED RECORD***" +
                        "\n  ID  ||  TITLE  ||  COST  ||  LENGTH  ||  LANGUAGE\n");

                R3.append("  " + rs.getLong(0)
                        + "   ||  " + rs.getString(1)
                        + "   ||  " + rs.getLong(2)
                        + "   ||  " + rs.getLong(3)
                        + "   ||  " + rs.getString(4)
                );

            }
            else{

                R1.setText("{ Such record does not exist }");
                R1.setTextColor(Color.parseColor("#B71C1C"));

                T.setEnabled(false);
                C.setEnabled(false);
                Len.setEnabled(false);
                Lang.setEnabled(false);

                Save.setEnabled(false);
                }

        }
        else if(v==Save)
        {
            state=con.compileStatement ( "UPDATE Movies SET Title = ?, Cost = ? ,RT = ?, Language = ? WHERE id=?" );

            state.bindString(1,
                    (!T.getText().toString().matches("")?
                    T.getText().toString().toLowerCase().trim() : OriTitle.toLowerCase())
                            );//title

            state.bindLong(2,
                    (!C.getText().toString().matches("")?
                    Integer.parseInt(C.getText().toString().trim()) : OriLen)
                          );//cost

            state.bindLong(3,
                    (!Len.getText().toString().matches("")?
                    Integer.parseInt(Len.getText().toString().trim()) : OriCost)
                            );//Length

            state.bindString(4,
                    (!Lang.getText().toString().matches("")?
                     Lang.getText().toString().toLowerCase().trim() : OriLang.toLowerCase())
                            );//Language

            state.bindLong(5,Integer.parseInt(Id.getText().toString().trim()));//ID

            state.executeInsert();

            R2.setText("{ The Data has been successfully updated to database }");
            R2.setTextColor(Color.parseColor("#2196F3"));

            R3.append(
                    "\n====================================\n"
                    +"                    ***THE UPDATED RECORD***"
                    +"\n  ID  ||  TITLE  ||  COST  ||  LENGTH  ||  LANGUAGE\n");

            R3.append("  " + rs.getLong(0)
                    + "   ||  " + (!T.getText().toString().matches("")?
                    T.getText().toString().toLowerCase().trim() : OriTitle.toLowerCase())
                    + "   ||  " + (!C.getText().toString().matches("")?
                    Integer.parseInt(C.getText().toString().trim()) : OriLen)
                    + "   ||  " + (!Len.getText().toString().matches("")?
                    Integer.parseInt(Len.getText().toString().trim()) : OriCost)
                    + "   ||  " + (!Lang.getText().toString().matches("")?
                    Lang.getText().toString().toLowerCase().trim() : OriLang.toLowerCase())
            );
        }
    }
}