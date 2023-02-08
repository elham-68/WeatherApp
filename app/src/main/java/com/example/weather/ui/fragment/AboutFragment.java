package com.example.weather.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.weather.R;
import com.example.weather.databinding.FragmentAboutBinding;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import com.example.weather.ui.activity.MainActivity;
import com.example.weather.utils.AppUtils;
import com.example.weather.utils.SharedPreferencesUtil;
import com.example.weather.utils.ViewAnimation;


public class AboutFragment extends DialogFragment {

    private FragmentAboutBinding binding;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAboutBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        initValues(view);
        darkTheme();
        return view;
    }


    private void initValues(View view) {
        activity = getActivity();
        if (activity != null) {

            String versionName = "";
            try {
                versionName = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {

            }
            setTextWithLinks(view.findViewById(R.id.text_application_info), getString(R.string.application_info_text, versionName));
            setTextWithLinks(view.findViewById(R.id.text_developer_info), getString(R.string.developer_info_text));
            setTextWithLinks(view.findViewById(R.id.text_design_api), getString(R.string.design_api_text));


        }
    }
    private void darkTheme(){
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

    private void setTextWithLinks(TextView text,String htmlText){
        AppUtils.setTextWithLinks(text,AppUtils.fromHtml(htmlText));
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