package com.example.weather.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.bumptech.glide.Glide;
import com.example.weather.R;
import com.example.weather.adapter.DailyAdapter;
import com.example.weather.adapter.TimesAdapter;
import com.example.weather.databinding.ActivityMainBinding;
import com.example.weather.dialog.DialogManagement;
import com.example.weather.model.ForecastDaily;
import com.example.weather.model.TimesResponse;
import com.example.weather.ui.fragment.AboutFragment;
import com.example.weather.ui.fragment.DescriptionFragment;
import com.example.weather.utils.AppUtils;
import com.example.weather.utils.SharedPreferencesUtil;
import com.example.weather.utils.SnackbarUtil;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements DailyAdapter.ItemClickListener {

    List<TimesResponse> timesResponseList;
    private List<ForecastDaily> forecastDailyList;
    private DailyAdapter dailyAdapter;
    TimesAdapter timesAdapter;
    private ActivityMainBinding binding;
    private static final String TAG = MainActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefEditor;
    String city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("didShowTarget", Context.MODE_PRIVATE);


        SpannableStringBuilder wordtoSpan = new SpannableStringBuilder();
        binding.toolbarLayout.txtCityName.setText(wordtoSpan, TextView.BufferType.SPANNABLE);


        initSearch();
        initRecyclerView();
        targetView();

        binding.bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAboutFragment();
            }
        });

    }


    private void targetView() {
        if (!sharedPreferences.getBoolean("didShowTarget", false)) {
            showEmptyLayout();
            TapTargetView.showFor(this
                    , TapTarget.forView(binding.toolbarLayout.txtCityName, "Search city", "click here and type English city name")
                            .outerCircleColor(R.color.dark_blue)
                            .outerCircleAlpha(0.96f)
                            .targetCircleColor(R.color.white)
                            .titleTextSize(24)
                            .titleTextColor(R.color.white)
                            .descriptionTextSize(18)
                            .descriptionTextColor(R.color.yellow)
                            .textColor(R.color.white)
                            .textTypeface(Typeface.SANS_SERIF)
                            .dimColor(R.color.black)
                            .drawShadow(true)
                            .tintTarget(true)
                            .cancelable(false)
                            .transparentTarget(false)
                            .targetRadius(60), new TapTargetView.Listener() {
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);
                            binding.toolbarLayout.searchView.showSearch();
                            prefEditor = sharedPreferences.edit();
                            prefEditor.putBoolean("didShowTarget", true);
                            prefEditor.apply();
                        }
                    });
        }
    }


    private void initRecyclerView() {
        timesResponseList = new ArrayList<>();
        timesAdapter = new TimesAdapter(MainActivity.this, timesResponseList);
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this,
                RecyclerView.HORIZONTAL, false);
        binding.mainLayout.recDay.setLayoutManager(manager);
        binding.mainLayout.recDay.setAdapter(timesAdapter);

        forecastDailyList = new ArrayList<>();
        dailyAdapter = new DailyAdapter(MainActivity.this, forecastDailyList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false);
        binding.mainLayout.recNextday.setLayoutManager(linearLayoutManager);
        binding.mainLayout.recNextday.setAdapter(dailyAdapter);


    }

    private void initSearch() {
        binding.toolbarLayout.searchView.setVoiceSearch(false);
        binding.toolbarLayout.searchView.setHint("Search");
        binding.toolbarLayout.searchView.setCursorDrawable(R.drawable.custom_curosr);
        binding.toolbarLayout.searchView.setEllipsize(true);
        binding.toolbarLayout.searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String cityName) {

                requestWeather(cityName);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }

        });
        binding.toolbarLayout.searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.toolbarLayout.searchView.showSearch();
                city = binding.toolbarLayout.txtCityName.getText().toString();
                SharedPreferencesUtil.write(SharedPreferencesUtil.CITY, city);

            }
        });
    }

    private void showEmptyLayout() {
        Glide.with(MainActivity.this).load(R.drawable.rainbg).into(binding.contentEmptyLayout.noCityImageView);
        binding.contentEmptyLayout.emptyLayout.setVisibility(View.VISIBLE);
        binding.mainLayout.nestedScrollView.setVisibility(View.GONE);
    }

    private void hideEmptyLayout() {
        binding.contentEmptyLayout.emptyLayout.setVisibility(View.GONE);
        binding.mainLayout.nestedScrollView.setVisibility(View.VISIBLE);

    }

    private void closeKeyboard() {
        View focusView = this.getCurrentFocus();
        if (focusView != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
        }
    }

    private void showAboutFragment() {
        AppUtils.showFragment(new AboutFragment(), getSupportFragmentManager(), true);

    }

    @SuppressLint("MissingPermission")
    private void requestWeather(String cityName) {
        if (AppUtils.isNetworkConnected()) {
            getWeatherInfo(cityName);
        } else {
            SnackbarUtil.with(binding.swipeContainer)
                    .setMessage("Error Internet Connection")
                    .setDuration(SnackbarUtil.LENGTH_SHORT).showError();
            binding.swipeContainer.setRefreshing(false);
        }
    }


    public void getWeatherInfo(String cityNames) {
        String url = "http://api.weatherapi.com/v1/forecast.json?key=9571c9737e104307aff62530222410&q=" + cityNames + "&days=7&aqi=yes&alerts=no";
        binding.toolbarLayout.txtCityName.setText(cityNames);

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest weatherInfo = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "success");
                timesResponseList.clear();
                forecastDailyList.clear();
                binding.swipeContainer.setRefreshing(false);
                hideEmptyLayout();
                closeKeyboard();

                try {
                    JSONObject location = response.getJSONObject("location");
                    JSONObject current = response.getJSONObject("current");
                    String name = location.getString("name");
                    String description = current.getJSONObject("condition").getString("text");
                    binding.mainLayout.txtDescription.setText(description);
                    String icon = current.getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(icon)).into(binding.mainLayout.weather);

                    double temp = current.getDouble("temp_c");
                    binding.mainLayout.txtTemp.setText(temp + "°C");

                    String country = location.getString("country");
                    binding.mainLayout.txtCountryName.setText(country);

                    JSONObject forecast = response.getJSONObject("forecast");
                    JSONObject forecastList = forecast.getJSONArray("forecastday").getJSONObject(0);
                    JSONObject day = forecastList.getJSONObject("day");
                    JSONObject astro = forecastList.getJSONObject("astro");
                    String sunrise = astro.getString("sunrise");

                    binding.mainLayout.txtSunrise.setText(sunrise);

                    String sunset = astro.getString("sunset");
                    binding.mainLayout.txtSuset.setText(sunset);

                    String localtime = location.getString("localtime");
                    binding.mainLayout.txtTimeTitle.setText(localtime);
                    JSONObject forecastLists = forecast.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArr = forecastLists.getJSONArray("hour");
                    for (int i = 0; i < hourArr.length(); i++) {
                        JSONObject hourObj = hourArr.getJSONObject(i);
                        double temps = hourObj.getDouble("temp_c");
                        String time = hourObj.getString("time");
                        String icons = hourObj.getJSONObject("condition").getString("icon");
                        String rain = hourObj.getString("chance_of_rain");

                        timesResponseList.add(new TimesResponse(temps, time, icons, rain));

                    }
                    timesAdapter.notifyDataSetChanged();


                    JSONArray dforecastList = forecast.getJSONArray("forecastday");
                    for (int i = 0; i < dforecastList.length(); i++) {
                        JSONObject dayObj = dforecastList.getJSONObject(i);
                        String maxTemp = dayObj.getJSONObject("day").getString("maxtemp_c");
                        String minTemp = dayObj.getJSONObject("day").getString("mintemp_c");
                        String date = dayObj.getString("date");
                        String dicon = dayObj.getJSONObject("day").getJSONObject("condition").getString("icon");

                        forecastDailyList.add(new ForecastDaily(maxTemp, minTemp, date, dicon, name, temp));
                    }
                    dailyAdapter.notifyDataSetChanged();

                    JSONObject currents = response.getJSONObject("current");
                    JSONObject air_quality = currents.getJSONObject("air_quality");
                    double co = air_quality.getDouble("co");
                    binding.mainLayout.progressCo.setProgress((int) co);
                    binding.mainLayout.progressCo.setMax(400);
                    double no2 = air_quality.getDouble("no2");
                    binding.mainLayout.progressNo2.setProgress((int) no2);
                    binding.mainLayout.progressNo2.setMax(400);
                    double o3 = air_quality.getDouble("o3");
                    binding.mainLayout.progressO3.setProgress((int) o3);
                    binding.mainLayout.progressO3.setMax(400);
                    double so2 = air_quality.getDouble("so2");
                    binding.mainLayout.progressSo2.setProgress((int) so2);
                    binding.mainLayout.progressSo2.setMax(400);
                    double pm2_5 = air_quality.getDouble("pm2_5");
                    binding.mainLayout.progressPm25.setProgress((int) pm2_5);
                    binding.mainLayout.progressPm25.setMax(400);
                    double pm10 = air_quality.getDouble("pm10");
                    binding.mainLayout.progressPm10.setProgress((int) pm10);
                    binding.mainLayout.progressPm10.setMax(400);


                } catch (JSONException e) {
                    Log.e(TAG, "onCreateView", e);

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "error");
                binding.swipeContainer.setRefreshing(false);
                showEmptyLayout();

            }
        });
        requestQueue.add(weatherInfo);
    }


    @Override
    public void onItemClick(View view, int position, String citys) {
        DescriptionFragment descriptionFragment = new DescriptionFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("id", String.valueOf(position));
        bundle.putString("city", citys);
        descriptionFragment.setArguments(bundle);
        transaction.replace(R.id.fragment_container, descriptionFragment).commit();
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        DialogManagement.exitBackPressed(MainActivity.this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        String cityname = SharedPreferencesUtil.read(SharedPreferencesUtil.CITY, "");
        getWeatherInfo(cityname);
    }

    @Override
    protected void onPause() {
        super.onPause();
        String citynames = binding.toolbarLayout.txtCityName.getText().toString();
        SharedPreferencesUtil.write(SharedPreferencesUtil.CITY, citynames);
        getWeatherInfo(citynames);


    }
}



