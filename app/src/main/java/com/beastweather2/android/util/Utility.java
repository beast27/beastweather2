package com.beastweather2.android.util;

import android.text.TextUtils;

import com.beastweather2.android.db.City;
import com.beastweather2.android.db.County;
import com.beastweather2.android.db.Province;
import com.beastweather2.android.gson.Air;
import com.beastweather2.android.gson.PicBing;
import com.beastweather2.android.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by beast on 2017/12/12.
 */

public class Utility {
    public static boolean handleProvinceRes(String response){
        if (!TextUtils.isEmpty(response))
        {
            try {
                JSONArray allprovinces = new JSONArray(response);
                for (int i = 0; i <  allprovinces.length();i++)
                {
                JSONObject provinceobject = allprovinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvincename(provinceobject.getString("name"));
                    province.setProvincecode(provinceobject.getInt("id"));
                    province.save();
                }
                return true;
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean handleCityRes(String response,int provinceId){
        if (!TextUtils.isEmpty(response)){
            try{
                JSONArray allcitys = new JSONArray(response);
                for (int i = 0; i < allcitys.length();i++){
                    JSONObject cityobject = allcitys.getJSONObject(i);
                    City city = new City();
                    city.setCityname(cityobject.getString("name"));
                    city.setCitycode(cityobject.getInt("id"));
                    city.setProvincecode(provinceId);
                    city.save();
                }
                return true;
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean handleCountyRes(String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            try{
                JSONArray allcounties = new JSONArray(response);
                for (int i = 0; i < allcounties.length();i++){
                    JSONObject countyobject = allcounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyname(countyobject.getString("name"));
                    county.setWeatherid(countyobject.getString("weather_id"));
                    county.setCityid(cityId);
                    county.save();
                }
                return true;
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    //JSON analyze weather info
    public static Weather handleWeatherRes(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static Air handleAirRes(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String airContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(airContent,Air.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //JSON analyze image url
    public static PicBing handleImageRes(String response){
            try{
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("images");
                String picBingContent = jsonArray.getJSONObject(0).toString();
                return new Gson().fromJson(picBingContent,PicBing.class);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        return null;
    }
}
