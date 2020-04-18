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

public class ShowMapActivity extends FragmentActivity implements OnMapReadyCallback {
    DatabaseHelper db;
    FusedLocationProviderClient fusedLocationProviderClient;
    String information;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        db = new DatabaseHelper(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        Cursor data = db.getPlantData();
        if (data.getCount() > 0)
        {
            while (data.moveToNext()) {
                ArrayList<LatLng> latLngArrayList = new ArrayList<>();
                ArrayList<String> listData = new ArrayList<>();
                listData.add(data.getString(2));//name
                listData.add(data.getString(4));//pollinator
                listData.add(data.getString(6));//latitude
                listData.add(data.getString(7));//longitude
                String name = listData.get(0);
                String pollinator = listData.get(1);
                Double lat = Double.parseDouble(listData.get(2));
                Double longy = Double.parseDouble(listData.get(3));
                LatLng location = new LatLng(lat, longy);
                latLngArrayList.add(location);
                information = name + " , " + pollinator+" ";
                googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,longy))
                        .title(information));
            }
        }
    }
}


