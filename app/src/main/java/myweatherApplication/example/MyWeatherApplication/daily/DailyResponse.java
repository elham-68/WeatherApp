package myweatherApplication.example.MyWeatherApplication.daily;

import android.os.Parcel;

public class DailyResponse {



    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    private String icon;

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    private String temp;
    private String cityDaily;
    private String description;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;


    public String getCityDaily() {
        return cityDaily;
    }

    public void setCityDaily(String cityDaily) {
        this.cityDaily = cityDaily;
    }

    public DailyResponse(){

    }
    protected DailyResponse(Parcel in) {
       temp = in.readString();
        icon = in.readString();
        cityDaily = in.readString();
        //daysName = in.readString();
        description = in.readString();
        date = in.readString();
    }

    public DailyResponse(String temp,String icon,String cityDaily,String description,String date){
        this.icon=icon;
        this.cityDaily=cityDaily;
        this.description=description;
        this.date=date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
