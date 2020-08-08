package com.androstock.myweatherapp.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.androstock.myweatherapp.MainActivity;
import com.androstock.myweatherapp.R;
import com.androstock.myweatherapp.location.LocationActivity;

public class SplashScreen extends AppCompatActivity {
    private boolean mFirstUse=false;
    private static final String FIRST_TIME="first_time";
    private ImageView img;
    private static int GECIS_SURESI=4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);

        img=findViewById(R.id.imagelogo);

        Animation animation= AnimationUtils.loadAnimation(this,R.anim.animation);
        img.startAnimation(animation);
        //Geçiş

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!FirstUse()) {

                    Intent intentToLocation = new Intent(SplashScreen.this, LocationActivity.class);
                    startActivity(intentToLocation);
                    finish();
                   markAppUsed();
                }else
                {
                    Intent intentToMain=new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(intentToMain);
                    finish();
                }


            }
        },GECIS_SURESI);
    }
     private boolean FirstUse() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mFirstUse = sharedPreferences.getBoolean(FIRST_TIME, false);
        return mFirstUse;
    }

    private void markAppUsed() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mFirstUse = true;
        sharedPreferences.edit().putBoolean(FIRST_TIME, mFirstUse).apply();
    }
}