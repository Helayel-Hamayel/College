package com.channel.example.implicitintentexamples;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera
        extends AppCompatActivity
        implements View.OnClickListener{

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private Button cam,delete,save;
    private ImageView V;
    private Bitmap imageBitmap;
    private File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_back));

        cam=findViewById(R.id.camera);
        delete=findViewById(R.id.delete);
        save=findViewById(R.id.save);

        delete.setEnabled(false);
        save.setEnabled(false);

        V=findViewById(R.id.pic);

        cam.setOnClickListener(this);
        delete.setOnClickListener(this);
        save.setOnClickListener(this);

    }

    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return super.onSupportNavigateUp();
    }



    @Override
    public void onClick(View v)
    {
        if(v==cam)
        {
            Intent Pic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Pic.resolveActivity(getPackageManager()) != null)
            {
                startActivityForResult(Pic, REQUEST_IMAGE_CAPTURE);
            }
        }

        else if(v==delete && V.getDrawable() != null)
        {
            V.setImageBitmap(null);
            delete.setEnabled(false);
            save.setEnabled(false);
            delete.setTextColor(Color.GRAY);
        }

        else if(v==save && imageBitmap != null)
        {
            saveThePicture();
        }
    }

    protected void saveThePicture()
    {
        File storageDir = Environment.getExternalStorageDirectory().getAbsoluteFile();

        File dir=new File(storageDir+"/HM3TEST/");

        if(!dir.exists())
        {
            dir.mkdirs();
        }

        String imageFileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        file=new File(dir,imageFileName+".png");
        OutputStream out=null;

        try {

            out=new FileOutputStream(file);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,out);

            Toast.makeText(getApplicationContext(),"{successfully saved the Picture at ="+file.getAbsolutePath()+"}", Toast.LENGTH_LONG).show();
        }
        catch (Exception R)
        {
            Toast.makeText(getApplicationContext(),R.toString(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageBitmap = (Bitmap) data.getExtras().get("data");
            V.setImageBitmap(imageBitmap);
            delete.setEnabled(true);
            save.setEnabled(true);
        }
    }


}
