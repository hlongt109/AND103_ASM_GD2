package com.ph30891.asm_ph30891_gd2.networking;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationRequest {
    public final static String SHOPID = "191531";
    public final static String TokenGHN = "8ef5d687-ec1f-11ee-a6e6-e60958111f48";
    private LocationService locationService;

    public LocationRequest(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request request = chain.request().newBuilder()
                    .addHeader("ShopId",SHOPID)
                    .addHeader("Token",TokenGHN)
                    .build();
            return  chain.proceed(request);
        });
        // create retrofit
        locationService = new Retrofit.Builder()
                .baseUrl(LocationService.GHN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build().create(LocationService.class);
    }
    public LocationService callAPI(){
        return locationService;
    }
}
