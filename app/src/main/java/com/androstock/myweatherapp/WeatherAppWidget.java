package com.androstock.myweatherapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.androstock.myweatherapp.Service.WeatherPOJO;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.androstock.myweatherapp.Service.Constant.ROOT_IMAGE;

/**
 * Implementation of App Widget functionality.
 */
public class WeatherAppWidget extends AppWidgetProvider {
    private static String CityName;
    static SharedPreferences sharedPreferences;

    static Context con;
    static WeatherPOJO Hava_veri = new WeatherPOJO();
    static RemoteViews views;
    public static String ButonaTıklama="com.androstock.myweatherapp.WeatherAppWidget.ButonaTıklama";
    static String OPEN_WEATHER_MAP_API="a7bf7effe24aa1c27ca941a5b625ea52";

    protected static PendingIntent getPendingSelgIntent(Context context,String action){
    Intent intent=new Intent(context,WeatherAppWidget.class) ;
    intent .setAction(action);
    return PendingIntent.getBroadcast(context,0,intent,0);

}
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        // Construct the RemoteViews object
        views=new RemoteViews(context.getPackageName(), R.layout.weather_app_widget);
        views.setOnClickPendingIntent(R.id.refresh_button,getPendingSelgIntent(context,ButonaTıklama));
        taskLoadUp("Gaziantep,TR");

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(ButonaTıklama.equals(intent.getAction())){

            taskLoadUp("Gaziantep,TR");
            AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
            RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.weather_app_widget);
            setText(views,context);
            ComponentName widgetGuncelle=new ComponentName(context,WeatherAppWidget.class);
            appWidgetManager.updateAppWidget(widgetGuncelle,views);
        }

    }
////refresh
    private static void setText(RemoteViews views, Context context) {
        if(Hava_veri.getDescription()!=null){
            int sayaç;
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            con=context;
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void taskLoadUp(String query) {
        SharedPreferences sharedPref=con.getSharedPreferences("myfile", MODE_PRIVATE);
        String Date_Widget=sharedPref.getString("updatedField","kayıt yok");
        views.setTextViewText(R.id.date_widget,Date_Widget);
        String City_Widget=sharedPref.getString("city","kayıt yok");
        views.setTextViewText(R.id.city_widget,City_Widget);
        String Current_temperature=sharedPref.getString("temperature", "Kayıt Yok");
        views.setTextViewText(R.id.current_temp, Current_temperature);

       String description_widget=sharedPref.getString("details","Kayıt Yok");
       views.setTextViewText(R.id.widget_description,description_widget);
       String wind_widget=sharedPref.getString("speed","Kayıt Yok");
       views.setTextViewText(R.id.widget_wind,wind_widget);
       String icon=sharedPref.getString("icon", "Kayıt Yok");
        views.setTextViewText(R.id.widget_icon,icon);

     /* Glide.with(con).load(ROOT_IMAGE + sharedPref.getString("icon","Kayıt yok") + ".png")
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(R.id.widget_icon);*/
        if(Current_temperature.equalsIgnoreCase("Kayıt Yok")){
            Intent ıntent = new Intent(con,MainActivity.class);
            con.startActivity(ıntent);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant fun
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @RequiresApi(api=Build.VERSION_CODES.CUPCAKE)
    static class DownloadWeather extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            String xml=Function.excuteGet("http://api.openweathermap.org/data/2.5/weather?q=" + args[0] +"&units=metric&appid=" + OPEN_WEATHER_MAP_API + "&lang=tr");
            return xml;
        }
        @Override
        protected void onPostExecute(String xml) {
            try {
                JSONObject json=new JSONObject(xml);
                if (json != null) {
                    JSONObject details=json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main=json.getJSONObject("main");
                    DateFormat df=DateFormat.getDateTimeInstance();
                    views.setTextViewText(R.id.date_widget, "2.02.2019");
                }
            } catch (JSONException e) {
                Toast.makeText(con, "Error, Check City", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

