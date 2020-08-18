package com.androstock.myweatherapp.settings;

import android.content.SharedPreferences;

public class UnitConvertor {
    public static float convertTemperature(float temp, String unit){
        if(unit.equals("°F")){
            return UnitConvertor.CelsiustoFahrenheit(temp);
        }
        else if(unit.equals("K")){
            return UnitConvertor.CelsiusTokelvin(temp);
        }
        else{
            return temp;
        }

    }


   private static float CelsiustoFahrenheit(float temp){
        return ((temp*9)/5)+32;
   }
    private static float CelsiusTokelvin(float temp) {
        return temp+73.15f;
    }






    public static float convertPressure(float pressure, String presUnit) {
        if (presUnit.equals("kPa")) {
            return pressure / 10;
        } else if (presUnit.equals("mm Hg")) {
            return (float) (pressure * 0.750061561303);
        } else if (presUnit.equals("in Hg")) {
            return (float) (pressure * 0.0295299830714);
        } else {
            return pressure;
        }
    }


    public static double convertWind(double wind,String speedUnit){
        if(speedUnit.equals("kph")){
            return wind*3.6;
        }
        else if(speedUnit.equals("mph")){
            return wind * 2.23693629205;
        }
        else{
            return wind;
        }
    }



}
