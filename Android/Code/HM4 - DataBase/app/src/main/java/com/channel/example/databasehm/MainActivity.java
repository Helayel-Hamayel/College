package com.channel.example.databasehm;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.*;


public class MainActivity
        extends AppCompatActivity
        implements View.OnClickListener {

    private Button Insert,Search,Delete,Modify,DisplayAll;
    private TextView OP1;
    private Cursor rs;
    private SQLiteDatabase con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_exit));

        OP1=findViewById(R.id.odb);

        con=this.openOrCreateDatabase ("dodo.db",MODE_PRIVATE,null);

        Insert=findViewById(R.id.insbutt);
        Search=findViewById(R.id.srchbutt);
        Delete=findViewById(R.id.deletbutt);
        Modify=findViewById(R.id.modbutt);
        DisplayAll=findViewById(R.id.dabutt);

        Insert.setOnClickListener(this);
        Search.setOnClickListener(this);
        Delete.setOnClickListener(this);
        Modify.setOnClickListener(this);
        DisplayAll.setOnClickListener(this);

        con.execSQL ( "CREATE TABLE if not exists Movies " +
                "("
                + "id    integer not null primary key,"
                + "Title  text, "
                + "Cost   integer,"
                + "RT integer," //Runing Time
                + "Language  text"+
                ");"
        );

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed ();
        return super.onSupportNavigateUp ();
    }


    /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_back));
@Override
    public boolean onSupportNavigateUp() {
        onBackPressed ();
        return super.onSupportNavigateUp ();
    }
*/
    @Override
    public void onClick(View v)
    {
        Intent K;
        if(v==Insert)
        {
            K=new Intent(this,Insert.class);
            startActivity(K);
        }
        else if(v==Search)
        {
            K=new Intent(this,Search.class);
            startActivity(K);
        }
        else if(v==Delete)
        {
            K=new Intent(this,Delete.class);
            startActivity(K);
        }
        else if(v==Modify)
        {
            K=new Intent(this,Modify.class);
            startActivity(K);
        }
        else if(v==DisplayAll)
        {
            OP1.setText("  ID  ||  TITLE  ||  COST  ||  LENGTH  ||  LANGUAGE");
            rs=con.rawQuery ( "select * from Movies" ,null);

            rs.moveToPosition ( -1 );
            int x=rs.getCount();

            if(x!=0)
            {
                while (rs.moveToNext()) {

                    OP1.append("\n  " + rs.getString(0)
                            + "   ||  " + rs.getString(1)
                            + "   ||  " + rs.getString(2)
                            + "   ||  " + rs.getString(3)
                            + "   ||  " + rs.getString(4));
                }
            }
            else
                {
                    OP1.append("\n\n                       { The Database is empty }");
                }
        }
    }

}






/*

        SQLiteDatabase con=this.openOrCreateDatabase ("dodo.db",MODE_PRIVATE,null);


        String  s=String.format ( "insert into Movies values('"
                +"ABC"+r.nextInt (100)+"',"
                +r.nextInt ( 1000 )+","
                +r.nextInt(40)+","
                +r.nextInt(60)+",0)");

        con.execSQL (s);
*/
/*

        SQLiteStatement state;
        state=con.compileStatement ( "insert into Movies (Title,Cost,RT,Language) values(?,?,?,?)" );

        state.bindString ( 1,"bye" );
        state.bindLong ( 2,333 );
        state.bindLong ( 3,333 );
        state.bindString ( 4,"hello" );
        state.executeInsert();
*/


//con.delete ( "table1", "id<?",new String[]{"300"});
//con.execSQL ( "delete from Movies" );

/*
        Cursor rs=con.rawQuery ( "select * from Movies WHERE ID = ?" ,new String[] {DDEL});

        rs.moveToPosition ( -1 );
        while(rs.moveToNext ())
        {
            OP1.append("\n  "+rs.getString ( 0 )
                      +"   ||  "+rs.getString ( 1 )
                      +"   ||  "+rs.getString ( 2 )
                      +"   ||  "+rs.getString ( 3 )
                      +"   ||  "+rs.getString ( 4 ));
        }

            //File t= getDatabasePath ( "lolo.db" );
            //Log.i("this--->",t.toString ());
*/