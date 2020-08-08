package com.androstock.myweatherapp.location;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androstock.myweatherapp.MainActivity;
import com.androstock.myweatherapp.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity  {
    public static final int REQUEST_LOCATION = 1;
    // Konum guncellemesi gerektirecek minimum degisim miktari
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // metre

    // Konum guncellemesi gerektirecek minimum sure miktari
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // dakika
    double latitude ,longitude;

    LocationManager locationManager;
// protected static final int REQUEST_LOCATION

    Button btlocation;
    TextView textlocation;
    String cityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        btlocation=findViewById(R.id.btkonumbul);
        textlocation=findViewById(R.id.textlocation);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);

        btlocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    OnGPS();
                }else{
                    getLocation();

                    Intent intentToMain=new Intent(LocationActivity.this,MainActivity.class);
                   // intentToMain.putExtra("cityname",cityName);
                    startActivity(intentToMain);
                }
            }
        } );

    }
    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                LocationActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                LocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                //  double lat =
                // double longi =
                latitude = locationGPS.getLatitude();
                longitude= locationGPS.getLongitude();
                textlocation.setText("Your Location: " +getCityfromLocation(latitude,longitude )+ "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
                cityName=getCityfromLocation(latitude,longitude);
                SharedPreferences sharedPreferences=getApplication().getApplicationContext().getSharedPreferences("myfile",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("city",cityName);
                editor.commit();

            } else {
                Toast.makeText(this, "konum bulunamadı", Toast.LENGTH_SHORT).show();
            }

        }

    }
    public String getCityfromLocation(double lat, double lon){
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        List<Address> addressList;
        try{
            addressList=geocoder.getFromLocation(lat,lon,10);
            if(addressList.size()>0){
                for(Address address:addressList){
                    if(address.getLocality()!=null && address.getLocality().length()>0){
                        cityName=address.getLocality();
                        break;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return cityName;
    }






}