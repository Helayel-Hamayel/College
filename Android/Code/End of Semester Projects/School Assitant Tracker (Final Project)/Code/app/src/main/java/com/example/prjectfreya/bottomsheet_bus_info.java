package com.example.prjectfreya;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class bottomsheet_bus_info extends BottomSheetDialogFragment {

    private static final String TAG ="bottomsheet_bus_info";

    private TextView Driver_info_Name,Driver_info_State,Car_info_Plate;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mainBase = database.getReference();
    String Bus_X;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottomsheet_bus_info,container,false);

        Bundle bundle = getArguments();
        if(bundle != null){
            Bus_X = bundle.getString("Choosen_bus");
            Log.d(TAG,"the choosen bus is : "+Bus_X);
        }

        Driver_info_Name = v.findViewById(R.id.DriverName);
        Driver_info_State = v.findViewById(R.id.Wrkst);
        Car_info_Plate = v.findViewById(R.id.PlateNumber);


        mainBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("Drivers").child(dataSnapshot.child("Bus").child(Bus_X).child("Assigned_driver").getValue().toString()).exists() && dataSnapshot.child("Bus").child(Bus_X).child("Car_info").child("Plate").exists() && dataSnapshot.child("Bus").child(Bus_X).child("State").exists() ) {
                   String ID = dataSnapshot.child("Bus").child(Bus_X).child("Assigned_driver").getValue().toString();

                    Driver_info_Name.setText("Driver name : " + dataSnapshot.child("Drivers").child(ID).child("Name").getValue() + " (" + dataSnapshot.child("Drivers").child(ID).child("Age").getValue() + " yr/old)");
                    Car_info_Plate.setText("Plate number : " + dataSnapshot.child("Bus").child(Bus_X).child("Car_info").child("Plate").getValue());
                    Driver_info_State.setText("Working State : " + dataSnapshot.child("Bus").child(Bus_X).child("State").getValue());

               Log.d(TAG,"DATA RETRIEVAL IS SUCCESS");
                }
                else{
                    Driver_info_Name.setText("Driver name : NaN" );
                    Car_info_Plate.setText("Plate number : NaN" );
                    Driver_info_State.setText("Working State : NaN" );
                    Log.d(TAG,"ERROR, COULDN'T RETRIEVE DATA FROM DATABASE");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        return v;
    }

    }
