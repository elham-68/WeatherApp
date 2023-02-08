package com.example.weather.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.model.TimesResponse;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public  class TimesAdapter extends RecyclerView.Adapter<TimesAdapter.TimeVH>{

    Context context;
    List<TimesResponse> timesResponseList;

    public TimesAdapter(Context context,List<TimesResponse> timesResponseList){
        this.context=context;
        this.timesResponseList=timesResponseList;
    }
    @NonNull
    @Override
    public TimeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_time,parent,false);
        return new TimeVH(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull TimeVH holder, int position) {
        TimesResponse itemTimes = timesResponseList.get(position);
        holder.txt_temp.setText(itemTimes.getTemp()+"°C");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        try{
            Date parseDate = sdf.parse(itemTimes.getTime());
            assert parseDate != null;
            String output = sdf1.format(parseDate);
            holder.txt_time.setText(output);

        }catch(Exception e) {

        }

        holder.txt_rain.setText(itemTimes.getRain()+"%");
        Picasso.get().load("http:".concat(itemTimes.getIcon())).into(holder.img_time);
        holder.img_rain.setImageResource(R.drawable.anumbrella);
         }



    @Override
    public int getItemCount() {
        return timesResponseList.size();
    }

    class TimeVH extends RecyclerView.ViewHolder {

        TextView txt_time,txt_temp,txt_rain;
        ImageView img_time,img_rain;
        public TimeVH(@NonNull View itemView) {
            super(itemView);
            txt_time=itemView.findViewById(R.id.txt_time);
            txt_temp=itemView.findViewById(R.id.txt_temp);
            txt_rain=itemView.findViewById(R.id.txt_rain);
            img_time=itemView.findViewById(R.id.img_time);
            img_rain=itemView.findViewById(R.id.img_rain);
        }

    }


}
