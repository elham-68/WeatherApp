package com.example.weather.model;

public class ForecastDaily {


    private String icon;
    private String date;
    private String temp_min;
    private String temp_max;
    private String city;
    double temp;

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }



    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(String temp_min) {
        this.temp_min = temp_min;
    }

    public String getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(String temp_max) {
        this.temp_max = temp_max;
    }



    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ForecastDaily(String temp_max, String temp_min, String date, String icon,String city,double temp){
        this.temp_max=temp_max;
        this.temp_min=temp_min;
        this.date=date;
        this.icon=icon;
        this.city=city;
        this.temp=temp;

    }


}
