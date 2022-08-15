package com.channel.example.databasehm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Search
        extends AppCompatActivity
        implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private EditText SID,STitle,SLang,SCost,SLength;
    private RadioButton RID,RTitle,RLang,RCost,RLength;
    private TextView RE;
    private CheckBox CLenE,RLenGT,RLenLT;
    private CheckBox CCostE,RCostGT,RCostLT;
    private Button Search;
    private SQLiteDatabase con;
    private Cursor rs;
    private RadioGroup G1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_back));

        con=this.openOrCreateDatabase ("dodo.db",MODE_PRIVATE,null);

        Search=findViewById(R.id.Srchbutt);
        RE=findViewById(R.id.RS);

        SID=findViewById(R.id.srchID);
        STitle=findViewById(R.id.srchTitle);
        SLang=findViewById(R.id.srchLang);
        SCost=findViewById(R.id.srchCost);
        SLength=findViewById(R.id.srchLen);

        STitle.setVisibility(View.GONE);
        SLang.setVisibility(View.GONE);
        SCost.setVisibility(View.GONE);
        SLength.setVisibility(View.GONE);

        RID=findViewById(R.id.rbID);
        RTitle=findViewById(R.id.rbTitle);
        RLang=findViewById(R.id.rbLang);
        RCost=findViewById(R.id.rbCost);
        RLength=findViewById(R.id.rbLen);

        G1=findViewById(R.id.RG1);

        RLenGT=findViewById(R.id.cbLGT);
        RLenLT=findViewById(R.id.cbLLT);
        CLenE=findViewById(R.id.cbLE);

        RLenGT.setVisibility(View.GONE);
        RLenLT.setVisibility(View.GONE);
        CLenE.setVisibility(View.GONE);

        RCostGT=findViewById(R.id.cbCGT);
        RCostLT=findViewById(R.id.cbCLT);
        CCostE=findViewById(R.id.cbCE);

        RCostGT.setVisibility(View.GONE);
        RCostLT.setVisibility(View.GONE);
        CCostE.setVisibility(View.GONE);

        G1.setOnCheckedChangeListener(this);
        Search.setOnClickListener(this);

        RLenGT.setOnClickListener(this);
        RLenLT.setOnClickListener(this);

        RCostGT.setOnClickListener(this);
        RCostLT.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed ();
        return super.onSupportNavigateUp ();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        SID.setVisibility(View.GONE);
        STitle.setVisibility(View.GONE);
        SLang.setVisibility(View.GONE);
        SCost.setVisibility(View.GONE);
        SLength.setVisibility(View.GONE);

        RCostGT.setVisibility(View.GONE);
        RCostLT.setVisibility(View.GONE);
        CCostE.setVisibility(View.GONE);

        RLenGT.setVisibility(View.GONE);
        RLenLT.setVisibility(View.GONE);
        CLenE.setVisibility(View.GONE);

        if(checkedId==RID.getId())
        {
            SID.setVisibility(View.VISIBLE);
        }
        else if(checkedId==RTitle.getId())
        {
            STitle.setVisibility(View.VISIBLE);
        }
        else if(checkedId==RLang.getId())
        {
            SLang.setVisibility(View.VISIBLE);
        }
        else if(checkedId==RCost.getId())
        {
            SCost.setVisibility(View.VISIBLE);
            RCostGT.setVisibility(View.VISIBLE);
            RCostLT.setVisibility(View.VISIBLE);
            CCostE.setVisibility(View.VISIBLE);

        }
        else if(checkedId==RLength.getId())
        {
            SLength.setVisibility(View.VISIBLE);
            RLenGT.setVisibility(View.VISIBLE);
            RLenLT.setVisibility(View.VISIBLE);
            CLenE.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v)
    {
     if(v==Search)
     {
         try{
        if(RID.isChecked())
        {

         if(!SID.getText().toString().matches(""))
         {
             String res = SID.getText().toString();
             rs = con.rawQuery("select * from Movies WHERE ID = ?", new String[]{res});
             rs.moveToPosition(0);


             RE.setText("  ID  ||  TITLE  ||  COST  ||  LENGTH  ||  LANGUAGE\n");

             RE.append("  " + rs.getLong(0)
                     + "   ||  " + rs.getString(1).toLowerCase()
                     + "   ||  " + rs.getLong(2)
                     + "   ||  " + rs.getLong(3)
                     + "   ||  " + rs.getString(4).toLowerCase()+ "\n"
             );
         }
        else if(SID.getText().toString().matches(""))
        {
          SID.setError("Fill the required data to Search");
        }
       }

       else if(RTitle.isChecked())
       {
             if(!STitle.getText().toString().matches(""))
             {
                 String res = STitle.getText().toString().toLowerCase();
                 rs = con.rawQuery("select * from Movies WHERE Title = ?", new String[]{res});
                 rs.moveToPosition(-1);

                 RE.setText("  ID  ||  TITLE  ||  COST  ||  LENGTH  ||  LANGUAGE\n");

                 while(rs.moveToNext())
                 {
                     RE.append("  " + rs.getLong(0)
                             + "   ||  " + rs.getString(1)
                             + "   ||  " + rs.getLong(2)
                             + "   ||  " + rs.getLong(3)
                             + "   ||  " + rs.getString(4)+ "\n"
                     );
                 }

             }
             else if(STitle.getText().toString().matches(""))
             {
                 STitle.setError("Fill the required data to Search");
             }
       }
        else if(RLang.isChecked())
        {
            if(!SLang.getText().toString().matches(""))
            {
                String res = SLang.getText().toString().toLowerCase();
                rs = con.rawQuery("select * from Movies WHERE Language = ?", new String[]{res});
                rs.moveToPosition(-1);

                RE.setText("  ID  ||  TITLE  ||  COST  ||  LENGTH  ||  LANGUAGE\n");

                while(rs.moveToNext())
                {
                    RE.append("  " + rs.getLong(0)
                            + "   ||  " + rs.getString(1).toLowerCase()
                            + "   ||  " + rs.getLong(2)
                            + "   ||  " + rs.getLong(3)
                            + "   ||  " + rs.getString(4).toLowerCase()+ "\n"
                    );
                }

            }
            else if(SLang.getText().toString().matches(""))
            {
                SLang.setError("Fill the required data to Search");
            }
        }


        else if(RCost.isChecked())
        {
            if(!SCost.getText().toString().matches(""))
            {
                String res = SCost.getText().toString().toLowerCase();
                int L=Integer.parseInt(res);

                if(CCostE.isChecked()&&RCostLT.isChecked())
                {
                    rs = con.rawQuery("select * from Movies WHERE Cost <= ?", new String[]{String.valueOf(res)});

                    rs.moveToPosition(-1);

                    RE.setText("  ID  ||  TITLE  ||  COST  ||  LENGTH  ||  LANGUAGE\n");

                    while(rs.moveToNext())
                    {
                        RE.append("  " + rs.getLong(0)
                                + "   ||  " + rs.getString(1).toLowerCase()
                                + "   ||  " + rs.getLong(2)
                                + "   ||  " + rs.getLong(3)
                                + "   ||  " + rs.getString(4).toLowerCase()+ "\n"
                        );
                    }

                }


                else if(CCostE.isChecked()&&RCostGT.isChecked())
                {
                    rs = con.rawQuery("select * from Movies WHERE Cost >= ?", new String[]{res});
                    rs.moveToPosition(-1);

                    RE.setText("  ID  ||  TITLE  ||  COST  ||  LENGTH  ||  LANGUAGE\n");

                    while(rs.moveToNext())
                    {
                        RE.append("  " + rs.getLong(0)
                                + "   ||  " + rs.getString(1).toLowerCase()
                                + "   ||  " + rs.getLong(2)
                                + "   ||  " + rs.getLong(3)
                                + "   ||  " + rs.getString(4).toLowerCase()+ "\n"
                        );
                    }


                }

                else if(CCostE.isChecked())
                {
                    rs = con.rawQuery("select * from Movies WHERE Cost = ?", new String[]{res});
                    rs.moveToPosition(-1);

                    RE.setText("  ID  ||  TITLE  ||  COST  ||  LENGTH  ||  LANGUAGE\n");

                    while(rs.moveToNext())
                    {
                        RE.append("  " + rs.getLong(0)
                                + "   ||  " + rs.getString(1).toLowerCase()
                                + "   ||  " + rs.getLong(2)
                                + "   ||  " + rs.getLong(3)
                                + "   ||  " + rs.getString(4).toLowerCase()+ "\n"
                        );
                    }

                }

                else if(RCostGT.isChecked())
                {
                    rs = con.rawQuery("select * from Movies WHERE Cost > ?", new String[]{res});
                    rs.moveToPosition(-1);

                    RE.setText("  ID  ||  TITLE  ||  COST  ||  LENGTH  ||  LANGUAGE\n");

                    while(rs.moveToNext())
                    {
                        RE.append("  " + rs.getLong(0)
                                + "   ||  " + rs.getString(1).toLowerCase()
                                + "   ||  " + rs.getLong(2)
                                + "   ||  " + rs.getLong(3)
                                + "   ||  " + rs.getString(4).toLowerCase()+ "\n"
                        );
                    }

                }

                else if(RCostLT.isChecked())
                {
                    rs = con.rawQuery("select * from Movies WHERE Cost < ?", new String[]{res});
                    rs.moveToPosition(-1);

                    RE.setText("  ID  ||  TITLE  ||  COST  ||  LENGTH  ||  LANGUAGE\n");

                    while(rs.moveToNext())
                    {
                        RE.append("  " + rs.getLong(0)
                                + "   ||  " + rs.getString(1).toLowerCase()
                                + "   ||  " + rs.getLong(2)
                                + "   ||  " + rs.getLong(3)
                                + "   ||  " + rs.getString(4).toLowerCase()+ "\n"
                        );
                    }

                }

            }
            else if(SCost.getText().toString().matches(""))
            {
                SCost.setError("Fill the required data to Search");
            }
        }
        else if(RLength.isChecked())
        {
            if(!SLength.getText().toString().matches(""))
            {
                String res = SLength.getText().toString().toLowerCase();
                int L=Integer.parseInt(res);

                if(CLenE.isChecked()&&RLenLT.isChecked())
                {
                    rs = con.rawQuery("select * from Movies WHERE RT <= ?", new String[]{String.valueOf(res)});

                    rs.moveToPosition(-1);

                    RE.setText("  ID  ||  TITLE  ||  COST  ||  LENGTH  ||  LANGUAGE\n");

                    while(rs.moveToNext())
                    {
                        RE.append("  " + rs.getLong(0)
                                + "   ||  " + rs.getString(1).toLowerCase()
                                + "   ||  " + rs.getLong(2)
                                + "   ||  " + rs.getLong(3)
                                + "   ||  " + rs.getString(4).toLowerCase()+ "\n"
                        );
                    }

                }

                else if(CLenE.isChecked()&&RLenGT.isChecked())
                {
                    rs = con.rawQuery("select * from Movies WHERE RT >= ?", new String[]{res});
                    rs.moveToPosition(-1);

                    RE.setText("  ID  ||  TITLE  ||  COST  ||  LENGTH  ||  LANGUAGE\n");

                    while(rs.moveToNext())
                    {
                        RE.append("  " + rs.getLong(0)
                                + "   ||  " + rs.getString(1).toLowerCase()
                                + "   ||  " + rs.getLong(2)
                                + "   ||  " + rs.getLong(3)
                                + "   ||  " + rs.getString(4).toLowerCase()+ "\n"
                        );
                    }


                }
                else if(CLenE.isChecked())
                {
                    rs = con.rawQuery("select * from Movies WHERE RT = ?", new String[]{res});
                    rs.moveToPosition(-1);

                    RE.setText("  ID  ||  TITLE  ||  COST  ||  LENGTH  ||  LANGUAGE\n");

                    while(rs.moveToNext())
                    {
                        RE.append("  " + rs.getLong(0)
                                + "   ||  " + rs.getString(1).toLowerCase()
                                + "   ||  " + rs.getLong(2)
                                + "   ||  " + rs.getLong(3)
                                + "   ||  " + rs.getString(4).toLowerCase()+ "\n"
                        );
                    }

                }
                else if(RLenGT.isChecked())
                {
                    rs = con.rawQuery("select * from Movies WHERE RT > ?", new String[]{res});
                    rs.moveToPosition(-1);

                    RE.setText("  ID  ||  TITLE  ||  COST  ||  LENGTH  ||  LANGUAGE\n");

                    while(rs.moveToNext())
                    {
                        RE.append("  " + rs.getLong(0)
                                + "   ||  " + rs.getString(1).toLowerCase()
                                + "   ||  " + rs.getLong(2)
                                + "   ||  " + rs.getLong(3)
                                + "   ||  " + rs.getString(4).toLowerCase()+ "\n"
                        );
                    }

                }
                else if(RLenLT.isChecked())
                {
                    rs = con.rawQuery("select * from Movies WHERE RT < ?", new String[]{res});
                    rs.moveToPosition(-1);

                    RE.setText("  ID  ||  TITLE  ||  COST  ||  LENGTH  ||  LANGUAGE\n");

                    while(rs.moveToNext())
                    {
                        RE.append("  " + rs.getLong(0)
                                + "   ||  " + rs.getString(1).toLowerCase()
                                + "   ||  " + rs.getLong(2)
                                + "   ||  " + rs.getLong(3)
                                + "   ||  " + rs.getString(4).toLowerCase()+ "\n"
                        );
                    }

                }

            }
            else if(SLength.getText().toString().matches(""))
            {
                SLength.setError("Fill the required data to Search");
            }
        }
}
catch(Exception R){RE.setText(R.toString());}

     }
     if(v==RLenGT && RLenLT.isChecked()){RLenLT.setChecked(false);}
     else if(v==RLenLT&& RLenGT.isChecked()){RLenGT.setChecked(false);}

     if(v==RCostGT && RCostLT.isChecked()){RCostLT.setChecked(false);}
     else if(v==RCostLT&& RCostGT.isChecked()){RCostGT.setChecked(false);}
    }
}
