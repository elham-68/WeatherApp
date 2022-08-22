package myweatherApplication.example.MyWeatherApplication.utils;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.RequiresPermission;
import androidx.core.os.ConfigurationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myWeatherApplication.R;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

public class AppUtils {

    public static void showFragment(Fragment fragment, FragmentManager fragmentManager, boolean withAnimation) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (withAnimation) {
            transaction.setCustomAnimations(R.anim.slide_up_anim, R.anim.slide_down_anim);
        } else {
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
        transaction.add(android.R.id.content, fragment).addToBackStack(null).commit();
    }

    public static boolean isTimePass(long lastStored) {
        return System.currentTimeMillis() - lastStored > Constant.TIME_TO_PASS;
    }
    static boolean isAtLeastVersion(int version) {
        return Build.VERSION.SDK_INT >= version;
    }

    public static boolean isRTL(Context context) {
        Locale locale = ConfigurationCompat.getLocales(context.getResources().getConfiguration()).get(0);
        final int directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }

    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;

    private static void init(final Application app) {
        if (sApplication == null) {
            if (app == null) {
                sApplication = getApplicationByReflect();
            } else {
                sApplication = app;
            }
        } else {
            if (app != null && app.getClass() != sApplication.getClass()) {
                sApplication = app;
            }
        }
    }

    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isNetworkConnected() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isConnected();
    }


    @RequiresPermission(ACCESS_NETWORK_STATE)
    private static NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager cm =
                (ConnectivityManager) getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return null;
        return cm.getActiveNetworkInfo();
    }

    public static Application getApp() {
        if (sApplication != null) return sApplication;
        Application app = getApplicationByReflect();
        init(app);
        return app;
    }

    private static Application getApplicationByReflect() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            return (Application) app;
        } catch (NoSuchMethodException | IllegalAccessException |
                InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("u should init first");
    }
    public static String getWeatherStatus(int weatherCode, boolean isRTL) {
        if (weatherCode / 100 == 2) {
            if (isRTL) {
                return Constant.WEATHER_STATUS_PERSIAN[0];
            } else {
                return Constant.WEATHER_STATUS[0];
            }
        } else if (weatherCode / 100 == 3) {
            if (isRTL) {
                return Constant.WEATHER_STATUS_PERSIAN[1];
            } else {
                return Constant.WEATHER_STATUS[1];
            }
        } else if (weatherCode / 100 == 5) {
            if (isRTL) {
                return Constant.WEATHER_STATUS_PERSIAN[2];
            } else {
                return Constant.WEATHER_STATUS[2];
            }
        } else if (weatherCode / 100 == 6) {
            if (isRTL) {
                return Constant.WEATHER_STATUS_PERSIAN[3];
            } else {
                return Constant.WEATHER_STATUS[3];
            }
        } else if (weatherCode / 100 == 7) {
            if (isRTL) {
                return Constant.WEATHER_STATUS_PERSIAN[4];
            } else {
                return Constant.WEATHER_STATUS[4];
            }
        } else if (weatherCode == 800) {
            if (isRTL) {
                return Constant.WEATHER_STATUS_PERSIAN[5];
            } else {
                return Constant.WEATHER_STATUS[5];
            }
        } else if (weatherCode == 801) {
            if (isRTL) {
                return Constant.WEATHER_STATUS_PERSIAN[6];
            } else {
                return Constant.WEATHER_STATUS[6];
            }
        } else if (weatherCode == 803) {
            if (isRTL) {
                return Constant.WEATHER_STATUS_PERSIAN[7];
            } else {
                return Constant.WEATHER_STATUS[7];
            }
        } else if (weatherCode / 100 == 8) {
            if (isRTL) {
                return Constant.WEATHER_STATUS_PERSIAN[8];
            } else {
                return Constant.WEATHER_STATUS[8];
            }
        }
        if (isRTL) {
            return Constant.WEATHER_STATUS_PERSIAN[4];
        } else {
            return Constant.WEATHER_STATUS[4];
        }
    }


}
