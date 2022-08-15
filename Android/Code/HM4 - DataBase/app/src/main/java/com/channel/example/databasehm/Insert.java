package com.channel.example.databasehm;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Insert
        extends AppCompatActivity
        implements View.OnClickListener {

    private SQLiteDatabase con;
    private EditText Title,Cost,Lang,Length;
    private Button Insert;
    private TextView Report;
    private SQLiteStatement state;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_back));

        Title=findViewById(R.id.ITitle);
        Cost=findViewById(R.id.ICost);
        Lang=findViewById(R.id.ILanguage);
        Length=findViewById(R.id.ILength);

        Insert=findViewById(R.id.InsData);

        Report=findViewById(R.id.repIns);

        Insert.setOnClickListener(this);

        con=this.openOrCreateDatabase ("dodo.db",MODE_PRIVATE,null);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed ();
        return super.onSupportNavigateUp ();
    }

    @Override
    public void onClick(View v)
    {

        if(     v==Insert
                && (!Title.getText().toString().matches(""))
                && (!Cost.getText().toString().matches(""))
                && (!Lang.getText().toString().matches(""))
                && (!Length.getText().toString().matches(""))
          )
        {
            try {
                state = con.compileStatement("insert into Movies (Title,Cost,RT,Language) values(?,?,?,?)");
                state.bindString(1, Title.getText().toString().toLowerCase().trim());
                state.bindLong(2, Integer.parseInt(Cost.getText().toString().trim()));
                state.bindLong(3, Integer.parseInt(Length.getText().toString().trim()));
                state.bindString(4, Lang.getText().toString().toLowerCase().trim());
                state.executeInsert();

                Report.setText("{ The Data has been successfully inserted to the database }");
                Report.setTextColor(Color.parseColor("#2196F3"));
            }
            catch (Exception E)
            {
                Report.setText("{ There was an error while inserting data to database }");
                Report.setTextColor(Color.parseColor("#B71C1C"));
            }

        }

        if(Title.getText().toString().matches(""))
        {
            Title.setError("PLEASE FILL THE REQUIRED DATA.");
        }

        if(Cost.getText().toString().matches(""))
        {
            Cost.setError("PLEASE FILL THE REQUIRED DATA.");
        }

        if(Length.getText().toString().matches(""))
        {
            Length.setError("PLEASE FILL THE REQUIRED DATA.");
        }

        if(Lang.getText().toString().matches(""))
        {
            Lang.setError("PLEASE FILL THE REQUIRED DATA.");
        }


    }
}
