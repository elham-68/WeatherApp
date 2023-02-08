package com.example.weather.model;

public class ResponseDescription {
    String dt_txt ;
    double temp;
    String icons;

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getIcons() {
        return icons;
    }

    public void setIcons(String icons) {
        this.icons = icons;
    }

    public ResponseDescription(String dt_txt, double temp, String icons){
        this.dt_txt=dt_txt;
        this.temp=temp;
        this.icons=icons;


    }


}
