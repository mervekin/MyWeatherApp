package com.androstock.myweatherapp.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.androstock.myweatherapp.Function;
import com.androstock.myweatherapp.MainActivity;
import com.androstock.myweatherapp.R;
import com.androstock.myweatherapp.Service.WeatherPOJO;

/**
 * Created by TOSHIBA on 06.08.2019.
 */
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.androstock.myweatherapp.notification.App.CHANNEL_1_ID;

public class NotificationReceiver extends BroadcastReceiver {
    String OPEN_WEATHER_MAP_API = "a7bf7effe24aa1c27ca941a5b625ea52";
     RequestQueue requestQueue;
    public NotificationManager notificationManager;

    WeatherPOJO notification_data = new WeatherPOJO();
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        requestQueue = Volley.newRequestQueue(context);

        SharedPreferences sharedPref=context.getSharedPreferences("myfile", MODE_PRIVATE);
            Log.e("bildirim", "else e Girdi");
        String City_Notif=sharedPref.getString("city","kayıt yok");
            taskLoadUp(City_Notif,context);



    }public void taskLoadUp(String query,Context context) {
        if (Function.isNetworkAvailable(context)) {
            DownloadWeather task = new DownloadWeather();
            task.execute(query);
        } else {



        }
    }

    @RequiresApi(api=Build.VERSION_CODES.CUPCAKE)
    class DownloadWeather extends AsyncTask< String, Void, String > {
        private String description;
        private String temperature;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }
        protected String doInBackground(String...args) {
            String xml = Function.excuteGet("http://api.openweathermap.org/data/2.5/weather?q=" + args[0] +
                    "&units=metric&appid=" + OPEN_WEATHER_MAP_API+ "&lang=tr");
            return xml;
        }
        @Override
        protected void onPostExecute(String xml) {


            try {
                JSONObject json = new JSONObject(xml);
                if (json != null) {
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();
                    notification_data.setCity(json.getString("name").toUpperCase(Locale.getDefault()) + ", " + json.getJSONObject("sys").getString("country"));
                   notification_data.setDescription(details.getString("description").toUpperCase(Locale.getDefault()));
                    notification_data.setTemp(String.format("%.2f", main.getDouble("temp")) + "°");
                    notification_data.setCity(json.getString("name").toUpperCase(Locale.getDefault()) + ", " + json.getJSONObject("sys").getString("country"));
                    String mesaj = notification_data.getTemp()+"    "+notification_data.getDescription();
                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                        Intent repeating_intent = new Intent(context, MainActivity.class);

                        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setContentIntent(pendingIntent2)
                                .setSmallIcon(android.R.drawable.arrow_up_float)
                                .setContentTitle(notification_data.getCity())
                                .setContentText(mesaj)
                                .setAutoCancel(true);

                        manager.notify(100, builder.build());
                    }
                    else {

                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                        Notification notification = new NotificationCompat
                                .Builder(context,CHANNEL_1_ID)
                                .setSmallIcon(R.drawable.ic_my_icon)
                                .setContentTitle(notification_data.getCity())
                                .setContentText(mesaj)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory(NotificationCompat.CATEGORY_MESSAGE).build();

                         notificationManagerCompat.notify(1,notification);

                    }
                    Log.e("bildirim", "if e girdi");
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);



Date date= new  Date();
if(date.getHours()>=12&&date.getHours()<21) {
    Calendar calendar=Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 21);
    calendar.set(Calendar.MINUTE, 00);
    calendar.set(Calendar.SECOND, 00);

    Intent ıntent=new Intent(context, NotificationReceiver.class);

    PendingIntent pendingIntent=PendingIntent.getBroadcast(context, 100, ıntent, PendingIntent.FLAG_UPDATE_CURRENT);

    AlarmManager alarmManager=(AlarmManager) context.getSystemService(ALARM_SERVICE);
    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
}else  if(date.getHours()>=21&&date.getHours()<23){
    Calendar calendar=Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 55);

    Intent ıntent=new Intent(context, NotificationReceiver.class);

    PendingIntent pendingIntent=PendingIntent.getBroadcast(context, 100, ıntent, PendingIntent.FLAG_UPDATE_CURRENT);

    AlarmManager alarmManager=(AlarmManager) context.getSystemService(ALARM_SERVICE);
    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
  }

   }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }}
}
