package com.celeste.celestedaylightapp.retrofit;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://vuyo921009-001-site1.gtempurl.com";
    //    private static final String BASE_URL = "http://celesteweb-001-site1.ftempurl.com:80";
    private static Retrofit retrofitInstance = null;

    public static Retrofit getInstance(Context context) {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addNetworkInterceptor(new NetworkConnectionInterceptor(context))
                .addInterceptor(new AuthHeader(context))
                .authenticator(new TokenAuthenticator(context));
        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .client(okHttpClient.build())
                    .build();
        }

        return retrofitInstance;
    }

    public static <T> T createRetrofitService(final Class<T> clazz, final String endPoint) {
        final Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(endPoint)
                .build();
        return restAdapter.create(clazz);
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

}
