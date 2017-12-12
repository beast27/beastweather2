package com.beastweather2.android.db;


import org.litepal.crud.DataSupport;

/**
 * Created by beast on 2017/12/12.
 */

public class City extends DataSupport {
    private int id;
    private String cityname;
    private int citycode;
    private int provincecode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public int getCitycode() {
        return citycode;
    }

    public void setCitycode(int citycode) {
        this.citycode = citycode;
    }

    public int getProvincecode() {
        return provincecode;
    }

    public void setProvincecode(int provincecode) {
        this.provincecode = provincecode;
    }
}
