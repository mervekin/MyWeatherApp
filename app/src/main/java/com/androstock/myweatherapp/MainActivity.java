package com.androstock.myweatherapp;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import android.widget.ImageView;

import com.androstock.myweatherapp.notification.NotificationReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {


    TextView  cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField,sunrise,sunset,wind_field;
    ProgressBar loader;
    ImageView selectCity;
    Typeface weatherFont;
    String city = "";
    String OPEN_WEATHER_MAP_API = "b93fd7d5703b0a73bd479fc5701b4208";

    RelativeLayout relativeLayout;

    String UNITS;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();//actionbar gizliyoruz ana sayfada
        setContentView(R.layout.activity_main);
        // konum kullanarak verileri alırsak eğer gelen city değeri
        SharedPreferences sharedPreferences=getApplication().getApplicationContext().getSharedPreferences("myfile",Context.MODE_PRIVATE);
        city=sharedPreferences.getString("city","Kayıt Yok");

        //konumdan gelen şehir
      /*  Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            city=bundle.getString("cityname");
        }*/

      UNITS=sharedPreferences.getString("units","metric");


        bildirim();//bildirim fonk

        relativeLayout=(RelativeLayout)findViewById(R.id.relative);
        loader = (ProgressBar) findViewById(R.id.loader);
        selectCity = (ImageView) findViewById(R.id.selectCity);
        sunrise=findViewById(R.id.sunrise);
        sunset=findViewById(R.id.sunset);
        cityField = (TextView) findViewById(R.id.city_field);
        updatedField = (TextView) findViewById(R.id.updated_field);
        detailsField = (TextView) findViewById(R.id.details_field);
        currentTemperatureField = (TextView) findViewById(R.id.current_temperature_field);
        humidity_field = (TextView) findViewById(R.id.humidity_field);
        pressure_field = (TextView) findViewById(R.id.pressure_field);
        wind_field=(TextView)findViewById(R.id.wind_field);
        weatherIcon = (TextView) findViewById(R.id.weather_icon);
        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");
        weatherIcon.setTypeface(weatherFont);
        City.city=city;
        taskLoadUp(city);

     //navigationview için gerekli kodlama
        final BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Main:
                        break;
                    case R.id.Weather:
                        Intent intent = new Intent(getApplicationContext(),WeatherActivity.class);//günlük verielr sayfası
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });


        //şehir değiştirmede imageview dinliyoruz
        selectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Şehir Değiştir");
                final EditText input = new EditText(MainActivity.this);
                input.setText(city);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setPositiveButton("Değiştir",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                city = input.getText().toString();
                                City.city=city;
                                taskLoadUp(city);//girilen citye göre verileri değiştirecek
                            }
                        });
                alertDialog.setNegativeButton("Sonra",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }
        });

    }

    private void bildirim() {

        Calendar calender=Calendar.getInstance();
        calender.set(Calendar.HOUR_OF_DAY,12);
        calender.set(Calendar.MINUTE,00);
        calender.set(Calendar.SECOND,00);
        Intent intent=new Intent (getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calender.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

    }

// internet yoksa sharedPrefences eklenen veriler alınıyor internet varsa DownloadWeather classıyla çekiyoruz.çekiyoruzz,
    public void taskLoadUp(String query) {
        if (Function.isNetworkAvailable(getApplicationContext())) {
            DownloadWeather task = new DownloadWeather();
            task.execute(query);
        } else {  SharedPreferences sharedPref = this.getSharedPreferences("myfile",MODE_PRIVATE);

            String Current_temperature= sharedPref.getString("temperature","Kayıt Yok");
            currentTemperatureField.setText(Current_temperature);
            String details = sharedPref.getString("details","Kayıt Yok");
            detailsField.setText(details);
            String humidity = sharedPref.getString("humidity","Kayıt Yok");
            humidity_field.setText(humidity);
            String pressure = sharedPref.getString("pressure","Kayıt Yok");
            pressure_field.setText(pressure);
            String wind=sharedPref.getString("speed","Kayıt Yok");
            wind_field.setText(wind);
            String icon= sharedPref.getString("icon","Kayıt Yok");
            weatherIcon.setText(icon);
            String updatedField = sharedPref.getString("updatedField","Kayıt Yok");
            loader.setVisibility(View.GONE);
            String city = sharedPref.getString("city","Kayıt Yok");
            cityField.setText(city);
            String Upsunrise=sharedPref.getString("sunrise","Kayıt yok");
            sunrise.setText(Upsunrise);
            String Upsunset=sharedPref.getString("sunset","Kayıt yok");
            sunrise.setText(Upsunset);


            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }



    @RequiresApi(api=Build.VERSION_CODES.CUPCAKE)
    class DownloadWeather extends AsyncTask < String, Void, String > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);
        }
        protected String doInBackground(String...args) {
            String xml = Function.excuteGet("http://api.openweathermap.org/data/2.5/weather?q=" + args[0] +
                    "&units="+UNITS+"&appid=" + OPEN_WEATHER_MAP_API+ "&lang=tr");
            return xml;
        }
        @Override
        protected void onPostExecute(String xml) {
            try {
                JSONObject json = new JSONObject(xml);
                if (json != null) {
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
                    JSONObject wind=json.getJSONObject("wind");
                    DateFormat df = DateFormat.getDateTimeInstance();

                    //get unit
                    String[] units=new String[2];
                    if(UNITS.equals("imperial")){
                        units[0]="°F";
                    }else{
                        units[0]="°C";
                    }



                    cityField.setText(json.getString("name").toUpperCase(Locale.getDefault()) + ", " + json.getJSONObject("sys").getString("country"));
                    detailsField.setText(details.getString("description").toUpperCase(Locale.getDefault()));
                    sunrise.setText(UnixTime(json.getJSONObject("sys").getLong("sunrise")));
                    sunset.setText(UnixTime(json.getJSONObject("sys").getLong("sunset")));
                    currentTemperatureField.setText(Integer.toString((int)Math.round(Float.parseFloat(main.getString("temp"))))+ units[0]);
                    humidity_field.setText("" + main.getString("humidity") + "%");
                    pressure_field.setText("" + main.getString("pressure") + " hPa");
                    wind_field.setText(wind.getString("speed") + "km/s");
                    updatedField.setText(df.format(new Date(json.getLong("dt") * 1000)));
                    weatherIcon.setText(Html.fromHtml(Function.setWeatherIcon(details.getInt("id"),
                            json.getJSONObject("sys").getLong("sunrise") * 1000,
                            json.getJSONObject("sys").getLong("sunset") * 1000)));
                    loader.setVisibility(View.GONE);

                    //shared preferencese kaydetme verileri internet problemi olduğu zamna kullanmak için
                    SharedPreferences sharedPref;
                    sharedPref=getApplication().getApplicationContext().getSharedPreferences("myfile",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("temperature",String.valueOf(currentTemperatureField.getText()));
                    editor.putString("humidity", String.valueOf(humidity_field.getText()));
                    editor.putString("details",String.valueOf(detailsField.getText()));
                    editor.putString("pressure",String.valueOf(pressure_field.getText()));
                    editor.putString("speed",String.valueOf(wind_field.getText()));
                    editor.putString("icon",String.valueOf(weatherIcon.getText()));
                    editor.putString("city",String.valueOf(cityField.getText()));
                    editor.putString("updatedField",String.valueOf(updatedField.getText()));
                    editor.putString("sunrise",String.valueOf(sunrise.getText()));
                    editor.putString("sunset",String.valueOf(sunset.getText()));
                    editor.commit(); //Kayıt



                    //background değişiklikleri
                    Calendar calendar=Calendar.getInstance();
                    double timeOfday=calendar.get(Calendar.HOUR_OF_DAY);
                    System.out.println("timeOfday"+ timeOfday);

                    int temp=Math.round(Float.parseFloat(main.getString("temp")));
                    String sunrisetime=UnixTime(json.getJSONObject("sys").getLong("sunrise"));
                    String sunsettime=UnixTime(json.getJSONObject("sys").getLong("sunset"));

                    String dtne= UnixTime(json.getLong("dt"));
                    System.out.println(" dtne"+ dtne);

                    System.out.println("temp "+temp + " sunrise" + sunrise);

                    if(details.getString("main").equals("Clouds")){
                        if( timeOfday>=5.3 && timeOfday<12 ) {
                            //sabah bulutlu
                            relativeLayout.setBackgroundResource(R.drawable.cloudsmorning);
                        }
                        else if(timeOfday>=12 && timeOfday< 15){
                            relativeLayout.setBackgroundResource(R.drawable.cloudsmorning);
                        }
                        else if(timeOfday>=15 && timeOfday<19.2){
                            relativeLayout.setBackgroundResource(R.drawable.eveningclouds);
                        }
                        else{
                            //gece bulutlu
                            relativeLayout.setBackgroundResource(R.drawable.cloudsnight);
                        }
                    }
                    else if (details.getString("main").equals("Clear")){

                        if( timeOfday>=5 && timeOfday<6) {
                            relativeLayout.setBackgroundResource(R.drawable.sunrise1);
                        }
                        else if( timeOfday>=6 && timeOfday<12) {
                            relativeLayout.setBackgroundResource(R.drawable.clearmorning);
                        }
                        else if(timeOfday>=12 && timeOfday<17 )
                        {   if( temp>=38)
                            relativeLayout.setBackgroundResource(R.drawable.summersunny);
                            else{
                                relativeLayout.setBackgroundResource(R.drawable.clearmorning); }
                        }
                        else if(timeOfday>=17 && timeOfday<19) {
                            relativeLayout.setBackgroundResource(R.drawable.clearmorning);
                        }
                        else if(timeOfday>=19 && timeOfday<19.15){
                            relativeLayout.setBackgroundResource(R.drawable.sunsetback);
                        }
                        else
                            relativeLayout.setBackgroundResource(R.drawable.clearnight);
                    }
                    else if (details.getString("main").equals("Rain")){
                        if( timeOfday>=0&& timeOfday<12) {
                            relativeLayout.setBackgroundResource(R.drawable.morningrain);
                        }else if(timeOfday>=12 && timeOfday<18){
                            relativeLayout.setBackgroundResource(R.drawable.rainmorning);
                        }
                        else
                        relativeLayout.setBackgroundResource(R.drawable.rainynight);
                    }
                    else if(details.getString("main").equals("Mist") || details.getString("main").equals("Haze")||
                            details.getString("main").equals("Fog")){
                        if( timeOfday>=0&& timeOfday<19) {
                            relativeLayout.setBackgroundResource(R.drawable.mistmorning1);
                        }else {
                            relativeLayout.setBackgroundResource(R.drawable.mistnight);
                        }
                    }
                    else if(details.getString("main").equals("Snowy")){
                        if( timeOfday>=0 && timeOfday<19) {
                            relativeLayout.setBackgroundResource(R.drawable.snowmorning);
                        }else
                        relativeLayout.setBackgroundResource(R.drawable.snownight);
                    }
                    else if(details.getString("main").equals("Thunderstorm")){
                        if( timeOfday>=0&& timeOfday<19) {
                            relativeLayout.setBackgroundResource(R.drawable.thunderstormmorning);
                        }else
                            relativeLayout.setBackgroundResource(R.drawable.thunderstormnight);
                    }
                    else {
                        relativeLayout.setBackgroundResource(R.drawable.back);
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Error, Check City", Toast.LENGTH_SHORT).show();
            }


        }


    }
//sunrise ve sunset değerlerinin dönüşünmü
    private String UnixTime(long timex) {

        Date date = new Date(timex *1000L);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+3"));
        return sdf.format(date);
    }



}