package myweatherApplication.example.MyWeatherApplication.utils;

public class Constant {
    public static final String API_KEY="0bffc4f4972106579d4a8587cd37b49f";
    public static final String CITY_INFO = "city-info";
    public static final String LAST_STORED_CURRENT = "last-stored-current";
    public static final long TIME_TO_PASS = 6 * 600000;
    public static final String LANGUAGE = "language";

    public static final String[] DAYS_OF_WEEK = {
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday"
    };

    public static final String[] DAYS_OF_WEEK_PERSIAN = {
            "یکشنبه",
            "دوشنبه",
            "سه‌شنبه",
            "چهارشنبه",
            "پنج‌شنبه",
            "جمعه",
            "شنبه"
    };
    public static final String[] WEATHER_STATUS_PERSIAN = {
            "رعد و برق",
            "نمنم باران",
            "باران",
            "برف",
            "جو ناپایدار",
            "صاف",
            "کمی ابری",
            "ابرهای پراکنده",
            "ابری"

    };
    public static final String[] WEATHER_STATUS = {
            "Thunderstorm",
            "Drizzle",
            "Rain",
            "Snow",
            "Atmosphere",
            "Clear",
            "Few Clouds",
            "Broken Clouds",
            "Cloud"
    };


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
    public static final String THREE_DAY_WEATHER_ITEM = "three-day-weather-item";
    public static final String DARK_THEME = "dark-theme";
    public static final String LAST_STORED_MULTIPLE_DAYS = "last-stored-multiple-days";


}
