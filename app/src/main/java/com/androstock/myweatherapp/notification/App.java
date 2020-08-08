package com.androstock.myweatherapp.notification;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;



public class App extends Application {
    public static final String CHANNEL_1_ID="channel1";
    @Override
    public void onCreate() {
        super.onCreate();
        creteNotificationChannels();
    }

    private void creteNotificationChannels() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel1=new NotificationChannel(CHANNEL_1_ID,"channel _1", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("This is channel1");
            NotificationManager maneger=getSystemService(NotificationManager.class);
            maneger.createNotificationChannel(channel1);
        }
    }
}
