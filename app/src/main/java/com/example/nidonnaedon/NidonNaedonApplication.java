package com.example.nidonnaedon;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NidonNaedonApplication extends Application {

    private static NidonNaedonAPI nidonNaedonAPI;

    @Override
    public void onCreate() {
        super.onCreate();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080") // 서버 포트를 8080으로 설정
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        nidonNaedonAPI = retrofit.create(NidonNaedonAPI.class);
    }

    public static NidonNaedonAPI getApi() {
        return nidonNaedonAPI;
    }
}
