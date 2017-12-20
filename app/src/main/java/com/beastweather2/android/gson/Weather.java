package com.beastweather2.android.gson;

import java.util.List;

/**
 * Created by beast on 2017/12/14.
 */

public class Weather {
    public String status;
    public Basic basic;
    public Update update;
    public List<Forecast> daily_forecast;
    public Now now;
    public List<Lifestyle> lifestyle;
}
