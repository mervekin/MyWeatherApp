package com.androstock.myweatherapp.settings;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.androstock.myweatherapp.MainActivity;
import com.androstock.myweatherapp.R;
import com.androstock.myweatherapp.location.LocationActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingActivity extends PreferenceActivity {
    //boolean notificationState;
   //   SharedPreferences sharedPreferences;
   // SharedPreferences.OnSharedPreferenceChangeListener listener;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        //addPreferencesFromResource(R.xml.prefs);

      LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        root.addView(toolbar, 0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);*/

       /* listener= new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                notificationState = sharedPreferences.getBoolean("bildirim_kontrol", true);

            }
        };*/

       //Load setting fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new MypreferenceFragment()).commit();
    }

    public static class MypreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);
//            setHasOptionsMenu(true)
           /* bindSummaryValue(findPreference("key_unit"));
            bindSummaryValue(findPreference("speedUnit"));
            bindSummaryValue(findPreference("pressureUnit"));
            bindSummaryValue(findPreference("enable_notificaiton"));*/
        }
    }

  /* private static void bindSummaryValue(@NonNull Preference preference){
       preference.setOnPreferenceChangeListener(listener);
       listener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey()," "));

}*/

    private static Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String stringValue = newValue.toString();
                if (preference instanceof ListPreference) {
                    ListPreference listPreference = (ListPreference) preference;
                    int index = listPreference.findIndexOfValue(stringValue);
                    preference.setSummary(index > 0 ? listPreference.getEntries()[index]
                            : null);

                } else if (preference instanceof RingtonePreference) {
                    if (TextUtils.isEmpty(stringValue)) {
                        // no ringtone
                        preference.setSummary("Silent");
                    } else {
                        Ringtone ringtone = RingtoneManager.getRingtone(preference.getContext(),
                                Uri.parse(stringValue));

                        if (ringtone == null) {
                            //clear the summary
                            preference.setSummary("notificationringtone seçiniz");
                        } else {
                            String name = ringtone.getTitle(preference.getContext());
                            preference.setSummary(name);
                        }
                    }

                }
                return true;
            }
        };

    }
/*
  public void onResume() {

      super.onResume();
      getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener((SharedPreferences.OnSharedPreferenceChangeListener) this);

      setListPreferenceSummary("unit");
      setListPreferenceSummary("lengthUnit");
      setListPreferenceSummary("speedUnit");
      setListPreferenceSummary("pressureUnit");

  }
  public void onPause() {

      super.onPause();
      getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener((SharedPreferences.OnSharedPreferenceChangeListener) this);
  }

  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key){
      switch(key){
          case "unit":
          case "speedUnit":
          case "pressureUnit":
              setListPreferenceSummary(key);
              break;

      }
  }

    private void setListPreferenceSummary(String preferenceKey) {
      ListPreference preference= (ListPreference) findPreference(preferenceKey);
      preference.setSummary(preference.getEntry());

    }*/

