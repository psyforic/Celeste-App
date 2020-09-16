package com.celeste.celestedaylightapp.retrofit;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ApiClient {
    private static Retrofit retrofitInstance = null;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static final String BASE_URL = "http://celesteweb-001-site1.ftempurl.com:80";
//    private static final String BASE_URL = "http://10.0.2.2:21021/";
    private static OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
        @Override
        public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
            Request originalRequest = chain.request();

            Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                    Credentials.basic("aUsername", "aPassword"));

            Request newRequest = builder.build();
            return chain.proceed(newRequest);
        }
    }).build();
    public static Retrofit getInstance() {
        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
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
