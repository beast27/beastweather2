package com.beastweather2.android.db;


import org.litepal.crud.DataSupport;

/**
 * Created by beast on 2017/12/12.
 */

public class Province extends DataSupport{
    private int id;
    private String provincename;
    private int provincecode;
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getProvincename(){
        return  provincename;
    }
    public void setProvincename(String provincename){
        this.provincename = provincename;
    }
    public int getProvincecode(){
        return provincecode;
    }
    public void setProvincecode(int provincecode){
        this.provincecode = provincecode;
    }
}
