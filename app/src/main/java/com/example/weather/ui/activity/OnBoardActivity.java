package com.example.weather.ui.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;


import com.example.weather.R;
import com.example.weather.adapter.SliderAdapter;
import com.example.weather.databinding.ActivityOnBoardBinding;

public class OnBoardActivity extends AppCompatActivity {

    SliderAdapter sliderAdapter;
    ActivityOnBoardBinding binding;
    TextView[] dots;
    String prevStarted = "yes";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getItem(0) > 0) {
                    binding.viewPager.setCurrentItem(getItem(-1), true);
                }

            }
        });

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getItem(0) < 2) {
                    binding.viewPager.setCurrentItem(getItem(1), true);
                } else {
                    Intent intent = new Intent(OnBoardActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        binding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OnBoardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        sliderAdapter = new SliderAdapter(this);
        binding.viewPager.setAdapter(sliderAdapter);
        setUpIndicator(0);
        binding.viewPager.addOnPageChangeListener(changeListener);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setUpIndicator(int position) {
        dots = new TextView[3];
        binding.lineDots.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(36);

            dots[i].setTextColor(getResources().getColor(R.color.gray_700, getApplicationContext().getTheme()));
            binding.lineDots.addView(dots[i]);

        }
        dots[position].setTextColor(getResources().getColor(R.color.dark_blue, getApplicationContext().getTheme()));
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onPageSelected(int position) {
            setUpIndicator(position);
            if (position > 0) {
                binding.btnBack.setVisibility(View.VISIBLE);
            } else {
                binding.btnBack.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    };


    private int getItem(int i) {
        return binding.viewPager.getCurrentItem() + i;

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!sharedpreferences.getBoolean(prevStarted, false)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(prevStarted, Boolean.TRUE);
            editor.apply();
        } else {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }
}
