package com.google.android.gms.samples.vision.barcodereader.model.retrofit_service;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit = null;
    private static final String TAG = "RetrofitInstance";
    private static final String BASE_URL = "https://192.168.1.3:4430/" ;

    public static MyServicesInterface getService() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder()
                    .addInterceptor(interceptor)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(40, TimeUnit.SECONDS)
                    .writeTimeout(40, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .client(okHttpClient)
                    .build();

        }

        return retrofit.create(MyServicesInterface.class);
    }

}

