package myweatherApplication.example.MyWeatherApplication.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import myweatherApplication.example.MyWeatherApplication.adapter.DailyAdapter;
import myweatherApplication.example.MyWeatherApplication.adapter.TimesAdapter;
import myweatherApplication.example.MyWeatherApplication.model.CityInfo;
import myweatherApplication.example.MyWeatherApplication.model.ForecastDaily;
import myweatherApplication.example.MyWeatherApplication.model.TimesResponse;
import myweatherApplication.example.MyWeatherApplication.ui.fragment.AboutFragment;
import myweatherApplication.example.MyWeatherApplication.ui.fragment.DescriptionFragment;
import myweatherApplication.example.MyWeatherApplication.utils.AppUtils;
import myweatherApplication.example.MyWeatherApplication.utils.Constant;
import myweatherApplication.example.MyWeatherApplication.utils.SharedPreferencesUtil;
import myweatherApplication.example.MyWeatherApplication.utils.SnackbarUtil;

import com.bumptech.glide.Glide;
import com.example.myWeatherApplication.R;
import com.example.myWeatherApplication.databinding.ActivityMainBinding;
import com.github.pwittchen.prefser.library.rx2.Prefser;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements DailyAdapter.OnClickItemDay{

    List<TimesResponse> timesResponseList;
    private List<ForecastDaily> forecastDailyList;
    private DailyAdapter dailyAdapter;
    private TimesResponse timesResponse;
    TimesAdapter timesAdapter;
    private ActivityMainBinding binding;
    private Prefser prefser;
    private boolean isLoad = false;
    String city;
    Bundle bundle;
    private static final String TAG = MainActivity.class.getSimpleName();
    private CityInfo cityInfo;
    private boolean isDark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        initRecyclerView();
        initValues();
        initSearch();
        checkLastUpdate();

    }

    private void initRecyclerView(){
        timesResponseList = new ArrayList<>();
        timesAdapter = new TimesAdapter(MainActivity.this, timesResponseList);
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this,
                RecyclerView.HORIZONTAL, false);
        binding.mainLayout.recDay.setLayoutManager(manager);
        binding.mainLayout.recDay.setAdapter(timesAdapter);

        forecastDailyList = new ArrayList<>();
        dailyAdapter = new DailyAdapter(MainActivity.this, forecastDailyList,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL,false);
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
            public boolean onQueryTextSubmit(String query) {
                requestWeather(query);
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
                binding.toolbarLayout.txtCityName.clearComposingText();
                binding.toolbarLayout.searchView.showSearch();
            }
        });
    }

    private void checkLastUpdate() {
        cityInfo = prefser.get(Constant.CITY_INFO, CityInfo.class, null);
        if (cityInfo != null) {
            binding.toolbarLayout.txtCityName.setText(String.format("%s, %s", cityInfo.getName(), cityInfo.getCountry()));
            if (prefser.contains(Constant.LAST_STORED_CURRENT)) {
                long lastSotred = prefser.get(Constant.LAST_STORED_CURRENT, Long.class, 0L);
                if (AppUtils.isTimePass(lastSotred)) {
                    requestWeather(cityInfo.getName());
                }
            } else {
                requestWeather(cityInfo.getName());
            }
        } else {
            showEmptyLayout();

        }
    }

    private void showEmptyLayout() {
        Glide.with(MainActivity.this).load(R.drawable.backg).into(binding.contentEmptyLayout.noCityImageView);
        binding.contentEmptyLayout.emptyLayout.setVisibility(View.VISIBLE);
        binding.mainLayout.nestedScrollView.setVisibility(View.GONE);
    }

    private void hideEmptyLayout() {
        binding.contentEmptyLayout.emptyLayout.setVisibility(View.GONE);
        binding.mainLayout.nestedScrollView.setVisibility(View.VISIBLE);

    }


    private void initValues() {
        prefser = new Prefser(this);
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cityInfo = prefser.get(Constant.CITY_INFO, CityInfo.class, null);
                if (cityInfo != null) {
                    long lastStored = prefser.get(Constant.LAST_STORED_CURRENT, Long.class, 0L);
                    if (AppUtils.isTimePass(lastStored)) {
                        requestWeather(cityInfo.getName());
                    } else {
                        binding.swipeContainer.setRefreshing(false);
                    }
                } else {
                    binding.swipeContainer.setRefreshing(false);
                }
            }
        });
        binding.bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAboutFragment();
            }
        });


    }



    private void showAboutFragment() {
        AppUtils.showFragment(new AboutFragment(), getSupportFragmentManager(), true);

    }


    private void requestWeather(String cityName) {
        if (AppUtils.isNetworkConnected()) {
            getWeatherInfo(cityName);
        } else {
            SnackbarUtil
                    .with(binding.swipeContainer)
                    .setMessage(getString(R.string.no_internet))
                    .setDuration(SnackbarUtil.LENGTH_LONG)
                    .showError();
            binding.swipeContainer.setRefreshing(false);
        }
    }

    public void getWeatherInfo(String cityName) {
        String url = "http://api.weatherapi.com/v1/forecast.json?key=919c01d95cb341bebd770827221901&q=" + cityName + "&days=7&aqi=yes&alerts=no";
        binding.toolbarLayout.txtCityName.setText(cityName);

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest weatherInfo = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                Log.e("TAG", "success");
                timesResponseList.clear();
                forecastDailyList.clear();
                binding.swipeContainer.setRefreshing(false);
                hideEmptyLayout();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.toolbarLayout.searchView.getWindowToken(), 0);


                try {

                    JSONObject location = response.getJSONObject("location");
                    JSONObject current = response.getJSONObject("current");
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
                    for (int i=0 ; i<dforecastList.length() ; i++){
                        JSONObject dayObj = dforecastList.getJSONObject(i);
                        String maxTemp =dayObj.getJSONObject("day").getString("maxtemp_c");
                        String minTemp = dayObj.getJSONObject("day").getString("mintemp_c");
                        String date = dayObj.getString("date");
                        String dicon = dayObj.getJSONObject("day").getJSONObject("condition").getString("icon");

                        forecastDailyList.add(new ForecastDaily(maxTemp,minTemp,date,dicon,cityName,temp));
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



                 /*  if (isLoad){
                       binding.mainLayout.txtDescription.setText(AppUtils.getWeatherStatus(timesResponse.getWeatherId(),AppUtils.isRTL(MainActivity.this)));
                   }else{
                       binding.mainLayout.txtDescription.setText(AppUtils.getWeatherStatus(timesResponse.getWeatherId(), AppUtils.isRTL(MainActivity.this)));
                   }*/
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
            }
        });
        requestQueue.add(weatherInfo);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        binding.toolbarLayout.searchView.setMenuItem(item);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (binding.toolbarLayout.searchView.isSearchOpen()) {
            binding.toolbarLayout.searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public void OnClick(String date, String temp, String icon,String city) {
        DescriptionFragment descriptionFragment = new DescriptionFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("date",date);
        bundle.putString("temp",temp);
        bundle.putString("icon",icon);
        bundle.putString("city",city);
        descriptionFragment.setArguments(bundle);
        transaction.replace(R.id.fragment_container,descriptionFragment).commit();
    }
}