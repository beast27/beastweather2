package com.beastweather2.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.beastweather2.android.gson.Air;
import com.beastweather2.android.gson.Forecast;
import com.beastweather2.android.gson.Lifestyle;
import com.beastweather2.android.gson.PicBing;
import com.beastweather2.android.gson.Weather;
import com.beastweather2.android.service.AutoUpdateService;
import com.beastweather2.android.util.HttpUtil;
import com.beastweather2.android.util.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carwashingText;
    private TextView sportText;

    public DrawerLayout drawerLayout;
    private Button switchButton;

    public SwipeRefreshLayout swipeRefresh;
    private String mWeatherId;

    private ImageView picBing;

    private  Button settingsButton;

    private ImageView weatherIconView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >=21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        //init
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime =(TextView)findViewById(R.id.title_update_time);
        degreeText = (TextView)findViewById(R.id.degree_text);
        weatherInfoText = (TextView)findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout)findViewById(R.id.forecast_layout);
        aqiText = (TextView)findViewById(R.id.aqi_text);
        pm25Text = (TextView)findViewById(R.id.pm25_text);
        comfortText = (TextView)findViewById(R.id.comfort_text);
        carwashingText = (TextView)findViewById(R.id.car_washing_suggestion);
        sportText = (TextView)findViewById(R.id.sport_suggestion);
        switchButton = (Button) findViewById(R.id.switch_button);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        picBing = (ImageView) findViewById(R.id.pic_bing);
        settingsButton = (Button) findViewById(R.id.menu_button);
        weatherIconView = (ImageView) findViewById(R.id.weather_icon);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = preferences.getString("weather",null);
        if (weatherString != null){
            Weather weather = Utility.handleWeatherRes(weatherString);
            mWeatherId = weather.basic.weatherId;
            showAllInfo(mWeatherId);
        }
        else {
            mWeatherId = getIntent().getStringExtra("weather_id");
            //String weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            showAllInfo(mWeatherId);
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                showAllInfo(mWeatherId);
            }
        });
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
        String urlPicBing = preferences.getString("pic_bing",null);
        if (urlPicBing != null){
            Glide.with(this).load(urlPicBing).into(picBing);
        }
        else {
            requestBackgroundImage();
        }
    }
    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        }
        else if(drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawers();
        }
        else{
            super.onBackPressed();
        }
    }
    //Get background image from Bing
    private void requestBackgroundImage(){
        String imageUrl = "http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
        HttpUtil.sendOkHttpRequest(imageUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final PicBing picbing = Utility.handleImageRes(responseText);
                Log.i("url", picbing.toString());
                final String url = "http://s.cn.bing.net" + picbing.url;
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("pic_bing",url);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (picbing != null){
                            Glide.with(WeatherActivity.this).load(url).into(picBing);
                        }
                    }
                });
            }
        });
    }
    //Get weather information
    public void requestWeather(final String weatherId){
        String weatherUrl = "https://free-api.heweather.com/s6/weather?location=" + weatherId
                + "&key=" + getString(R.string.api_key1);
       // Log.i("Weather",weatherUrl);
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"Failed to get weather information",Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                        Log.i("INFO", "Connection failed");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherRes(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfo(weather);
                        }
                        else {
                            Toast.makeText(WeatherActivity.this,"Failed to get weather information",Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        requestBackgroundImage();
    }
    //Get air information
    public void requestAir(final String weatherId){
        String airUrl = "https://free-api.heweather.com/s6/air?location=" + weatherId + "&key="
                + getString(R.string.api_key1);
         Log.i("airurl=",airUrl);
        HttpUtil.sendOkHttpRequest(airUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"Failed to get air information",Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                        Log.i("INFO", "Connection failed");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Air air = Utility.handleAirRes(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (air != null && "ok".equals(air.status)){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("air",responseText);
                            editor.apply();
                            showAirInfo(air);
                        }
                        else {
                            Toast.makeText(WeatherActivity.this,"Failed to get weather information",Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        requestBackgroundImage();
    }
    //Show weather information
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.update.updateTime.split(" ")[1];
        String degree = weather.now.tmp + "℃";
        String weatherInfo = weather.now.cond_txt;
        Log.i("degree", degree);
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        Glide.with(this).load(getWeatherIcon(weather.now.cond_code)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(weatherIconView);
        weatherIconView.setVisibility(View.VISIBLE);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.daily_forecast) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            String infotext = "白天" + forecast.cond_txt_d + "  夜间"+ forecast.cond_txt_n;
            infoText.setText(infotext);
            maxText.setText(forecast.tmp_max);
            minText.setText(forecast.tmp_min);
            forecastLayout.addView(view);
        }
        String comfort = null;
        String carWashing = null;
        String sport = null;
        for (Lifestyle lifestyle : weather.lifestyle) {
            if (lifestyle.type.equals("comf")) {
                comfort = "Comfortability:" + lifestyle.txt;
            }
            if (lifestyle.type.equals("cw")) {
                carWashing = "Suggestion of car washing;" + lifestyle.txt;
            }
            if (lifestyle.type.equals("sport")) {
                sport = "Suggestion of sport event:" + lifestyle.txt;
            }
        }
        comfortText.setText(comfort);
        carwashingText.setText(carWashing);
        sportText.setText(sport);

    }
    private void showAirInfo(Air air){
        if(air != null && air.status.equals("ok")){
            aqiText.setText(air.air_now_city.aqi);
            pm25Text.setText(air.air_now_city.pm25);
        }
    }
    private void showAllInfo(final String weatherId){
        requestWeather(weatherId);
        requestAir(weatherId);
        weatherLayout.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }
    private String getWeatherIcon(String weatherCode){
        String weatherIconUrl = "https://cdn.heweather.com/cond_icon/" + weatherCode + ".png";
        return weatherIconUrl;
    }
}
