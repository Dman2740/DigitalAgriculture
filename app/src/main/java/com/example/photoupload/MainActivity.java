package com.example.photoupload;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity
{
    Location currentLocation;
    public MarkerOptions options = new MarkerOptions();
    public ArrayList<LatLng> latLngArrayList=new ArrayList<>();
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE=101;
    Button takePic, sendData, viewData, btnDelete,
            btnviewUpdate,btnRegister,btnLogin,btnOpenMap,btnGraph;
    ImageView imageView;
    DatabaseHelper mDatabaseHelper;
    EditText editTextName, editTextDate, editTextPollinator,
            editTextId,editTextLocation,editTextCategory;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    Bitmap photo;
    long runningCounter;
    public LocationRequest locationRequest;
    public LocationCallback locationCallback;
    SimpleDateFormat sdf;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        setContentView(R.layout.activity_main);
        mDatabaseHelper = new DatabaseHelper(this);
        sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        date=new Date();
        String currentDate=sdf.format(date);
        TextView textViewDate = findViewById(R.id.datebox);
        textViewDate.setText(currentDate);
        btnGraph=findViewById(R.id.buttonGraph);
        btnDelete = findViewById(R.id.button_Delete);
        takePic = findViewById(R.id.takePic);
        imageView = findViewById(R.id.image);
        sendData = findViewById(R.id.sendData);
        editTextName = findViewById(R.id.nameText);
        editTextDate = findViewById(R.id.datebox);
        editTextPollinator = findViewById(R.id.pollinatorText);
        editTextCategory=findViewById(R.id.textCategory);
        editTextId = findViewById(R.id.editText_id);
        btnviewUpdate = findViewById(R.id.button_update);
        editTextLocation = findViewById(R.id.editTextUserLoc);
        viewData = findViewById(R.id.viewData);
        btnRegister = findViewById(R.id.gotoRegister);
        btnLogin = findViewById(R.id.gotoLogin);
        btnOpenMap = findViewById(R.id.buttonOpenMap);
        runningCounter = mDatabaseHelper.countPollinators() + 1;
        editTextId.setText(String.valueOf(runningCounter));
        takePicture();
        gotoLogin();
        gotoRegister();
        viewAll();
        AddData();
        DeleteData();
        UpdateData();
        gotoMap();
        gotoGraph();
        fetchLastLocation();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20 * 1000);
        locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        currentLocation=location;
                        editTextLocation.setText(currentLocation.getLatitude()+" , "+currentLocation.getLongitude());
                    }
                }
            }
        };
    }




    public void gotoLogin()
    {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginScreen.class));
            }
        });

    }
    public void gotoGraph()
    {
        btnGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GraphActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }

    public void gotoRegister()
    {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterScreen.class));
            }
        });

    }

    public void gotoMap()
    {
        btnOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this,ShowMapActivity.class));
            }
        });

    }

    public void takePicture()
    {
        takePic.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
    }

    public void viewAll() {
        viewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
                startActivityForResult(intent,0);
             }
        });
    }

    public void AddData()
    {
        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameEntry = editTextName.getText().toString();
                String dateEntry = editTextDate.getText().toString();
                String pollinatorEntry = editTextPollinator.getText().toString();
                String userLoc=editTextLocation.getText().toString();
                String[] values=userLoc.split(" , ");
                Double latitude = Double.parseDouble(values[0]);
                Double longitude = Double.parseDouble(values[1]);
                String categoryEntry=editTextCategory.getText().toString();
                boolean isInserted = mDatabaseHelper.insertData(categoryEntry,nameEntry,dateEntry,pollinatorEntry,photo,latitude,longitude);
                if ((nameEntry.length() != 0) && (dateEntry.length() != 0) && (pollinatorEntry.length() != 0))
                {
                    if (isInserted == true)
                    {
                        sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        date=new Date();
                        String currentDate=sdf.format(date);
                        TextView textViewDate = findViewById(R.id.datebox);
                        textViewDate.setText(currentDate);
                        editTextName.setText("");
                        editTextPollinator.setText("");
                        latLngArrayList.add(new LatLng(latitude,longitude));
                        runningCounter=runningCounter+1;
                        editTextId.setText(String.valueOf(runningCounter));
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);
                        Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Data Not Inserted", Toast.LENGTH_LONG).show();
                    }
                }
                else
                    {
                    toastMessage("You Must Put Something In Each Text Field");
                }
            }
        });
    }

    public void DeleteData() {
        btnDelete.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        mDatabaseHelper.deletePictureFile(editTextId.getText().toString());
                        mDatabaseHelper.deleteData(editTextId.getText().toString());
                        runningCounter=runningCounter-1;
                        editTextId.setText(String.valueOf(runningCounter));
                        sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        date=new Date();
                        String currentDate=sdf.format(date);
                        TextView textViewDate = findViewById(R.id.datebox);
                        textViewDate.setText(currentDate);
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                        toastMessage("Removed from database");
                    }
                }
        );
    }

    public void UpdateData() {
        btnviewUpdate.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        boolean isUpdate=mDatabaseHelper.updateData(editTextCategory.getText().toString(),editTextId.getText().toString(),
                                editTextName.getText().toString(),
                                editTextDate.getText().toString(),editTextPollinator.getText().toString(),
                                photo);
                        if(isUpdate != false) {
                            Toast.makeText(MainActivity.this, "Data Update", Toast.LENGTH_LONG).show();
                            sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                            date=new Date();
                            String currentDate=sdf.format(date);
                            TextView textViewDate = findViewById(R.id.datebox);
                            textViewDate.setText(currentDate);
                            fetchLastLocation();
                        }
                        else if(isUpdate == false) {
                            Toast.makeText(MainActivity.this, "Data not Updated", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode==REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                fetchLastLocation();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            super.onActivityResult(requestCode,resultCode,data);
            photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }

    private void fetchLastLocation()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE);
            return;
        }
        Task<Location> task=fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>()
        {
            @Override
            public void onSuccess(Location location) {
                if(location != null)
                {
                    currentLocation=location;
                    editTextLocation.setText(currentLocation.getLatitude()+" , "+currentLocation.getLongitude());
                }
            }
        });
    }

    private void toastMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}