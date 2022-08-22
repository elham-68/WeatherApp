package myweatherApplication.example.MyWeatherApplication.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.myWeatherApplication.R;

public class SplashScreen extends AppCompatActivity {

    TextView txt_splash;
    LottieAnimationView lottie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        txt_splash=findViewById(R.id.txt_splash);
        lottie=findViewById(R.id.lottie);

        txt_splash.animate().translationY(-400).setDuration(2700).setStartDelay(0);
        lottie.animate().setDuration(3000).setStartDelay(0);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        },5000);
    }
}