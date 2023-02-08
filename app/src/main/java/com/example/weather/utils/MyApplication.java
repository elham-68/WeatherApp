package com.example.weather.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;


import com.example.weather.R;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.objectbox.BoxStore;



public class MyApplication extends Application {



  @Override
  public void onCreate() {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    super.onCreate();


    ViewPump.init(ViewPump.builder()
        .addInterceptor(new CalligraphyInterceptor(
            new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Vazir.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()))
        .build());

    if (SharedPreferencesUtil.getInstance(this).isDarkThemeEnabled())
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    else
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);



  }








}
