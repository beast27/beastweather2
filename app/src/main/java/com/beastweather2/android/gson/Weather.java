package com.beastweather2.android.gson;

import java.util.List;

/**
 * Created by beast on 2017/12/14.
 */

public class Weather {
    public String status;
    public Basic basic;
    public Aqi aqi;
    public Now now;
    public Suggestion suggestion;
    public List<Forecast> daily_forecast;
}
