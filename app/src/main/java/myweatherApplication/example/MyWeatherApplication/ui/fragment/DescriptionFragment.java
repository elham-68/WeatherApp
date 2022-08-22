package myweatherApplication.example.MyWeatherApplication.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myWeatherApplication.databinding.FragmentDescriptionBinding;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import myweatherApplication.example.MyWeatherApplication.adapter.DescriptionAdapter;
import myweatherApplication.example.MyWeatherApplication.model.ForecastDaily;
import myweatherApplication.example.MyWeatherApplication.model.ResponseDescription;
import myweatherApplication.example.MyWeatherApplication.model.TimesResponse;
import myweatherApplication.example.MyWeatherApplication.utils.AppUtils;


public class DescriptionFragment extends DialogFragment {

    private String cityName, date, icon,temp;
    String responseDescriptionLists;
    String timesResponseList;
    List<ResponseDescription> responseDescriptionList;
    List<ForecastDaily> forecastDailyList;
    FragmentDescriptionBinding binding;
    DescriptionAdapter descriptionAdapter;
    private Typeface typeface;
    private ResponseDescription responseDescriptions;
    List<String> time;



    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDescriptionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

       // timesResponseLists = new ArrayList<>();
        initValues(view);
        getDescription(cityName);
        initRecyclerView();

        binding.fabClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }

    public void initRecyclerView() {
        responseDescriptionList = new ArrayList<>();
        forecastDailyList = new ArrayList<>();
        descriptionAdapter = new DescriptionAdapter(getContext(), forecastDailyList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),
                RecyclerView.HORIZONTAL, false);
        binding.recDescription.setLayoutManager(manager);
        binding.recDescription.setItemAnimator(new DefaultItemAnimator());
        binding.recDescription.setAdapter(descriptionAdapter);
    }

    public void initValues(View view) {
        Bundle bundle =getArguments();
        timesResponseList = bundle.getString("list");
        icon = bundle.getString("icon");
        Picasso.get().load("http://".concat(icon)).into(binding.animationView);
        temp = bundle.getString("temp");
        binding.tempTextView.setText(temp + "");
        cityName = bundle.getString("city");
        responseDescriptionLists = bundle.getString("list");
        date = bundle.getString("date");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE");
        try {
            Date dt = sdf.parse(date);
            String output = sdf1.format(dt);
            binding.dayNameTextView.setText(output);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        }

    public void getDescription(String city) {
        String url = "http://api.weatherapi.com/v1/forecast.json?key=919c01d95cb341bebd770827221901&q=" + city + "&days=7&aqi=yes&alerts=no";
        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        JsonObjectRequest descriptionInfo = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONObject response) {
               responseDescriptionList.clear();
               // timesResponseList.clear();


                try {
                  JSONObject forecasts = response.getJSONObject("forecast");
                    JSONObject forecastLists = forecasts.getJSONArray("forecastday").getJSONObject(0);
                    JSONObject day =forecastLists.getJSONObject("day");
                    String minTemp = day.getString("mintemp_c");
                    String maxTemp = day.getString("maxtemp_c");
                    JSONArray hourArray = forecastLists.getJSONArray("hour");
                    for (int i = 0; i < hourArray.length(); i++) {
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        double temps = hourObj.getDouble("temp_c");
                        String time = hourObj.getString("time");
                        String icons = hourObj.getJSONObject("condition").getString("icon");
                        forecastDailyList.add(new ForecastDaily(minTemp,maxTemp,time,icons,city,temps));

                    }
                    descriptionAdapter.notifyDataSetChanged();
                    setChart(forecastDailyList);

                 /*   JSONObject forecast = response.getJSONObject("forecast");
                    JSONArray dforecastList = forecast.getJSONArray("forecastday");
                    for (int i=0 ; i<dforecastList.length() ; i++) {
                        JSONObject dayObj = dforecastList.getJSONObject(i);

                        String date = dayObj.getString("date");
                        String dicon = dayObj.getJSONObject("day").getJSONObject("condition").getString("icon");

                        responseDescriptionList.add(new ResponseDescription(date,temp,dicon));
                    }*/


                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(descriptionInfo);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
        return dialog;
    }

   private void setChart(@NonNull List<ForecastDaily> forecastDailyList) {
           List<Entry> entries = new ArrayList<>();
           int i =0;
       if (AppUtils.isRTL(requireContext())) {
           int j = responseDescriptionList.size() - 1;
           while (j >= 0) {
               entries.add(new Entry(i, (float) responseDescriptionList.get(j).getTemp()));
               i++;
               j--;
           }
       } else {
           for (ForecastDaily forecastDaily : forecastDailyList) {
               entries.add(new Entry(i, (float) forecastDaily.getTemp()));
               i++;
           }
       }
       LineDataSet dataSet = new LineDataSet(entries, "lable");
       dataSet.setLineWidth(4f);
       dataSet.setCircleRadius(3f);
       dataSet.setHighlightEnabled(false);
       dataSet.setCircleColor(Color.parseColor("#266EAA"));
       dataSet.setValueTextSize(10);
       dataSet.setValueTextColor(Color.parseColor("#266EAA"));
       dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
       dataSet.setValueTypeface(typeface);
       dataSet.setValueFormatter(new ValueFormatter() {
           @Override
           public String getFormattedValue(float value) {
               return String.format(Locale.getDefault(), "%.0f", value);

           }
       });
       LineData lineData = new LineData(dataSet);
       binding.chart.getDescription().setEnabled(false);
       binding.chart.getAxisLeft().setDrawLabels(false);
       binding.chart.getAxisRight().setDrawLabels(false);
       binding.chart.getXAxis().setDrawLabels(false);
       binding.chart.getLegend().setEnabled(false);
       binding.chart.getXAxis().setDrawGridLines(false);
       binding.chart.getAxisLeft().setDrawGridLines(false);
       binding.chart.getAxisRight().setDrawGridLines(false);
       binding.chart.getAxisLeft().setDrawAxisLine(false);
       binding.chart.getAxisRight().setDrawAxisLine(false);
       binding.chart.getXAxis().setDrawAxisLine(false);
       binding.chart.setScaleEnabled(false);
       binding.chart.setData(lineData);
       binding.chart.animateY(500);
   }

   }
