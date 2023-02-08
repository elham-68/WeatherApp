package com.example.weather.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesUtil {
  private static SharedPreferencesUtil instance = null;
  private static SharedPreferences preferences = null;
  public static final String CITY="city";
  public static final String DARK_THEME = "dark-theme";




  private SharedPreferencesUtil(Context context) {
    preferences = PreferenceManager.getDefaultSharedPreferences(context);
  }

  public static SharedPreferencesUtil getInstance(Context context) {

    if (instance == null) {
      instance = new SharedPreferencesUtil(context);
    }

    return instance;
  }
  public static void init(Context context){
    if (preferences == null)
      preferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
  }
  //////////////////////////////////////////////
  public static String read(String key, String defValue) {
    return preferences.getString(key, defValue);
  }

    public static void write(String key, String value) {
        SharedPreferences.Editor prefsEditor = preferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

  public boolean isDarkThemeEnabled() {
    return preferences.getBoolean(DARK_THEME, false);
  }


  public void setDarkThemeEnabled(boolean state) {
    preferences.edit().putBoolean(DARK_THEME, state).apply();
  }

}
