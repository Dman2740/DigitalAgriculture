package com.example.photoupload;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class ShowMapActivity extends FragmentActivity implements OnMapReadyCallback
{
    DatabaseHelper db;
    public MarkerOptions options = new MarkerOptions();
    ArrayList<String> listData = new ArrayList<>();
    ArrayList<LatLng> latLngArrayList=new ArrayList<>();
    FusedLocationProviderClient fusedLocationProviderClient;
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        db=new DatabaseHelper(this);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        Cursor data = db.getPlantData();
        while (data.moveToNext())
        {
            listData.add(data.getString(1));//name
            listData.add(data.getString(2));//date
            listData.add(data.getString(3));//pollinator
            listData.add(data.getString(5));//latitude
            listData.add(data.getString(6));//longitude
            String name=listData.get(0);
            String date=listData.get(1);
            String pollinator=listData.get(2);
            Double lat=Double.parseDouble(listData.get(3));
            Double longy=Double.parseDouble(listData.get(4));
            LatLng location=new LatLng(lat,longy);
            latLngArrayList.add(location);
            String information=name+" , "+date+" ";
            for (LatLng point:latLngArrayList)
            {
                options.position(point);
                options.title(pollinator);
                options.snippet(information);
                googleMap.addMarker(options);
            }

        }

    }

}
