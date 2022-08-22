package myweatherApplication.example.MyWeatherApplication.daily;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myWeatherApplication.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DailyActivity extends AppCompatActivity {


   ArrayList<DailyResponse> dailyResponseArrayList;
   private TextView txt_city_daily,txt_country_daily,txt_temp_daily,txt_description_daily,txt_time_daily;
   private LottieAnimationView weather_daily;
   private ImageView img_back;
   RecyclerView rec_daily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_daily);
        txt_city_daily = findViewById(R.id.txt_city_daily);
        txt_country_daily = findViewById(R.id.txt_country_daily);
        txt_temp_daily = findViewById(R.id.txt_temp_daily);
        txt_description_daily = findViewById(R.id.txt_description_daily);
        txt_time_daily = findViewById(R.id.txt_time_daily);
        weather_daily = findViewById(R.id.weather_daily);
        img_back = findViewById(R.id.img_back);
        rec_daily=findViewById(R.id.rec_daily);


        Intent i=getIntent();

        String name=i.getStringExtra("cityName");
        txt_city_daily.setText(name);
        String temp=i.getStringExtra("cityTemp");
        txt_temp_daily.setText(temp);
        String time=i.getStringExtra("cityTime");
        txt_time_daily.setText(time);
        String country=i.getStringExtra("country");
        txt_country_daily.setText(country);
        String description=i.getStringExtra("description");
        txt_description_daily.setText(description);
        String weatherImg=i.getStringExtra("weatherImg");


        getDailyWeather(name);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void getDailyWeather(String cityNameDaily){
        String url="http://api.openweathermap.org/data/2.5/forecast?q="+cityNameDaily+"&appid=0bffc4f4972106579d4a8587cd37b49f";

        RequestQueue queue= Volley.newRequestQueue(this);
        JsonObjectRequest dailyInfo = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONObject response) {


                try {



                    //icon
                    JSONObject list = response.getJSONArray("list").getJSONObject(0);
                    JSONObject weathers = list.getJSONArray("weather").getJSONObject(0);
                    String icon = weathers.getString("icon");
                    Picasso.get().load("http:".concat(icon)).into(weather_daily);



                    JSONArray listArray = response.getJSONArray("list");
                    for (int i = 0; i < listArray.length(); i++) {
                        JSONObject hourObj = listArray.getJSONObject(i);
                        String dt_txt = hourObj.getString("time");
                        String temp = hourObj.getJSONObject("main").getString("temp_c");
                        String icons = hourObj.getJSONObject("weather").getString("icon");
                        String description = hourObj.getJSONObject("weather").getString("decription");

                       // dailyResponseArrayList.add(new DailyResponse(temp,icons,cityNameDaily,description,dt_txt));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(dailyInfo);
    }



}