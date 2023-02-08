package com.example.weather.model;

public class TimesResponse {
    private String time;
    private double temp;
    private String icon;
    private String rain;

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    private int weatherId;



    public TimesResponse(double temps, String time, String icons, String rain) {
        this.temp=temps;
        this.time=time;
        this.icon=icons;
        this.rain=rain;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRain() {
        return rain;
    }

    public void setRain(String rain) {
        this.rain = rain;
    }



}
