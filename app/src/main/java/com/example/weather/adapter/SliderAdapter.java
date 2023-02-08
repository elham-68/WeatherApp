package com.example.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.weather.R;


public class SliderAdapter extends PagerAdapter{

    Context context;

    int[] images ={
            R.drawable.weather,R.drawable.qualitys,R.drawable.rainy
    };
    int[] title = {
            R.string.first_title,R.string.second_title,R.string.third_title
    };
    int[] description = {
            R.string.first_desc,R.string.second_desc,R.string.third_desc
    };

    public SliderAdapter(Context context){
        this.context=context;

    }
    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_slider,container,false);

        ImageView imageSlider = (ImageView) view.findViewById(R.id.img_slider);
        TextView txtSlider = (TextView) view.findViewById(R.id.title_slider);
        TextView descSlider = (TextView) view.findViewById(R.id.desc_slider);

        imageSlider.setImageResource(images[position]);
        txtSlider.setText(title[position]);
        descSlider.setText(description[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}

