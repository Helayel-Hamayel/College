package com.example.prjectfreya;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class bottomsheet_buses extends BottomSheetDialogFragment {

    private BottomSheetListener mListener;

    private Button BusButton,DismissBS;

    private GoogleMap mMap;
    private MapView mapView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottomsheet_buses,container,false);

        BusButton = v.findViewById(R.id.button1);
        DismissBS = v.findViewById(R.id.DissButt);

        BusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("Choosen_bus","Bus_1");

                bottomsheet_bus_info bottomSheet2 = new bottomsheet_bus_info();

                bottomSheet2.setArguments(bundle);

                bottomSheet2.show(getChildFragmentManager(), "Bottomsheet_bus_info");

                mListener.moveTheCamera("Bus_1");
            }
        });

        DismissBS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            dismiss();
            }
        });

        return v;
    }

    public interface BottomSheetListener {
        void moveTheCamera(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BottomSheetListener");
        }
    }


}
