package com.celeste.celestedaylightapp.retrofit;

import android.content.Context;
import android.util.Log;

import com.celeste.celestedaylightapp.utils.Constants;
import com.celeste.celestedaylightapp.utils.Tools;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.GeneralSecurityException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class AuthHeader implements Interceptor {
    private Context context;

    public AuthHeader(Context ctx) {
        this.context = ctx;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        String accessToken = null;
        int tenantID = 0;
        try {
            accessToken = Tools.getEncryptedSharedPreferences(context).getString(Constants.ACCESS_TOKEN, null);
            tenantID = Tools.getEncryptedSharedPreferences(context).getInt(Constants.TENANT_ID,0);
            Log.d("TENANTID", "ID" + tenantID);
        }catch (GeneralSecurityException ex)
        {
            ex.printStackTrace();
        }
        Request request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer" + accessToken)
                .addHeader("Abp.TenantId", "" + tenantID)
                .build();
        return chain.proceed(request);
    }
}
