package myweatherApplication.example.MyWeatherApplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import myweatherApplication.example.MyWeatherApplication.model.ForecastDaily;
import myweatherApplication.example.MyWeatherApplication.model.ResponseDescription;

import com.example.myWeatherApplication.R;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DescriptionAdapter extends RecyclerView.Adapter<DescriptionAdapter.DescriprionVH> {

    Context context;
    List<ForecastDaily> forecastDailyList;

    public DescriptionAdapter(Context context,List<ForecastDaily> forecastDailyList){
        this.context=context;
        this.forecastDailyList=forecastDailyList;
    }

    @NonNull
    @Override
    public DescriprionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_description,parent,false);
        return new DescriprionVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DescriprionVH holder, int position) {
      ForecastDaily itemDescription=forecastDailyList.get(position);
      holder.txt_temp.setText(itemDescription.getTemp()+"°C");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sd1 = new SimpleDateFormat("HH:mm");
        try{
            Date parseDate = sd.parse(String.valueOf(itemDescription.getDate()));
            assert parseDate != null;
            String output = sd1.format(parseDate);
            holder.txt_time.setText(output);

        }catch(Exception e) {

        }
        Picasso.get().load("http:".concat(itemDescription.getIcon())).into(holder.img_weather);

    }

    @Override
    public int getItemCount() {
        return forecastDailyList.size();
    }

    class DescriprionVH extends RecyclerView.ViewHolder {
        TextView txt_time,txt_temp;
        ImageView img_weather;
        public DescriprionVH(@NonNull View itemView) {
            super(itemView);
            txt_time=itemView.findViewById(R.id.txt_time);
            txt_temp=itemView.findViewById(R.id.txt_temp);
            img_weather=itemView.findViewById(R.id.img_time);
        }
    }

}
