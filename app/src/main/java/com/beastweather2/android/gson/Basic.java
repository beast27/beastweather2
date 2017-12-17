package com.beastweather2.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by beast on 2017/12/14.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherId;
    public Update update;
    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
