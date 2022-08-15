package com.channel.example.databasehm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Delete
        extends AppCompatActivity
        implements View.OnClickListener {

    private EditText IDDel;
    private Button Delete;
    private TextView ReportDel,OPD;
    private SQLiteDatabase con;
    private Cursor rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_back));

        IDDel=findViewById(R.id.DelID);
        Delete=findViewById(R.id.DelButt);

        ReportDel=findViewById(R.id.repDel);
        OPD=findViewById(R.id.odbd);
        con=this.openOrCreateDatabase ("dodo.db",MODE_PRIVATE,null);

        Delete.setOnClickListener(this);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed ();
        return super.onSupportNavigateUp ();
    }

    @Override
    public void onClick(View v)
    {

        if(v==Delete && (!IDDel.getText().toString().matches("")))
        {
            String res=IDDel.getText().toString();
            rs=con.rawQuery ( "select * from Movies WHERE ID = ?" ,new String[] {res});
            rs.moveToPosition ( -1 );

            if(rs.moveToNext ())
            {
            try {

                OPD.setText("     ***THE RECORD THAT WAS BEEN DELETED***" +
                        "\n  ID  ||  TITLE  ||  COST  ||  LENGTH  ||  LANGUAGE\n");

                OPD.append("  "+rs.getLong ( 0 )
                        +"   ||  "+rs.getString ( 1 )
                        +"   ||  "+rs.getLong ( 2 )
                        +"   ||  "+rs.getLong ( 3 )
                        +"   ||  "+rs.getString ( 4 )
                          );

            String D=IDDel.getText().toString().trim();
            con.delete ( "Movies", "ID=?",new String[]{D});
                ReportDel.setText("{ The Data has been successfully Deleted from database }");
                ReportDel.setTextColor(Color.parseColor("#2196F3"));
            }
            catch (Exception E)
            {
                ReportDel.setText(E.toString());
                //ReportDel.setText("{ There was an error while Deleting data to database }");
                ReportDel.setTextColor(Color.parseColor("#B71C1C"));
            }
            }
            else
                {
                    ReportDel.setText("{ Such a Record does not exist }");
                    ReportDel.setTextColor(Color.parseColor("#B71C1C"));

                }
        }

        if(IDDel.getText().toString().matches(""))
        {
            IDDel.setError("PLEASE FILL THE REQUIRED DATA.");
        }


    }
}
