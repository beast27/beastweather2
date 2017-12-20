package com.beastweather2.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by beast on 2017/12/14.
 */

public class Basic {
    @SerializedName("location")
    public String cityName;
    @SerializedName("cid")
    public String weatherId;
}
