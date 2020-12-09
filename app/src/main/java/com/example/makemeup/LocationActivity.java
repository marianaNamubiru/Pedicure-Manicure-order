package com.example.makemeup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LocationActivity extends AppCompatActivity {
    TextView textLocation;

    //variable to store decoded location
    String address;

    //request code to match the permission
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    //CREATE A REFERENCE TO GEOCODER CLASS
    Geocoder geocoder;

    //list reference to store the info that we getabout the location
    List<Address> addressList;
    //variable to hold users latitude and longitude
    double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);

        //reference to textview
        textLocation = findViewById(R.id.location);
        // create an instance of geocoder class
        geocoder = new Geocoder(this, Locale.getDefault());
        //call the method to request permissions from the user
        turnOnLocation();

    }

    //request permission from the user
    private void turnOnLocation() {
        //use location manager to get location service
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //first i check if i can get location from gps or network of user
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //launch a dialog with two options yes or no(use alert dialog builder)
            //new instance of alert dialog builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //customise dialog
            builder.setTitle(("Location needs to be enabled to use this app"));
            //set up the options
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //take user to settings app to turn on location
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(LocationActivity.this, "Location needs to be enabled o use this app", Toast.LENGTH_LONG).show();
                }
            });
            builder.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    LocationActivity.super.onBackPressed();
                }
            });
            //execute the builder
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        } else {
            Toast.makeText(this, "Location is on.", Toast.LENGTH_SHORT).show();
        }
    }

    public void getCurrent(View v) {
        //call the method to ask for users permission
        getLocations();
    }  //user permission to get the fine location

    private void getLocations() {
        //alert dialog
        new AlertDialog.Builder(this)
                .setTitle("Allow app to get your detailed location")
                .setMessage("will allow app to work better")

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //check whether permission is granted
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(
                                    LocationActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION
                            );
                        } else {
                            //if permission already set
                            getCurrentLocation();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .setIcon(R.drawable.ic_baseline_keyboard_backspace_24)
                .setCancelable(false)
                .show();
    }
    //method to handle users permission choice

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            getCurrentLocation();
        } else {
            Toast.makeText(this, "User denied permission", Toast.LENGTH_SHORT).show();


        }
    }

    //fetch longitude and latitude of the user
    private void getCurrentLocation() {
        //request for user location from android os
        final LocationRequest locationRequest = new LocationRequest();

        //create instance of locationrequest class //set metadata
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(LocationActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        //we can get lat,long.geocder to convert this into actual address
                        LocationServices.getFusedLocationProviderClient(LocationActivity.this).removeLocationUpdates(this);
                        //check if location result actually has a result
                        if(locationResult != null && locationResult.getLocations().size() > 0){
                            //set up location index which will allow us to get location lat and longitude
                            int latestlocationIndex= locationResult.getLocations().size() -1;//removing last known location from array list

                            //get users lat and long
                            lat = locationResult.getLocations().get(latestlocationIndex).getLatitude();
                            Log.d("LocationActivity","lat is " + lat);
                            lon = locationResult.getLocations().get(latestlocationIndex).getLongitude();
                            Log.d("LocationActivity","lon is" + lon);

                            //use geocoder class to make sense out of lat and long
                            try {
                                //populate list item with the address result //reverse geocoding
                                addressList= geocoder.getFromLocation(lat,lon, 1); //1 for max result be be represented

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            //using the list i can access the details of the user curent location //address
                            address = addressList.get(0).getAddressLine(0);
                            //to get other info
                            String City = addressList.get(0).getLocality();
                            String Country = addressList.get(0).getCountryName();
                            String Phone = addressList.get(0).getPhone();
                            String PostalCode = addressList.get(0).getPostalCode();

                            textLocation.setText(address);


                        }
                    }
                }, Looper.getMainLooper());

    }
}