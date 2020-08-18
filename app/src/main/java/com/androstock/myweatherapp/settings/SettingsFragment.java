package com.androstock.myweatherapp.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;


import android.widget.Toast;


import com.androstock.myweatherapp.R;
import com.androstock.myweatherapp.notification.NotificationReceiver;

public class SettingsFragment extends PreferenceFragment {
    public static final String Pref_temp="key_temp_unit";
    public static final String Pref_speed="speed_unitkey";
    public static final String Pref_pressure="pressureUnitkey";
    public static final String PREF_IS_NOTIFICATION_ENABLED="enable_notification";
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

       sharedPreferences=getActivity().getSharedPreferences("myfile",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        final SwitchPreference notificationSwitch = (SwitchPreference) findPreference(PREF_IS_NOTIFICATION_ENABLED);

        notificationSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if((boolean)newValue){
                    notificationSwitch.setChecked(false);
                    Toast.makeText(getActivity(),"enable", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"disable", Toast.LENGTH_SHORT).show();

                }
                System.out.println("newvalue"+newValue);
                savePrefs("new", (Boolean) newValue);
                return true;
            }
        });




        preferenceChangeListener=new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
            {
                if(key.equals(Pref_temp))
                {
                    Preference tempPref=findPreference(key);
                    tempPref.setSummary(sharedPreferences.getString(key,""));
                    String temp1=sharedPreferences.getString(key,"");
                    editor.putString(key,temp1);
                    editor.commit();
                    System.out.println("temp1 from setting :"+temp1);
                }
                if(key.equals(Pref_speed))
                {
                    Preference speedPref=findPreference(key);
                    speedPref.setSummary(sharedPreferences.getString(key,""));
                    editor.putString(key,sharedPreferences.getString(key,"")).commit();
                    System.out.println("speed from setting :"+sharedPreferences.getString(key,""));
                }
                if(key.equals(Pref_pressure))
                {
                    Preference presPref=findPreference(key);
                    presPref.setSummary(sharedPreferences.getString(key,""));
                    editor.putString(key,sharedPreferences.getString(key,"")).commit();
                    System.out.println("pressure from setting :"+sharedPreferences.getString(key,""));
                }
      
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        Preference  tempPref=findPreference(Pref_temp);
        tempPref.setSummary(getPreferenceScreen().getSharedPreferences().getString(Pref_temp,""));
        System.out.println("tempPref" + tempPref);
        Preference speedPref=findPreference(Pref_speed);
        speedPref.setSummary(getPreferenceScreen().getSharedPreferences().getString(Pref_speed,""));

        Preference pressPref=findPreference(Pref_pressure);
        pressPref.setSummary(getPreferenceScreen().getSharedPreferences().getString(Pref_pressure,""));


         android.preference.SwitchPreference preferenceSw= (SwitchPreference) findPreference(PREF_IS_NOTIFICATION_ENABLED);
         preferenceSw.setSummaryOff("Hava durumu uyarıları kapalı");
         preferenceSw.setSummaryOn("Hava durumu uyarıları açık ");




    }



    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }


    public void savePrefs(String key, Boolean value){
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("myfile",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    //get prefs
    private Boolean loadPrefs(String key, Boolean value){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("myfile",Context.MODE_PRIVATE);
        Boolean data = sharedPreferences.getBoolean(key, value);
        return data;
    }



}
