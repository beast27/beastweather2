package com.beastweather2.android.gson;

/**
 * Created by beast on 2017/12/14.
 */

public class Forecast {
    public String date;
    public Cond cond;
    public Tmp tmp;
    public class Cond{
        public String txt_d;
    }
    public class Tmp{
        public String max;
        public String min;
    }

}
