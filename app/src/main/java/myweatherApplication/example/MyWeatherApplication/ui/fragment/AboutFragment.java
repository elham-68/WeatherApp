package myweatherApplication.example.MyWeatherApplication.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.airbnb.lottie.L;
import com.example.myWeatherApplication.R;
import com.example.myWeatherApplication.databinding.FragmentAboutBinding;

import java.util.Calendar;
import java.util.Locale;

import myweatherApplication.example.MyWeatherApplication.ui.activity.MainActivity;
import myweatherApplication.example.MyWeatherApplication.utils.AppUtils;
import myweatherApplication.example.MyWeatherApplication.utils.LocaleManager;
import myweatherApplication.example.MyWeatherApplication.utils.MyApplication;
import myweatherApplication.example.MyWeatherApplication.utils.SharedPreferencesUtil;
import myweatherApplication.example.MyWeatherApplication.utils.ViewAnimation;


public class AboutFragment extends DialogFragment {

    private FragmentAboutBinding binding;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        initValues(view);
return view;
    }


    private void initValues(View view) {
        activity = getActivity();
        binding.nightModeSwitch.setChecked(SharedPreferencesUtil.getInstance(activity).isDarkThemeEnabled());
        binding.nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
             SharedPreferencesUtil.getInstance(activity).setDarkThemeEnabled(b);
                if (b){
                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_YES);
                }else {
                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_NO);
                }
                restartActivity();
            }
        });
        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        binding.btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        binding.toggleInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
    }
    private void toggle(){
        boolean show = toggleArrow(binding.btnInfo);
        if (show){
            ViewAnimation.expand(binding.expandLayout, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {

                }
            });
        }else {
            ViewAnimation.collapse(binding.expandLayout);
        }

    }

    private boolean toggleArrow(View view){
        if (view.getRotation() == 0){
            view.animate().setDuration(200).rotation(180);
        return true;
        }else{
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

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
    private void restartActivity() {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }



}